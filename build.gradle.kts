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
  version = "13.0.4"
}

defaultTasks("clean", "build")

repositories {
  mavenCentral()
  maven {setUrl("https://maven.vaadin.com/vaadin-prereleases/")}
  maven {setUrl("http://maven.vaadin.com/vaadin-addons")}
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

  
  //Ebean
  compile("io.ebean:ebean:11.41.1")
  compile("io.ebean:ebean-querybean:11.40.1")
  implementation("io.ebean:ebean-agent:11.41.1")
  compile("io.ebean.tools:finder-generator:11.34.1")
  compile("mysql:mysql-connector-java:5.1.47")
  //Utils
  compile("org.imgscalr:imgscalr-lib:4.2")
  compile("org.cups4j:cups4j:0.7.6")
  //Addon Vaadin
  compile("com.github.appreciated:app-layout-addon:2.1.1")
  compile("org.vaadin.olli:browser-opener:0.3")
  compile("org.claspina:confirm-dialog:1.0.0")
  compile("org.vaadin.crudui:crudui:3.9.0")
  compile("com.github.appreciated:card:0.9.4")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

