plugins {
    java
}

dependencies {
    implementation("io.micrometer:micrometer-registry-prometheus:1.11.5")
    // Используем правильную зависимость Prometheus
    implementation("io.prometheus:simpleclient:0.16.0")
    implementation("io.prometheus:simpleclient_common:0.16.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.2.0")
    implementation("org.slf4j:slf4j-api:2.0.9")
    
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
