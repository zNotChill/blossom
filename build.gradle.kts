plugins {
    `maven-publish`
    kotlin("jvm") version "2.2.0"
}

group = "me.znotchill"
version = "1.4.1"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.minestom:minestom:2025.09.13-1.21.8")
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

            groupId = "me.znotchill"
            artifactId = "blossom"
            version
        }
    }

    repositories {
        mavenLocal()
    }
}