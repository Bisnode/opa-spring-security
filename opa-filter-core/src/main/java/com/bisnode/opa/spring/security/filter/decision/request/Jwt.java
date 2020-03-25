package com.bisnode.opa.spring.security.filter.decision.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.function.Predicate.not;

class Jwt {
    private static final Logger log = LoggerFactory.getLogger(Jwt.class);
    private final String encoded;

    private Jwt(String encodedToken) {
        this.encoded = encodedToken;
    }

    static Optional<Jwt> fromSecurityContext() {
        return extractAccessToken(SecurityContextHolder.getContext().getAuthentication())
                .filter(not(String::isBlank))
                .filter(Jwt::looksLikeJwt)
                .map(Jwt::new);
    }

    private static Optional<String> extractAccessToken(final Authentication authentication) {
        if (isNull(authentication)) {
            log.warn("Received null authentication");
            return Optional.empty();
        }

        // For Spring Security in Spring < 5
        if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
            final String accessToken = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
            if (nonNull(accessToken) && !accessToken.isBlank()) {
                return Optional.of(accessToken);
            }
        }

        // For Spring Security in Spring >= 5
        if (authentication.getCredentials() instanceof AbstractOAuth2Token) {
            final String accessToken = ((AbstractOAuth2Token) authentication.getCredentials()).getTokenValue();
            return Optional.ofNullable(accessToken).filter(not(String::isBlank));
        }

        log.warn("Could not find access token in Authentication");
        return Optional.empty();
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
