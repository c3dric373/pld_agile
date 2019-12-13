package view;

import lombok.Setter;
import model.core.management.ApplicationManager;
import model.data.*;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.sql.Time;
import java.util.List;

public class UserInterface implements Observer {

    /**
     * ViewVisitor to handle changes from the model.
     */
    private ViewVisitor viewVisitor = new ViewVisitor();

    /**
     * Interface to the model.
     */
    @Setter
    private ApplicationManager controller;
    /**
     * The object to handle events fired by our java fx interface.
     */
    private DashBoardController dashBoardController;

    /**
     * Constructor.
     *
     * @param newController the controller of the application.
     */
    public UserInterface(final ApplicationManager newController) {
        this.controller = newController;
    }

    /**
     * Set's the {@link DashBoardController} for this interface, and the
     * visitor.
     *
     * @param newDashBoardController the given {@link DashBoardController}.
     */
    public void setDashBoardController(final DashBoardController
                                               newDashBoardController) {
        Validate.notNull(dashBoardController,
                "controller ist not null");
        this.dashBoardController = newDashBoardController;
        this.viewVisitor.addController(dashBoardController);
    }

    @Override
    public void update(final GenData genData) {
        genData.accept(viewVisitor);
    }

    /**
     * Invokes a Controller to load a map.
     *
     * @param selectedFile The selected file to load.
     */
    public void loadMap(final File selectedFile) {
        Validate.notNull(selectedFile, "Selected File Is Null");
        this.controller.loadMap(selectedFile);
    }

    /**
     * Invokes a Controller to load a delivery request.
     *
     * @param selectedFile The selected file to load.
     */
    public void loadDeliveryRequest(final File selectedFile) {
        Validate.notNull(selectedFile, "Selected File Is Null");
        this.controller.loadTour(selectedFile);
    }

    /**
     * Invokes a method on the controller to calculate an optimal Tour.
     */
    public void calculateTour() {
        this.controller.calculateTour();
    }

    /**
     * Invokes the model to find the closest point to a given latitude and
     * longitude  stored in the controller. The goal is to create an actionPoint
     * from this information. Therefore more information is transmitted to
     * the controller.
     *
     * @param latitude   the given latitude.
     * @param longitude  the given longitude.
     * @param actionType the type of Point it should be.
     * @param time       the time it will take to comple the specific action
     *                   at the
     *                   point.
     */
    void getNearestPoint(final double latitude, final

    double longitude,
                         final ActionType actionType, final Time time) {
        this.controller.findNearestPoint(latitude, longitude, actionType, time);
    }

    /**
     * Invokes method to find the {@link DeliveryProcess} that matches a
     * given {@link ActionPoint}.
     *
     * @param oldValue The given {@link ActionPoint}.
     * @param tour     the {@link Tour} in which to find the fitting
     *                 {@llink DeliveryProcess}
     */
    public void getDeliveryProcessFromActionPoint(final ActionPoint oldValue,
                                                  final Tour tour) {
        Validate.notNull(oldValue, "oldValue null");
        Validate.notNull(tour, "tour null");
        this.controller.getDeliveryProcess(tour.getDeliveryProcesses(),
                oldValue);
    }

    /**
     * Invokes the model to delete a specific deliveryProcess.
     *
     * @param deliveryProcessLoaded the {@link DeliveryProcess} to delete.
     */
    public void deleteDp(final DeliveryProcess deliveryProcessLoaded) {
        Validate.notNull(deliveryProcessLoaded, "deliveryProcess null");
        this.controller.deleteDeliveryProcess(deliveryProcessLoaded);
    }

    /**
     * Invokes the model to get a journeyList between the start and en point
     * of the delivery process.
     *
     * @param deliveryProcess the {@link DeliveryProcess} from which we want
     *                        to retrieve the Journey list of the stored
     *                        {@link Tour} that goes from his
     *                        pick up point to his deliveryPoint.
     */
    public void getJourneyList(final DeliveryProcess deliveryProcess) {
        Validate.notNull(deliveryProcess, "deliveryProcess null");
        this.controller.getJourneyList(deliveryProcess);
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
        this.controller.addDeliveryProcess(newPickUpActionPoint,
                newDeliveryActionPoint);
    }

    /**
     * Invokes the model foa a change of the delivery order.
     *
     * @param actionPoints the newly sorted {@link ActionPoint}s.
     */
    public void changeDeliveryOrder(final List<ActionPoint> actionPoints) {
        this.controller.changeDeliveryOrder(actionPoints);
    }

    /**
     * Invokes an undo on the model.
     */
    public void undo() {
        this.controller.undo();
    }
}
