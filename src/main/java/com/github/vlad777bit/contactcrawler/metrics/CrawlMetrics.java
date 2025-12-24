package com.github.vlad777bit.contactcrawler.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class CrawlMetrics {

    private static final String METRIC_TASK_DURATION = "crawler_task_parse_duration_seconds";
    private static final String METRIC_TASK_SUCCESS = "crawler_task_success_total";
    private static final String METRIC_TASK_ERROR = "crawler_task_error_total";
    private static final String METRIC_DB_RECORDS_SAVED = "crawler_db_records_saved";

    private final MeterRegistry registry;

    private final Timer taskDuration;
    private final Counter taskSuccess;
    private final Counter taskError;
    private final DistributionSummary dbRecordsSaved;

    public CrawlMetrics(MeterRegistry registry) {
        this.registry = registry;
        this.taskDuration = registry.timer(METRIC_TASK_DURATION);
        this.taskSuccess = Counter.builder(METRIC_TASK_SUCCESS).register(registry);
        this.taskError = Counter.builder(METRIC_TASK_ERROR).register(registry);
        this.dbRecordsSaved = DistributionSummary.builder(METRIC_DB_RECORDS_SAVED).register(registry);
    }

    public Timer.Sample startTaskTimer() {
        return Timer.start(registry);
    }

    public void stopTaskTimer(Timer.Sample sample) {
        if (sample == null) return;
        sample.stop(taskDuration);
    }

    public void incSuccess() {
        taskSuccess.increment();
    }

    public void incError() {
        taskError.increment();
    }

    public void recordSavedRecords(int count) {
        if (count <= 0) return;
        dbRecordsSaved.record(count);
    }
}
