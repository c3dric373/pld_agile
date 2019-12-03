package model.data;

import java.util.List;

/**
 * This class serves as a container for all data a project contains.
 * It offers getter and setter methods for all stored data classes.
 */
public interface ProjectData {
    /**
     * Returns the saved tour of the project
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
    void setTour(final Tour newTour);

    /**
     * Sets the graph of the project.
     *
     * @param graph the graph to be set.
     */
    void setGraph(final Graph graph);

    /**
     * Returns the graph of the project.
     *
     * @return The graph to be returned.
     */
    Graph getGraph();


}
