allprojects {
    version = "0.0.2"
    group = "com.bisnode.opa"
}

subprojects {
    apply {
        plugin("checkstyle")
        plugin("codenarc")
        plugin("java")
        plugin("java-library")
        plugin("groovy")
        plugin("maven-publish")
    }

    repositories {
        mavenCentral()
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        withSourcesJar()
        withJavadocJar()
    }

    configure<CodeNarcExtension> {
        reportFormat = "console"
    }
}

tasks.wrapper {
    gradleVersion = Versions.gradle
}

// anything below this line will be evaluated AFTER the children
evaluationDependsOnChildren()
subprojects {
    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("${project.name}-module") {
                artifactId = project.name
                from(components["java"])
                pom {
                    name.set("${project.ext["pomName"]}")
                    description.set("${project.ext["pomDescription"]}")
                    url.set("https://github.com/Bisnode/opa-spring-security")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/Bisnode/opa-spring-security.git")
                        developerConnection.set("scm:git:ssh://github.com:Bisnode/opa-spring-security.git")
                        url.set("https://github.com/Bisnode/opa-spring-security.git")
                    }
                    developers {
                        developer {
                            name.set("Łukasz Kamiński")
                            email.set("lukasz.kaminski@bisnode.com")
                            organization.set("Bisnode")
                            organizationUrl.set("https://www.bisnode.com")
                        }
                    }
                }
            }
        }
        repositories {
            maven {
                val ossrhUsername: String? by project
                val ossrhPassword: String? by project
                name = "OSSRH"
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
                val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots")
                url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            }
        }
    }
}