package com.cardamom.ledger.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for estate authentication and JWT token issuance.
 */
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {

    private boolean enabled = true;
    private String username = "ajith@highlandestate.com";
    private String password = "cardamom2026";
    private String name = "Ajith Nair";
    private String role = "Estate General Manager";
    private String estate = "Highland Cardamom Valley, Idukki";
    private String jwtSecret = "";
    private long tokenExpirationHours = 720L;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEstate() {
        return estate;
    }

    public void setEstate(String estate) {
        this.estate = estate;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public long getTokenExpirationHours() {
        return tokenExpirationHours;
    }

    public void setTokenExpirationHours(long tokenExpirationHours) {
        this.tokenExpirationHours = tokenExpirationHours;
    }
}

