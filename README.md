# OPA Spring Security Library

[![Maven Central](https://img.shields.io/maven-central/v/com.bisnode.opa/opa-filter-spring-boot-starter?color=light-green)](https://search.maven.org/artifact/com.bisnode.opa/opa-filter-spring-boot-starter) ![build](https://github.com/Bisnode/opa-spring-security/workflows/build/badge.svg)

OPA Spring Security is a library that enables using OPA for authorization in Spring applications.

## Installation

#### Prerequisites 

- Java 11 or higher

#### Using the starter

Add dependency using Maven

[![Maven Central](https://img.shields.io/maven-central/v/com.bisnode.opa/opa-filter-spring-boot-starter?color=light-green)](https://maven-badges.herokuapp.com/maven-central/com.bisnode.opa/opa-filter-spring-boot-starter)

```xml
<dependency>
    <groupId>com.bisnode.opa</groupId>
    <artifactId>opa-filter-spring-boot-starter</artifactId>
    <version>{version}</version>
</dependency>
```

or Gradle

```groovy
implementation 'com.bisnode.opa:opa-filter-spring-boot-starter:{version}'
```

## Configuration

**All properties are prefixed with `opa.filter`**

| Property       | Default value | Description                                  | Example                    |
|----------------|---------------|----------------------------------------------|----------------------------|
| `enabled`      |    `true`     | Whether the filter should be enabled         | _false_                    |
| `documentPath` |               | Name of OPA document path to use for queries | _"http/request/authz"_     |
| `instance`     |               | Address of OPA instance                      | _"http://localhost:8181/"_ |

## Policy requirements

Currently, the filter sends following information to OPA:

- `path` - path of the resource, that's being requested, e.g. `/messages/2` 
- `method` - HTTP method, e.g. `GET`
- `encodedJwt` - encoded token from authorization (if found, `null` otherwise)

Those properties are available in your policy's `input`.

---

Your OPA policy response should contain following properties:

- `allow` - with value `true` if to requested resource should be allowed. `false` otherwise 
- `reason` - an **optional** string containing reason behind given decision, it will be supplied to exception message 

Unknown properties are ignored.

> Example OPA response would look as follows:
> ```json
> {
>   "result": 
>   {
>       "allow": false,
>       "reason": "You shall not pass"
>   }
> }
> ```



## Developing and building
Build process and dependency management is done using Gradle.
Tests are written in Spock.
