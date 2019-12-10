package model.data;


import view.Observer;

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
     * Loads a Tour to the view
     */
    void loadTour(final Tour tour);

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
     * Delete a DeliveryProcess to the current Tour.
     *
     * @param deliveryProcess the delivery process to delete
     */
    void deleteDeliveryProcess(final DeliveryProcess deliveryProcess);

    /**
     * Return the nearestPoint from the param.
     *
     * @param newPoint the point from which to find the nearest point.
     */
    void findNearestPoint(final Point newPoint);

    void addObserver(final Observer observer);


}
