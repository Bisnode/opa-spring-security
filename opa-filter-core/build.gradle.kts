import java.util.Base64

plugins {
    `maven-publish`
}

dependencies {
    api(Opa.client)
    implementation(Slf4j.api)
    implementation(SpringSecurity.web)
    implementation(SpringSecurity.OAuth2.core)
    implementation(SpringSecurity.OAuth2.jose)
    implementation(Javax.servletApi)

    testImplementation(Spock.groovyLang)
    testImplementation(Spock.core)
    testRuntimeOnly(Spock.byteBuddy)
    testRuntimeOnly(Spock.objenesis)
}

java {
    withJavadocJar()
}

tasks.javadoc {
    source = sourceSets["main"].allJava
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("coreModule") {
            artifactId = "opa-filter-core"
            from(components["java"])
            pom {
                name.set("OPA Spring Security filter")
                description.set("Spring filter that uses Open Policy Agent to allow and deny access")
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
    sign(publishing.publications["coreModule"])
}
