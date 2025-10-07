plugins {
	java
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.corthos"
version = "0.0.1"
description = "Corbit is a PDF converter project"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

	compileOnly("org.projectlombok:lombok")

	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter:1.21.3")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")


    //	implementation("org.springframework.boot:spring-boot-starter-amqp")
    //	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    //	implementation("org.springframework.kafka:spring-kafka")
    //  implementation("org.apache.tika:tika-parsers-standard-package:3.2.3")
    //	runtimeOnly("org.postgresql:postgresql")
    //  testImplementation("org.testcontainers:testcontainers:1.21.3")
    //	testImplementation("org.springframework.amqp:spring-rabbit-test")
    //	testImplementation("org.springframework.kafka:spring-kafka-test")
    //  implementation("com.github.librepdf:openpdf:3.0.0")
    //  implementation("org.apache.poi:poi-ooxml:5.4.1")
    //  implementation("fr.opensagres.xdocreport:fr.opensagres.xdocreport.converter.docx.xwpf:2.1.0")
    //  implementation("org.apache.tika:tika-core:3.2.3")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = true
    mainClass.set("ru.corthos.corbit.CorbitApplication")
}

tasks.named<Jar>("jar") {
    enabled = false
}