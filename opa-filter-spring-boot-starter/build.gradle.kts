plugins {
    `maven-publish`
}

dependencies {
    implementation(project(Project.core))
    implementation(project(Project.autoconfigure))
}

publishing {
    publications {
        create<MavenPublication>("starterModule") {
            artifactId = "opa-filter-spring-boot-starter"
            from(components["java"])
            pom {
                name.set("OPA Spring Security filter Spring Boot Starter")
                description.set("Spring Boot Starter for OPA Spring Security filter")
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

signing {
    sign(publishing.publications["starterModule"])
}
