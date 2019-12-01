package model.data;


/**
 * This class acts as a wrapper for the {@link ProjectData} object. It offers
 * convenient methods to add, modify and remove project to all
 * project types that are stored inside the ProjectData object. This interface
 * also implements the  interface for the View
 * to be able to observe it for changes. Most methods making changes to the
 * project notify the View about those changes.
 */
public interface ProjectDataWrapper {

    /**
     * Loads a map to the view.
     */
    void loadMap(final Graph graph);


}
