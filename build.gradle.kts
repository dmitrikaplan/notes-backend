import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	war
	id("org.springframework.boot") version "3.1.5" apply false
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


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

