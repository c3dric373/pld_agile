package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Setter;
import model.core.management.ApplicationManager;
import model.data.*;
import org.apache.commons.lang.Validate;

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
    private ObservableList<String> tourData =
            FXCollections.observableArrayList();
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

    public void setController(final DashBoardController controller) {
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

    public void getNearPoint(double latitude, double longitude,
                             ActionType actionType, Time time) {
        this.model.findNearestPoint(latitude, longitude, actionType, time);
    }

    public void showDeliveryProcess(final ActionPoint oldValue,
                                    final Tour tour) {
        Validate.notNull(oldValue, "oldValue null");
        Validate.notNull(tour, "tour null");
        this.model.getDeliveryProcess(tour.getDeliveryProcesses(), oldValue);
    }

    /**
     * Invokes the model to delete a specific deliveryProcess.
     *
     * @param deliveryProcessLoaded the {@link DeliveryProcess} to delete.
     */
    public void deleteDp(final DeliveryProcess deliveryProcessLoaded) {
        Validate.notNull(deliveryProcessLoaded, "deliveryProcess null");
        this.model.deleteDeliveryProcess(deliveryProcessLoaded);
    }

    /**
     * Invokes the model to get a journeyList between the start and en point
     * of the delivery process.
     *
     * @param deliveryProcess
     */
    public void getJourneyList(final DeliveryProcess deliveryProcess) {
        this.model.getJourneyList(deliveryProcess);
    }

    /**
     * Invokes the model to add a DeliveryProcess to the {@Tour} object stored
     * in the model.
     *
     * @param newPickUpActionPoint   new Pick up point to add.
     * @param newDeliveryActionPoint new delivery point to add.
     */
    public void addDeliveryProcess(final ActionPoint newPickUpActionPoint,
                                   final ActionPoint newDeliveryActionPoint) {
        this.model.addDeliveryProcess(newPickUpActionPoint,
                newDeliveryActionPoint);
    }

    /**
     * Invokes the model foa a change of the delivery order.
     *
     * @param actionPoints the newly sorted {@link ActionPoint}s.
     */
    public void changeDeliveryOrder(final List<ActionPoint> actionPoints) {
        this.model.changeDeliveryOrder(actionPoints);
    }

    /**
     * Invokes an undo on the model.
     */
    public void undo() {
        this.model.undo();
    }
}
