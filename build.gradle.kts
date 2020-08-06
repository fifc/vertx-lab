import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val gradle_version = "6.6-rc-6"

buildscript {
    extra.apply {
        //set("kotlinVersion", "1.3.72")
        //set("kotlinVersion", "1.4-M3")
        set("kotlinVersion", "1.4.0-rc")
    }

    repositories {
	jcenter()
	mavenCentral()
	maven { url = uri("https://dl.bintray.com/kotlin/kotlin-dev/") }
	maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
	maven ("https://dl.bintray.com/kotlin/kotlin-eap")
	maven ("https://kotlin.bintray.com/kotlinx")

    }
}

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version("5.2.0")
    id("org.jetbrains.kotlin.jvm") version("${property("kotlinVersion")}")
}

version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_14

repositories {
	jcenter()
	mavenCentral()
	maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
	maven { url = uri("https://dl.bintray.com/kotlin/kotlin-dev/") }
	maven ("https://dl.bintray.com/kotlin/kotlin-eap")
	maven ("https://kotlin.bintray.com/kotlinx")
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    implementation("io.vertx:vertx-core:+")
    implementation("io.vertx:vertx-lang-kotlin:+")
    implementation("io.vertx:vertx-web:+")
    implementation("io.vertx:vertx-mqtt:+")
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
		jvmTarget = "14"
	}
}

//tasks.run {
//  args = ["run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$mainClassName", "--on-redeploy=$doOnChange"]
//}

// gradle wrapper --gradle-version 5.6.3 --distribution-type all

tasks.wrapper {
    gradleVersion = gradle_version
    distributionType = Wrapper.DistributionType.ALL
}
