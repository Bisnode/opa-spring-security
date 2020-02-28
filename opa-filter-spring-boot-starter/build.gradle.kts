plugins {
    java
    `java-library`
    groovy
}

dependencies {
    implementation(project(":opa-filter-autoconfigure"))
    implementation(project(":opa-filter-core"))
}