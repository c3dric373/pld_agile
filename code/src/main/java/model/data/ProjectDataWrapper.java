package model.data;


/**
 * This class acts as a wrapper for the {@link ProjectData} object. It offers
 * convenient methods to add, modify and remove project to all
 * project types that are stored inside the ProjectData object. This interface
 * also implements the  interface for the View
 * to be able to observe it for changes. Most methods making changes to the
 * project notify the View about those changes.
 */
public interface ProjectDataWrapper {

    /**
     * Loads a map to the view.
     */
    void loadMap(final Graph graph);

    /**
     * Sends a modified Tour to the view.
     */
    void modifyTour(final Tour tour);

    /**
     * Returns the {@link ProjectData} object that is stored inside the wrapper.
     *
     * @return the {@link ProjectData} object that is stored inside the wrapper.
     */
    ProjectData getProject();

    /**
     * Add a DeliveryProcess to the current Tour.
     * @param deliveryProcess
     */
    void addDeliveryProcess(final DeliveryProcess deliveryProcess);

    /**
     * Delete a DeliveryProcess to the current Tour.
     * @param deliveryProcess
     */
    void deleteDeliveryProcess(final DeliveryProcess deliveryProcess);

    /**
     * Return the nearestPoint from the param.
     * @param newPoint
     */
    void findNearestPoint(final Point newPoint);



}
