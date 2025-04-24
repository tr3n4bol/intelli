// OhmCalculatorController.java
package org.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.demo.FXApplication;
import org.example.demo.entities.OhmCalculation;
import org.example.demo.services.OhmCalculatorService;
import org.example.demo.util.SessionManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OhmCalculatorController {
    @FXML private TextField voltageField;
    @FXML private TextField currentField;
    @FXML private TextField resistanceField;
    @FXML private Label resultLabel;
    @FXML private ListView<String> historyList;
    
    private final OhmCalculatorService calculatorService = new OhmCalculatorService();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @FXML
    public void initialize() {
        loadHistory();
    }

    @FXML
    private void handleCalculate() {
        try {
            OhmCalculation calculation = new OhmCalculation();
            
            if (voltageField.getText().isEmpty()) {
                calculation.setCurrent(parseDouble(currentField));
                calculation.setResistance(parseDouble(resistanceField));
                calculation.setCalculationType("V");
            } else if (currentField.getText().isEmpty()) {
                calculation.setVoltage(parseDouble(voltageField));
                calculation.setResistance(parseDouble(resistanceField));
                calculation.setCalculationType("I");
            } else if (resistanceField.getText().isEmpty()) {
                calculation.setVoltage(parseDouble(voltageField));
                calculation.setCurrent(parseDouble(currentField));
                calculation.setCalculationType("R");
            } else {
                showAlert("Error", "Please leave one field empty for calculation");
                return;
            }
            
            calculation = calculatorService.calculate(
                calculation, 
                SessionManager.getCurrentUser().getUsername()
            );
            
            displayResult(calculation);
            clearFields();
            loadHistory();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers");
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private double parseDouble(TextField field) {
        return Double.parseDouble(field.getText());
    }

    private void displayResult(OhmCalculation calculation) {
        String result = switch (calculation.getCalculationType()) {
            case "V" -> String.format("Voltage: %.2f V", calculation.getVoltage());
            case "I" -> String.format("Current: %.2f A", calculation.getCurrent());
            case "R" -> String.format("Resistance: %.2f Ω", calculation.getResistance());
            default -> "Calculation error";
        };
        resultLabel.setText(result);
    }

    private void clearFields() {
        voltageField.clear();
        currentField.clear();
        resistanceField.clear();
    }

    @FXML
    private void handleBackToMenu() {
        try {
            FXApplication.showMainView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHistory() {
        List<OhmCalculation> calculations = calculatorService.getHistory(
            SessionManager.getCurrentUser().getUsername()
        );
        
        historyList.getItems().clear();
        for (OhmCalculation calc : calculations) {
            String entry = String.format("%s: %s (%s)",
                dateFormatter.format(calc.getTimestamp()),
                getCalculationString(calc),
                calc.getCalculationType()
            );
            historyList.getItems().add(entry);
        }
    }

    private String getCalculationString(OhmCalculation calc) {
        return switch (calc.getCalculationType()) {
            case "V" -> String.format("I=%.2fA, R=%.2fΩ → V=%.2fV",
                    calc.getCurrent(), calc.getResistance(), calc.getVoltage());
            case "I" -> String.format("V=%.2fV, R=%.2fΩ → I=%.2fA",
                    calc.getVoltage(), calc.getResistance(), calc.getCurrent());
            case "R" -> String.format("V=%.2fV, I=%.2fA → R=%.2fΩ",
                    calc.getVoltage(), calc.getCurrent(), calc.getResistance());
            default -> "Unknown calculation";
        };
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}