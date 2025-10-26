plugins {
    id("java")
    id("io.spring.dependency-management") version "1.1.4"
}

allprojects {
    group = "com.company"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
        testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
        testImplementation("org.assertj:assertj-core:3.24.2")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}