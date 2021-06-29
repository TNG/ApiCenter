import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    kotlin("jvm") version "1.5.20"
    kotlin("plugin.spring") version "1.5.10"
    kotlin("plugin.jpa") version "1.5.10"

    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    id("se.patrikerdes.use-latest-versions") version "0.2.17"
    id("com.github.ben-manes.versions") version "0.39.0"
}

java.sourceCompatibility = JavaVersion.VERSION_12

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("com.tngtech.archunit:archunit-junit5-api:0.19.0")
    testImplementation("com.tngtech.archunit:archunit-junit5-engine:0.19.0")

    runtimeOnly("com.h2database:h2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("buildWithFrontend") {
    dependsOn("build")
    dependsOn("copyFrontend")
    mustRunAfter("copyFrontend")
}

tasks.register("runWithFrontend") {
    dependsOn("bootRun")
    dependsOn("copyFrontend")
    mustRunAfter("copyFrontend")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootBuildImage>("bootBuildImage") {
    imageName = "tngtech/apicenter"
    dependencies {
        runtimeOnly(project(":frontend"))
    }
}

tasks.register("lint") {
    dependsOn("ktlintCheck")
}

tasks.register<Copy>("copyFrontend") {
    dependsOn(":frontend:assemble")
    from("../frontend/dist/apicenter")
    into("src/main/resources/public")
    exclude("spotless*/**")
}
