package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.demo.util.HibernateUtil;

import java.io.IOException;
import java.net.URL;

public class FXApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        showLoginView();
    }

    public static void showLoginView() throws IOException {
        loadAndShowView("/views/login.fxml", "CalculatorFX - Login");
    }

    public static void showMainView() throws IOException {
        loadAndShowView("/views/main.fxml", "CalculatorFX - Main Menu");
    }

    public static void showConverterView() throws IOException {
        loadAndShowView("/views/converter.fxml", "CalculatorFX - Currency Converter");
    }

    public static void showOhmCalculatorView() throws IOException {
        loadAndShowView("/views/ohm-calculator.fxml", "CalculatorFX - Ohm's Law Calculator");
    }

    public static void showAdminView() throws IOException {
        loadAndShowView("/views/admin.fxml", "CalculatorFX - Admin Panel");
    }

    private static void loadAndShowView(String fxmlPath, String title) throws IOException {
        try {
            // 1. Получаем URL FXML-файла
            URL fxmlUrl = FXApplication.class.getResource(fxmlPath);

            // 2. Проверяем, что файл найден
            if (fxmlUrl == null) {
                throw new IOException("FXML file not found: " + fxmlPath);
            }

            // 3. Загружаем FXML
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // 4. Настраиваем сцену
            Scene scene = new Scene(root);

            // 5. Настраиваем главное окно
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);

            // 6. Показываем окно
            if (!primaryStage.isShowing()) {
                primaryStage.show();
            }
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + fxmlPath);
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            // Инициализация Hibernate
            HibernateUtil.init();

            // Запуск JavaFX приложения
            launch(args);
        } catch (Exception e) {
            System.err.println("Application initialization failed");
            e.printStackTrace();
            HibernateUtil.shutdown();
        }
    }
}