package nl.inholland.nl.denisaminu720645endassignment;

import javafx.css.converter.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditShowingController extends ShowingsController{

    public void setShowing(Showing currentShowing) {
        this.currentShowing = currentShowing;
        populateFields();
    }

    @FXML
    protected void populateFields() {
        if (currentShowing != null) {
            titleField.setText(currentShowing.getTitle());

            // Ensure startDate and endDate are not null
            if (currentShowing.getStartDate() != null && currentShowing.getEndDate() != null) {
                // Check if the dates are in the correct format, Set the time fields
                startTimeField.setText(currentShowing.getStartDate().substring(11));  // Extract start time part
                endTimeField.setText(currentShowing.getEndDate().substring(11));      // Extract end time part

                // Set the date pickers
                startDatePicker.setValue(LocalDate.parse(currentShowing.getStartDate().substring(0, 10), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                endDatePicker.setValue(LocalDate.parse(currentShowing.getEndDate().substring(0, 10), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            } else {
                errorLabel.setText("Date information is missing for this showing.");
            }
        }
    }

    @FXML
    public void onSaveShowingButton(ActionEvent event) {
        if(areFieldsValid()) {
            currentShowing.setTitle(titleField.getText());
            currentShowing.setStartDate(startDatePicker.getValue().format(dateFormatter) + " " + startTimeField.getText());
            currentShowing.setEndDate(endDatePicker.getValue().format(dateFormatter) + " " + endTimeField.getText());

            db.updateShowing(currentShowing);
            db.saveShowingsToFile();

            SceneHelper.closeStage(event);
//        } else{
//            SceneHelper.showAlert("Invalid input", "Please check the fields.");
        }
    }

    @FXML
    protected void onCancelShowingButton(ActionEvent event) {
        SceneHelper.closeStage(event);
    }
}