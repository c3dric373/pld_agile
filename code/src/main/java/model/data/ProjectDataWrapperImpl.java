package model.data;

import view.Observer;

/**
 * Implementation of {@link ProjectDataWrapper}.
 */
public class ProjectDataWrapperImpl implements ProjectDataWrapper{

    /**
     * Observer to notify when changes occur.
     */
    Observer observer;

    /**
     * Project Data for Project.
     */
    ProjectData projectData;

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


    private void notify(final GenData genData){

        observer.update(genData);
    }
}
