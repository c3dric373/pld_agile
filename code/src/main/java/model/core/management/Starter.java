package model.core.management;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.commons.lang.Validate;
import view.DashBoardController;
import view.UserInterface;

import java.io.IOException;

/**
 * Class responsible for starting the application.
 */
public class Starter extends Application {

    /**
     * Stage needed to Start.
     */
    private Stage primaryStage;

    /**
     * BorderPane needed to Start.
     */
    private BorderPane rootLayout;

    /**
     * Entry point of the application. Starts Deli'Velov.
     *
     * @param args Command line arguments that should be empty.
     */
    public static void main(final String[] args) {
        Validate.notNull(args, "args null");
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Déli'Vélo");

        initRootLayout();
        showDashboard();
    }

    /**
     * Initialize main layout aka root layout.
     */
    private void initRootLayout() {
        try {
            // Load root Layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            System.out.println();
            loader.setLocation(
                    UserInterface.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize show person.
     */
    private void showDashboard() {
        try {
            // Load root Layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserInterface.class.getResource
                    ("DashBoard.fxml"));
            AnchorPane dashboardOverview = loader.load();

            // Set person overview into the center of root layout
            rootLayout.setCenter(dashboardOverview);

            // Give the controller access to the main app.
            DashBoardController controller = loader.getController();
            ApplicationManager model = new ApplicationManagerImpl();
            UserInterface view = new UserInterface(model);
            model.setObserver(view);
            controller.setMainApp(view);
            view.setDashBoardController(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
