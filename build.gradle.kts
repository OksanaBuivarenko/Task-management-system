plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.3.6"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.effectivemobile"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
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

jacoco {
	toolVersion = "0.8.12"
	reportsDirectory = layout.buildDirectory.dir("customJacocoReportDir")
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required = false
		csv.required = true
		html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
	}
	classDirectories.setFrom(
		files(classDirectories.files.map {
			fileTree(it) {
				exclude("**/dto/**")
			}
		})
	)
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("redis.clients:jedis")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("org.mapstruct:mapstruct:1.6.2")
	implementation("org.hibernate.orm:hibernate-core:6.2.7.Final")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	implementation("javax.xml.bind:jaxb-api:2.3.1")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.2")
	annotationProcessor( "org.projectlombok:lombok-mapstruct-binding:0.2.0")
	annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:6.2.7.Final")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("junit:junit:4.13.1")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
