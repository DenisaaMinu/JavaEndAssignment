package nl.inholland.nl.denisaminu720645endassignment;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

public class TicketsController {
    @FXML protected TableView<Showing> tableView;
    @FXML protected TableColumn<Showing, String> startColumn;
    @FXML protected TableColumn<Showing, String> endColumn;
    @FXML protected TableColumn<Showing, String> titleColumn;
    @FXML protected TableColumn<Showing, Integer> seatsColumn;
    @FXML protected Label selectedSeatLabel;
    @FXML protected Button selectShowingButton;

    protected Database db;
    protected Showing currentShowing;

    protected void setDatabase(Database db) {
        this.db = db;
        initializeTable();
    }

    // Initialize the TableView and set the selection listener
    protected void initializeTable() {
        if (db != null) {
            ObservableList<Showing> showingObservableList = db.getAvailableShowings();

            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            startColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            endColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            seatsColumn.setCellValueFactory(new PropertyValueFactory<>("seatsLeft"));

            tableView.setPlaceholder(new Label("No showings yet"));
            tableView.setItems(showingObservableList);
            addTableSelectionListener();     // Add listener for row selection

            selectShowingButton.setDisable(true);
        } else {
            System.err.println("Database is not set in TicketsController.");
        }
    }

    // Add a listener to detect when a user selects a row in the TableView
    private void addTableSelectionListener() {
        if (tableView != null) {
            tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    currentShowing = newSelection;  // Update the currentShowing when a row is selected
                    selectedSeatLabel.setTextFill(Color.BLACK);
                    selectedSeatLabel.setText("Selected: " + newSelection.getStartDate() + " " + newSelection.getTitle());

                    selectShowingButton.setDisable(false);
                } else {
                    selectShowingButton.setDisable(true);
                }
            });
        }
    }

    // Manage Showings button action
    public void onManageShowingsButton(ActionEvent event) {
        try {
            SceneHelper.switchScene(event, "/nl/inholland/nl/denisaminu720645endassignment/showings-view.fxml", "Showings", ShowingsController.class, db);
        } catch (Exception e) {
            selectedSeatLabel.setTextFill(Color.RED);
            selectedSeatLabel.setText("Failed to load showings. Please try again.");
        }
    }


    // Select showing to sell tickets
    @FXML
    protected void onSelectShowing(ActionEvent event) {
        if (currentShowing != null) {
            SceneHelper.openShowingDialog("/nl/inholland/nl/denisaminu720645endassignment/sellTickets-view.fxml", "Sell tickets", currentShowing, db, this);
        } else {
            selectedSeatLabel.setTextFill(Color.RED);
            selectedSeatLabel.setText("No showing selected");
        }
    }

    // Sales History button action
    @FXML
    protected void onSalesHistoryButton(ActionEvent event) {
        try {
            SceneHelper.switchScene(event, "/nl/inholland/nl/denisaminu720645endassignment/salesHistory-view.fxml", "Sales History", SalesHistoryController.class, db);
        } catch (Exception e) {
            selectedSeatLabel.setTextFill(Color.RED);
            selectedSeatLabel.setText("Failed to load sales history. Please try again.");
        }
    }
}
