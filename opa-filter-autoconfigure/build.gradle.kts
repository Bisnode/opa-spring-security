plugins {
    groovy
    java
   `java-library`
    `maven-publish`
}

dependencies {
    implementation(project(":opa-filter-core"))
    //implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.security:spring-security-config:5.2.2.RELEASE")
//    implementation("org.springframework.security:spring-security-web:5.2.2.RELEASE")
    implementation("org.springframework.security:spring-security-oauth2-resource-server:5.2.2.RELEASE")
    //implementation("javax.servlet:javax.servlet-api:$javaxServletApiVersion")
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.2.4.RELEASE")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.2.4.RELEASE")
	testImplementation("org.codehaus.groovy:groovy-all:2.5.7")
    testImplementation("org.spockframework:spock-core:1.3-groovy-2.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.2.4.RELEASE")

}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withJavadocJar()
    withSourcesJar()
}