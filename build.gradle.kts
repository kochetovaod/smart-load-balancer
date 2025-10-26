plugins {
    java
}

allprojects {
    group = "com.company"
    version = "1.0.0-SNAPSHOT"
    
    repositories {
        mavenCentral()
    }
    
    apply(plugin = "java")
    
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
    }
}
