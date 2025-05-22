package com.edutech.authservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfigProperties {
    private String secret;
    private long expiration;
    private RefreshProperties refresh = new RefreshProperties();

    public static class RefreshProperties {
        private long expiration;

        public long getExpiration() {
            return expiration;
        }

        public void setExpiration(long expiration) {
            this.expiration = expiration;
        }
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public RefreshProperties getRefresh() {
        return refresh;
    }

    public void setRefresh(RefreshProperties refresh) {
        this.refresh = refresh;
    }
}
