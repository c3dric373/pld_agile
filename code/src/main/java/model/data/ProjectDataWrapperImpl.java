package model.data;

import view.Observer;

/**
 * Implementation of {@link ProjectDataWrapper}.
 */
public class ProjectDataWrapperImpl implements ProjectDataWrapper{

    /**
     * Observer to notify when changes occur.
     */
    private Observer observer;

    /**
     * Project Data for Project.
     */
    private ProjectData projectData;

    public ProjectDataWrapperImpl(final Observer newObserver){
        this.observer = newObserver;
        projectData = new ProjectDataImpl();
    }
    @Override
    public void loadMap(final Graph graph) {
        projectData.setGraph(graph);
        notify(graph);
    }

    @Override
    public void loadTour(Tour tour) {
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

    public void addDeliveryProcess(final DeliveryProcess deliveryProcess){
        Tour tour = projectData.getTour();
        tour.addDeliveryProcess(deliveryProcess);
        projectData.setTour(tour);
        notify(tour);
    }

    public void deleteDeliveryProcess(final DeliveryProcess deliveryProcess){
        Tour tour = projectData.getTour();
        tour.deleteDeliveryProcess(deliveryProcess);
        projectData.setTour(tour);
        notify(tour);
    }

    public void findNearestPoint(final Point newPoint){
        notify(newPoint);
    }


    private void notify(final GenData genData){

        observer.update(genData);
    }
}
