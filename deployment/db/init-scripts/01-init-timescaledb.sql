-- Инициализация TimescaleDB расширения
CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;

-- Создание схемы для метрик load balancer
CREATE SCHEMA IF NOT EXISTS lb_metrics;
GRANT USAGE ON SCHEMA lb_metrics TO smartlb_user;

-- Настройка поискового пути для удобства
ALTER DATABASE smart_load_balancer SET search_path TO public, lb_metrics;
