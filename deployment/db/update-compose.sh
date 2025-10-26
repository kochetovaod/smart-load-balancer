#!/bin/bash

# This script updates the docker-compose file to include the new init scripts
# Note: This is a helper script - manual update might be needed

echo "Updating docker-compose.db.yml to include new initialization scripts..."

# Check if file exists
if [ ! -f "../../docker-compose.db.yml" ]; then
    echo "Error: docker-compose.db.yml not found in parent directory"
    exit 1
fi

# Create backup
cp ../../docker-compose.db.yml ../../docker-compose.db.yml.backup

# Update the volumes section for postgres-timescaledb
# This is a simplified approach - manual editing might be more reliable
echo "Please manually update the volumes section in docker-compose.db.yml:"
echo ""
echo "Replace the existing volumes for postgres-timescaledb with:"
echo "volumes:"
echo "  - postgres-data:/var/lib/postgresql/data"
echo "  - ./deployment/db/init-scripts:/docker-entrypoint-initdb.d:ro"
echo "  - ./deployment/db/backups:/backups:rw"
echo ""
echo "Backup created as: docker-compose.db.yml.backup"
