// MainController.java
package org.example.demo.controllers;

import javafx.fxml.FXML;
import org.example.demo.FXApplication;
import org.example.demo.util.SessionManager;

import java.io.IOException;

public class MainController {
    @FXML
    private void handleCurrencyConverter() throws IOException {
        FXApplication.showConverterView();
    }

    @FXML
    private void handleOhmCalculator() throws IOException {
        FXApplication.showOhmCalculatorView();
    }

    @FXML
    private void handleLogout() throws IOException {
        SessionManager.logout();
        FXApplication.showLoginView();
    }
}