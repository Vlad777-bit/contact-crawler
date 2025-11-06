# contact-crawler

Multithreaded contact crawler built with **Java 17** and **Spring Boot 3.5.7**.

## Quick start

### Prerequisites
- Java 17+
- Maven 3.6.3+

### Run the app
```bash
mvn spring-boot:run
```
The app starts at `http://localhost:8080/`. (Spring Boot Maven Plugin `run` goal: see docs.)


## API

### Create a crawling task — `POST /crawl`

**Request body (JSON):**
```json
{
  "seedUrl": "https://example.com",
  "maxDepth": 1,
  "maxPages": 50
}
```

**cURL:**
```bash
curl -i -X POST "http://localhost:8080/crawl"      -H "Content-Type: application/json"      -d '{"seedUrl":"https://example.com","maxDepth":1,"maxPages":50}'
```
> Примечание: ключевой заголовок `Content-Type: application/json` (для `curl -d` POST JSON).

**Response:**
- `201 Created`
- `Location: /results?taskId={id}` — ссылка на результаты задачи
- Body:
```json
{
  "id": 1,
  "seedUrl": "https://example.com",
  "maxDepth": 1,
  "maxPages": 50,
  "status": "PENDING",
  "createdAt": "2025-11-07T00:57:20.123Z"
}
```
> Семантика `201 Created` + `Location` соответствует HTTP (RFC 9110) и поддерживается через `ResponseEntity.created(location)` в Spring MVC.

### Get results — `GET /results`

**Query params:**
- `taskId` (required) — ID задачи
- `type` (optional) — `EMAIL` | `PHONE` | `ADDRESS`
- `q` (optional) — подстрочный поиск по `rawValue / normalizedValue / sourceUrl / context`
- `page` (default `0`), `size` (default `50`, max `200`)

**Examples:**
```bash
# Все контакты по задаче 1 (страница 0, по 50 штук)
curl "http://localhost:8080/results?taskId=1"

# Только EMAIL, с фильтром по строке "example"
curl "http://localhost:8080/results?taskId=1&type=EMAIL&q=example&page=0&size=50"
```

**Response (Page):**
```json
{
  "content": [
    {
      "id": 10,
      "type": "EMAIL",
      "rawValue": "info@example.com",
      "normalizedValue": "info@example.com",
      "sourceUrl": "https://example.com",
      "context": "footer",
      "createdAt": "2025-11-07T00:57:25.000Z"
    }
  ],
  "page": 0,
  "size": 50,
  "totalElements": 1,
  "totalPages": 1
}
```
> Пагинация реализована через Spring Data `Pageable`/`Page`. Внутри сервиса используется дополнительная параллельная постобработка (`parallelStream`) с дедупликацией по `normalizedValue`.


## Scheduler & concurrency

- Планировщик ищет `PENDING`-задачи и отправляет их в кастомный пул.
- По умолчанию сканирование — **каждые 10 секунд** (`cron` ниже).
- Настройка пулов/расписания — через properties.

```properties
# Scheduling
spring.task.scheduling.pool.size=4
crawler.schedule.cron=*/10 * * * * *

# HTTP settings for crawler
crawler.http.userAgent=contact-crawler/1.0 (+https://example.invalid)
crawler.http.timeoutMillis=5000
crawler.http.maxBodySizeBytes=1048576
```

Полезно знать:
- `@Scheduled` поддерживает `fixedRate`, `fixedDelay`, и CRON-выражения (улучшенный парсер cron доступен в современных версиях Spring Framework).
- Ответ `POST /crawl` отдает `201 Created` с `Location` (URI созданного ресурса/результатов).

## H2 Database (dev)

Приложение использует H2 in-memory (авто-DDL + `data.sql` для демо-данных).

- JDBC URL: `jdbc:h2:mem:contactdb;DB_CLOSE_DELAY=-1`
- Console: `http://localhost:8080/h2-console`
- Свойства уже включены в `application.properties`.

## Build & test

```bash
# Все тесты
mvn test

# Полная сборка + тесты
mvn clean verify

# Запуск одного класса
mvn test -Dtest=ContactExtractionServiceTest

# Запуск одного метода
mvn test -Dtest=ContactCrawlerControllerTest#createCrawl_returns201AndLocation

# Сборка JAR
mvn clean package
java -jar target/contact-crawler-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

- **Ambiguous `Executor` bean** при старте (два бина типа `Executor`): убедитесь, что в `CrawlingScheduler` на параметре конструктора стоит `@Qualifier("crawlerExecutor")`.
- **Компиляция:** ошибка “`InterruptedException` is never thrown…” — уберите недостижимый `catch (InterruptedException)` (уже исправлено).
- **`415 Unsupported Media Type` при POST:** добавьте заголовок `-H "Content-Type: application/json"` в `curl`.
- **H2 Console не открывается:** проверьте, что включено `spring.h2.console.enabled=true` и путь `/h2-console`.

## Notes & References
- Spring Boot Maven Plugin: `mvn spring-boot:run`.
- `ResponseEntity.created(URI)` и `Location` в ответе.
- HTTP 201 + Location (RFC 9110).
- Spring Data: пагинация через `Pageable`/`Page`.
- `@Scheduled` и cron (Spring Framework).
- H2 Console конфигурация.
- cURL: POST JSON (`-H "Content-Type: application/json" -d ...`).
