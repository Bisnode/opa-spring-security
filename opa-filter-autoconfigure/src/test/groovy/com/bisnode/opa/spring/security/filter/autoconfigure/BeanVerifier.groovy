package com.bisnode.opa.spring.security.filter.autoconfigure

trait BeanVerifier {
    List<String> possibleBeanNamesOf(Class aClass) {
        return [aClass.getName(),
                aClass.getSimpleName(),
                aClass.getSimpleName().uncapitalize()
        ]
    }
}