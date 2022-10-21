import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED

plugins {
    kotlin("jvm") version "1.7.20"
    `maven-publish`
    `java-library`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "io.github.jbonifay"
version = "1.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events = setOf(PASSED, SKIPPED, FAILED)
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("motec-parser")
                description.set("Kotlin parser for Motec files")
                url.set("https://github.com/JBonifay/motec-file-parser")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("JoffreyB")
                        name.set("Joffrey Bonifay")
                        email.set("joffreybonifay83@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/JBonifay/motec-file-parser.git")
                    developerConnection.set("scm:git@github.com:JBonifay/motec-file-parser.git")
                    url.set("https://github.com/JBonifay/motec-file-parser")
                }
            }
        }
    }
}

signing {
    val signinKey = System.getenv("ORG_GRADLE_PROJECT_signingKey")
    val signinKeyPwd = System.getenv("ORG_GRADLE_PROJECT_signingPassword")
    useInMemoryPgpKeys(signinKey, signinKeyPwd)
    sign(*publishing.publications.toTypedArray())
}