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

    /**
     * Selected JourneyList for ProjectDataWapper.
     */
    private ListJourneyFromDeliveryProcess journeyList;

    /**
     * Selected DeliveryProcess for ProjectDataWrapper.
     */
    private DeliveryProcess selectedDeliveryProcess;

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

    @Override
    public void setSelectedDeliveryProcess(final DeliveryProcess deliveryProcess) {
        selectedDeliveryProcess = deliveryProcess;
    }

    @Override
    public DeliveryProcess getSelectedDeliveryProcess() {
        return selectedDeliveryProcess;
    }

    @Override
    public void setJourneyListForDeliveryProcess(
            final ListJourneyFromDeliveryProcess listForDeliveryProcess) {
        journeyList = listForDeliveryProcess;
    }

    @Override
    public ListJourneyFromDeliveryProcess getSelectedJourneyList() {
        return journeyList;
    }

}
