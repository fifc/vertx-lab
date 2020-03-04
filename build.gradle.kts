import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    application
    id("org.jetbrains.kotlin.jvm") version "1.3.70"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	jcenter()
	mavenCentral()
	maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    implementation("io.vertx:vertx-core:+")
    implementation("io.vertx:vertx-lang-kotlin:+")
    implementation("io.vertx:vertx-web:+")
}

val mainClassName = "io.vertx.core.Launcher"
val mainVerticleName = "vx.Vx"
val watchForChange = "src/**/*"
val doOnChange = "./gradlew classes"
application.mainClassName = mainClassName

tasks.shadowJar {
	classifier = "fat"
	manifest {
		attributes["Main-Verticle"] = mainVerticleName
	}
	mergeServiceFiles {
		include ("META-INF/services/io.vertx.core.spi.VerticleFactory")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

//tasks.run {
//  args = ["run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$mainClassName", "--on-redeploy=$doOnChange"]
//}

// gradle wrapper --gradle-version 5.6.3 --distribution-type all

tasks.wrapper {
    gradleVersion = "6.2.1"
    distributionType = Wrapper.DistributionType.ALL
}
