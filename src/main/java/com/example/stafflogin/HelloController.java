package com.example.stafflogin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HelloController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button cancelButton;

    private final String directoryPath = "C:/example/directory/";
    private final File credentialsFile = new File(directoryPath + "credentials.txt");

    @FXML
    void loginButtonOnAction() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if (!username.isEmpty() && !password.isEmpty()) {
            try {
                String hashedPassword = hashPassword(password);
                if (validateCredentials(username, hashedPassword)) {
                    loginMessageLabel.setText("Login successful!");
                } else {
                    loginMessageLabel.setText("Invalid username or password");
                }
            } catch (NoSuchAlgorithmException | IOException e) {
                loginMessageLabel.setText("Error: " + e.getMessage());
            }
        } else {
            loginMessageLabel.setText("Please enter username and password");
        }
    }

    @FXML
    void registerButtonOnAction() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if (!username.isEmpty() && !password.isEmpty()) {
            try {
                String hashedPassword = hashPassword(password);
                if (!isUsernameTaken(username)) {
                    saveCredentials(username, hashedPassword);
                    loginMessageLabel.setText("Registration successful! You can now login.");
                } else {
                    loginMessageLabel.setText("Username already taken.");
                }
            } catch (NoSuchAlgorithmException | IOException e) {
                loginMessageLabel.setText("Error: " + e.getMessage());
            }
        } else {
            loginMessageLabel.setText("Please enter username and password");
        }
    }

    @FXML
    void cancelButtonOnAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private void saveCredentials(String username, String hashedPassword) throws IOException {
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter writer = new FileWriter(credentialsFile, true)) {
            writer.write(username + ":" + hashedPassword + "\n");
        }
    }

    private boolean isUsernameTaken(String username) throws IOException {
        if (!credentialsFile.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validateCredentials(String username, String hashedPassword) throws IOException {
        if (!credentialsFile.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username) && parts[1].equals(hashedPassword)) {
                    return true;
                }
            }
        }
        return false;
    }
}
