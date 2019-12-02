package model.data;


/**
 * Defines what data has to be implemented for a project.
 *
 * @author Klara
 */
public class ProjectDataImpl implements ProjectData{

    /**
     * Tour for ProjectDataWrapper
     */
    private Tour tour;
    private Graph graph;

    @Override
    public Tour getTour() {
        return null;
    }

    @Override
    public void setTour(final Tour newTour) {
        tour = newTour;
    }

    @Override
    public void setGraph( final Graph newGraph) {
        graph = newGraph;
    }

    @Override
    public void getGraph() {

    }
}
