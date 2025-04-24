package org.example.demo.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.example.demo.dao.UserDao;
import org.example.demo.entities.User;

import java.util.Optional;

public class AuthService {
    private final UserDao userDao = new UserDao();

    public boolean register(User user) {
        if (userDao.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }

        // Альтернативная реализация хеширования на случай проблем с BCrypt
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userDao.save(user);
        return true;
    }

    public Optional<User> authenticate(String username, String password) {
        return userDao.findByUsername(username)
                .filter(user -> verifyPassword(password, user.getPassword()));
    }

    private String hashPassword(String password) {
        try {
            return BCrypt.withDefaults().hashToString(12, password.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    private boolean verifyPassword(String inputPassword, String storedHash) {
        try {
            BCrypt.Result result = BCrypt.verifyer().verify(inputPassword.toCharArray(), storedHash);
            return result.verified;
        } catch (Exception e) {
            return false;
        }
    }
}