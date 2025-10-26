#!/bin/bash

# Script for creating Kafka topics for Smart Load Balancer
# This script runs when Kafka starts up

set -e

# Wait for Kafka to be ready
echo "Waiting for Kafka to be ready..."
while ! kafka-topics --bootstrap-server localhost:29092 --list > /dev/null 2>&1; do
    echo "Kafka not ready yet, waiting..."
    sleep 5
done

echo "Kafka is ready, creating topics..."

# Topic for service metrics
kafka-topics --bootstrap-server localhost:29092 \
    --create \
    --topic service-metrics \
    --partitions 3 \
    --replication-factor 1 \
    --config retention.ms=604800000 \
    --config retention.bytes=1073741824 \
    --config cleanup.policy=delete \
    --if-not-exists

# Topic for health status updates
kafka-topics --bootstrap-server localhost:29092 \
    --create \
    --topic health-status \
    --partitions 2 \
    --replication-factor 1 \
    --config retention.ms=259200000 \
    --config retention.bytes=536870912 \
    --config cleanup.policy=delete \
    --if-not-exists

# Topic for routing decisions
kafka-topics --bootstrap-server localhost:29092 \
    --create \
    --topic routing-decisions \
    --partitions 3 \
    --replication-factor 1 \
    --config retention.ms=86400000 \
    --config retention.bytes=268435456 \
    --config cleanup.policy=compact \
    --if-not-exists

# Topic for configuration updates
kafka-topics --bootstrap-server localhost:29092 \
    --create \
    --topic config-updates \
    --partitions 1 \
    --replication-factor 1 \
    --config retention.ms=-1 \
    --config retention.bytes=-1 \
    --config cleanup.policy=compact \
    --if-not-exists

# Topic for alerts and notifications
kafka-topics --bootstrap-server localhost:29092 \
    --create \
    --topic alerts \
    --partitions 2 \
    --replication-factor 1 \
    --config retention.ms=172800000 \
    --config retention.bytes=268435456 \
    --config cleanup.policy=delete \
    --if-not-exists

# Topic for audit logs
kafka-topics --bootstrap-server localhost:29092 \
    --create \
    --topic audit-logs \
    --partitions 1 \
    --replication-factor 1 \
    --config retention.ms=2592000000 \
    --config retention.bytes=1073741824 \
    --config cleanup.policy=delete \
    --if-not-exists

# Internal topics for streams processing
kafka-topics --bootstrap-server localhost:29092 \
    --create \
    --topic metrics-aggregated \
    --partitions 3 \
    --replication-factor 1 \
    --config retention.ms=300000 \
    --config retention.bytes=536870912 \
    --config cleanup.policy=delete \
    --if-not-exists

kafka-topics --bootstrap-server localhost:29092 \
    --create \
    --topic health-scores \
    --partitions 3 \
    --replication-factor 1 \
    --config retention.ms=600000 \
    --config retention.bytes=268435456 \
    --config cleanup.policy=delete \
    --if-not-exists

echo "All topics created successfully:"
kafka-topics --bootstrap-server localhost:29092 --list
