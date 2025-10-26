-- Smart Load Balancer Permissions Setup
-- Script: 03-grant-permissions.sql
-- Purpose: Grant appropriate permissions to application users

-- Operational database permissions for lb_operator
\c loadbalancer_operational

-- Grant usage on operational schema
GRANT USAGE ON SCHEMA lb_operational TO lb_operator;
GRANT USAGE ON SCHEMA lb_operational TO lb_monitor;

-- Grant permissions to lb_operator (full access to operational tables)
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA lb_operational TO lb_operator;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA lb_operational TO lb_operator;

-- Grant permissions to lb_monitor (read-only access)
GRANT SELECT ON ALL TABLES IN SCHEMA lb_operational TO lb_monitor;

-- Set default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA lb_operational 
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO lb_operator;

ALTER DEFAULT PRIVILEGES IN SCHEMA lb_operational 
    GRANT SELECT ON TABLES TO lb_monitor;

-- Metrics database permissions for lb_metrics
\c loadbalancer_metrics

-- Grant usage on metrics schema
GRANT USAGE ON SCHEMA lb_metrics TO lb_metrics;
GRANT USAGE ON SCHEMA lb_metrics TO lb_monitor;

-- Grant permissions to lb_metrics (write access to metrics tables)
GRANT SELECT, INSERT ON ALL TABLES IN SCHEMA lb_metrics TO lb_metrics;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA lb_metrics TO lb_metrics;

-- Grant permissions to lb_monitor (read-only access to metrics)
GRANT SELECT ON ALL TABLES IN SCHEMA lb_metrics TO lb_monitor;

-- Set default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA lb_metrics 
    GRANT SELECT, INSERT ON TABLES TO lb_metrics;

ALTER DEFAULT PRIVILEGES IN SCHEMA lb_metrics 
    GRANT SELECT ON TABLES TO lb_monitor;

-- Additional permissions for TimescaleDB operations
GRANT EXECUTE ON FUNCTION create_hypertable TO lb_metrics;
GRANT EXECUTE ON FUNCTION add_retention_policy TO lb_metrics;
GRANT EXECUTE ON FUNCTION time_bucket TO lb_metrics;
GRANT EXECUTE ON FUNCTION time_bucket TO lb_monitor;

-- Create operational tables and grant permissions
\c loadbalancer_operational

-- Services registry table
CREATE TABLE IF NOT EXISTS lb_operational.services (
    service_id VARCHAR(100) PRIMARY KEY,
    service_name VARCHAR(200) NOT NULL,
    service_type VARCHAR(50) NOT NULL CHECK (service_type IN ('REST', 'GRPC', 'WEB_SOCKET')),
    description TEXT,
    version VARCHAR(50) DEFAULT '1.0.0',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    
    -- Health check configuration
    health_check_endpoint VARCHAR(500) DEFAULT '/health',
    health_check_interval INTEGER DEFAULT 30,
    health_check_timeout INTEGER DEFAULT 5,
    healthy_threshold INTEGER DEFAULT 2,
    unhealthy_threshold INTEGER DEFAULT 3,
    
    -- Metadata
    tags JSONB DEFAULT '{}',
    metadata JSONB DEFAULT '{}'
);

COMMENT ON TABLE lb_operational.services IS 'Registered services for load balancing';
COMMENT ON COLUMN lb_operational.services.service_type IS 'Type of service: REST, GRPC, WEB_SOCKET';

