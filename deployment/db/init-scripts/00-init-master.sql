-- Smart Load Balancer Master Initialization Script
-- This script orchestrates the execution of all initialization scripts

\set ON_ERROR_STOP on

-- Log start
\echo 'Starting Smart Load Balancer database initialization...'
\echo 'Timestamp: ' `date`

-- Execute initialization scripts in order
\echo 'Executing 01-init-databases.sql...'
\i /docker-entrypoint-initdb.d/01-init-databases.sql

\echo 'Executing 02-create-users.sql...'
\i /docker-entrypoint-initdb.d/02-create-users.sql

\echo 'Executing 03-grant-permissions.sql...'
\i /docker-entrypoint-initdb.d/03-grant-permissions.sql

\echo 'Executing 04-test-data.sql...'
\i /docker-entrypoint-initdb.d/04-test-data.sql

-- Log completion
\echo 'Database initialization completed successfully!'
\echo 'Timestamp: ' `date`

-- Display summary
\c loadbalancer_operational
SELECT 
    'Database Summary' as summary,
    (SELECT COUNT(*) FROM lb_operational.services) as services,
    (SELECT COUNT(*) FROM lb_operational.service_instances) as instances,
    (SELECT COUNT(*) FROM lb_operational.routing_strategies) as strategies;

\c loadbalancer_metrics
SELECT 
    'Metrics Summary' as summary,
    (SELECT COUNT(*) FROM lb_metrics.service_metrics) as service_metrics,
    (SELECT COUNT(*) FROM lb_metrics.health_status) as health_checks,
    (SELECT COUNT(*) FROM lb_metrics.routing_decisions) as routing_decisions;
