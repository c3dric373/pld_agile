package view;

import model.data.GenData;

/**
 * Interface for the observer in the Observer-Pattern.
 */
public interface Observer {

    /**
     * Method to update changes.
     *
     * @param genData data to be transmitted
     */
    void update(GenData genData);
}
