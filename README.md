# Smart Adaptive Load Balancer

[![Smart Load Balancer CI](https://github.com/your-company/smart-load-balancer/actions/workflows/ci.yml/badge.svg)](https://github.com/your-company/smart-load-balancer/actions/workflows/ci.yml)

Умный адаптивный балансировщик нагрузки с динамической маршрутизацией на основе метрик в реальном времени.

## 🏗 Архитектура

Проект построен по модульной архитектуре с использованием современных технологий:

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Клиенты       │───▶│   Load Balancer  │───▶│   Бэкенд-       │
│                 │    │                  │    │   сервисы       │
└─────────────────┘    └──────────────────┘    └─────────────────┘
        │                         │
        │                         │
┌───────▼──────────┐      ┌───────▼──────────┐
│   Kafka Streams  │      │   Metrics Agents │
│   (аналитика)    │      │   (gRPC)         │
└──────────────────┘      └──────────────────┘
        │                         │
        │                         │
┌───────▼──────────┐      ┌───────▼──────────┐
│   Prometheus     │      │   Конфигурация   │
│   + Grafana      │      │                  │
└──────────────────┘      └──────────────────┘
```

## 📋 Требования

- **Java 17+** (рекомендуется Eclipse Temurin 17)
- **Gradle 8.5+** (используется Gradle Wrapper)
- **Docker** (для запуска инфраструктуры разработки)
- **Git** (для контроля версий)

## 🚀 Быстрый старт

### Клонирование и сборка

```bash
# Клонирование репозитория
git clone https://github.com/your-company/smart-load-balancer.git
cd smart-load-balancer

# Сборка проекта
./gradlew clean build

# Запуск тестов
./gradlew test

# Запуск всех проверок качества
./gradlew qualityCheck
```

### Запуск инфраструктуры разработки

```bash
# Запуск Kafka, Zookeeper, Prometheus, Grafana
docker-compose up -d

# Остановка инфраструктуры
docker-compose down
```

## 🏗️ Структура проекта

Проект состоит из 6 модулей:

| Модуль | Назначение | Основные зависимости |
|--------|------------|---------------------|
| **core** | Основная логика балансировки | Spring Context, Micrometer |
| **networking** | HTTP сервер и клиент | Netty, Jackson |
| **metrics** | Сбор и агрегация метрик | Micrometer, Kafka, gRPC |
| **storage** | Работа с базой данных | PostgreSQL, Spring Data |
| **agent** | Агент сбора метрик | gRPC, Protobuf |
| **monitoring** | Мониторинг и метрики | Prometheus, Spring Boot Actuator |

## 📁 Модульная структура

```
smart-load-balancer/
├── core/                    # Основная логика балансировки
├── networking/             # Netty сервер и клиент
├── metrics/               # Сбор и агрегация метрик
├── storage/               # Работа с БД (PostgreSQL)
├── agent/                 # gRPC агент для метрик
├── monitoring/            # Prometheus метрики
├── gradle/               # Version catalog конфигурации
├── .github/workflows/    # CI/CD пайплайны
└── deployment/           # Docker и Kubernetes конфиги
```

## 🛠️ Команды разработки

### Сборка и тестирование

```bash
# Полная сборка проекта
./gradlew clean build

# Запуск всех тестов
./gradlew test

# Запуск тестов с покрытием
./gradlew jacocoRootReport

# Проверка обновлений зависимостей
./gradlew dependencyUpdates
```

### Модульная разработка

```bash
# Сборка конкретного модуля
./gradlew :core:build

# Запуск тестов конкретного модуля
./gradlew :networking:test

# Зависимости модуля
./gradlew :metrics:dependencies
```

### Качество кода

```bash
# Полная проверка качества
./gradlew qualityCheck

# Генерация отчета покрытия
./gradlew jacocoRootReport

# Проверка устаревших зависимостей
./gradlew dependencyUpdates
```

## 🔧 Конфигурация

### Основные конфигурационные файлы

- `build.gradle.kts` - корневая конфигурация сборки
- `settings.gradle.kts` - настройки проекта и модулей
- `gradle/libs.versions.toml` - централизованное управление зависимостями
- `gradle.properties` - свойства Gradle и проекта
- `docker-compose.yml` - инфраструктура разработки

### Переменные окружения

```bash
# Java options
export JAVA_HOME=/path/to/java17
export GRADLE_OPTS="-Xmx2g"

# Application properties
export SMART_LB_PORT=8080
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```

## 📊 Мониторинг и метрики

Проект предоставляет метрики через:

- **Prometheus** - сбор метрик
- **Grafana** - визуализация
- **Spring Boot Actuator** - health checks и метрики
- **Micrometer** - abstraction layer для метрик

## 🤝 Разработка

### Code Style

Проект использует:
- Java 17 features (records, text blocks)
- Gradle Kotlin DSL для конфигурации
- JUnit 5 для тестирования
- AssertJ для assertions

### Git Workflow

1. Создайте feature branch от `main`
2. Регулярно делайте коммиты
3. Создайте Pull Request
4. Убедитесь, что все тесты проходят
5. Получите code review

### CI/CD

При каждом PR и push в `main` автоматически запускается:
- Сборка проекта
- Запуск всех тестов
- Проверка покрытия кода
- Статический анализ
- Сборка Docker образов

## 🐛 Troubleshooting

### Частые проблемы

**Проблема**: Ошибки сборки из-за версий Java
**Решение**: Убедитесь, что используется Java 17+

```bash
java -version
# Должно быть: openjdk version "17.x.x"
```

**Проблема**: Ошибки зависимостей
**Решение**: Очистите кэш Gradle

```bash
./gradlew clean
rm -rf ~/.gradle/caches/
```

**Проблема**: Docker Compose ошибки
**Решение**: Проверьте версию Docker Compose

```bash
docker-compose version
# Должно быть: 1.29+ или 2.x
```