-- Service instances table
CREATE TABLE IF NOT EXISTS lb_operational.service_instances (
    instance_id VARCHAR(100) PRIMARY KEY,
    service_id VARCHAR(100) NOT NULL REFERENCES lb_operational.services(service_id) ON DELETE CASCADE,
    instance_url VARCHAR(500) NOT NULL,
    instance_host VARCHAR(200) NOT NULL,
    instance_port INTEGER NOT NULL,
    instance_weight INTEGER DEFAULT 100 CHECK (instance_weight BETWEEN 1 AND 1000),
    instance_priority INTEGER DEFAULT 1 CHECK (instance_priority BETWEEN 0 AND 10),
    
    -- Status fields
    enabled BOOLEAN DEFAULT TRUE,
    healthy BOOLEAN DEFAULT FALSE,
    last_health_check TIMESTAMPTZ,
    last_seen TIMESTAMPTZ DEFAULT NOW(),
    
    -- Capacity and limits
    max_connections INTEGER DEFAULT 1000,
    max_rps INTEGER DEFAULT 1000,
    
    -- Metadata
    instance_zone VARCHAR(100) DEFAULT 'default',
    instance_region VARCHAR(100) DEFAULT 'default',
    labels JSONB DEFAULT '{}',
    
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

COMMENT ON TABLE lb_operational.service_instances IS 'Registered instances of services';
COMMENT ON COLUMN lb_operational.service_instances.instance_weight IS 'Weight for weighted round-robin (1-1000)';

-- Routing strategies table
CREATE TABLE IF NOT EXISTS lb_operational.routing_strategies (
    strategy_id VARCHAR(100) PRIMARY KEY,
    strategy_name VARCHAR(200) NOT NULL,
    strategy_type VARCHAR(50) NOT NULL CHECK (strategy_type IN (
        'ROUND_ROBIN', 'WEIGHTED_ROUND_ROBIN', 'LEAST_CONNECTIONS', 
        'LEAST_RESPONSE_TIME', 'IP_HASH', 'URL_HASH'
    )),
    description TEXT,
    configuration JSONB NOT NULL DEFAULT '{}',
    enabled BOOLEAN DEFAULT TRUE,
    is_default BOOLEAN DEFAULT FALSE,
    
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

COMMENT ON TABLE lb_operational.routing_strategies IS 'Available routing strategies';

-- Service routing assignments
CREATE TABLE IF NOT EXISTS lb_operational.service_routing (
    service_id VARCHAR(100) PRIMARY KEY REFERENCES lb_operational.services(service_id) ON DELETE CASCADE,
    strategy_id VARCHAR(100) NOT NULL REFERENCES lb_operational.routing_strategies(strategy_id),
    configuration JSONB DEFAULT '{}',
    
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

COMMENT ON TABLE lb_operational.service_routing IS 'Routing strategy assignments for services';

-- Create metrics tables
\c loadbalancer_metrics

-- Service metrics table (TimescaleDB hypertable)
CREATE TABLE IF NOT EXISTS lb_metrics.service_metrics (
    time TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    service_id VARCHAR(100) NOT NULL,
    instance_id VARCHAR(100) NOT NULL,
    
    -- Performance metrics
    requests_total BIGINT DEFAULT 0,
    requests_per_second DOUBLE PRECISION,
    p50_latency DOUBLE PRECISION,
    p95_latency DOUBLE PRECISION,
    p99_latency DOUBLE PRECISION,
    error_rate DOUBLE PRECISION,
    error_count INTEGER DEFAULT 0,
    success_count INTEGER DEFAULT 0,
    
    -- Connection metrics
    active_connections INTEGER DEFAULT 0,
    connection_errors INTEGER DEFAULT 0,
    timeout_errors INTEGER DEFAULT 0,
    
    -- Resource utilization
    cpu_usage DOUBLE PRECISION,
    memory_usage DOUBLE PRECISION,
    heap_usage DOUBLE PRECISION,
    thread_count INTEGER,
    
    -- Business metrics
    response_size_avg DOUBLE PRECISION,
    request_size_avg DOUBLE PRECISION,
    
    -- Metadata
    tags JSONB DEFAULT '{}',
    
    PRIMARY KEY (time, service_id, instance_id)
);

COMMENT ON TABLE lb_metrics.service_metrics IS 'Time-series metrics for service instances';

-- Convert to hypertable
SELECT create_hypertable(
    'lb_metrics.service_metrics', 
    'time',
    if_not_exists => TRUE,
    migrate_data => TRUE
);

-- Health status table (TimescaleDB hypertable)
CREATE TABLE IF NOT EXISTS lb_metrics.health_status (
    time TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    service_id VARCHAR(100) NOT NULL,
    instance_id VARCHAR(100) NOT NULL,
    
    -- Health status
    status VARCHAR(20) NOT NULL CHECK (status IN ('HEALTHY', 'UNHEALTHY', 'UNKNOWN', 'DEGRADED')),
    response_time DOUBLE PRECISION,
    status_code INTEGER,
    error_message TEXT,
    
    -- Check details
    check_type VARCHAR(50) DEFAULT 'HTTP',
    check_endpoint VARCHAR(500),
    
    -- Metadata
    tags JSONB DEFAULT '{}',
    
    PRIMARY KEY (time, service_id, instance_id)
);

COMMENT ON TABLE lb_metrics.health_status IS 'Health check status history';

SELECT create_hypertable(
    'lb_metrics.health_status', 
    'time',
    if_not_exists => TRUE
);

-- Routing decisions table
CREATE TABLE IF NOT EXISTS lb_metrics.routing_decisions (
    time TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    request_id UUID DEFAULT uuid_generate_v4(),
    service_id VARCHAR(100) NOT NULL,
    selected_instance_id VARCHAR(100) NOT NULL,
    client_ip INET,
    
    -- Decision factors
    strategy_used VARCHAR(100),
    candidate_instances INTEGER,
    selection_duration DOUBLE PRECISION,
    
    -- Request context
    request_path VARCHAR(500),
    user_agent VARCHAR(500),
    
    -- Metadata
    tags JSONB DEFAULT '{}',
    
    PRIMARY KEY (time, request_id)
);

COMMENT ON TABLE lb_metrics.routing_decisions IS 'Load balancer routing decision log';

SELECT create_hypertable(
    'lb_metrics.routing_decisions', 
    'time',
    if_not_exists => TRUE
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_service_metrics_service_time 
ON lb_metrics.service_metrics (service_id, time DESC);

CREATE INDEX IF NOT EXISTS idx_service_metrics_instance_time 
ON lb_metrics.service_metrics (instance_id, time DESC);

CREATE INDEX IF NOT EXISTS idx_health_status_service_time 
ON lb_metrics.health_status (service_id, time DESC);

CREATE INDEX IF NOT EXISTS idx_health_status_instance_time 
ON lb_metrics.health_status (instance_id, time DESC);

CREATE INDEX IF NOT EXISTS idx_routing_decisions_service_time 
ON lb_metrics.routing_decisions (service_id, time DESC);

-- Set up retention policies
SELECT add_retention_policy('lb_metrics.service_metrics', INTERVAL '30 days', if_not_exists => TRUE);
SELECT add_retention_policy('lb_metrics.health_status', INTERVAL '15 days', if_not_exists => TRUE);
SELECT add_retention_policy('lb_metrics.routing_decisions', INTERVAL '7 days', if_not_exists => TRUE);

-- Log completion
DO $$ 
BEGIN
    RAISE NOTICE 'Permissions and schema setup completed successfully';
    RAISE NOTICE 'Created operational tables: services, service_instances, routing_strategies, service_routing';
    RAISE NOTICE 'Created metrics tables: service_metrics, health_status, routing_decisions';
    RAISE NOTICE 'Applied retention policies and created indexes';
END $$;
