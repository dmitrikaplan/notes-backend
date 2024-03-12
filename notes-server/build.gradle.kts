import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	war
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.9.0"
	kotlin("plugin.spring") version "1.9.0"
	kotlin("plugin.jpa") version "1.9.0"
}

group = "ru.kaplaan"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(project(":domain"))
	//starters
	implementation("org.springframework.boot:spring-boot-starter-web")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-security")

	//jwt
	implementation(group = "io.jsonwebtoken", name = "jjwt-api",version = "0.11.5")
	runtimeOnly(group = "io.jsonwebtoken", name = "jjwt-impl", version = "0.11.5")
	runtimeOnly(group = "io.jsonwebtoken", name = "jjwt-jackson", version = "0.11.5")

	//database
	implementation("org.postgresql:postgresql")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	//validation
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")

	//serialization
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

	//swagger generator
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}