/**
 * Корневой файл сборки Smart Load Balancer проекта
 * Использует Gradle Kotlin DSL для типобезопасной конфигурации
 */

// Плагины применяемые ко всему проекту
plugins {
    java              // Поддержка Java проектов
    jacoco            // Покрытие кода
    id("com.github.ben-manes.versions") version "0.50.0" // Проверка обновлений зависимостей
}

// Конфигурация применяемая ко всем проектам (включая корневой)
allprojects {
    // Метаданные проекта
    group = "com.company"
    version = "1.0.0-SNAPSHOT"
    
    // Репозитории для разрешения зависимостей
    repositories {
        mavenCentral() // Основной репозиторий Maven
    }
    
    // Применяем плагины ко всем проектам
    apply(plugin = "java")
    apply(plugin = "jacoco")
    
    // Настройка Java компиляции
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

// Конфигурация применяемая ко всем подпроектам (модулям)
subprojects {
    // Зависимости для всех модулей
    dependencies {
        // Тестовые зависимости (JUnit 5, Mockito, AssertJ)
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
        testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
        testImplementation("org.assertj:assertj-core:3.24.2")
    }
    
    // Конфигурация задачи test
    tasks.test {
        useJUnitPlatform() // Используем JUnit 5
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = true
        }
        finalizedBy(tasks.jacocoTestReport) // Всегда генерируем отчет покрытия после тестов
    }
    
    // Конфигурация отчета JaCoCo
    tasks.jacocoTestReport {
        dependsOn(tasks.test) // Зависит от выполнения тестов
        reports {
            xml.required.set(true)   // Для CI интеграции
            html.required.set(true)  // Для локального просмотра
            csv.required.set(false)
        }
    }
}

/**
 * Агрегированный отчет JaCoCo для всех модулей
 * Собирает данные покрытия со всех подпроектов в единый отчет
 */
tasks.register<JacocoReport>("jacocoRootReport") {
    dependsOn(subprojects.map { it.tasks.named("test") })
    
    // Источники всех модулей
    additionalSourceDirs.setFrom(files(subprojects.map { it.sourceSets.main.get().allSource.srcDirs }))
    sourceDirectories.setFrom(files(subprojects.map { it.sourceSets.main.get().allSource.srcDirs }))
    classDirectories.setFrom(files(subprojects.map { it.sourceSets.main.get().output }))
    executionData.setFrom(files(subprojects.map { it.tasks.jacocoTestReport.get().executionData }))
    
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

/**
 * Задача для комплексной проверки качества кода
 * Объединяет сборку, тестирование, покрытие и проверку зависимостей
 */
tasks.register("qualityCheck") {
    dependsOn("build", "test", "jacocoRootReport", "dependencyUpdates")
    group = "verification"
    description = "Runs all quality checks including tests, coverage and dependency updates"
}
