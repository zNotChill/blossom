plugins {
    `maven-publish`
    kotlin("jvm") version "2.3.0-dev-9673"
}

group = "me.znotchill"
version = "1.4.9"

repositories {
    mavenCentral()
    maven("https://repo.znotchill.me/repository/maven-releases/")
    maven("https://redirector.kotlinlang.org/maven/bootstrap")
    maven(url = "https://central.sonatype.com/repository/maven-snapshots/") {
        content {
            includeModule("net.minestom", "minestom")
            includeModule("net.minestom", "testing")
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.minestom:minestom:2025.12.19-1.21.10")
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
            url = uri("https://repo.znotchill.me/repository/maven-releases/")
            credentials {
                username = findProperty("zRepoUsername") as String? ?: System.getenv("MAVEN_USER")
                password = findProperty("zRepoPassword") as String? ?: System.getenv("MAVEN_PASS")
            }
        }
        mavenLocal()
    }
}