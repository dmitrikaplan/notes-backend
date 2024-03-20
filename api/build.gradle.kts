import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
}



group = "ru.kaplaan"
version = "0.0.1"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	//starters
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	//providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	implementation("org.springframework.boot:spring-boot-starter-security")

	//jwt
	implementation(group = "io.jsonwebtoken", name = "jjwt-api",version = "0.11.5")
	runtimeOnly(group = "io.jsonwebtoken", name = "jjwt-impl", version = "0.11.5")
	runtimeOnly(group = "io.jsonwebtoken", name = "jjwt-jackson", version = "0.11.5")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")


	//validation
	implementation("org.springframework.boot:spring-boot-starter-validation")

	//swagger generator
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

	//tests
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
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
