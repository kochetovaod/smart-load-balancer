# Smart Load Balancer - Docker Compose Management

This directory contains the main Docker Compose configuration and management scripts for the Smart Load Balancer project.

## Quick Start

### Prerequisites

- Docker 20.10+
- Docker Compose 2.0+
- Java 17+ (for building)
- 4GB+ RAM available

### Starting All Services

```bash
# Build and start all services
./deployment/management.sh start --build

# Or start without building
./deployment/management.sh start

# Check status
./deployment/management.sh status
```

Service URLs
After starting, access the services at:

Grafana: http://localhost:3000

Username: admin

Password: grafana_admin_2024

Prometheus: http://localhost:9090

Kafka UI: http://localhost:8083

PgAdmin: http://localhost:8082

Email: admin@smartlb.local

Password: admin_password_123

Jaeger: http://localhost:16686

Load Balancer: http://localhost:8080

Mock Services: http://localhost:8085, http://localhost:8086

Service Architecture
Core Services
Load Balancer Core: Main routing logic and API

Metrics Agent: Collects and publishes metrics

Infrastructure Services
PostgreSQL + TimescaleDB: Operational data and metrics storage

Kafka + Zookeeper: Message bus for metrics and events

Prometheus: Metrics collection and alerting

Grafana: Metrics visualization and dashboards

Jaeger: Distributed tracing (optional)

Management Services
PgAdmin: Database administration

Kafka UI: Kafka cluster management

Mock Services
Mock User/Order Services: Test services for load balancing

Management Commands
Basic Operations

```bash
# Start services
./deployment/management.sh start

# Stop services
./deployment/management.sh stop

# Restart services
./deployment/management.sh restart

# Show status
./deployment/management.sh status

# View logs
./deployment/management.sh logs
./deployment/management.sh logs kafka
```

## Database Management

```bash
# Database operations
./deployment/management.sh db init
./deployment/management.sh db backup
./deployment/management.sh db status
```

## Kafka Management

```bash
# Kafka operations
./deployment/management.sh kafka topics
./deployment/management.sh kafka create-topic my-topic
./deployment/management.sh kafka consume service-metrics
```

## Scaling and Maintenance

```bash
# Scale services
./deployment/management.sh scale metrics-agent 3

# Backup data
./deployment/management.sh backup

# Cleanup everything
./deployment/management.sh cleanup

# Update services
./deployment/management.sh update
```

### Health Checks

All critical services include health checks with proper dependencies:

Zookeeper → Kafka → Database → Applications

Prometheus depends on database and Kafka

Grafana depends on Prometheus

Applications depend on both database and Kafka

### Monitoring Stack

Prometheus Configuration
Scrapes metrics from all services every 15s

Alert rules for critical conditions

30-day data retention

### Grafana Configuration

Pre-configured datasources (Prometheus, Jaeger, PostgreSQL)

### Dashboard provisioning

Default admin credentials
