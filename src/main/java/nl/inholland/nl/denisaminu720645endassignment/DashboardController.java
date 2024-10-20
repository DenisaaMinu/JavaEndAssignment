package nl.inholland.nl.denisaminu720645endassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardController {
    @FXML private Label welcomeLabel;
    @FXML private Label roleLabel;
    @FXML private Label dateLabel;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private Database db;

    // Method to set the Database
    public void setDatabase(Database database) {
        this.db = database;
    }

    // Method to greet the user
    protected void greetUser(String username, String role) {
        welcomeLabel.setText("Welcome " + username);
        roleLabel.setText("You are logged in as " + role);
        dateLabel.setText("The current date and time is " + updateDateTime());
    }

    // Method to Update and return the current date and time formatted as a string
    protected String updateDateTime() {
        return LocalDateTime.now().format(dateFormatter);
    }

    // Method to show showings
    @FXML
    public void onManageShowingsButton(ActionEvent event) {
        try {
            SceneHelper.switchScene(event, "/nl/inholland/nl/denisaminu720645endassignment/showings-view.fxml", "Showings", ShowingsController.class, db);
        } catch (Exception e) {
            welcomeLabel.setText("Failed to load showings. Please try again.");
        }
    }

    // Method to sell tickets for a selected showing
    @FXML
    public void onSellTicketsButton(ActionEvent event) {
        try {
            SceneHelper.switchScene(event, "/nl/inholland/nl/denisaminu720645endassignment/tickets-view.fxml", "Tickets", TicketsController.class, db);
        } catch (Exception e) {
            welcomeLabel.setText("Failed to load tickets. Please try again.");
        }
    }

    // Method to see the sales history
    @FXML
    public void onSalesHistoryButton(ActionEvent event) {
        try {
            SceneHelper.switchScene(event, "/nl/inholland/nl/denisaminu720645endassignment/salesHistory-view.fxml", "Sales History", SalesHistoryController.class, db);
        } catch (Exception e) {
            welcomeLabel.setText("Failed to load sales history. Please try again.");
        }
    }

}
