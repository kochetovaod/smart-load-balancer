dependencies {
    implementation(project(":networking"))
    implementation(project(":metrics"))

    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("io.micrometer:micrometer-core:1.11.5")
}