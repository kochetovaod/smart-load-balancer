-- Smart Load Balancer User Creation
-- Script: 02-create-users.sql
-- Purpose: Create application users with appropriate permissions

-- Switch to operational database to create schemas and users
\c loadbalancer_operational

-- Create operational schema
CREATE SCHEMA IF NOT EXISTS lb_operational;
COMMENT ON SCHEMA lb_operational IS 'Schema for load balancer operational data and configuration';

-- Create metrics schema in metrics database
\c loadbalancer_metrics
CREATE SCHEMA IF NOT EXISTS lb_metrics;
COMMENT ON SCHEMA lb_metrics IS 'Schema for load balancer time-series metrics data';

-- Create operator user for operational database
\c loadbalancer_operational
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'lb_operator') THEN
        CREATE ROLE lb_operator WITH
            LOGIN
            NOSUPERUSER
            NOCREATEDB
            NOCREATEROLE
            INHERIT
            NOREPLICATION
            CONNECTION LIMIT 10
            PASSWORD 'lb_operator_password_2024';
        
        RAISE NOTICE 'User lb_operator created';
    ELSE
        RAISE NOTICE 'User lb_operator already exists';
    END IF;
END $$;

COMMENT ON ROLE lb_operator IS 'Operator user for load balancer operational database';

-- Create metrics user for metrics database
\c loadbalancer_metrics
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'lb_metrics') THEN
        CREATE ROLE lb_metrics WITH
            LOGIN
            NOSUPERUSER
            NOCREATEDB
            NOCREATEROLE
            INHERIT
            NOREPLICATION
            CONNECTION LIMIT 50
            PASSWORD 'lb_metrics_password_2024';
        
        RAISE NOTICE 'User lb_metrics created';
    ELSE
        RAISE NOTICE 'User lb_metrics already exists';
    END IF;
END $$;

COMMENT ON ROLE lb_metrics IS 'Metrics user for load balancer time-series data';

-- Create read-only user for monitoring
\c postgres
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'lb_monitor') THEN
        CREATE ROLE lb_monitor WITH
            LOGIN
            NOSUPERUSER
            NOCREATEDB
            NOCREATEROLE
            INHERIT
            NOREPLICATION
            CONNECTION LIMIT 5
            PASSWORD 'lb_monitor_password_2024';
        
        RAISE NOTICE 'User lb_monitor created';
    ELSE
        RAISE NOTICE 'User lb_monitor already exists';
    END IF;
END $$;

COMMENT ON ROLE lb_monitor IS 'Read-only user for monitoring and dashboards';

-- Grant connection permissions
GRANT CONNECT ON DATABASE loadbalancer_operational TO lb_operator;
GRANT CONNECT ON DATABASE loadbalancer_metrics TO lb_metrics;
GRANT CONNECT ON DATABASE loadbalancer_operational TO lb_monitor;
GRANT CONNECT ON DATABASE loadbalancer_metrics TO lb_monitor;

-- Log completion
DO $$ 
BEGIN
    RAISE NOTICE 'User creation completed successfully';
    RAISE NOTICE 'Created users: lb_operator, lb_metrics, lb_monitor';
END $$;
