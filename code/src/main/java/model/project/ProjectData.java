package model.project;

import model.genData.DeliveryProcess;
import model.genData.Graph;
import model.genData.ListJourneyFromDeliveryProcess;
import model.genData.Tour;

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
     * Set the list of journeys needed to make a delivery Process.
     *
     * @param listForDeliveryProcess list of journey for the delivery process
     */
    void setJourneyListForDeliveryProcess(ListJourneyFromDeliveryProcess
                                                  listForDeliveryProcess);
}
