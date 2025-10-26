-- View для агрегированных метрик по сервисам
CREATE OR REPLACE VIEW lb_metrics.service_metrics_summary AS
SELECT 
    service_id,
    time_bucket('1 minute', time) AS bucket,
    AVG(rps) as avg_rps,
    PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY p95_latency) as p95_latency,
    AVG(error_rate) as avg_error_rate,
    MAX(active_connections) as max_connections,
    COUNT(*) as sample_count
FROM lb_metrics.service_metrics
WHERE time > NOW() - INTERVAL '1 hour'
GROUP BY service_id, bucket
ORDER BY bucket DESC, service_id;

-- View для текущего статуса здоровья сервисов
CREATE OR REPLACE VIEW lb_metrics.current_health_status AS
SELECT DISTINCT ON (service_id, instance_id)
    service_id,
    instance_id,
    status,
    response_time,
    time as last_check
FROM lb_metrics.health_status
ORDER BY service_id, instance_id, time DESC;

-- View для статистики использования ресурсов
CREATE OR REPLACE VIEW lb_metrics.resource_usage_stats AS
SELECT 
    service_id,
    time_bucket('5 minutes', time) AS bucket,
    AVG(cpu_usage) as avg_cpu_usage,
    AVG(memory_usage) as avg_memory_usage,
    AVG(heap_usage) as avg_heap_usage
FROM lb_metrics.service_metrics
WHERE time > NOW() - INTERVAL '1 day'
GROUP BY service_id, bucket
ORDER BY bucket DESC, service_id;

-- Права на views
GRANT SELECT ON ALL TABLES IN SCHEMA lb_metrics TO smartlb_user;
