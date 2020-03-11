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