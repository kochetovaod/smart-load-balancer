# Smart Adaptive Load Balancer

Умный адаптивный балансировщик нагрузки с динамической маршрутизацией на основе метрик в реальном времени.

## Архитектура

![Архитектура системы](docs/architecture/overview.png)

## Быстрый старт

```bash
# Клонирование репозитория
git clone <repository-url>
cd smart-load-balancer

# Запуск в Docker
docker-compose up -d

# Или сборка из исходников
./gradlew build