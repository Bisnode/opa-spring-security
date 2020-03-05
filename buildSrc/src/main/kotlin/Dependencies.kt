object SpringBoot {
    const val autoconfigure = "org.springframework.boot:spring-boot-autoconfigure:${Versions.springBoot}"
    const val annotationProcessor = "org.springframework.boot:spring-boot-configuration-processor:${Versions.springBoot}"
}

object SpringSecurity {
    const val config = "org.springframework.security:spring-security-config:${Versions.springSecurity}"
    const val web = "org.springframework.security:spring-security-web:${Versions.springSecurity}"
    object OAuth2 {
        const val core = "org.springframework.security:spring-security-oauth2-core:${Versions.springSecurity}"
        const val jose = "org.springframework.security:spring-security-oauth2-jose:${Versions.springSecurity}"
        const val resourceServer = "org.springframework.security:spring-security-oauth2-resource-server:${Versions.springSecurity}"
    }
}

object Opa {
    const val client = "com.bisnode.opa:opa-java-client:${Versions.opaClient}"
}

object Testing {
    const val springBoot = "org.springframework.boot:spring-boot-starter-test:${Versions.springBoot}"
    const val wiremock = "com.github.tomakehurst:wiremock-jre8:${Versions.wiremock}"
    const val httpClient = "org.codehaus.groovy.modules.http-builder:http-builder:${Versions.httpClient}"
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