plugins {
  java
  id("com.moowork.node") version "1.3.1"
}

tasks.withType<Jar>() {
  dependsOn("npm_run_build")

  from("dist/apicenter")
  into("static")
}
