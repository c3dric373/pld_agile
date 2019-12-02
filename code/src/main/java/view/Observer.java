package view;

import model.data.GenData;

/**
 * Interface for the observer in the Observer-Pattern.
 */
public interface Observer {

    /**
     * Method to update changes.
     */
    void update(final GenData genData);

}