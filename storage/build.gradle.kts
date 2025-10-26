plugins {
    java
}

dependencies {
    // Database
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("com.zaxxer:HikariCP:5.1.0")
    
    // Spring Data (базовые зависимости)
    implementation("org.springframework.data:spring-data-commons:3.2.0")
    implementation("org.springframework:spring-jdbc:6.1.0")
    implementation("org.springframework:spring-tx:6.1.0")
    
    // Connection pooling
    implementation("com.zaxxer:HikariCP:5.1.0")
    
    // Utilities
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")
    
    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.testcontainers:postgresql:1.19.3")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
