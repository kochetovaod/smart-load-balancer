-- Таблица для метрик сервисов
CREATE TABLE IF NOT EXISTS lb_metrics.service_metrics (
    time TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    service_id VARCHAR(100) NOT NULL,
    instance_id VARCHAR(100) NOT NULL,
    
    -- Основные метрики
    rps DOUBLE PRECISION,           -- Requests per second
    p95_latency DOUBLE PRECISION,   -- 95th percentile latency
    error_rate DOUBLE PRECISION,    -- Error rate (0-1)
    active_connections INTEGER,     -- Active connections
    
    -- Дополнительные метрики
    cpu_usage DOUBLE PRECISION,     -- CPU usage percentage
    memory_usage DOUBLE PRECISION,  -- Memory usage percentage
    heap_usage DOUBLE PRECISION,    -- Heap usage percentage
    
    -- Метаданные
    tags JSONB DEFAULT '{}',
    
    PRIMARY KEY (time, service_id, instance_id)
);

-- Преобразование в hypertable для TimescaleDB
SELECT create_hypertable(
    'lb_metrics.service_metrics', 
    'time',
    if_not_exists => TRUE,
    migrate_data => TRUE
);

-- Индексы для эффективных запросов
CREATE INDEX IF NOT EXISTS idx_service_metrics_service_time 
ON lb_metrics.service_metrics (service_id, time DESC);

CREATE INDEX IF NOT EXISTS idx_service_metrics_instance_time 
ON lb_metrics.service_metrics (instance_id, time DESC);

CREATE INDEX IF NOT EXISTS idx_service_metrics_tags 
ON lb_metrics.service_metrics USING GIN (tags);

-- Таблица для health checks
CREATE TABLE IF NOT EXISTS lb_metrics.health_status (
    time TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    service_id VARCHAR(100) NOT NULL,
    instance_id VARCHAR(100) NOT NULL,
    
    -- Статус здоровья
    status VARCHAR(20) NOT NULL,  -- HEALTHY, UNHEALTHY, UNKNOWN
    response_time DOUBLE PRECISION,
    error_message TEXT,
    
    -- Метаданные
    check_type VARCHAR(50) DEFAULT 'HTTP',
    
    PRIMARY KEY (time, service_id, instance_id)
);

-- Hypertable для health checks
SELECT create_hypertable(
    'lb_metrics.health_status', 
    'time',
    if_not_exists => TRUE
);

-- Таблица для конфигурации балансировки
CREATE TABLE IF NOT EXISTS lb_metrics.load_balancer_config (
    config_key VARCHAR(100) PRIMARY KEY,
    config_value JSONB NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    updated_by VARCHAR(100) DEFAULT 'system'
);

-- Начальная конфигурация
INSERT INTO lb_metrics.load_balancer_config (config_key, config_value) VALUES
('routing.strategy', '{"type": "WEIGHTED_ROUND_ROBIN", "weights": {}}'),
('health.check', '{"interval": 30, "timeout": 5, "healthy_threshold": 2, "unhealthy_threshold": 3}'),
('metrics.retention', '{"days": 30}')
ON CONFLICT (config_key) DO NOTHING;

-- Права доступа
GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA lb_metrics TO smartlb_user;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA lb_metrics TO smartlb_user;

-- Retention policy для автоматической очистки старых данных
SELECT add_retention_policy('lb_metrics.service_metrics', INTERVAL '30 days', if_not_exists => TRUE);
SELECT add_retention_policy('lb_metrics.health_status', INTERVAL '30 days', if_not_exists => TRUE);
