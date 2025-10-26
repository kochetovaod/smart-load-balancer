plugins {
    java
    jacoco
    id("com.github.ben-manes.versions") version "0.50.0"
}

allprojects {
    group = "com.company"
    version = "1.0.0-SNAPSHOT"
    
    repositories {
        mavenCentral()
    }
    
    apply(plugin = "java")
    apply(plugin = "jacoco")
    
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

subprojects {
    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
        testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
        testImplementation("org.assertj:assertj-core:3.24.2")
    }
    
    tasks.test {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)
    }
    
    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required.set(true)
            html.required.set(true)
            csv.required.set(false)
        }
    }
}

// Агрегированный отчет JaCoCo для всех модулей
tasks.register<JacocoReport>("jacocoRootReport") {
    dependsOn(subprojects.map { it.tasks.named("test") })
    
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

// Задача для проверки качества кода
tasks.register("qualityCheck") {
    dependsOn("build", "test", "jacocoRootReport", "dependencyUpdates")
    group = "verification"
    description = "Runs all quality checks including tests, coverage and dependency updates"
}

// Configure integration tests for all subprojects
subprojects {
    tasks.register<Test>("integrationTest") {
        description = "Runs integration tests"
        group = "verification"
        
        useJUnitPlatform()
        
        include("**/*BuildTest*")
        include("**/*IntegrationTest*")
        
        systemProperty("integration.test", "true")
        timeout.set(java.time.Duration.ofMinutes(10))
    }
    
    tasks.check {
        dependsOn("integrationTest")
    }
}
