package nl.inholland.nl.denisaminu720645endassignment;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddShowingController extends ShowingsController{

    // Method to save the new added showing
    @FXML
    public void onSaveShowingButton(ActionEvent event) {
        if(areFieldsValid()){
            String title = titleField.getText();
            Showing newShowing = createShowing(title);

            db.addShowing(newShowing);
            db.saveShowingsToFile();
            SceneHelper.closeStage(event);
        }
    }

    // Method to create a new showing
    private Showing createShowing(String title) throws DateTimeParseException {
        LocalDateTime startDateTime = LocalDateTime.of(startDatePicker.getValue(), LocalTime.parse(startTimeField.getText(), timeFormatter));
        LocalDateTime endDateTime = LocalDateTime.of(endDatePicker.getValue(), LocalTime.parse(endTimeField.getText(), timeFormatter));

        // Check that the start date/time is before the end date/time
        if (endDateTime.isBefore(startDateTime)) {
            throw new DateTimeParseException("End date/time is before start date/time", endDateTime.toString(), 0);
        }

        // Format dates and times as "dd-MM-yyyy HH:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String startDateFormatted = startDateTime.format(formatter);
        String endDateFormatted = endDateTime.format(formatter);

        // Return the new Showing object with formatted date/time strings
        return new Showing(title, startDateFormatted, endDateFormatted);
    }

    // Method to cancel adding the new showing
    @FXML
    protected void onCancelShowingButton(ActionEvent event) {
        SceneHelper.closeStage(event);
    }
}