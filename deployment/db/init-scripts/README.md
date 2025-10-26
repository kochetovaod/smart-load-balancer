# Database Initialization Scripts

This directory contains SQL scripts for initializing the Smart Load Balancer databases.

## Script Execution Order

The scripts are executed in numerical order when the PostgreSQL container starts:

1. **01-init-databases.sql** - Creates operational and metrics databases
2. **02-create-users.sql** - Creates application users with secure passwords
3. **03-grant-permissions.sql** - Grants appropriate permissions and creates schemas
4. **04-test-data.sql** - Inserts test data for development and demonstration

## Database Structure

### Operational Database (`loadbalancer_operational`)
- **Services**: Service registry and configuration
- **Service Instances**: Registered service instances with health status
- **Routing Strategies**: Available load balancing algorithms
- **Service Routing**: Strategy assignments for services

### Metrics Database (`loadbalancer_metrics`)
- **Service Metrics**: Time-series performance metrics (TimescaleDB hypertable)
- **Health Status**: Health check history (TimescaleDB hypertable)
- **Routing Decisions**: Load balancer routing decision log

## Users and Permissions

### lb_operator
- **Purpose**: Operational database access
- **Permissions**: SELECT, INSERT, UPDATE, DELETE on operational tables
- **Password**: `lb_operator_password_2024`

### lb_metrics
- **Purpose**: Metrics database write access
- **Permissions**: SELECT, INSERT on metrics tables
- **Password**: `lb_metrics_password_2024`

### lb_monitor
- **Purpose**: Read-only monitoring access
- **Permissions**: SELECT on all tables
- **Password**: `lb_monitor_password_2024`

## Test Data

The test data includes:

### Services
- API Gateway, User Service, Order Service, Product Service, Payment Service, Notification Service

### Service Instances
- Multiple instances per service with different weights and priorities
- Realistic configuration for development and testing

### Routing Strategies
- Round Robin, Weighted Round Robin, Least Connections, Least Response Time, IP Hash

### Sample Metrics
- Generated metrics for the last hour with realistic patterns
- Health check history for monitoring
- Routing decisions for analysis

## Manual Execution

To manually execute the scripts:

```bash
# Connect to PostgreSQL
psql -h localhost -U postgres -d postgres

# Execute individual scripts
\i deployment/db/init-scripts/01-init-databases.sql
\i deployment/db/init-scripts/02-create-users.sql
\i deployment/db/init-scripts/03-grant-permissions.sql
\i deployment/db/init-scripts/04-test-data.sql
```
## Security Notes

- Change default passwords in production
- Review and adjust permissions based on security requirements
- Consider using external secret management for passwords
- Regularly rotate database credentials

## Troubleshooting

- Check PostgreSQL logs for initialization errors
- Verify that TimescaleDB extension is properly installed
- Ensure sufficient disk space for metrics data
- Monitor database connections and performance
