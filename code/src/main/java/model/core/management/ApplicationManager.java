package model.core.management;

import model.data.DeliveryProcess;

import java.io.File;

/**
 * Represents the application from the perspective of the view. It provides a
 * method for every function of the program.
 */
public interface ApplicationManager {

    /**
     * Loads a map and creates all necessary components for the project.
     *
     * @param file The map uploaded by the user.
     */
    void loadMap(final File file);

    void setObserver();

    /**
     * Loads a tour and creates all necessary components for the project.
     *
     * @param file The tour uploaded by the user.
     */
    void loadTour(final File file);

    void calculateTour();

    /**
     * Adds a {@link  DeliveryProcess}to an already uploaded tour.
     *
     * @param deliveryProcess the {@link DeliveryProcess} to add.
     */
    void addDeliveryProcess(final DeliveryProcess deliveryProcess);

    /**
     * Deletes a {@link  DeliveryProcess}to an already uploaded tour.
     *
     * @param deliveryProcess the {@link DeliveryProcess} to delete.
     */
    void deleteDeliveryProcess(final DeliveryProcess deliveryProcess);

    void modifyTour();


}
