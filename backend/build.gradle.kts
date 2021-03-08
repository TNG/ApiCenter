import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.31"
    kotlin("plugin.jpa") version "1.4.30"

    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("se.patrikerdes.use-latest-versions") version "0.2.15"
    id("com.github.ben-manes.versions") version "0.38.0"
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
    testImplementation("com.tngtech.archunit:archunit-junit5-api:0.16.0")
    testImplementation("com.tngtech.archunit:archunit-junit5-engine:0.16.0")

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

    dependencies {
        runtimeOnly(project(":frontend"))
    }
}

tasks.register("runWithFrontend") {
    dependsOn("bootRun")

    dependencies {
        runtimeOnly(project(":frontend"))
    }
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
