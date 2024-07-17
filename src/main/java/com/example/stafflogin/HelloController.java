package com.example.stafflogin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
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
    private Button cancelButton;

    @FXML
    void loginButtonOnAction() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if (!username.isEmpty() && !password.isEmpty()) {
            try {
                String hashedPassword = hashPassword(password);
                saveCredentials(username, hashedPassword);
                loginMessageLabel.setText("Login successful!");
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
        // Specify the directory where the file should be saved
        String directoryPath = "C:/example/directory/";
        File directory = new File(directoryPath);

        // Create the directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create the file in the specified directory
        File file = new File(directoryPath + "credentials.txt");

        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write("Username: " + username + ", Password: " + hashedPassword + "\n");
        }
    }
}
