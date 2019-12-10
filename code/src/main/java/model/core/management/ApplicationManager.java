package model.core.management;

import model.data.ActionPoint;
import model.data.DeliveryProcess;
import model.data.Point;
import model.data.Tour;
import view.UserInterface;

import java.io.File;
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
    void loadMap(final File file);

    void setObserver(final UserInterface userInterface);

    /**
     * Loads a tour and creates all necessary components for the project.
     *
     * @param file The tour uploaded by the user.
     */
    void loadTour(final File file);

    void calculateTour();

    /**
     * Adds a new DeliveryProcess to a Tour
     * @param tour current tour
     * @param pickUpPoint pickUp Point of the new DeliveryProcess to add
     * @param deliveryPoint Delivery Point of the new DeliveryProcess to add
     */
    void addDeliveryProcess(final Tour tour,
                                   final ActionPoint pickUpPoint,
                                   final ActionPoint deliveryPoint);

    /**
     * Deletes a {@link  DeliveryProcess}to an already uploaded tour.
     *
     * @param deliveryProcess the {@link DeliveryProcess} to delete.
     */
    void deleteDeliveryProcess(final DeliveryProcess deliveryProcess);

    /**
     * Changes the delivery order of the actionPoints.
     *
     * @param actionPoints the whole list of actionPoints of the tour stored in
     *                     the {@link model.data.ProjectData}
     */
    void changeDeliveryOrder(final List<ActionPoint> actionPoints);

    /**
     * Changes the position of a point in a tour showed in the map.
     *
     * @param oldPoint the old position
     * @param newPoint the new position
     */
    void changePointPosition(final ActionPoint oldPoint, final Point newPoint);

    /**
     * Finds nearest point of a given point with lat and long.
     *
     * @param latitude  latitude of the point
     * @param longitude longitude of the point
     */
    void findNearestPoint(final double latitude, final double longitude);

}
