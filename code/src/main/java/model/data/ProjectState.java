package model.data;

public enum ProjectState {

    /**
     * Application is opened nothing loaded.
     */
    INITIALIZED,

    /**
     * The map was loaded.
     */
    MAP_LOADED,

    /**
     * Tour was loaded.
     */
    TOUR_LOADED,

    /**
     * Tour was calculated.
     */
    TOUR_CALCULATED;


}
