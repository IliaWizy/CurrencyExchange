plugins {
    kotlin("jvm") version "1.9.0"
    application
    war
}

group = "com.wizy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.10")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")

//    implementation("org.mapstruct:mapstruct:1.5.5.Final")
//    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

//service {
//    mainClass.set("MainKt")
//}
