#!/bin/bash

# Script for managing Smart Load Balancer Kafka operations

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Default values
KAFKA_BROKER=${KAFKA_BROKER:-localhost:9092}
KAFKA_INTERNAL_BROKER=${KAFKA_INTERNAL_BROKER:-kafka:29092}
ZOOKEEPER_HOST=${ZOOKEEPER_HOST:-localhost:2181}

# Function to print colored output
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if Kafka is ready
wait_for_kafka() {
    local max_attempts=30
    local attempt=1
    
    print_info "Waiting for Kafka to be ready..."
    
    while [ $attempt -le $max_attempts ]; do
        if kafka-topics --bootstrap-server $KAFKA_BROKER --list >/dev/null 2>&1; then
            print_info "Kafka is ready!"
            return 0
        fi
        
        print_warning "Attempt $attempt/$max_attempts - Kafka not ready, waiting..."
        sleep 5
        attempt=$((attempt + 1))
    done
    
    print_error "Kafka failed to become ready after $max_attempts attempts"
    return 1
}

# Function to check if Zookeeper is ready
wait_for_zookeeper() {
    local max_attempts=30
    local attempt=1
    
    print_info "Waiting for Zookeeper to be ready..."
    
    while [ $attempt -le $max_attempts ]; do
        if nc -z $(echo $ZOOKEEPER_HOST | sed 's/:/ /') >/dev/null 2>&1; then
            print_info "Zookeeper is ready!"
            return 0
        fi
        
        print_warning "Attempt $attempt/$max_attempts - Zookeeper not ready, waiting..."
        sleep 3
        attempt=$((attempt + 1))
    done
    
    print_error "Zookeeper failed to become ready after $max_attempts attempts"
    return 1
}

# Function to list topics
list_topics() {
    print_info "Listing Kafka topics:"
    kafka-topics --bootstrap-server $KAFKA_BROKER --list
}

# Function to describe topic
describe_topic() {
    local topic=$1
    
    if [ -z "$topic" ]; then
        print_error "Please specify topic name"
        return 1
    fi
    
    print_info "Describing topic: $topic"
    kafka-topics --bootstrap-server $KAFKA_BROKER --describe --topic "$topic"
}

# Function to create topic
create_topic() {
    local topic=$1
    local partitions=${2:-3}
    local replication=${3:-1}
    
    if [ -z "$topic" ]; then
        print_error "Please specify topic name"
        return 1
    fi
    
    print_info "Creating topic: $topic (partitions: $partitions, replication: $replication)"
    
    kafka-topics --bootstrap-server $KAFKA_BROKER \
        --create \
        --topic "$topic" \
        --partitions "$partitions" \
        --replication-factor "$replication" \
        --if-not-exists
}

# Function to delete topic
delete_topic() {
    local topic=$1
    
    if [ -z "$topic" ]; then
        print_error "Please specify topic name"
        return 1
    fi
    
    print_warning "Deleting topic: $topic"
    read -p "Are you sure? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_info "Deletion cancelled"
        return 0
    fi
    
    kafka-topics --bootstrap-server $KAFKA_BROKER --delete --topic "$topic"
    print_info "Topic $topic deleted"
}

# Function to produce test message
produce_message() {
    local topic=$1
    local message=${2:-"test message"}
    
    if [ -z "$topic" ]; then
        print_error "Please specify topic name"
        return 1
    fi
    
    print_info "Producing message to topic: $topic"
    echo "$message" | kafka-console-producer --bootstrap-server $KAFKA_BROKER --topic "$topic"
}

# Function to consume messages
consume_messages() {
    local topic=$1
    local max_messages=${2:-10}
    
    if [ -z "$topic" ]; then
        print_error "Please specify topic name"
        return 1
    fi
    
    print_info "Consuming messages from topic: $topic (max: $max_messages)"
    kafka-console-consumer --bootstrap-server $KAFKA_BROKER \
        --topic "$topic" \
        --from-beginning \
        --max-messages "$max_messages" \
        --timeout-ms 5000
}

# Function to show consumer groups
list_consumer_groups() {
    print_info "Listing consumer groups:"
    kafka-consumer-groups --bootstrap-server $KAFKA_BROKER --list
}

# Function to describe consumer group
describe_consumer_group() {
    local group=$1
    
    if [ -z "$group" ]; then
        print_error "Please specify consumer group name"
        return 1
    fi
    
    print_info "Describing consumer group: $group"
    kafka-consumer-groups --bootstrap-server $KAFKA_BROKER --describe --group "$group"
}

# Function to show cluster status
show_cluster_status() {
    print_info "Kafka Cluster Status:"
    echo "Bootstrap Server: $KAFKA_BROKER"
    echo "Zookeeper: $ZOOKEEPER_HOST"
    
    # Check Kafka connectivity
    if kafka-topics --bootstrap-server $KAFKA_BROKER --list >/dev/null 2>&1; then
        print_info "Kafka Connection: OK"
        
        # Show basic cluster info
        print_info "Cluster Topics:"
        kafka-topics --bootstrap-server $KAFKA_BROKER --list
        
        print_info "Broker Information:"
        kafka-broker-api-versions --bootstrap-server $KAFKA_BROKER | head -10
    else
        print_error "Kafka Connection: FAILED"
    fi
}

# Main script logic
case "${1:-}" in
    "start")
        docker-compose -f docker-compose.kafka.yml up -d
        wait_for_zookeeper
        wait_for_kafka
        ;;
    "stop")
        docker-compose -f docker-compose.kafka.yml down
        ;;
    "restart")
        docker-compose -f docker-compose.kafka.yml restart
        wait_for_zookeeper
        wait_for_kafka
        ;;
    "status")
        show_cluster_status
        ;;
    "topics")
        list_topics
        ;;
    "describe-topic")
        describe_topic "$2"
        ;;
    "create-topic")
        create_topic "$2" "$3" "$4"
        ;;
    "delete-topic")
        delete_topic "$2"
        ;;
    "produce")
        produce_message "$2" "$3"
        ;;
    "consume")
        consume_messages "$2" "$3"
        ;;
    "groups")
        list_consumer_groups
        ;;
    "describe-group")
        describe_consumer_group "$2"
        ;;
    "logs")
        docker-compose -f docker-compose.kafka.yml logs -f "$2"
        ;;
    "init-topics")
        wait_for_kafka
        docker exec smartlb-kafka /usr/bin/create-topics.sh
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status|topics|describe-topic|create-topic|delete-topic|produce|consume|groups|describe-group|logs|init-topics}"
        echo ""
        echo "Commands:"
        echo "  start           - Start Kafka services"
        echo "  stop            - Stop Kafka services"
        echo "  restart         - Restart Kafka services"
        echo "  status          - Show cluster status"
        echo "  topics          - List all topics"
        echo "  describe-topic  - Describe specific topic"
        echo "  create-topic    - Create new topic"
        echo "  delete-topic    - Delete topic"
        echo "  produce         - Produce test message to topic"
        echo "  consume         - Consume messages from topic"
        echo "  groups          - List consumer groups"
        echo "  describe-group  - Describe consumer group"
        echo "  logs            - Show service logs"
        echo "  init-topics     - Initialize required topics"
        echo ""
        echo "Environment variables:"
        echo "  KAFKA_BROKER, KAFKA_INTERNAL_BROKER, ZOOKEEPER_HOST"
        exit 1
        ;;
esac
