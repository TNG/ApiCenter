plugins {
  java
  id("com.moowork.node") version "1.3.1"
}

tasks.withType<Jar> {
  dependsOn("npm_run_build")

  from("dist/apicenter")
  into("static")
}

tasks.register("e2e", com.moowork.gradle.node.npm.NpmTask::class) {
  setNpmCommand("run", "e2e")
}

tasks.register("npmTest", com.moowork.gradle.node.npm.NpmTask::class) {
	dependsOn("npmInstall")
  setArgs(listOf("run-script", "test"))
}

task("lint", com.moowork.gradle.node.npm.NpmTask::class) {
  setArgs(listOf("run-script", "lint"))
}
