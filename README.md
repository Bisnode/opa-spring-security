# OPA Spring Security Library

OPA Spring Security is a library that enables usage of OPA as authorization tool in Spring applications.

## Installation

#### Prerequisites 

- Java 11 or higher.
- Your project has to have `org.springframework.security:spring-security-oauth2-jose` on classpath  

#### Using the starter

Add dependency using Maven

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

| Property   | Default value | Description                           | Example                    |
|------------|---------------|---------------------------------------|----------------------------|
| `enabled`  |    `true`     | Whether the filter should be enabled  | _false_                    |
| `policy`   |               | Name of OPA policy to use for queries | _"http/request/authz"_     |
| `instance` |               | Address of OPA instance               | _"http://localhost:8181/"_ |

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
