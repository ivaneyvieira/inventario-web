import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val karibudsl_version = "0.7.0"
//https://github.com/mvysny/bookstore-vok/blob/master/build.gradle.kts
//https://github.com/mvysny/bookstore-vok
plugins {
    kotlin("jvm") version "1.3.41"
    id("org.gretty") version "2.3.1"
    war
    id("com.devsoap.vaadin-flow") version "1.2"
}

vaadin {
    version = "14.0.0.rc4"
}

defaultTasks("clean", "build")

repositories {
    jcenter()
    maven { setUrl("https://maven.vaadin.com/vaadin-prereleases/") }
}

gretty {
    contextPath = "/"
    servletContainer = "jetty9.4"
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        // to see the exceptions of failed tests in Travis-CI console.
        exceptionFormat = TestExceptionFormat.FULL
    }
}

dependencies {
    // Karibu-DSL dependency, includes Vaadin
    compile("com.github.mvysny.karibudsl:karibu-dsl-v10:$karibudsl_version")
    compile("com.vaadin:vaadin-core:${vaadin.version}")
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")

    // logging
    // currently we are logging through the SLF4J API to LogBack. See src/main/resources/logback.xml file for the logger configuration
    compile("ch.qos.logback:logback-classic:1.2.3")
    compile("org.slf4j:slf4j-api:1.7.25")

    compile(kotlin("stdlib-jdk8"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

