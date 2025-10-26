#!/bin/bash

# Smart Load Balancer Management Script
# Unified script for managing all services

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default values
COMPOSE_FILE="docker-compose.yml"
PROJECT_NAME="smartlb"

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

print_debug() {
    echo -e "${BLUE}[DEBUG]${NC} $1"
}

# Function to check dependencies
check_dependencies() {
    local deps=("docker" "docker-compose")
    
    for dep in "${deps[@]}"; do
        if ! command -v "$dep" &> /dev/null; then
            print_error "Required dependency not found: $dep"
            exit 1
        fi
    done
    
    print_info "All dependencies are available"
}

# Function to wait for services to be healthy
wait_for_services() {
    local timeout=${1:-300}
    local start_time=$(date +%s)
    
    print_info "Waiting for services to be healthy (timeout: ${timeout}s)..."
    
    while [ $(($(date +%s) - start_time)) -lt $timeout ]; do
        local unhealthy_services=$(docker-compose -f $COMPOSE_FILE -p $PROJECT_NAME ps --services | \
            xargs -I {} docker-compose -f $COMPOSE_FILE -p $PROJECT_NAME ps {} | \
            grep -v "Up (healthy)" | grep "Up" | wc -l)
        
        if [ "$unhealthy_services" -eq 0 ]; then
            print_info "All services are healthy!"
            return 0
        fi
        
        print_debug "Waiting for $unhealthy_services services to become healthy..."
        sleep 10
    done
    
    print_error "Timeout waiting for services to become healthy"
    return 1
}

# Function to start all services
start_services() {
    print_info "Starting Smart Load Balancer services..."
    
    # Build project first if needed
    if [ "$1" == "--build" ]; then
        print_info "Building project..."
        ./gradlew clean build
    fi
    
    # Start infrastructure
    docker-compose -f $COMPOSE_FILE -p $PROJECT_NAME up -d
    
    # Wait for services to be ready
    wait_for_services
    
    print_info "Services started successfully!"
    show_status
}

# Function to stop services
stop_services() {
    print_info "Stopping Smart Load Balancer services..."
    docker-compose -f $COMPOSE_FILE -p $PROJECT_NAME down
    print_info "Services stopped successfully!"
}

# Function to restart services
restart_services() {
    print_info "Restarting Smart Load Balancer services..."
    stop_services
    start_services
}

# Function to show service status
show_status() {
    print_info "Service Status:"
    echo ""
    docker-compose -f $COMPOSE_FILE -p $PROJECT_NAME ps
    
    echo ""
    print_info "Service URLs:"
    echo "  Grafana:          http://localhost:3000 (admin/grafana_admin_2024)"
    echo "  Prometheus:       http://localhost:9090"
    echo "  Kafka UI:         http://localhost:8083"
    echo "  PgAdmin:          http://localhost:8082 (admin@smartlb.local/admin_password_123)"
    echo "  Jaeger:           http://localhost:16686"
    echo "  Load Balancer:    http://localhost:8080"
    echo "  Mock User Service: http://localhost:8085"
    echo "  Mock Order Service: http://localhost:8086"
}

# Function to show logs
show_logs() {
    local service=$1
    
    if [ -z "$service" ]; then
        docker-compose -f $COMPOSE_FILE -p $PROJECT_NAME logs -f
    else
        docker-compose -f $COMPOSE_FILE -p $PROJECT_NAME logs -f "$service"
    fi
}

# Function to scale services
scale_service() {
    local service=$1
    local instances=$2
    
    if [ -z "$service" ] || [ -z "$instances" ]; then
        print_error "Usage: $0 scale <service> <instances>"
        exit 1
    fi
    
    print_info "Scaling $service to $instances instances..."
    docker-compose -f $COMPOSE_FILE -p $PROJECT_NAME up -d --scale "$service=$instances" --no-recreate
}

# Function to execute database management
db_management() {
    ./deployment/db/db-management.sh "$@"
}

# Function to execute Kafka management
kafka_management() {
    ./deployment/kafka/kafka-management.sh "$@"
}

# Function to backup data
backup_data() {
    local backup_dir="./backups"
    local timestamp=$(date +%Y%m%d_%H%M%S)
    local backup_path="$backup_dir/backup_$timestamp"
    
    mkdir -p "$backup_path"
    
    print_info "Starting data backup to $backup_path..."
    
    # Backup databases
    print_info "Backing up databases..."
    ./deployment/db/db-management.sh backup
    
    # Backup configuration
    print_info "Backing up configurations..."
    cp -r deployment/ "$backup_path/" 2>/dev/null || true
    cp docker-compose*.yml "$backup_path/" 2>/dev/null || true
    
    # Create backup archive
    tar -czf "$backup_path.tar.gz" -C "$backup_path" .
    rm -rf "$backup_path"
    
    print_info "Backup completed: $backup_path.tar.gz"
}

# Function to cleanup resources
cleanup() {
    print_warning "Cleaning up all resources (containers, volumes, networks)..."
    
    read -p "Are you sure? This will delete ALL data. (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_info "Cleanup cancelled"
        return 0
    fi
    
    docker-compose -f $COMPOSE_FILE -p $PROJECT_NAME down -v
    docker system prune -f
    
    print_info "Cleanup completed"
}

# Main script logic
case "${1:-}" in
    "start")
        check_dependencies
        start_services "$2"
        ;;
    "stop")
        stop_services
        ;;
    "restart")
        restart_services
        ;;
    "status")
        show_status
        ;;
    "logs")
        show_logs "$2"
        ;;
    "scale")
        scale_service "$2" "$3"
        ;;
    "db")
        db_management "${@:2}"
        ;;
    "kafka")
        kafka_management "${@:2}"
        ;;
    "backup")
        backup_data
        ;;
    "cleanup")
        cleanup
        ;;
    "health")
        wait_for_services
        ;;
    "build")
        ./gradlew clean build
        ;;
    "update")
        print_info "Updating services..."
        docker-compose -f $COMPOSE_FILE -p $PROJECT_NAME pull
        restart_services
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status|logs|scale|db|kafka|backup|cleanup|health|build|update}"
        echo ""
        echo "Commands:"
        echo "  start [--build]    Start all services (optionally build project)"
        echo "  stop               Stop all services"
        echo "  restart            Restart all services"
        echo "  status             Show service status and URLs"
        echo "  logs [service]     Show logs for all or specific service"
        echo "  scale service N    Scale service to N instances"
        echo "  db <command>       Execute database management command"
        echo "  kafka <command>    Execute Kafka management command"
        echo "  backup             Create backup of all data"
        echo "  cleanup            Remove all containers, volumes, and networks"
        echo "  health             Wait for all services to be healthy"
        echo "  build              Build the project"
        echo "  update             Update and restart all services"
        echo ""
        echo "Examples:"
        echo "  $0 start --build    Build and start all services"
        echo "  $0 db init          Initialize databases"
        echo "  $0 kafka topics     List Kafka topics"
        echo "  $0 logs kafka       Show Kafka logs"
        echo "  $0 scale metrics-agent 2  Scale metrics agent to 2 instances"
        exit 1
        ;;
esac
