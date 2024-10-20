package nl.inholland.nl.denisaminu720645endassignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ShowingsController {
    @FXML public TableView<Showing> tableView;
    @FXML protected TableColumn<Showing, String> startColumn;
    @FXML protected TableColumn<Showing, String> endColumn;
    @FXML protected TableColumn<Showing, String> titleColumn;
    @FXML protected TableColumn<Showing, Integer> seatsColumn;

    @FXML protected Button addShowingButton;
    @FXML protected Button editShowingButton;
    @FXML protected Button deleteShowingButton;
    @FXML protected Button manageShowingsButton;

    @FXML protected Label errorLabel;
    @FXML protected TextField titleField;
    @FXML protected DatePicker startDatePicker;
    @FXML protected DatePicker endDatePicker;
    @FXML protected TextField startTimeField;
    @FXML protected TextField endTimeField;

    protected Database db;
    protected Showing currentShowing;
    protected final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    protected final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // Method to set the database
    protected void setDatabase(Database db) {
        this.db = db;
        addTableSelectionListener();
    }

    // Add a listener to detect when a user selects a row in the TableView
    private void addTableSelectionListener() {
        if(tableView != null){
            tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    currentShowing = newSelection;  // Update the currentShowing when a row is selected
                    errorLabel.setText("");        // Clear any previous error messages
                    editShowingButton.setDisable(false);
                    deleteShowingButton.setDisable(false);
                } else{
                    editShowingButton.setDisable(true);
                    deleteShowingButton.setDisable(true);
                }
            });
        }
    }

    // Method to initialize the table with showing data from the db
    protected void initializeTable() {
        if (startColumn != null && endColumn != null && titleColumn != null && seatsColumn != null) {
            startColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            endColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            seatsColumn.setCellValueFactory(new PropertyValueFactory<>("seatsLeft"));

            tableView.setPlaceholder(new Label("No showings yet"));
            ObservableList<Showing> updatedShowings = FXCollections.observableArrayList(db.getShowings()); // Ensure ObservableList
            tableView.setItems(updatedShowings);  // Set the items
            tableView.refresh();                 // Force table to refresh

            editShowingButton.setDisable(true);
            deleteShowingButton.setDisable(true);
        }
    }

    // Method to validate the field
    protected boolean areFieldsValid() {
        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            errorLabel.setText("Title cannot be empty");
            return false;
        }
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            errorLabel.setText("Please select a valid date.");
            return false;
        }
        try {
            LocalTime.parse(startTimeField.getText(), timeFormatter);
            LocalTime.parse(endTimeField.getText(), timeFormatter);
        } catch (DateTimeParseException e) {
            errorLabel.setText("Invalid time format.");
            return false;
        }
        errorLabel.setText("");  // Clear any previous errors
        return true;            // All fields are valid
    }

    // Method to add a showing by clicking the button
    @FXML
    protected void onAddShowingButton(ActionEvent event) {
        SceneHelper.openShowingDialog("/nl/inholland/nl/denisaminu720645endassignment/addShowing-view.fxml","Add Showing",null, db, this);
    }

    // Edit a showing
    public void onEditShowingButton(ActionEvent event) {
        if (currentShowing != null) {
            SceneHelper.openShowingDialog("/nl/inholland/nl/denisaminu720645endassignment/editShowing-view.fxml", "Edit Showing",currentShowing, db, this);
        } else {
            errorLabel.setText("No showing selected");
        }
    }

    // Delete a showing
    @FXML
    public void onDeleteShowingButton(ActionEvent event) {
        // Get the selected showing from the TableView
        Showing selectedShowing = tableView.getSelectionModel().getSelectedItem();

        // Check if a showing has been selected
        if(selectedShowing != null) {
            LocalDateTime showingDateTime = LocalDateTime.parse(selectedShowing.getStartDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            LocalDateTime now = LocalDateTime.now();

            // Check if all tickets have been sold and the showing date has passed
            if (selectedShowing.getTicketsSold() == selectedShowing.getTotalSeats() && showingDateTime.isBefore(now)) {
                db.deleteShowing(selectedShowing);
                tableView.getItems().remove(selectedShowing);
                tableView.refresh();
            } else if (selectedShowing.getTicketsSold() > 0) {
                errorLabel.setText("Tickets have already been sold for this showing.");
            } else {
                db.deleteShowing(selectedShowing);
                tableView.getItems().remove(selectedShowing);
                tableView.refresh();
            }
            errorLabel.setText("");
        } else{
            errorLabel.setText("No showing selected");
        }
    }

    @FXML
    protected void onSellTicketsButton(ActionEvent event) {
        SceneHelper.switchScene(event, "/nl/inholland/nl/denisaminu720645endassignment/tickets-view.fxml", "Sell Tickets", TicketsController.class, db);
    }

    @FXML
    protected void onSalesHistoryButton(ActionEvent event) {
        try {
            SceneHelper.switchScene(event, "/nl/inholland/nl/denisaminu720645endassignment/salesHistory-view.fxml", "Sales History", SalesHistoryController.class, db);
        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Failed to load sales history. Please try again.");
        }
    }
}