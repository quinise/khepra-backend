package com.khepraptah.khepra_site_backend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FirebaseUserService {
    private final FirebaseAuth firebaseAuth;

    public FirebaseUserService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public boolean existsByEmail(String rawEmail) {
        String email = rawEmail == null ? "" : rawEmail.trim();
        if (email.isEmpty()) return false;
        try {
            firebaseAuth.getUserByEmail(email); // throws if not found
            return true;
        } catch (FirebaseAuthException e) {
            // USER_NOT_FOUND is the common code, but “not found” may also surface as a generic exception
            if ("USER_NOT_FOUND".equals(e.getErrorCode())) return false;
            // For other Firebase errors (quota, network), you can either return false or rethrow
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "FIREBASE_LOOKUP_FAILED", e);
        }
    }
}
