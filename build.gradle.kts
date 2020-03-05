allprojects {
    version = "0.0.1-SNAPSHOT"
    group = "com.bisnode.opa"
}

subprojects {
    apply {
        plugin("java")
        plugin("java-library")
        plugin("groovy")
        plugin("maven-publish")
    }

    repositories {
        mavenCentral()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        withSourcesJar()
    }

}

tasks.wrapper {
    gradleVersion = Versions.gradle
}
