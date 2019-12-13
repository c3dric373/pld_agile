package model.data;

import java.util.List;

/**
 * This class serves as a container for all data a project contains.
 * It offers getter and setter methods for all stored data classes.
 */
public interface ProjectData {
    /**
     * Returns the saved tour of the project.
     *
     * @return tour of the project
     */
    Tour getTour();

    /**
     * The list representing the map.
     *
     * @return the map
     */
    List<Point> getPointList();

    /**
     * Sets the tour of the project.
     *
     * @param newTour the tour to set.
     */
    void setTour(Tour newTour);

    /**
     * Sets the graph of the project.
     *
     * @param graph the graph to be set.
     */
    void setGraph(Graph graph);

    /**
     * Returns the graph of the project.
     *
     * @return The graph to be returned.
     */
    Graph getGraph();

    /**
     * Sets the selected delivery process of the project.
     *
     * @param deliveryProcess The delivery process to set.
     */
    void setSelectedDeliveryProcess(DeliveryProcess deliveryProcess);

    /**
     * Returns the selected delivery process of the project.
     *
     * @return The delivery process to be returned.
     */
    DeliveryProcess getSelectedDeliveryProcess();

    /**
     * Set the list of journeys needed to make a delivery Process.
     *
     * @param listForDeliveryProcess list of journey for the delivery process
     */
    void setJourneyListForDeliveryProcess
    (ListJourneyFromDeliveryProcess listForDeliveryProcess);

    /**
     * Get the list of journeys needed to make a delivery process.
     *
     * @return ListJourneyFromDeliveryProcess
     */
    ListJourneyFromDeliveryProcess getSelectedJourneyList();
}
