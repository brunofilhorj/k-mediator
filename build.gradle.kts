plugins {
    kotlin("jvm") version "1.9.22"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

allprojects {
    repositories {
        val artifactoryRepository: String by project
        val artifactoryUser: String by project
        val artifactoryPassword: String by project

        mavenLocal()
        maven {
            setUrl(artifactoryRepository)
            credentials {
                username = artifactoryUser
                password = artifactoryPassword
            }
        }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    tasks.test {
        useJUnitPlatform()
    }
}
