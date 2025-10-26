plugins {
    java
}

dependencies {
    // Project modules
    implementation(project(":networking"))
    implementation(project(":metrics"))
    implementation(project(":storage"))
    implementation(project(":agent"))
    implementation(project(":monitoring"))
    
    // Core dependencies
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("io.micrometer:micrometer-core:1.11.5")
    
    // Utilities
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.3")
    
    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
    
    // Set longer timeout for integration tests
    timeout.set(java.time.Duration.ofMinutes(10))
    
    // Configure system properties for integration tests
    systemProperty("project.root", project.rootDir.absolutePath)
}

// Configure separate task for integration tests
tasks.register<Test>("integrationTest") {
    description = "Runs integration tests"
    group = "verification"
    
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
    
    timeout.set(java.time.Duration.ofMinutes(15))
    
    include("**/*BuildTest*")
    include("**/*IntegrationTest*")
    
    systemProperty("project.root", project.rootDir.absolutePath)
    systemProperty("integration.test", "true")
}

// Make check task depend on integration tests
tasks.check {
    dependsOn("integrationTest")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
