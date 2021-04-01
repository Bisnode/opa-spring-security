object Versions {
    const val gradle = "6.2.2"

    const val springBoot = "2.2.4.RELEASE"
    const val spring = "5.2.2.RELEASE"
    const val legacySecurity = "2.4.0.RELEASE"
    const val opaClient = "0.0.2"
    const val javaxServletApi = "4.0.1"
    const val slf4j = "1.7.30"
    const val groovy = "2.5.7"
    const val spock = "1.3-groovy-2.5"
    const val byteBuddy = "1.9.3"
    const val objenesis = "2.6"
    const val wiremock = "2.26.0"
    const val httpClient = "0.7.1"
}


object SpringBoot {
    const val autoconfigure = "org.springframework.boot:spring-boot-autoconfigure:${Versions.springBoot}"
    const val annotationProcessor = "org.springframework.boot:spring-boot-configuration-processor:${Versions.springBoot}"
}

object SpringSecurity {
    const val config = "org.springframework.security:spring-security-config:${Versions.spring}"
    const val web = "org.springframework.security:spring-security-web:${Versions.spring}"
}


object Opa {
    const val client = "com.bisnode.opa:opa-java-client:${Versions.opaClient}"
}

object Testing {
    const val springBoot = "org.springframework.boot:spring-boot-starter-test:${Versions.springBoot}"
    const val springBootStarterWeb = "org.springframework.boot:spring-boot-starter-web:${Versions.springBoot}"
    const val wiremock = "com.github.tomakehurst:wiremock-jre8:${Versions.wiremock}"
    const val httpClient = "org.codehaus.groovy.modules.http-builder:http-builder:${Versions.httpClient}"
    const val springTest = "org.springframework:spring-test:${Versions.spring}"
}

object Spock {
    const val groovyLang = "org.codehaus.groovy:groovy-all:${Versions.groovy}"
    const val core = "org.spockframework:spock-core:${Versions.spock}"
    const val spring = "org.spockframework:spock-spring:${Versions.spock}"
    const val byteBuddy = "net.bytebuddy:byte-buddy:${Versions.byteBuddy}"
    const val objenesis = "org.objenesis:objenesis:${Versions.objenesis}"
}

object Javax {
    const val servletApi = "javax.servlet:javax.servlet-api:${Versions.javaxServletApi}"
}

object Slf4j {
    const val api = "org.slf4j:slf4j-api:${Versions.slf4j}"
}

object Project {
    const val core = ":opa-filter-core"
    const val autoconfigure = ":opa-filter-autoconfigure"
}
