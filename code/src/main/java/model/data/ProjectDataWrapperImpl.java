package model.data;

import model.core.service.TourService;
import view.Observer;

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
        System.out.println("Test appl");
        projectData.setGraph(graph);
        notify(graph);
    }

    @Override
    public void loadTour(final Tour tour) {
        projectData.setTour(tour);
        notify(tour);
        System.out.println("loqd tour pdz");
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
    public void addDeliveryProcess(final DeliveryProcess deliveryProcess) {
        Tour tour = projectData.getTour();
        tour.addDeliveryProcess(deliveryProcess);
        projectData.setTour(tour);
        notify(tour);
    }

    @Override
    public void deleteDeliveryProcess(final DeliveryProcess deliveryProcess) {
        Tour tour = projectData.getTour();
        Tour newTour = TourService.deleteDeliveryProcess(tour, deliveryProcess);
        projectData.setTour(newTour);
        notify(tour);
    }

    @Override
    public void findNearestPoint(final Point newPoint) {
        notify(newPoint);
    }

    public void addObserver(final Observer observer){
        this.observer = observer;
    }

    public void selectDeliveryProcess(final DeliveryProcess deliveryProcess) {
        projectData.setSelectedDeliveryProcess(deliveryProcess);
        notify(deliveryProcess);
    }


    /**
     * Notifies the view of changes.
     *
     * @param genData the changed data to be updated to the view.
     */
    private void notify(final GenData genData) {
        System.out.println("notify");
        observer.update(genData);

    }
}
