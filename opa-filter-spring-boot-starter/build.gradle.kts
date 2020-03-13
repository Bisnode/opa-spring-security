ext {
    set("pomName", "OPA Spring Security filter Spring Boot Starter")
    set("pomDescription", "Spring Boot Starter for OPA Spring Security filter")
}

dependencies {
    implementation(project(Project.core))
    implementation(project(Project.autoconfigure))
}
