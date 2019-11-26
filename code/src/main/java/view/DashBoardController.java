package view;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;

public class DashBoardController {


    // Reference to the main application
    private MainApp mainApp;


    public DashBoardController() {

    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        //firstNameColumn.setCellValueFactory(
        //        cellData -> cellData.getValue().firstNameProperty());
        //lastNameColumn.setCellValueFactory(
        //        cellData -> cellData.getValue().lastNameProperty());

        // Clear person details.
        //showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
        //personTable.getSelectionModel().selectedItemProperty().addListener(
        //        (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }


    public void handleLoadMap() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Map XML");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            if(selectedFile.getName().contains("xml")) {
                System.out.println("File selected: " + selectedFile.getName());
            }else {
                    System.out.println("Error Loading not xml File");
                }
        }
        else {
            System.out.println("File selection cancelled.");
        }



    }

    public void handleLoadTour() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Tour XML");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            if(selectedFile.getName().contains("xml")) {
                System.out.println("File selected: " + selectedFile.getName());
            }else {
                System.out.println("Error Loading not xml File");
            }
        }
        else {
            System.out.println("File selection cancelled.");
        }
    }
    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        // tourTable.setItems(mainApp.getTour());
    }
}
