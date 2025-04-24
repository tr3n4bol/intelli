// LoginController.java
package org.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import org.example.demo.FXApplication;
import org.example.demo.entities.User;
import org.example.demo.services.AuthService;
import org.example.demo.util.SessionManager;

import java.io.IOException;
import java.util.Optional;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Optional<User> user = authService.authenticate(username, password);
        if (user.isPresent()) {
            SessionManager.setCurrentUser(user.get());
            if (SessionManager.isAdmin()) {
                FXApplication.showAdminView();
            } else {
                FXApplication.showMainView();
            }
        } else {
            showAlert("Login Failed", "Invalid username or password");
        }
    }

    @FXML
    private void handleRegister() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Registration Failed", "Username and password cannot be empty");
            return;
        }

        User newUser = new User(username, password);
        if (authService.register(newUser)) {
            showAlert("Registration Successful", "You can now login with your credentials");
        } else {
            showAlert("Registration Failed", "Username already exists");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}