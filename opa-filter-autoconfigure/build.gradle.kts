import java.util.*

plugins {
    `maven-publish`
}

dependencies {
    implementation(project(Project.core))

    implementation(SpringSecurity.config)
    implementation(SpringSecurity.OAuth2.resourceServer)
    implementation(Javax.servletApi)
    implementation(SpringBoot.autoconfigure)

    annotationProcessor(SpringBoot.annotationProcessor)

    testImplementation(Testing.springBootStarterWeb)
    testImplementation(Testing.springBoot)
    testImplementation(Testing.wiremock)
    testImplementation(Testing.httpClient)
    testImplementation(Spock.groovyLang)
    testImplementation(Spock.core)
    testImplementation(Spock.spring)
}

publishing {
    publications {
        create<MavenPublication>("autoconfigureModule") {
            artifactId = "opa-filter-autoconfigure"
            from(components["java"])
            pom {
                name.set("OPA Spring Security filter autoconfiguration")
                description.set("Autoconfiguration project for Opa Spring Security filter")
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
    val signingKey: String? by project
    val signingPassword: String? by project
    val decodedKey = signingKey
            ?.let { Base64.getDecoder().decode(signingKey) }
            ?.let { String(it) }
    useInMemoryPgpKeys(decodedKey, signingPassword)
    sign(publishing.publications["autoconfigureModule"])
}