plugins {
    java
}

dependencies {
    // Networking dependencies
    implementation("io.netty:netty-all:4.1.100.Final")
    implementation("io.netty:netty-codec-http:4.1.100.Final")
    implementation("io.netty:netty-handler:4.1.100.Final")
    
    // HTTP client
    implementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
    
    // JSON processing
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.3")
    
    // Utilities
    implementation("org.slf4j:slf4j-api:2.0.9")
    
    // Testing
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
