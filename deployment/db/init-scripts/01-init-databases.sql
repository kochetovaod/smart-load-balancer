-- Smart Load Balancer Database Initialization
-- Script: 01-init-databases.sql
-- Purpose: Create operational and metrics databases

-- Create operational database for load balancer configuration and state
CREATE DATABASE loadbalancer_operational
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TEMPLATE = template0
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE loadbalancer_operational IS 'Operational database for Smart Load Balancer configuration and runtime state';

-- Create metrics database for time-series metrics data
CREATE DATABASE loadbalancer_metrics
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TEMPLATE = template0
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE loadbalancer_metrics IS 'Time-series metrics database for Smart Load Balancer performance data';

-- Grant basic permissions
GRANT CONNECT ON DATABASE loadbalancer_operational TO PUBLIC;
GRANT CONNECT ON DATABASE loadbalancer_metrics TO PUBLIC;

-- Create extensions in both databases
\c loadbalancer_operational

-- Enable UUID extension for unique identifiers
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Enable TimescaleDB for time-series data in metrics database
\c loadbalancer_metrics

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;

-- Set up search paths for convenience
ALTER DATABASE loadbalancer_operational SET search_path TO public, lb_operational;
ALTER DATABASE loadbalancer_metrics SET search_path TO public, lb_metrics;

-- Log completion
DO $$ 
BEGIN
    RAISE NOTICE 'Database initialization completed successfully';
    RAISE NOTICE 'Created databases: loadbalancer_operational, loadbalancer_metrics';
END $$;
