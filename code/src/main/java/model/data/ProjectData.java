package model.data;

import java.util.List;

/**
 * This class serves as a container for all data a project contains.
 * It offers getter and setter methods for all stored data classes.
 */
public interface ProjectData {

    Tour getTour();

    List<Point> getPointList();

    void setTour(final Tour newTour);

    void setGraph( final Graph graph);

    Graph getGraph();

    

}
