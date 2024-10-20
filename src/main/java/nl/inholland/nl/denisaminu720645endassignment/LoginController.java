package nl.inholland.nl.denisaminu720645endassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {
    @FXML private Label errorLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());
    private final Database database = new Database();

    // Method to validate the login details
    private boolean validateLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            displayError("Please enter both username and password.");
            return false;
        }
        return true;
    }

    // Method to pass the login details
    @FXML
    public void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (validateLogin(username, password)) {
            User user = database.getUser(username, password);
            if (user != null) {
                loadDashboard(event, user);
            } else {
                displayError("Invalid username or password.");
            }
        }
    }

    // Method to load the dashboard/main view
    private void loadDashboard(ActionEvent event, User user) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/nl/inholland/nl/denisaminu720645endassignment/main-view.fxml"));
            Parent root = fxmlLoader.load();

            DashboardController controller = fxmlLoader.getController();
            controller.greetUser(user.getUsername(), user.getRole());
            controller.setDatabase(database);

            switchScene(event, root, "Dashboard");
        } catch (IOException e) {
           LOGGER.log(Level.SEVERE, "Error loading the main view", e);
           displayError("Error loading the main view.");
        }
    }

    private void displayError(String message) {
        errorLabel.setText(message);
    }

    // Method to switch to the next scene
    private void switchScene(ActionEvent event, Parent root, String title) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}