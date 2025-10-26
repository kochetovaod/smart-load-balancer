-- Smart Load Balancer Test Data
-- Script: 04-test-data.sql
-- Purpose: Insert test data for development and testing

-- Operational database test data
\c loadbalancer_operational

-- Insert routing strategies
INSERT INTO lb_operational.routing_strategies (strategy_id, strategy_name, strategy_type, description, configuration, is_default) VALUES
('round_robin', 'Round Robin', 'ROUND_ROBIN', 'Distribute requests evenly across all available instances', '{"max_attempts": 3}', true),
('weighted_rr', 'Weighted Round Robin', 'WEIGHTED_ROUND_ROBIN', 'Distribute requests based on instance weights', '{"weight_field": "instance_weight"}', false),
('least_conn', 'Least Connections', 'LEAST_CONNECTIONS', 'Route to instance with fewest active connections', '{"consider_weights": true}', false),
('least_resp', 'Least Response Time', 'LEAST_RESPONSE_TIME', 'Route to instance with lowest average response time', '{"window_size": "5m", "min_samples": 10}', false),
('ip_hash', 'IP Hash', 'IP_HASH', 'Route based on client IP address for session affinity', '{"hash_header": "x-forwarded-for"}', false)
ON CONFLICT (strategy_id) DO UPDATE SET
    strategy_name = EXCLUDED.strategy_name,
    strategy_type = EXCLUDED.strategy_type,
    description = EXCLUDED.description,
    configuration = EXCLUDED.configuration,
    is_default = EXCLUDED.is_default;

-- Insert test services
INSERT INTO lb_operational.services (service_id, service_name, service_type, description, health_check_endpoint, health_check_interval, tags) VALUES
('api-gateway', 'API Gateway', 'REST', 'Main API gateway service', '/health', 15, '{"critical": true, "environment": "development"}'),
('user-service', 'User Service', 'REST', 'User management and authentication', '/actuator/health', 30, '{"critical": true, "team": "identity"}'),
('order-service', 'Order Service', 'REST', 'Order processing and management', '/health', 30, '{"critical": true, "team": "orders"}'),
('product-service', 'Product Service', 'REST', 'Product catalog and inventory', '/status', 45, '{"critical": false, "team": "catalog"}'),
('payment-service', 'Payment Service', 'REST', 'Payment processing', '/health', 30, '{"critical": true, "team": "payments", "pci": true}'),
('notification-service', 'Notification Service', 'REST', 'Email and push notifications', '/ready', 60, '{"critical": false, "team": "notifications"}')
ON CONFLICT (service_id) DO UPDATE SET
    service_name = EXCLUDED.service_name,
    service_type = EXCLUDED.service_type,
    description = EXCLUDED.description,
    health_check_endpoint = EXCLUDED.health_check_endpoint,
    health_check_interval = EXCLUDED.health_check_interval,
    tags = EXCLUDED.tags;

-- Insert test service instances
INSERT INTO lb_operational.service_instances (instance_id, service_id, instance_url, instance_host, instance_port, instance_weight, instance_priority, max_connections, instance_zone, instance_region, labels) VALUES
-- API Gateway instances
('api-gw-1', 'api-gateway', 'http://api-gw-1:8080', 'api-gw-1', 8080, 100, 1, 5000, 'zone-a', 'us-east-1', '{"version": "1.2.3", "commit": "abc123"}'),
('api-gw-2', 'api-gateway', 'http://api-gw-2:8080', 'api-gw-2', 8080, 100, 1, 5000, 'zone-b', 'us-east-1', '{"version": "1.2.3", "commit": "def456"}'),
('api-gw-3', 'api-gateway', 'http://api-gw-3:8080', 'api-gw-3', 8080, 75, 2, 3000, 'zone-c', 'us-east-1', '{"version": "1.2.2", "commit": "ghi789"}'),

-- User Service instances
('user-svc-1', 'user-service', 'http://user-svc-1:8081', 'user-svc-1', 8081, 100, 1, 1000, 'zone-a', 'us-east-1', '{"version": "2.1.0", "commit": "xyz123"}'),
('user-svc-2', 'user-service', 'http://user-svc-2:8081', 'user-svc-2', 8081, 100, 1, 1000, 'zone-b', 'us-east-1', '{"version": "2.1.0", "commit": "xyz456"}'),
('user-svc-3', 'user-service', 'http://user-svc-3:8081', 'user-svc-3', 8081, 50, 3, 500, 'zone-c', 'us-east-1', '{"version": "2.0.1", "commit": "xyz789"}'),

