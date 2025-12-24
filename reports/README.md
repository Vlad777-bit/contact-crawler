```json
{
  "threads": [
    {
      "threadName": "Reference Handler",
      "threadId": 9,
      "blockedTime": -1,
      "blockedCount": 0,
      "waitedTime": -1,
      "waitedCount": 0,
      "lockOwnerId": -1,
      "daemon": true,
      "inNative": false,
      "suspended": false,
      "threadState": "RUNNABLE",
      "priority": 10,
      "stackTrace": [
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "waitForReferencePendingList",
          "fileName": "Reference.java",
          "lineNumber": -2,
          "className": "java.lang.ref.Reference",
          "nativeMethod": true
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "processPendingReferences",
          "fileName": "Reference.java",
          "lineNumber": 246,
          "className": "java.lang.ref.Reference",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "run",
          "fileName": "Reference.java",
          "lineNumber": 208,
          "className": "java.lang.ref.Reference$ReferenceHandler",
          "nativeMethod": false
        }
      ],
      "lockedMonitors": [],
      "lockedSynchronizers": []
    },
    {
      "threadName": "Finalizer",
      "threadId": 10,
      "blockedTime": -1,
      "blockedCount": 0,
      "waitedTime": -1,
      "waitedCount": 1,
      "lockName": "java.lang.ref.NativeReferenceQueue$Lock@ae568f7",
      "lockOwnerId": -1,
      "daemon": true,
      "inNative": false,
      "suspended": false,
      "threadState": "WAITING",
      "priority": 8,
      "stackTrace": [
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "wait0",
          "fileName": "Object.java",
          "lineNumber": -2,
          "className": "java.lang.Object",
          "nativeMethod": true
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "wait",
          "fileName": "Object.java",
          "lineNumber": 366,
          "className": "java.lang.Object",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "wait",
          "fileName": "Object.java",
          "lineNumber": 339,
          "className": "java.lang.Object",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "await",
          "fileName": "NativeReferenceQueue.java",
          "lineNumber": 48,
          "className": "java.lang.ref.NativeReferenceQueue",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "remove0",
          "fileName": "ReferenceQueue.java",
          "lineNumber": 158,
          "className": "java.lang.ref.ReferenceQueue",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "remove",
          "fileName": "NativeReferenceQueue.java",
          "lineNumber": 89,
          "className": "java.lang.ref.NativeReferenceQueue",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "run",
          "fileName": "Finalizer.java",
          "lineNumber": 173,
          "className": "java.lang.ref.Finalizer$FinalizerThread",
          "nativeMethod": false
        }
      ],
      "lockedMonitors": [],
      "lockedSynchronizers": [],
      "lockInfo": {
        "className": "java.lang.ref.NativeReferenceQueue$Lock",
        "identityHashCode": 182806775
      }
    },
    {
      "threadName": "Signal Dispatcher",
      "threadId": 11,
      "blockedTime": -1,
      "blockedCount": 0,
      "waitedTime": -1,
      "waitedCount": 0,
      "lockOwnerId": -1,
      "daemon": true,
      "inNative": false,
      "suspended": false,
      "threadState": "RUNNABLE",
      "priority": 9,
      "stackTrace": [],
      "lockedMonitors": [],
      "lockedSynchronizers": []
    },
    {
      "threadName": "Notification Thread",
      "threadId": 26,
      "blockedTime": -1,
      "blockedCount": 0,
      "waitedTime": -1,
      "waitedCount": 0,
      "lockOwnerId": -1,
      "daemon": true,
      "inNative": false,
      "suspended": false,
      "threadState": "RUNNABLE",
      "priority": 9,
      "stackTrace": [],
      "lockedMonitors": [],
      "lockedSynchronizers": []
    },
    {
      "threadName": "Common-Cleaner",
      "threadId": 27,
      "blockedTime": -1,
      "blockedCount": 0,
      "waitedTime": -1,
      "waitedCount": 6,
      "lockName": "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject@10bce44a",
      "lockOwnerId": -1,
      "daemon": true,
      "inNative": false,
      "suspended": false,
      "threadState": "TIMED_WAITING",
      "priority": 8,
      "stackTrace": [
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "park",
          "fileName": "Unsafe.java",
          "lineNumber": -2,
          "className": "jdk.internal.misc.Unsafe",
          "nativeMethod": true
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "parkNanos",
          "fileName": "LockSupport.java",
          "lineNumber": 269,
          "className": "java.util.concurrent.locks.LockSupport",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "await",
          "fileName": "AbstractQueuedSynchronizer.java",
          "lineNumber": 1852,
          "className": "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "await",
          "fileName": "ReferenceQueue.java",
          "lineNumber": 71,
          "className": "java.lang.ref.ReferenceQueue",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "remove0",
          "fileName": "ReferenceQueue.java",
          "lineNumber": 143,
          "className": "java.lang.ref.ReferenceQueue",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "remove",
          "fileName": "ReferenceQueue.java",
          "lineNumber": 218,
          "className": "java.lang.ref.ReferenceQueue",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "run",
          "fileName": "CleanerImpl.java",
          "lineNumber": 140,
          "className": "jdk.internal.ref.CleanerImpl",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "runWith",
          "fileName": "Thread.java",
          "lineNumber": 1596,
          "className": "java.lang.Thread",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "run",
          "fileName": "Thread.java",
          "lineNumber": 1583,
          "className": "java.lang.Thread",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "run",
          "fileName": "InnocuousThread.java",
          "lineNumber": 186,
          "className": "jdk.internal.misc.InnocuousThread",
          "nativeMethod": false
        }
      ],
      "lockedMonitors": [],
      "lockedSynchronizers": [],
      "lockInfo": {
        "className": "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject",
        "identityHashCode": 280814666
      }
    },
    {
      "threadName": "Catalina-utility-1",
      "threadId": 46,
      "blockedTime": -1,
      "blockedCount": 1,
      "waitedTime": -1,
      "waitedCount": 421,
      "lockName": "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject@714de16c",
      "lockOwnerId": -1,
      "daemon": false,
      "inNative": false,
      "suspended": false,
      "threadState": "WAITING",
      "priority": 1,
      "stackTrace": [
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "park",
          "fileName": "Unsafe.java",
          "lineNumber": -2,
          "className": "jdk.internal.misc.Unsafe",
          "nativeMethod": true
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "park",
          "fileName": "LockSupport.java",
          "lineNumber": 371,
          "className": "java.util.concurrent.locks.LockSupport",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "block",
          "fileName": "AbstractQueuedSynchronizer.java",
          "lineNumber": 519,
          "className": "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionNode",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "unmanagedBlock",
          "fileName": "ForkJoinPool.java",
          "lineNumber": 3778,
          "className": "java.util.concurrent.ForkJoinPool",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "managedBlock",
          "fileName": "ForkJoinPool.java",
          "lineNumber": 3723,
          "className": "java.util.concurrent.ForkJoinPool",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "await",
          "fileName": "AbstractQueuedSynchronizer.java",
          "lineNumber": 1712,
          "className": "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "take",
          "fileName": "ScheduledThreadPoolExecutor.java",
          "lineNumber": 1177,
          "className": "java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "take",
          "fileName": "ScheduledThreadPoolExecutor.java",
          "lineNumber": 899,
          "className": "java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "getTask",
          "fileName": "ThreadPoolExecutor.java",
          "lineNumber": 1070,
          "className": "java.util.concurrent.ThreadPoolExecutor",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "runWorker",
          "fileName": "ThreadPoolExecutor.java",
          "lineNumber": 1130,
          "className": "java.util.concurrent.ThreadPoolExecutor",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "run",
          "fileName": "ThreadPoolExecutor.java",
          "lineNumber": 642,
          "className": "java.util.concurrent.ThreadPoolExecutor$Worker",
          "nativeMethod": false
        },
        {
          "classLoaderName": "app",
          "methodName": "run",
          "fileName": "TaskThread.java",
          "lineNumber": 63,
          "className": "org.apache.tomcat.util.threads.TaskThread$WrappingRunnable",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "runWith",
          "fileName": "Thread.java",
          "lineNumber": 1596,
          "className": "java.lang.Thread",
          "nativeMethod": false
        },
        {
          "moduleName": "java.base",
          "moduleVersion": "21.0.9",
          "methodName": "run",
          "fileName": "Thread.java",
          "lineNumber": 1583,
          "className": "java.lang.Thread",
          "nativeMethod": false
        }
      ],
      "lockedMonitors": [],
      "lockedSynchronizers": [],
      "lockInfo": {
        "className": "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject",
        "identityHashCode": 1900929388
      }
    }
  ]
}
```