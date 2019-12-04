package model.data;


import java.util.List;

/**
 * Defines what data has to be implemented for a project.
 */
public class ProjectDataImpl implements ProjectData {

    /**
     * Tour for ProjectDataWrapper.
     */
    private Tour tour;

    /**
     *
     */
    private List<Point> pointList;

    /**
     * Graph for ProjectDataWrapper.
     */
    private Graph graph;

    @Override
    public Tour getTour() {
        return tour;
    }

    public List<Point> getPointList() {
        return pointList;
    }

    @Override
    public void setTour(final Tour newTour) {
        tour = newTour;
    }

    @Override
    public void setGraph(final Graph newGraph) {
        graph = newGraph;
    }

    @Override
    public Graph getGraph() {
        return graph;
    }
}
