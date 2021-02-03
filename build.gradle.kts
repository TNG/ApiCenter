import net.researchgate.release.ReleaseExtension
import net.researchgate.release.GitAdapter.GitConfig

plugins {
    id("net.researchgate.release") version "2.8.1"
}

tasks.register("buildAll") {
    dependsOn(":backend:buildWithFrontend")
}

tasks.register("runAll") {
    dependsOn(":backend:runWithFrontend")
}

fun ReleaseExtension.git(configure: GitConfig.() -> Unit) = (getProperty("git") as GitConfig).configure()

release {
    failOnUnversionedFiles = false
    buildTasks = listOf("buildAll")
    git {
        requireBranch = "main"
    }
}
