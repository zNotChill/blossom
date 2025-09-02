plugins {
    `maven-publish`
    kotlin("jvm") version "2.2.0"
}

group = "me.znotchill"
version = "1.1"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.minestom:minestom:2025.07.30-1.21.8")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            group
            artifactId = "blossom"
            version
        }
    }

    repositories {
        mavenLocal()
    }
}