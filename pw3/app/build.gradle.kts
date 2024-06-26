/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.6/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation("com.rabbitmq:amqp-client:5.20.0")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass.set("edu.distributedsystems.pw3.App")
}

tasks.register<JavaExec>("clientWriter") {
    mainClass.set("edu.distributedsystems.pw3.ClientWriter")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("replica") {
    mainClass.set("edu.distributedsystems.pw3.Replica")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("clientReaderV1") {
    mainClass.set("edu.distributedsystems.pw3.ClientReaderV1")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("clientReaderV2") {
    mainClass.set("edu.distributedsystems.pw3.ClientReaderV2")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
