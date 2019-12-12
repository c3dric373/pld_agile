package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Setter;
import model.core.management.ApplicationManager;
import model.data.*;
import org.apache.commons.lang.Validate;

import javax.swing.*;
import java.io.File;
import java.sql.Time;
import java.util.List;

public class UserInterface implements Observer {

    /**
     * ViewVisitor to handle changes from the model
     */
    private ViewVisitor viewVisitor = new ViewVisitor();

    /**
     * Interface to the model.
     */
    @Setter
    private ApplicationManager model;

    /**
     * The data as an observable list of Persons.
     */
    // TODO Modifier ca en autre chose que string genre en liste de livraison
    private ObservableList<String> tourData = FXCollections.observableArrayList();
    private DashBoardController controller;

    /**
     * Constructor
     *
     * @param model
     */
    public UserInterface(final ApplicationManager model) {
        // Add some sample data List
        // tourData.addAll();
        this.model = model;
    }

    /**
     * Returns the data as an observable list of Delivery from Tour.
     *
     * @return
     */
    public ObservableList<String> getTour() {
        return tourData;
    }

    public void setController(DashBoardController controller) {
        Validate.notNull(controller, "controller ist not null");
        this.controller = controller;
        this.viewVisitor.addController(controller);
    }

    @Override
    public void update(final GenData genData) {
        System.out.println("Visitor User interface");
        genData.accept(viewVisitor);
    }

    public void loadMap(final File selectedFile) {
        Validate.notNull(selectedFile, "Selected File Is Null");
        if (this.model != null) {
            this.model.loadMap(selectedFile);
        }
    }

    public void loadDeliveryRequest(final File selectedFile) {
        Validate.notNull(selectedFile, "Selected File Is Null");
        if (this.model != null) {
            this.model.loadTour(selectedFile);
        }
    }

    public void calculateTour() {
        this.model.calculateTour();
    }


    public void addDeliveryProcess(DeliveryProcess deliveryProcess) {
        //this.model.addDeliveryProcess(deliveryProcess);
    }

    public void getNearPoint(double latitude, double longitude, ActionType actionType, Time time) {
        this.model.findNearestPoint(latitude, longitude, actionType, time);
    }

    public void showDeliveryProcess(ActionPoint oldValue, Tour tour) {
        this.model.getDeliveryProcess(tour.getDeliveryProcesses(),oldValue);
    }

    public void deleteDp(final DeliveryProcess deliveryProcessLoaded) {
        System.out.println("Send message to delete dp");
        this.model.deleteDeliveryProcess(deliveryProcessLoaded);
    }

    public void getJourneyList(List<Journey> journeyList, DeliveryProcess deliveryProcess) {
        this.model.getJourneyList(journeyList, deliveryProcess);
    }
}
