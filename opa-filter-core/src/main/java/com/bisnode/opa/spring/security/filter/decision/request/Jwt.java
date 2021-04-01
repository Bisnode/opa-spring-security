package com.bisnode.opa.spring.security.filter.decision.request;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static java.util.function.Predicate.not;

class Jwt {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";

    private final String encoded;

    private Jwt(String encodedToken) {
        this.encoded = encodedToken;
    }

    static Optional<Jwt> fromHttpRequest(HttpServletRequest httpRequest) {
        return extractAccessToken(httpRequest)
                .filter(not(String::isBlank))
                .filter(Jwt::looksLikeJwt)
                .map(Jwt::new);
    }

    private static Optional<String> extractAccessToken(HttpServletRequest httpRequest) {
        return Optional.ofNullable(httpRequest)
                .map(request -> request.getHeader(AUTHORIZATION))
                .map(String::trim)
                .filter(authorization -> authorization.toLowerCase().startsWith(BEARER.toLowerCase()))
                .map(header -> header.substring(BEARER.length()))
                .map(String::trim);
    }

    /**
     * 'Borrowed' from former {@link org.springframework.security}.
     * The assumption is that proper Jwt consists of three base64 strings separated with '.'
     * Here, we also assume that Jwt is always signed (i.e. has not empty 'crypto' section)
     *
     * @param token encoded token we suspect is Jwt
     * @return true if passed string conforms to encoded Jwt format, false otherwise
     */

    private static boolean looksLikeJwt(String token) {
        int firstPeriod = token.indexOf('.');
        int lastPeriod = token.lastIndexOf('.');
        // 1. has three sections separated with dots
        if (firstPeriod <= 0 || lastPeriod <= firstPeriod) {
            return false;
        }
        // 2. third section (crypto) is not empty
        return lastPeriod != token.length() - 1;
    }

    public String getEncoded() {
        return this.encoded;
    }
}
