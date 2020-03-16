ext {
    set("pomName", "OPA Spring Security filter")
    set("pomDescription", "Spring filter that uses Open Policy Agent to allow and deny access")
}

dependencies {
    api(Opa.client)
    implementation(Slf4j.api)
    implementation(SpringSecurity.web)
    implementation(SpringSecurity.oAuth2Core)
    implementation(Javax.servletApi)

    testImplementation(Spock.groovyLang)
    testImplementation(Spock.core)
    testRuntimeOnly(Spock.byteBuddy)
    testRuntimeOnly(Spock.objenesis)
}

tasks.javadoc {
    source = sourceSets["main"].allJava
}

tasks.test {
    useJUnitPlatform()
}

