import org.apache.tools.ant.util.Native2AsciiUtils

plugins {
    java
    war
}

repositories {
    mavenCentral()
}

group = "stars"
version = "1"
description = "lab-2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(properties["javaVersion"].toString())
    }
}

dependencies {
    implementation("org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1")
    implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.0")
    implementation("org.eclipse.persistence:org.eclipse.persistence.jpa:${properties["jpaVersion"]}")
    implementation("org.primefaces:primefaces:${properties["primefacesVersion"]}:jakarta")
    implementation("org.postgresql:postgresql:${properties["postgresqlVersion"]}")
    implementation("jakarta.servlet:jakarta.servlet-api:${properties["servletVersion"]}")
    implementation("jakarta.platform:jakarta.jakartaee-api:${properties["jakartaeeVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${properties["junitVersion"]}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${properties["junitVersion"]}")
}

tasks.test {
    useJUnitPlatform()
    reports {
        junitXml.required.set(true)
        html.required.set(true)
    }
}

// Конфигурация преобразования properties-файлов
tasks.register<Copy>("native2ascii") {
    from("src/main/resources") {
        include("**/*.properties")
    }
    filter { line -> Native2AsciiUtils.native2ascii(line) }
    into("build/native2ascii")

    // Убедимся, что каталог существует
    doFirst {
        mkdir("build/native2ascii")
    }
}

tasks.register("my_build") {
    dependsOn("war")
}


// build (включает compileJava и processResources, упаковывает в jar и затем report)
tasks.register<Jar>("buildJar") {
    dependsOn(tasks.compileJava, tasks.processResources)
    from(sourceSets.main.get().output)
    manifest {
        attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Main-Class" to "your.main.ClassName" // Замените на ваш класс запуска
        )
    }
    archiveBaseName.set(project.name)
    archiveVersion.set(project.version.toString())
}


//report (после успешного тестирования сохраняет отчеты и коммитит в SVN)
tasks.register("report") {
    dependsOn(tasks.test)
    doLast {
        val reportDir = file("build/test-results/test")
        if (reportDir.exists()) {
            exec { commandLine("svn", "add", "--force", reportDir) }
            exec { commandLine("svn", "commit", "-m", "Add JUnit test reports: version ${project.version}") }
        } else {
            logger.lifecycle("No test report directory found at: $reportDir")
        }
    }
}