// AdminController.java
package org.example.demo.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.demo.FXApplication;
import org.example.demo.entities.User;
import org.example.demo.services.UserService;
import org.example.demo.util.SessionManager;

import java.io.IOException;

public class AdminController {
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> roleColumn;
    
    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        
        loadUsers();
    }

    private void loadUsers() {
        usersTable.setItems(FXCollections.observableArrayList(userService.getAllUsers()));
    }

    @FXML
    private void handleSwitchRole() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null && !selectedUser.getUsername().equals(SessionManager.getCurrentUser().getUsername())) {
            userService.switchUserRole(selectedUser.getUsername());
            loadUsers();
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null && !selectedUser.getUsername().equals(SessionManager.getCurrentUser().getUsername())) {
            userService.deleteUser(selectedUser.getId());
            loadUsers();
        }
    }

    @FXML
    private void handleBackToMenu() throws IOException {
        FXApplication.showMainView();
    }

    @FXML
    private void handleLogout() throws IOException {
        SessionManager.logout();
        FXApplication.showLoginView();
    }
}