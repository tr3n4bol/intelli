// UserService.java
package org.example.demo.services;

import org.example.demo.dao.UserDao;
import org.example.demo.entities.User;

import java.util.List;

public class UserService {
    private final UserDao userDao = new UserDao();

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void switchUserRole(String username) {
        userDao.findByUsername(username).ifPresent(user -> {
            user.setRole(user.getRole().equals("USER") ? "ADMIN" : "USER");
            userDao.update(user);
        });
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }
}