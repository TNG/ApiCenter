plugins {
  java
  id("com.moowork.node") version "1.3.1"
}

tasks.withType<Jar>() {
  dependsOn("npm_run_build")

  from("dist/apicenter")
  into("static")
}

tasks.register("e2e", com.moowork.gradle.node.npm.NpmTask::class) {
  setNpmCommand("run", "e2e")
}