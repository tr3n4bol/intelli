// ConverterController.java
package org.example.demo.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.demo.FXApplication;
import org.example.demo.entities.ConversionOperation;
import org.example.demo.services.CurrencyConverterService;
import org.example.demo.util.SessionManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConverterController {
    @FXML private ComboBox<String> fromCurrencyCombo;
    @FXML private ComboBox<String> toCurrencyCombo;
    @FXML private TextField amountField;
    @FXML private Label resultLabel;
    @FXML private ListView<String> historyList;

    private final CurrencyConverterService converterService = new CurrencyConverterService();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @FXML
    public void initialize() {
        loadCurrencies();
        loadHistory();
    }

    private void loadCurrencies() {
        List<String> currencies = List.of("USD", "EUR", "RUB", "GBP");
        fromCurrencyCombo.setItems(FXCollections.observableArrayList(currencies));
        toCurrencyCombo.setItems(FXCollections.observableArrayList(currencies));

        // Set default values
        fromCurrencyCombo.getSelectionModel().selectFirst();
        toCurrencyCombo.getSelectionModel().select(1);
    }

    @FXML
    private void handleConvert() {
        try {
            String fromCurrency = fromCurrencyCombo.getValue();
            String toCurrency = toCurrencyCombo.getValue();
            double amount = Double.parseDouble(amountField.getText());

            double result = converterService.convertCurrency(
                    fromCurrency,
                    toCurrency,
                    amount,
                    SessionManager.getCurrentUser().getUsername()
            );

            resultLabel.setText(String.format(
                    "%s %s = %s %s",
                    decimalFormat.format(amount),
                    fromCurrency,
                    decimalFormat.format(result),
                    toCurrency
            ));

            loadHistory();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for amount");
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
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
        List<ConversionOperation> operations = converterService.getHistory(
                SessionManager.getCurrentUser().getUsername()
        );

        historyList.getItems().clear();
        for (ConversionOperation op : operations) {
            String entry = String.format("%s: %s %s → %s %s (%s)",
                    dateFormatter.format(op.getTimestamp()),
                    decimalFormat.format(op.getAmount()),
                    op.getFromCurrency(),
                    decimalFormat.format(op.getConvertedAmount()),
                    op.getToCurrency()
            );
            historyList.getItems().add(entry);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}