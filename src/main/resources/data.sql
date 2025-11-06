-- Минимальные демо-данные для проверки репозиториев.
-- Важно: работает после создания схемы Hibernate из-за spring.jpa.defer-datasource-initialization=true

INSERT INTO crawling_task (seed_url, max_depth, max_pages, status, created_at, updated_at)
VALUES ('https://example.com', 1, 25, 'PENDING', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Привяжем парочку артефактов к задаче с id=1
INSERT INTO contact_info (type, raw_value, normalized_value, source_url, context, created_at, task_id)
VALUES ('EMAIL', 'info@example.com', 'info@example.com', 'https://example.com', 'footer', CURRENT_TIMESTAMP(), 1);

INSERT INTO contact_info (type, raw_value, normalized_value, source_url, context, created_at, task_id)
VALUES ('PHONE', '+1 (555) 010-020', '+1555010020', 'https://example.com', 'header', CURRENT_TIMESTAMP(), 1);
