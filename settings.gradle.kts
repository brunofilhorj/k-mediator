
rootProject.name = "k-mediator"

include("k-mediator-core")
include("k-mediator-kafka")

pluginManagement {

    val artifactoryRepository: String by settings
    val artifactoryUser: String by settings
    val artifactoryPassword: String by settings

    repositories {
        mavenLocal()
        maven {
            url = java.net.URI(artifactoryRepository)
            credentials {
                username = artifactoryUser
                password = artifactoryPassword
            }
        }
    }
}
