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

    public ProjectDataWrapperImpl(final Observer newObserver){
        this.observer = newObserver;
    }
    @Override
    public void loadMap(final Graph graph) {

    }

    @Override
    public void modifyTour(Tour tour) {

    }


    private void notify(final GenData genData){

        observer.update(genData);
    }
}