-- Order Service instances
('order-svc-1', 'order-service', 'http://order-svc-1:8082', 'order-svc-1', 8082, 100, 1, 800, 'zone-a', 'us-east-1', '{"version": "1.5.2", "commit": "ord123"}'),
('order-svc-2', 'order-service', 'http://order-svc-2:8082', 'order-svc-2', 8082, 100, 1, 800, 'zone-b', 'us-east-1', '{"version": "1.5.2", "commit": "ord456"}'),

-- Product Service instances
('product-svc-1', 'product-service', 'http://product-svc-1:8083', 'product-svc-1', 8083, 100, 1, 600, 'zone-a', 'us-east-1', '{"version": "3.0.0", "commit": "prod123"}'),
('product-svc-2', 'product-service', 'http://product-svc-2:8083', 'product-svc-2', 8083, 100, 1, 600, 'zone-b', 'us-east-1', '{"version": "3.0.0", "commit": "prod456"}'),

-- Payment Service instances
('payment-svc-1', 'payment-service', 'https://payment-svc-1:8443', 'payment-svc-1', 8443, 100, 1, 200, 'zone-a', 'us-east-1', '{"version": "1.0.1", "commit": "pay123", "secure": true}'),

-- Notification Service instances
('notification-svc-1', 'notification-service', 'http://notification-svc-1:8084', 'notification-svc-1', 8084, 100, 1, 300, 'zone-a', 'us-east-1', '{"version": "1.2.0", "commit": "notif123"}')
ON CONFLICT (instance_id) DO UPDATE SET
    service_id = EXCLUDED.service_id,
    instance_url = EXCLUDED.instance_url,
    instance_host = EXCLUDED.instance_host,
    instance_port = EXCLUDED.instance_port,
    instance_weight = EXCLUDED.instance_weight,
    instance_priority = EXCLUDED.instance_priority,
    max_connections = EXCLUDED.max_connections,
    instance_zone = EXCLUDED.instance_zone,
    instance_region = EXCLUDED.instance_region,
    labels = EXCLUDED.labels;

-- Assign routing strategies to services
INSERT INTO lb_operational.service_routing (service_id, strategy_id, configuration) VALUES
('api-gateway', 'round_robin', '{}'),
('user-service', 'weighted_rr', '{"consider_health": true}'),
('order-service', 'least_conn', '{"consider_weights": true}'),
('product-service', 'round_robin', '{}'),
('payment-service', 'ip_hash', '{"hash_header": "x-client-ip"}'),
('notification-service', 'least_resp', '{"window_size": "10m"}')
ON CONFLICT (service_id) DO UPDATE SET
    strategy_id = EXCLUDED.strategy_id,
    configuration = EXCLUDED.configuration;

-- Mark some instances as healthy for testing
UPDATE lb_operational.service_instances 
SET healthy = true, last_health_check = NOW()
WHERE instance_id IN ('api-gw-1', 'api-gw-2', 'user-svc-1', 'user-svc-2', 'order-svc-1', 'product-svc-1', 'payment-svc-1', 'notification-svc-1');

-- Insert test metrics data
\c loadbalancer_metrics

-- Generate test metrics for the last hour (for development/demo purposes)
INSERT INTO lb_metrics.service_metrics (time, service_id, instance_id, requests_per_second, p95_latency, error_rate, active_connections, cpu_usage, memory_usage)
SELECT 
    time,
    service_id,
    instance_id,
    -- Realistic metric patterns
    (50 + random() * 100)::double precision as rps,
    (20 + random() * 80)::double precision as p95_latency,
    (random() * 0.05)::double precision as error_rate,
    (10 + random() * 50)::integer as active_connections,
    (0.1 + random() * 0.7)::double precision as cpu_usage,
    (0.2 + random() * 0.6)::double precision as memory_usage
FROM 
    generate_series(
        NOW() - INTERVAL '1 hour',
        NOW(),
        INTERVAL '1 minute'
    ) as time,
    (VALUES 
        ('api-gateway', 'api-gw-1'),
        ('api-gateway', 'api-gw-2'),
        ('user-service', 'user-svc-1'),
        ('user-service', 'user-svc-2'),
        ('order-service', 'order-svc-1'),
        ('product-service', 'product-svc-1'),
        ('payment-service', 'payment-svc-1'),
        ('notification-service', 'notification-svc-1')
    ) as instances(service_id, instance_id)
ON CONFLICT (time, service_id, instance_id) DO NOTHING;

-- Insert health status history
INSERT INTO lb_metrics.health_status (time, service_id, instance_id, status, response_time, status_code)
SELECT 
    time,
    service_id,
    instance_id,
    CASE 
        WHEN random() < 0.95 THEN 'HEALTHY' 
        ELSE 'UNHEALTHY' 
    END as status,
    (5 + random() * 45)::double precision as response_time,
    CASE 
        WHEN random() < 0.95 THEN 200 
        ELSE 500 
    END as status_code
