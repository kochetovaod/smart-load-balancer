#!/bin/bash

# Script for managing Smart Load Balancer database operations

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Default values
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-5432}
DB_NAME=${DB_NAME:-smart_load_balancer}
DB_USER=${DB_USER:-smartlb_user}
DB_PASSWORD=${DB_PASSWORD:-smartlb_password_123}

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

# Function to check if database is ready
wait_for_db() {
    local max_attempts=30
    local attempt=1
    
    print_info "Waiting for database to be ready..."
    
    while [ $attempt -le $max_attempts ]; do
        if pg_isready -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME >/dev/null 2>&1; then
            print_info "Database is ready!"
            return 0
        fi
        
        print_warning "Attempt $attempt/$max_attempts - Database not ready, waiting..."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    print_error "Database failed to become ready after $max_attempts attempts"
    return 1
}

# Function to run SQL file
run_sql_file() {
    local file=$1
    local db_name=${2:-$DB_NAME}
    
    if [ ! -f "$file" ]; then
        print_error "SQL file not found: $file"
        return 1
    fi
    
    print_info "Running SQL file: $file on database: $db_name"
    
    PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $db_name -f "$file"
}

# Function to create backup
create_backup() {
    local backup_dir="./deployment/db/backups"
    local timestamp=$(date +%Y%m%d_%H%M%S)
    local backup_file="$backup_dir/backup_${timestamp}.sql"
    
    mkdir -p "$backup_dir"
    
    print_info "Creating database backup: $backup_file"
    
    PGPASSWORD=$DB_PASSWORD pg_dump -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME \
        --verbose --clean --no-owner --no-acl --format=custom > "$backup_file"
    
    if [ $? -eq 0 ]; then
        print_info "Backup created successfully: $backup_file"
        ls -lh "$backup_file"
    else
        print_error "Backup failed"
        return 1
    fi
}

# Function to restore from backup
restore_backup() {
    local backup_file=$1
    
    if [ ! -f "$backup_file" ]; then
        print_error "Backup file not found: $backup_file"
        return 1
    fi
    
    print_warning "This will restore database from backup: $backup_file"
    read -p "Are you sure? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_info "Restore cancelled"
        return 0
    fi
    
    print_info "Restoring database from backup: $backup_file"
    
    PGPASSWORD=$DB_PASSWORD pg_restore -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME \
        --verbose --clean --no-owner --no-acl "$backup_file"
    
    if [ $? -eq 0 ]; then
        print_info "Restore completed successfully"
    else
        print_error "Restore failed"
        return 1
    fi
}

# Function to show database status
show_status() {
    print_info "Database Status:"
    echo "Host: $DB_HOST"
    echo "Port: $DB_PORT"
    echo "Database: $DB_NAME"
    echo "User: $DB_USER"
    
    # Check connection
    if PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "SELECT 1" >/dev/null 2>&1; then
        print_info "Connection: OK"
        
        # Show basic stats
        PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME << EOF
SELECT 
    schemaname || '.' || relname as table_name,
    n_live_tup as row_count,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||relname)) as total_size
FROM pg_stat_user_tables 
WHERE schemaname = 'lb_metrics'
ORDER BY n_live_tup DESC;
