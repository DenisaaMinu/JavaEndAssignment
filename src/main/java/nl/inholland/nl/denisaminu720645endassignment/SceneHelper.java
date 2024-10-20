package nl.inholland.nl.denisaminu720645endassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SceneHelper {

    // Method to switch to a new full window
    public static <T> void switchScene(ActionEvent event, String fxmlPath, String title, Class<T> controllerClass, Database db) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneHelper.class.getResource(fxmlPath));
            Parent root = loader.load();

            T controller = loader.getController();
            setDatabaseIfNeeded(controller, db);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(SceneHelper.class.getName()).log(Level.SEVERE, "Error loading the view: " + fxmlPath, e);
            //showAlert("Error", "Failed to load the requested screen.");
        }
    }

    // Method to handle database setup based on controller type
    private static void setDatabaseIfNeeded(Object controller, Database db) {
        if (controller instanceof ShowingsController) {
            ((ShowingsController) controller).setDatabase(db);
            ((ShowingsController) controller).initializeTable();
        } else if (controller instanceof TicketsController) {
            ((TicketsController) controller).setDatabase(db);
        } else if (controller instanceof SalesHistoryController) {
            ((SalesHistoryController) controller).setDatabase(db);
        }
    }

    // Method to open a modal dialog and refresh the parent controller's table after closing
    public static void openShowingDialog(String fxmlPath, String title, Showing showing, Database db, Object parentController) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneHelper.class.getResource(fxmlPath));
            Parent root = fxmlLoader.load();

            Object controller = fxmlLoader.getController();

            if (controller instanceof ShowingsController) {
                ((ShowingsController) controller).setDatabase(db);
                if (controller instanceof EditShowingController) {
                    ((EditShowingController) controller).setShowing(showing);
                }
            } else if (controller instanceof SellTicketsController) {
                ((SellTicketsController) controller).setDatabase(db);
                ((SellTicketsController) controller).getCurrentShowing(showing);
                if (parentController instanceof TicketsController) {
                    ((SellTicketsController) controller).setShowingsTableView(((TicketsController) parentController).tableView);
                } else if (parentController instanceof ShowingsController) {
                    ((SellTicketsController) controller).setShowingsTableView(((ShowingsController) parentController).tableView);
                }
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);  // Block interactions with other windows
            dialogStage.showAndWait();

            if (parentController instanceof ShowingsController) {
                ((ShowingsController) parentController).initializeTable();  // Refresh table if it's a ShowingsController
            }
        } catch (IOException e) {
            Logger.getLogger(SceneHelper.class.getName()).log(Level.SEVERE, "Error opening showing dialog", e);
        }
    }

    // Method to close the stage
    public static void closeStage(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
