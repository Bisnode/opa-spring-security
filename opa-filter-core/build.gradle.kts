plugins {
    java
    `java-library`
    `maven-publish`
    groovy
}

dependencies {
    api("com.bisnode.opa:opa-java-client:0.0.1-SNAPSHOT")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("org.springframework.security:spring-security-web:5.2.2.RELEASE")
    implementation("org.springframework.security:spring-security-oauth2-core:5.2.2.RELEASE")
    implementation("org.springframework.security:spring-security-oauth2-jose:5.2.2.RELEASE")
    implementation("javax.servlet:javax.servlet-api:4.0.1")

    testImplementation("org.codehaus.groovy:groovy-all:2.5.7")
    testImplementation("org.spockframework:spock-core:1.3-groovy-2.5")
    testRuntimeOnly("net.bytebuddy:byte-buddy:1.9.3")
    testRuntimeOnly("org.objenesis:objenesis:2.6")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withJavadocJar()
    withSourcesJar()
}