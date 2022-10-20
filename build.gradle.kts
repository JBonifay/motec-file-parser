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
version = "1.0"

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
        sonatype { // only for users registered in Sonatype after 24 Feb 2021
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            signing {
                val GPG_SIGNING_KEY = System.getenv("GPG_SIGNING_KEY")
                val GPG_SIGNING_KEY_PWD = System.getenv("GPG_SIGNING_KEY_PWD")
                useInMemoryPgpKeys(GPG_SIGNING_KEY, GPG_SIGNING_KEY_PWD)
                sign(publishing.publications["mavenJava"])
            }
            from(components["java"])
            pom {
                name.set("motec-parser")
                description.set("Kotlin parser for Motec files")
                url.set("https://github.com/JBonifay/motec-file-parser")
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://github.com/JBonifay/motec-file-parser/blob/main/LICENSE")
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

    nexusPublishing {
        repositories {
            create("myNexus") {
                nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"))
                snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
                username.set(System.getenv("NEXUS_USERNAME"))
                password.set(System.getenv("NEXUS_PASSWORD"))
            }
        }
    }
}
