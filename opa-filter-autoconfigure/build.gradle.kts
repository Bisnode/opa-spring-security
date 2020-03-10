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

