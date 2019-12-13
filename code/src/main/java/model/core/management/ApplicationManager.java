package model.core.management;

import model.data.*;
import view.UserInterface;

import java.io.File;
import java.sql.Time;
import java.util.List;

/**
 * Represents the application from the perspective of the view. It provides a
 * method for every function of the program.
 */
public interface ApplicationManager {

    /**
     * Loads a map and creates all necessary components for the project.
     *
     * @param file The map uploaded by the user.
     */
    void loadMap(File file);

    /**
     * Set the Observer.
     *
     * @param userInterface UserInterface
     */
    void setObserver(UserInterface userInterface);

    /**
     * Loads a tour and creates all necessary components for the project.
     *
     * @param file The tour uploaded by the user.
     */
    void loadTour(File file);

    /**
     * Calculate the Tour.
     */
    void calculateTour();

    /**
     * Adds a new DeliveryProcess to a Tour.
     *
     * @param tour current tour
     * @param pickUpPoint pickUp Point of the new DeliveryProcess to add
     * @param deliveryPoint Delivery Point of the new DeliveryProcess to add
     */
    void addDeliveryProcess(Tour tour,
                                   ActionPoint pickUpPoint,
                                   ActionPoint deliveryPoint);

    /**
     * Deletes a {@link  DeliveryProcess}to an already uploaded tour.
     *
     * @param deliveryProcess the {@link DeliveryProcess} to delete.
     */
    void deleteDeliveryProcess(DeliveryProcess deliveryProcess);

    /**
     * Changes the delivery order of the actionPoints.
     *
     * @param actionPoints the whole list of actionPoints of the tour stored in
     *                     the {@link model.data.ProjectData}
     */
    void changeDeliveryOrder(List<ActionPoint> actionPoints);

    /**
     * Changes the position of a point in a tour showed in the map.
     *
     * @param oldPoint the old position
     * @param newPoint the new position
     */
    void changePointPosition(ActionPoint oldPoint, Point newPoint);

    /**
     * Finds nearest point of a given point with lat and long.
     *
     * @param latitude  latitude of the point
     * @param longitude longitude of the point
     * @param actionType Action Type of the Point
     * @param actionTime Action Time of the Point
     */
    void findNearestPoint(double latitude, double longitude,
                          ActionType actionType, Time actionTime);

    /**
     * Get the delivery process correspondent to the given action point.
     *
     * @param actionPoint a pick up point or a delivery point.
     * @param deliveryProcesses list of Delivery Process.
     */
    void getDeliveryProcess(List<DeliveryProcess> deliveryProcesses,
                            ActionPoint actionPoint);

    /**
     * Get the journeyList of a deliveryProcess.
     *
     * @param journeyList List of journey
     * @param deliveryProcess Delivery Process
     */
    void getJourneyList(List<Journey> journeyList,
                        DeliveryProcess deliveryProcess);
}
