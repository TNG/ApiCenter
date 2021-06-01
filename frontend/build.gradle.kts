import org.siouan.frontendgradleplugin.infrastructure.gradle.RunNpmYarn

plugins {
  id("org.siouan.frontend-jdk11") version "5.2.0"
}

frontend {
  nodeVersion.set("14.17.0")
  installScript.set("install")
  cleanScript.set("run clean")
  assembleScript.set("run build")
}

tasks.withType<Jar> {
  dependsOn("npm_run_build")

  from("dist/apicenter")
  into("static")
}

tasks.register<RunNpmYarn>("test") {
  dependsOn(tasks.getByName("installFrontend"))
  script.set("run-script test")
}

task<RunNpmYarn>("lint") {
  dependsOn(tasks.getByName("installNode"))
  script.set("run-script lint")
}
