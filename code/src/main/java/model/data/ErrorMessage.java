package model.data;

import lombok.Getter;

/**
 *
 */
public enum ErrorMessage implements GenData {

    /**
     * Path is null.
     */
    PATH_NULL("Path should not be null"),

    /**
     * Path is empty.
     */
    PATH_EMPTY("Path should not be empty"),

    /**
     * error during the Xml load.
     */
    XML_LOAD_ERROR("Error during the XML load"),

    /**
     * Point doesn't exist.
     */
    POINT_DOESNT_EXIST("Point doesn't exist, you should load a bigger map"),

    /**
     * ActionType is null.
     */
    ACTION_TYPE_NULL("Action Type should not be null"),

    /**
     * Time is null.
     */
    TIME_NULL("Time should not be null"),

    /**
     * ActionPoint is null.
     */
    ACTION_POINT_NULL("Action Point should not be null"),

    /**
     * DeliveryPoint is null.
     */
    DELIVERY_POINT_NULL("Delivery point should not be null"),

    /**
     * Pickup^Point is null.
     */
    PICKUP_POINT_NULL("Pickup point should not be null"),

    /**
     * list of ActionPoint is null.
     */
    ACTION_POINT_LIST_NULL("Action Point list should not be null"),

    /**
     * list of ActionPoint contains null Elements.
     */
    ACTION_POINT_LIST_NULL_ELEMENT("Action Point list's Elements should not be null"),

    /**
     * list of ActionPoint is empty.
     */
    ACTION_POINT_LIST_EMPTY("Action Point list should not be empty"),

    /**
     * Point is null.
     */
    POINT_NULL("Point should not be null"),

    /**
     * List of Point is null.
     */
    POINT_LIST_NULL("List of points should not be null"),

    /**
     * List of Point contains null elements.
     */
    POINT_LIST_NULL_ELEMENT("List of points should not contain "
            + "null Elements"),

    /**
     * Application is not oppened.
     */
    APPLICATION_NOT_OPENED("Application not opened"),

    /**
     * Map is not loaded.
     */
    MAP_NOT_LOADED("Map not loaded"),

    /**
     * File is null.
     */
    FILE_NULL("File should not be null"),

    /**
     * Tour is not loaded.
     */
    TOUR_NOT_LOADED("Tour should be loaded"),

    /**
     * Tour is not calculated.
     */
    TOUR_NOT_CALCULATED("Tour should be calculated"),

    /**
     * Tour is null.
     */
    TOUR_NULL("Tour should not be null"),

    /**
     * DeliveryProcess is null.
     */
    DELIVERY_PROCESS_NULL("DeliveryProcess is null"),

    /**
     * Point doesn't belong to the map.
     */
    POINT_NOT_ON_MAP("Point does not belong to the map, you should load a " +
            "bigger map"),

    /**
     * Impossible to remove delivery.
     */
    IMPOSSIBLE_TO_REMOVE_DELIVERY("IT is not possible to delete "
            + "the delivery because the delivery Process does not exist"),

    /**
     * Another action is in progress.
     */
    ANOTHER_ACTION_IN_PROGRESS("There is another action in progress, the "
            + "changes can't be made yet."),

    /**
     * File corrupted.
     */
    FILE_CORRUPTED("The file you chose hasn't the right datas. Maybe you "
            + "should check the file"),

    /**
     * Cannot delete the Base.
     */
    CANNOT_DELETE_BASE("Cannot delete Base Point"),

    /**
     * Cannot reach Point.
     */
    CANT_REACH_POINT("Point is unreachable ");
    /**
     * The Error message that can be displayed to the user.
     */
    @Getter
    private final String message;

    /**
     * Constructor of ErrorMessage.
     *
     * @param msg String containing the message
     */
    ErrorMessage(final String msg) {
        message = msg;
    }

    @Override
    public void accept(final GenDataVisitor genDataVisitor) {
        genDataVisitor.visit(this);
    }
}
