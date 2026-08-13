plugins {
    `maven-publish`
    kotlin("jvm") version "2.3.0"
}

group = "me.znotchill"
version = project.property("version")!!

repositories {
    mavenCentral()
    maven("https://repo.znotchill.me/releases")
    maven("https://redirector.kotlinlang.org/maven/bootstrap")
    maven(url = "https://central.sonatype.com/repository/maven-snapshots/") {
        content {
            includeModule("net.minestom", "minestom")
            includeModule("net.minestom", "testing")
        }
    }
}

dependencies {
    compileOnly("org.slf4j:slf4j-api:2.0.17")
    compileOnly("net.minestom:minestom:${project.property("minestom_version")}")
    compileOnly("net.kyori:adventure-text-minimessage:4.26.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(25)
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
        maven {
            name = "znotchill"
            url = uri("https://repo.znotchill.me/releases")
            credentials {
                username = findProperty("zRepoUsername") as String? ?: System.getenv("MAVEN_USER")
                password = findProperty("zRepoPassword") as String? ?: System.getenv("MAVEN_PASS")
            }
        }
        mavenLocal()
    }
}