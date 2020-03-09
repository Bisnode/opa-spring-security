plugins {
    id("org.springframework.boot").version("2.2.5.RELEASE")
    id("io.spring.dependency-management").version("1.0.9.RELEASE")
}

dependencies {
    implementation(project(Project.core))

    implementation(SpringSecurity.config)
    implementation(SpringSecurity.OAuth2.resourceServer)
    implementation(Javax.servletApi)
    implementation(SpringBoot.autoconfigure)

    annotationProcessor(SpringBoot.annotationProcessor)

    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation(Testing.springBoot)
    testImplementation(Testing.wiremock)
    testImplementation(Testing.httpClient)
    testImplementation(Spock.groovyLang)
    testImplementation(Spock.core)
    testImplementation(Spock.spring)
}

