package com.khepraptah.khepra_site_backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")  // Register the filter for all URLs
public class JwtAuthenticationFilter implements Filter {
    static String secretKey = System.getenv("KS_BACKEND_SECRET_KEY");
    private static final String SECRET_KEY = secretKey;  // Secret key for signing and validating JWTs

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Cast the ServletRequest to HttpServletRequest for easy access to headers
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Extract token from request
        String token = extractTokenFromRequest(httpRequest);

        // If token exists, validate it and add user info to request
        if (token != null) {
            Claims claims = validateTokenAndGetClaims(token);
            if (claims != null) {
                String email = claims.getSubject();  // Assuming the email is stored in the 'subject' claim
                // Set admin status on request attributes
                httpRequest.setAttribute("isAdmin", "devin.ercolano@gmail.com".equals(email));
            }
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // Remove the "Bearer " prefix
        }
        return null;
    }

    private Claims validateTokenAndGetClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;  // Invalid token
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
