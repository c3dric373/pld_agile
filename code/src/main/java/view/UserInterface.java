package view;

import controlller.DashBoardController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.core.management.ApplicationManager;

import java.io.IOException;

public class UserInterface extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

     /**
     * Interface to the model.
     */
    private ApplicationManager model;

    /**
     * The data as an observable list of Persons.
     */
    // TODO Modifier ca en autre chose que string genre en liste de livraison
    private ObservableList<String> tourData = FXCollections.observableArrayList();

    /**
     * Constructor
     * @param model
     */
    public UserInterface(final ApplicationManager model) {
        // Add some sample data List
        // tourData.addAll();
        this.model = model;
    }

    /**
     * Returns the data as an observable list of Delivery from Tour.
     * @return
     */
    public ObservableList<String> getTour() {
        return tourData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Déli'Vélo");

        initRootLayout();
        showDashboard();
    }

    // Initialize main layout aka root layout
    private void initRootLayout() {
        try {
            // Load root Layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            System.out.println();
            loader.setLocation(UserInterface.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Initialize show person
    private void showDashboard() {
        try {
            // Load root Layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserInterface.class.getResource("DashBoard.fxml"));
            AnchorPane dashboardOverview =  loader.load();

            // Set person overview into the center of root layout
            rootLayout.setCenter(dashboardOverview);

            // Give the controller access to the main app.
            DashBoardController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
