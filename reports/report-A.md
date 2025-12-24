# A. Spring Boot Actuator: метрики, threaddump и HTTP-запросы

## A1. Endpoints Actuator и проверка доступности

- Health: `GET /actuator/health`  
  Ответ: `status = UP`

- Info: `GET /actuator/info`  
  Ответ: (пусто)

- Metrics list: `GET /actuator/metrics`  
  Количество метрик: **73**

> Важно: для threaddump использовался `Accept: application/json` (JSON-формат).

---

## A2. Обзор основных метрик

В списке метрик присутствуют:

- **JVM/OS**: `jvm.*`, `process.*`, `system.*`
- **Пулы/потоки**: `executor.*`, `threads.*`
- **HTTP**: `http.server.requests`, `http.server.requests.active`
- **БД (HikariCP)**: `hikaricp.*`
- **Кастомные метрики краулера**:
  - `crawler_task_parse_duration_seconds`
  - `crawler_task_success_total`
  - `crawler_task_error_total`
  - `crawler_db_records_saved`

---

## A3. Анализ threaddump

Threaddump получен с `GET /actuator/threaddump` (JSON).

### Количество потоков
Всего потоков в дампе: **35**

### Распределение по состояниям (threadState)
7 "threadState":"RUNNABLE"
      6 "threadState":"TIMED_WAITING"
     22 "threadState":"WAITING"

В терминах интерпретации:
- **RUNNABLE** — поток выполняет работу.
- **WAITING/TIMED_WAITING** — поток ждёт (очередь, таймер, планировщик, парковка).
- **BLOCKED** — поток ждёт монитор (признак contention по `synchronized`).  

**BLOCKED = 0**, то есть явного lock contention в момент снимка не наблюдается.

### Потоки краулера
Найдено потоков, содержащих `crawler` в имени: **12**  
Распределение по состояниям (crawler-*): {'WAITING': 11, 'TIMED_WAITING': 1}

Типичная картина:
- `crawler-exec-*` — ожидают задачи из очереди пула.
- `crawler-sched-*` — ожидают “тик” планировщика.

#### Пример стека `crawler-exec-1`
- jdk.internal.misc.Unsafe.park (line -2)
- java.util.concurrent.locks.LockSupport.park (line 371)
- java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionNode.block (line 519)
- java.util.concurrent.ForkJoinPool.unmanagedBlock (line 3778)
- java.util.concurrent.ForkJoinPool.managedBlock (line 3723)
- java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await (line 1712)
- java.util.concurrent.LinkedBlockingQueue.take (line 435)
- java.util.concurrent.ThreadPoolExecutor.getTask (line 1070)
- java.util.concurrent.ThreadPoolExecutor.runWorker (line 1130)
- java.util.concurrent.ThreadPoolExecutor$Worker.run (line 642)

#### Пример стека `crawler-sched-1`
- jdk.internal.misc.Unsafe.park (line -2)
- java.util.concurrent.locks.LockSupport.park (line 371)
- java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionNode.block (line 519)
- java.util.concurrent.ForkJoinPool.unmanagedBlock (line 3778)
- java.util.concurrent.ForkJoinPool.managedBlock (line 3723)
- java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await (line 1712)
- java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take (line 1177)
- java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take (line 899)
- java.util.concurrent.ThreadPoolExecutor.getTask (line 1070)
- java.util.concurrent.ThreadPoolExecutor.runWorker (line 1130)

---

## A4. Анализ HTTP-метрики `http.server.requests`

Метрика: `GET /actuator/metrics/http.server.requests`

### Общие значения
- COUNT = **357**
- TOTAL_TIME = **1.365001 s**
- MAX = **17.09 ms**
- AVG (оценочно) = **3.82 ms** (TOTAL_TIME / COUNT)

### Доступные теги (availableTags)
- exception: none
- method: POST, GET
- error: none
- uri: /actuator/metrics/{requiredMetricName}, /crawl, /actuator/threaddump, /actuator/info, /actuator/health, /actuator/metrics, /actuator/prometheus
- outcome: SUCCESS
- status: 200, 201

### Drill-down по `uri:/crawl`
Запрос: `GET /actuator/metrics/http.server.requests?tag=uri:/crawl`

- COUNT = **12**
- TOTAL_TIME = **0.099133 s**
- MAX = **9.01 ms**
- AVG (оценочно) = **8.26 ms**

> Примечание: в собранных данных тег `uri` не содержит `/results` — значит, в момент сбора отчёта запросы к `/results` не выполнялись (или выполнялись вне периода сбора).

---

## A5. Итог по пункту A

1) Actuator endpoints доступны, health показывает `UP`.  
2) Threaddump снят и проинтерпретирован: доминируют WAITING/TIMED_WAITING, BLOCKED отсутствует.  
3) HTTP-метрики `http.server.requests` проанализированы, выполнен drill-down по тегам (пример — `/crawl`).  
