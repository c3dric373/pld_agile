package model.project;

import model.genData.*;
import view.Observer;

import java.util.List;

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
     *
     * @param graph Graph
     */
    void loadMap(Graph graph);

    /**
     * Loads a Tour to the view.
     *
     * @param tour Tour
     */
    void loadTour(Tour tour);

    /**
     * Sends a modified Tour to the view.
     *
     * @param tour Tour
     */
    void modifyTour(Tour tour);

    /**
     * Returns the {@link ProjectData} object that is stored inside the wrapper.
     *
     * @return the {@link ProjectData} object that is stored inside the wrapper.
     */
    ProjectData getProject();

    /**
     * Return the nearest Point from the param.
     *
     * @param newActionPoint the ActionPoint from which to find the nearest
     *                       point.
     */
    void findNearestPoint(ActionPoint newActionPoint);

    /**
     * Add an Observer to the ProjectDataWrapper.
     *
     * @param observer Observer
     */
    void addObserver(Observer observer);

    /**
     * Select a DeliveryProcess in the Tour.
     *
     * @param deliveryProcess the delivery process to select
     */
    void selectDeliveryProcess(DeliveryProcess deliveryProcess);

    /**
     * Get the list of Journeys.
     *
     * @param journeyList Journey List
     */
    void getJourneyList(List<Journey> journeyList);

    /**
     * Send error Message to the view.
     *
     * @param error error message
     */
    void sendErrorMessage(ErrorMessage error);
}
