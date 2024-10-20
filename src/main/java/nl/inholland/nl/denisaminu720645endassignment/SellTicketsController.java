package nl.inholland.nl.denisaminu720645endassignment;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SellTicketsController{

    @FXML private GridPane ticketsGrid;
    @FXML private Button sellTicketButton;
    @FXML private Label selectedSeatLabel;
    @FXML private ListView<String> selectedSeatsList;
    @FXML private TextField customerNameTextField;

    private final List<String> selectedSeats = new ArrayList<>();
    private Map<String, Boolean> seatAvailability = new HashMap<>();
    private TableView<Showing> showingsTableView;  // Add a reference to the showings TableView
    private Database db;
    private Showing currentShowing;
    private int tickets = 0;
    private static final double BUTTON_WIDTH = 29;
    private static final double BUTTON_HEIGHT = 26;

    // Method to set the showings TableView
    public void setShowingsTableView(TableView<Showing> showingsTableView) {
        this.showingsTableView = showingsTableView;
    }

    // Method to set the database
    protected void setDatabase(Database db) {
        this.db = db;
    }

    // Method to get the current showing and initialize the seat buttons
    protected void getCurrentShowing(Showing currentShowing) {
        this.currentShowing = currentShowing;
        initializeSeats();
        updateSellButtonText();
    }

    // Initialize seat buttons based on the current showing's seat availability
    private void initializeSeats() {
        ticketsGrid.getChildren().clear();
        selectedSeats.clear();
        selectedSeatsList.getItems().clear();
        seatAvailability = currentShowing.getSeatAvailability();
        createSeatsButtons();
    }

    // Method to create and display seat buttons in the grid
    private void createSeatsButtons() {
        final int totalRows = 6;
        final int seatsPerRow = 12;

        // Create seat buttons in the grid
        for (int row = 1; row <= totalRows; row++) {
            for (int col = 1; col <= seatsPerRow; col++) {
                String seatKey = "Row " + row + " Seat " + col;  // Unique key for the seat
                ToggleButton seatButton = createSeatButton(seatKey, col);
                ticketsGrid.add(seatButton, col-1, row-1);
                GridPane.setMargin(seatButton, new Insets(5, 5, 5, 5));
            }
        }
    }

    // Create a seat button with the given seat key and seat number.
    private ToggleButton createSeatButton(String seatKey, int seatNumber) {
        ToggleButton seatButton = new ToggleButton(String.valueOf(seatNumber));
        seatButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        seatButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        updateSeatButtonStyle(seatButton, seatKey);
        seatButton.setOnAction(e -> handleSeatToggle(seatButton, seatKey));
        return seatButton;
    }

    // Update the style of a seat button based on its availability
    private void updateSeatButtonStyle(ToggleButton seatButton, String seatKey) {
        if (seatAvailability.getOrDefault(seatKey, false)) {
            seatButton.setStyle("-fx-background-color: red;");
            seatButton.setDisable(true);
        } else {
            seatButton.setStyle("-fx-background-color: grey;");
            seatButton.setDisable(false);
        }
    }

    // Handle the selection or deselection of a seat
    private void handleSeatToggle(ToggleButton seatButton, String seatKey) {
        if (seatButton.isSelected()) {
            addSeat(seatKey);
            seatButton.setStyle("-fx-background-color: green;");
            seatButton.setDisable(false);
        } else {
            removeSeat(seatKey);
            seatButton.setStyle("-fx-background-color: grey;");
        }
    }

    // Add a seat to the selected list and update the UI
    private void addSeat(String seatKey) {
        selectedSeats.add(seatKey);
        selectedSeatLabel.setText("Selected seat: " + seatKey);
        selectedSeatsList.getItems().add("Seat " + seatKey);
        tickets++;
        updateSellButtonText();
    }

    // Remove a seat to the selected list and update the UI
    private void removeSeat(String seatKey) {
        selectedSeats.remove(seatKey);
        selectedSeatLabel.setText("");
        selectedSeatsList.getItems().removeIf(item -> item.contains(seatKey));
        tickets--;
        updateSellButtonText();
    }

    // Update the text of the sell button to reflect the number of selected tickets
    private void updateSellButtonText() {
        if (tickets == 0) {
            sellTicketButton.setDisable(true);  // Disable when no tickets are selected
            sellTicketButton.setText("Sell tickets");
        } else {
            sellTicketButton.setDisable(false); // Re-enable the button when at least one ticket is selected
            sellTicketButton.setText("Sell " + tickets + " tickets");
        }
    }

    // Sell the selected tickets and update the showing data
    public void onSellTicketButton(ActionEvent actionEvent) {
        if (customerNameTextField.getText().isEmpty()) {
            selectedSeatLabel.setTextFill(Color.RED);
            selectedSeatLabel.setText("Please enter a customer name.");
            return;
        } else{
            selectedSeatLabel.setText("");
        }
        processTicketSale();

        if (showingsTableView != null) {
            ObservableList<Showing> updatedShowings = db.getAvailableShowings();
            showingsTableView.setItems(updatedShowings);
            showingsTableView.refresh();
        }
        resetAfterSale(actionEvent);
    }

    // Process the ticket sale, updating the showing and marking seats as sold
    private void processTicketSale() {
        currentShowing.setTicketsSold(currentShowing.getTicketsSold() + tickets);
        markSeatsAsSold();
        db.saveShowingsToFile();
        saveSaleHistory();
    }

    // Mark the selected seats as sold in the seat availability map and UI
    private void markSeatsAsSold() {
        for (String seatKey : selectedSeats) {
            seatAvailability.put(seatKey, true);
            updateSeatButtonToSold(seatKey);
        }
    }

    // Update the seat button's style to reflect that it has been sold
    private void updateSeatButtonToSold(String seatKey) {
        for (Node node : ticketsGrid.getChildren()) {
            if (node instanceof ToggleButton seatButton) {
                String buttonKey = "Row " + GridPane.getRowIndex(seatButton) + " Seat " + GridPane.getColumnIndex(seatButton);
                if (buttonKey.equals(seatKey)) {
                    seatButton.setStyle("-fx-background-color: red;");
                    seatButton.setDisable(true);
                }
            }
        }
    }

    // Save the ticket sale to the sales history
    private void saveSaleHistory() {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        SalesHistory sale = new SalesHistory(dateTime, tickets, customerNameTextField.getText(), currentShowing);
        db.addSalesHistory(sale);
    }

    // Reset the UI after selling tickets and close the current stage
    private void resetAfterSale(ActionEvent actionEvent) {
        selectedSeats.clear();
        selectedSeatsList.getItems().clear();
        sellTicketButton.setText("Sell Tickets");
        sellTicketButton.setDisable(true);
        selectedSeatLabel.setText("");
        tickets = 0;
        SceneHelper.closeStage(actionEvent);
    }

    // Method to cancel the selling of tickets for current selected showing
    @FXML
    protected void onCancelButton(ActionEvent event) {
       SceneHelper.closeStage(event);
    }
}