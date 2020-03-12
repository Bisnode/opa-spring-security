allprojects {
    version = "0.0.1-SNAPSHOT"
    group = "com.bisnode.opa"
}

subprojects {
    apply {
        plugin("checkstyle")
        plugin("codenarc")
        plugin("java")
        plugin("java-library")
        plugin("groovy")
        plugin("signing")
    }

    repositories {
        mavenCentral()
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        withSourcesJar()
    }

    configure<CodeNarcExtension> {
        reportFormat = "console"
    }

}

tasks.wrapper {
    gradleVersion = Versions.gradle
}
