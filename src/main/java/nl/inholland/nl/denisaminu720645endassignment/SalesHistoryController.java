package nl.inholland.nl.denisaminu720645endassignment;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

public class SalesHistoryController {
    @FXML protected TableView<SalesHistory> tableView;
    @FXML protected TableColumn<SalesHistory, String> dateTime;
    @FXML protected TableColumn<SalesHistory, Integer> numberOfTickets;
    @FXML protected TableColumn<SalesHistory, String> customerName;
    @FXML protected TableColumn<SalesHistory, String> showing;
    @FXML protected Label errorLabel;

    protected Database db;

    // Set the database and initialize the table
    protected void setDatabase(Database db) {
        this.db = db;
        initializeTable();
    }

    // Initialize the TableView with data from the database
    protected void initializeTable() {
        if (isTableColumnsLoaded()) {
            // Bind TableColumns to SalesHistory properties
            dateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
            numberOfTickets.setCellValueFactory(new PropertyValueFactory<>("numberOfTickets"));
            customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));

            // Custom cell factory for Showing to concatenate date and title
            showing.setCellValueFactory(cellData -> {
                Showing showing = cellData.getValue().getShowing();
                String displayString = showing.getStartDate().substring(0, 10) + " " + showing.getStartDate().substring(11) + " " + showing.getTitle();
                return new SimpleStringProperty(displayString);
            });

            // Populate the TableView with data from the Database
            tableView.setItems(db.getSalesHistoryFromFile());
            tableView.refresh();  // Ensure table view is refreshed
        } else {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("TableColumns are not properly loaded from FXML.");
        }
    }

    // Check if the TableColumns are properly loaded from FXML
    private boolean isTableColumnsLoaded() {
        return dateTime != null && numberOfTickets != null && showing != null && customerName != null;
    }

    // Manage Showings button action
    @FXML
    protected void onManageShowingsButton(ActionEvent event) {
        try {
            SceneHelper.switchScene(event, "/nl/inholland/nl/denisaminu720645endassignment/showings-view.fxml", "Showings", ShowingsController.class, db);
        } catch (Exception e) {
            errorLabel.setText("Failed to load the showings screen.");
        }
    }

    // Sell Tickets button action
    @FXML
    protected void onSellTicketsButton(ActionEvent event) {
        try {
            SceneHelper.switchScene(event, "/nl/inholland/nl/denisaminu720645endassignment/tickets-view.fxml", "Tickets", TicketsController.class, db);
        } catch (Exception e) {
            errorLabel.setText("Failed to load the tickets screen.");
        }
    }
}