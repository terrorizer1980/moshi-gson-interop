/*
 * Copyright (C) 2020 Slack Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.4.10"
  kotlin("kapt") version "1.4.10"
  id("org.jetbrains.dokka") version "1.4.10"
  id("com.diffplug.spotless") version "5.6.0"
  id("com.vanniktech.maven.publish") version "0.13.0"
  id("io.gitlab.arturbosch.detekt") version "1.13.1"
}

repositories {
  mavenCentral()
  // Until Moshi 1.11.0 is released
  maven("https://oss.sonatype.org/content/repositories/snapshots")
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = "1.8"
    freeCompilerArgs = listOf("-progressive")
  }
}

tasks.withType<Detekt>().configureEach {
  jvmTarget = "1.8"
}

spotless {
  format("misc") {
    target("*.md", ".gitignore")
    trimTrailingWhitespace()
    endWithNewline()
  }
  val ktlintVersion = "0.38.1"
  val ktlintUserData = mapOf("indent_size" to "2", "continuation_indent_size" to "2")
  kotlin {
    target("**/*.kt")
    ktlint(ktlintVersion).userData(ktlintUserData)
    trimTrailingWhitespace()
    endWithNewline()
    licenseHeaderFile("spotless/spotless.kt")
    targetExclude("**/spotless.kt")
  }
  kotlinGradle {
    ktlint(ktlintVersion).userData(ktlintUserData)
    trimTrailingWhitespace()
    endWithNewline()
    licenseHeaderFile("spotless/spotless.kt", "(import|plugins|buildscript|dependencies|pluginManagement)")
  }
}

val moshiVersion = "1.11.0-SNAPSHOT"
dependencies {
  implementation("com.google.code.gson:gson:2.8.6")
  implementation("com.squareup.moshi:moshi:$moshiVersion")

  kaptTest("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")
  testImplementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
  testImplementation("junit:junit:4.13")
  testImplementation("com.google.truth:truth:1.0.1")
}
