import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "9.1.1"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("org.springframework.boot") version "2.2.3.RELEASE"

    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
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
