# Kafka Infrastructure for Smart Load Balancer

This directory contains the Kafka configuration and management scripts for the Smart Load Balancer project.

## Services

- **Zookeeper**: Coordination service for Kafka cluster
- **Kafka**: Message broker for metrics and events
- **Kafka UI**: Web-based administration and monitoring tool
- **Kafka Connect**: Framework for stream data integration (optional)

## Quick Start

### Starting Kafka Services

```bash
# Start all Kafka services
./deployment/kafka/kafka-management.sh start

# Initialize required topics
./deployment/kafka/kafka-management.sh init-topics

# Check cluster status
./deployment/kafka/kafka-management.sh status
```
## Accessing Services

- Kafka Broker: localhost:9092 (external), kafka:29092 (internal)

- Zookeeper: localhost:2181

- Kafka UI: http://localhost:8083

- Kafka Connect: http://localhost:8084 (optional)

## Topics

The following topics are automatically created:

### Core Topics

- service-metrics: Time-series metrics from services (RPS, latency, errors)
- health-status: Health check results and status updates
- routing-decisions: Load balancer routing decisions and changes
- config-updates: Configuration changes and updates
- alerts: System alerts and notifications
- audit-logs: Audit trail for system operations

## Internal Processing Topics

- metrics-aggregated: Aggregated metrics for streams processing
- health-scores: Calculated health scores for instances

## Message Schemas

### Service Metrics Message

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "serviceId": "user-service",
  "instanceId": "user-service-1",
  "metrics": {
    "rps": 150.5,
    "p95Latency": 45.2,
    "errorRate": 0.02,
    "activeConnections": 25,
    "cpuUsage": 0.65,
    "memoryUsage": 0.42
  },
  "tags": {
    "environment": "production",
    "region": "us-east-1"
  }
}
```
## Health Status Message

 ```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "serviceId": "user-service",
  "instanceId": "user-service-1",
  "status": "HEALTHY",
  "responseTime": 25.4,
  "checkType": "HTTP",
  "details": {
    "statusCode": 200,
    "responseBody": "OK"
  }
}
```
## Management

### Topic Operations

```bash
# List all topics
./deployment/kafka/kafka-management.sh topics

# Create custom topic
./deployment/kafka/kafka-management.sh create-topic my-topic 3 1

# Describe topic
./deployment/kafka/kafka-management.sh describe-topic service-metrics

# Produce test message
./deployment/kafka/kafka-management.sh produce service-metrics '{"test": "message"}'

# Consume messages
./deployment/kafka/kafka-management.sh consume service-metrics 5
```
### Consumer Groups

```bash
# List consumer groups
./deployment/kafka/kafka-management.sh groups

# Describe consumer group
./deployment/kafka/kafka-management.sh describe-group my-consumer-group
```
### Maintenance

```bash
# Stop services
./deployment/kafka/kafka-management.sh stop

# View logs
./deployment/kafka/kafka-management.sh logs kafka

# Restart services
./deployment/kafka/kafka-management.sh restart
```

## Configuration

### Environment Variables

```bash
export KAFKA_BROKER=localhost:9092
export KAFKA_INTERNAL_BROKER=kafka:29092
export ZOOKEEPER_HOST=localhost:2181
```
## Kafka Settings

- Partitions: 3 for high-throughput topics
- Replication: 1 (single node cluster)
- Retention: 7 days for metrics, 30 days for audit logs
- Message Size: Up to 10MB for large metrics payloads

## Monitoring

### Kafka UI Features

- Topic browsing and message inspection
- Consumer group monitoring
- Broker metrics and health
- Real-time message streaming
- Topic configuration management

### Health Checks

- Zookeeper: TCP connectivity and mode detection
- Kafka: Topic listing capability
- Kafka UI: HTTP endpoint responsiveness

## Integration with Load Balancer

The load balancer uses Kafka for:

- Metrics Collection: Services publish metrics to service-metrics topic
- Health Monitoring: Health checks publish to health-status topic
- Configuration Distribution: Config changes via config-updates topic
- Event Processing: Stream processing for real-time analytics
- Alerting: System alerts published to alerts topic

## Performance Considerations

- Batch Size: Configure appropriate batch sizes for metrics collection
- Compression: Enable compression for large metric payloads
- Partitioning: Distribute load across partitions based on service ID
- Retention: Adjust retention based on storage requirements and compliance
