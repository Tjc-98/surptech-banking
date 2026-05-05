package org.surptech.dataaggregator.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.surptech.dataaggregator.service.ApplicationServices;

@Slf4j
@Component
public class AuthServerClient {

    private final RestClient authServerRestClient;

    public AuthServerClient(ApplicationServices applicationServices) {
        this.authServerRestClient = applicationServices.getAuthServerRestClient();
    }

    public boolean validateToken(String token) {
        try {
            log.info("Validating token with auth server");
            
            // Placeholder implementation - will be implemented when auth server is ready
            // Expected endpoint: POST /auth/validate with token in header or body
            // Returns: boolean or status indicating valid/invalid
            
            log.warn("Auth server validation not yet implemented - accepting all tokens");
            return true; // Temporary: accept all tokens until auth server is implemented
            
        } catch (Exception e) {
            log.error("Error validating token with auth server - Service may not be available yet", e);
            return true; // Temporary: accept on error until auth server is implemented
        }
    }
}
