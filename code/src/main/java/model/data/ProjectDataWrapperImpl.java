package model.data;

import org.apache.commons.lang.Validate;
import view.Observer;

import java.util.List;

/**
 * Implementation of {@link ProjectDataWrapper}.
 */
public class ProjectDataWrapperImpl implements ProjectDataWrapper {

    /**
     * Observer to notify when changes occur.
     */
    private Observer observer;

    /**
     * Project Data for Project.
     */
    private ProjectData projectData;

    /**
     * Instantiates a new Project Data Wrapper.
     */
    public ProjectDataWrapperImpl() {
        projectData = new ProjectDataImpl();
    }

    @Override
    public void loadMap(final Graph graph) {
        projectData.setGraph(graph);
        notify(graph);
    }

    @Override
    public void loadTour(final Tour tour) {
        projectData.setTour(tour);
        notify(tour);
    }

    @Override
    public void modifyTour(final Tour tour) {
        projectData.setTour(tour);
        notify(tour);
    }

    @Override
    public ProjectData getProject() {
        return projectData;
    }

    @Override
    public void findNearestPoint(final ActionPoint newActionPoint) {
        notify(newActionPoint);
    }

    @Override
    public void addObserver(final Observer newObserver) {
        this.observer = newObserver;
    }

    @Override
    public void selectDeliveryProcess(final DeliveryProcess deliveryProcess) {
        projectData.setSelectedDeliveryProcess(deliveryProcess);
        notify(deliveryProcess);
    }

    @Override
    public void getJourneyList(final List<Journey> journeyList) {
        ListJourneyFromDeliveryProcess listJourneyFromDeliveryProcess =
                new ListJourneyFromDeliveryProcess();
        listJourneyFromDeliveryProcess.setJourneyList(journeyList);
        projectData.setJourneyListForDeliveryProcess(
                listJourneyFromDeliveryProcess);
        notify(listJourneyFromDeliveryProcess);
    }

    @Override
    public void sendErrorMessage(final ErrorMessage error) {
        Validate.notNull(error, "error null");
        notify(error);
    }

    /**
     * Notifies the view of changes.
     *
     * @param genData the changed data to be updated to the view.
     */
    private void notify(final GenData genData) {
        Validate.notNull(genData);
        observer.update(genData);
    }
}
