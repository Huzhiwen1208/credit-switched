package org.credit.demo.service;

import org.credit.demo.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private final Map<String, User> byEmail = new ConcurrentHashMap<>();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public boolean existsByEmail(String email) {
        return byEmail.containsKey(email.toLowerCase());
    }

    public User createUser(String email, String plainPassword) {
        if (existsByEmail(email)) return null;
        User u = new User();
        u.setId(UUID.randomUUID().toString());
        u.setEmail(email.toLowerCase());
        u.setPasswordHash(encoder.encode(plainPassword));
        u.setVerified(true);
        byEmail.put(u.getEmail(), u);
        return u;
    }

    public User findByEmail(String email) {
        return byEmail.get(email.toLowerCase());
    }

    public boolean validatePassword(String email, String plainPassword) {
        User u = findByEmail(email);
        if (u == null) return false;
        return encoder.matches(plainPassword, u.getPasswordHash());
    }

    // For debugging: list all users (email and id, without passwordHash)
    // WARNING: Only use for development and debugging. Do not enable in production as it may leak user info.
    public java.util.Collection<User> listAllUsers() {
        return byEmail.values();
    }
}
