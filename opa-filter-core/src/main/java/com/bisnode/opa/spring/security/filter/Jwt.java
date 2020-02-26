package com.bisnode.opa.spring.security.filter;

import static org.apache.commons.lang3.StringUtils.isBlank;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Optional;

public class Jwt {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Jwt.class);
    private final String encoded;

    private Jwt(String encoded) {
        this.encoded = encoded;
    }

    public static Optional<Jwt> fromSecurityContext() {
        return extractAccessToken(SecurityContextHolder.getContext().getAuthentication())
                .filter(Jwt::looksLikeJwt)
                .map(Jwt::new);
    }

    private static Optional<String> extractAccessToken(final Authentication authentication) {
        if (!(authentication instanceof OAuth2Authentication)) {
            log.debug("Authentication not an instance of OAuth2Authentication.");
            return Optional.empty();
        }

        if (!(authentication.getDetails() instanceof OAuth2AuthenticationDetails)) {
            log.debug("AuthenticationDetails not an instance of OAuth2AuthenticationDetails.");
            return Optional.empty();
        }

        final String accessToken = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
        if (isBlank(accessToken)) {
            log.debug("No valid access token found in context (null or empty).");
            return Optional.empty();
        }
        return Optional.of(accessToken);
    }

    /**
     * 'Borrowed' from {@link org.springframework.security.jwt.JwtHelper#decode(String)}
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
