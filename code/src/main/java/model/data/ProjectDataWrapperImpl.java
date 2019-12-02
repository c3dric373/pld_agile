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
    public void modifyTour(final Tour tour) {

    }


    private void notify(final GenData genData){

        observer.update(genData);
    }
}
