tasks.register("buildAll") {
    dependsOn(":backend:buildWithFrontend")
}

tasks.register("runAll") {
    dependsOn(":backend:runWithFrontend")
}