FROM 
    generate_series(
        NOW() - INTERVAL '30 minutes',
        NOW(),
        INTERVAL '30 seconds'
    ) as time,
    (VALUES 
        ('api-gateway', 'api-gw-1'),
        ('api-gateway', 'api-gw-2'),
        ('user-service', 'user-svc-1'),
        ('user-service', 'user-svc-2')
    ) as instances(service_id, instance_id)
ON CONFLICT (time, service_id, instance_id) DO NOTHING;

-- Insert sample routing decisions
INSERT INTO lb_metrics.routing_decisions (time, service_id, selected_instance_id, client_ip, strategy_used, candidate_instances, selection_duration, request_path)
SELECT 
    time,
    service_id,
    instance_id,
    ('192.168.1.' || (floor(random() * 255)::integer))::inet as client_ip,
    'round_robin' as strategy_used,
    2 as candidate_instances,
    (0.1 + random() * 2)::double precision as selection_duration,
    ('/api/' || (CASE floor(random() * 5)
        WHEN 0 THEN 'users'
        WHEN 1 THEN 'orders'
        WHEN 2 THEN 'products'
        WHEN 3 THEN 'payments'
        ELSE 'health'
    END)) as request_path
FROM 
    generate_series(
        NOW() - INTERVAL '15 minutes',
        NOW(),
        INTERVAL '10 seconds'
    ) as time,
    (VALUES 
        ('api-gateway', 'api-gw-1'),
        ('api-gateway', 'api-gw-2'),
        ('user-service', 'user-svc-1'),
        ('user-service', 'user-svc-2'),
        ('order-service', 'order-svc-1')
    ) as instances(service_id, instance_id)
ON CONFLICT (time, request_id) DO NOTHING;

-- Create useful views for monitoring
\c loadbalancer_metrics

-- Current health status view
CREATE OR REPLACE VIEW lb_metrics.current_health_status AS
SELECT DISTINCT ON (service_id, instance_id)
    service_id,
    instance_id,
    status,
    response_time,
    time as last_check
FROM lb_metrics.health_status
ORDER BY service_id, instance_id, time DESC;

-- Service metrics summary (last 5 minutes)
CREATE OR REPLACE VIEW lb_metrics.service_metrics_summary AS
SELECT 
    service_id,
    instance_id,
    AVG(requests_per_second) as avg_rps,
    PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY p95_latency) as p95_latency,
    AVG(error_rate) as avg_error_rate,
    MAX(active_connections) as max_connections,
    AVG(cpu_usage) as avg_cpu_usage,
    COUNT(*) as sample_count
FROM lb_metrics.service_metrics
WHERE time > NOW() - INTERVAL '5 minutes'
GROUP BY service_id, instance_id
ORDER BY service_id, instance_id;

-- Routing statistics view
CREATE OR REPLACE VIEW lb_metrics.routing_stats AS
SELECT 
    service_id,
    selected_instance_id,
    COUNT(*) as request_count,
    AVG(selection_duration) as avg_selection_time,
    COUNT(DISTINCT client_ip) as unique_clients
FROM lb_metrics.routing_decisions
WHERE time > NOW() - INTERVAL '1 hour'
GROUP BY service_id, selected_instance_id
ORDER BY request_count DESC;

-- Grant permissions to views
GRANT SELECT ON lb_metrics.current_health_status TO lb_operator, lb_metrics, lb_monitor;
GRANT SELECT ON lb_metrics.service_metrics_summary TO lb_operator, lb_metrics, lb_monitor;
GRANT SELECT ON lb_metrics.routing_stats TO lb_operator, lb_metrics, lb_monitor;

-- Log completion with data summary
\c loadbalancer_operational

DO $$ 
DECLARE
    service_count INTEGER;
    instance_count INTEGER;
    strategy_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO service_count FROM lb_operational.services;
    SELECT COUNT(*) INTO instance_count FROM lb_operational.service_instances;
    SELECT COUNT(*) INTO strategy_count FROM lb_operational.routing_strategies;
    
    RAISE NOTICE 'Test data insertion completed successfully';
    RAISE NOTICE 'Inserted: % services, % instances, % routing strategies', service_count, instance_count, strategy_count;
    
    -- Show healthy instances
    RAISE NOTICE 'Healthy instances by service:';
    FOR service_rec IN 
        SELECT s.service_id, s.service_name, COUNT(i.instance_id) as healthy_count
        FROM lb_operational.services s
        LEFT JOIN lb_operational.service_instances i ON s.service_id = i.service_id AND i.healthy = true
        GROUP BY s.service_id, s.service_name
        ORDER BY s.service_id
    LOOP
        RAISE NOTICE '  % (%): % healthy instances', service_rec.service_id, service_rec.service_name, service_rec.healthy_count;
    END LOOP;
END $$;
