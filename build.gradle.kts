import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.3.50"
}


allprojects {
    group = "ch.deletescape.krempl"
    version = "0.0.1"
    apply(plugin = "kotlin")

    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
    }

    tasks.withType<Test> {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
        }

        failFast = true
        testLogging {
            events = setOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

