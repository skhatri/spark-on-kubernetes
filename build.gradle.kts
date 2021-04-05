import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val sparkVersion = "3.0.1"
val scalaVersion = "2.12"
val uber by configurations.creating

plugins {
    id("java")
    id("scala")
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

repositories{
  mavenCentral()
}

dependencies {
    implementation("org.scala-lang:scala-compiler:2.12.10")
    implementation("org.scala-lang:scala-library:2.12.10")

    listOf("spark-core", "spark-sql", "spark-kubernetes").forEach { name ->
        implementation("org.apache.spark:${name}_${scalaVersion}:$sparkVersion")
    }
    uber("com.datastax.oss:java-driver-core:4.10.0")
    uber("com.fasterxml.jackson.module:jackson-module-scala_2.12:2.11.2") {
      exclude(module="scala-library")
    }

    implementation(uber)
}

tasks.withType<ShadowJar> {
    isZip64 = true
    configurations = listOf(uber)
}
