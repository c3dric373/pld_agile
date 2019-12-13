package model.data;
import lombok.Getter;

/**
 *
 */
public enum ErrorMessage implements GenData {

    PATH_NULL("Path should not be null"),

    PATH_EMPTY("Path should not be empty"),

    XML_LOAD_ERROR("Error during the XML load"),

    POINT_DOESNT_EXIST("Point doesn't exist"),

    ACTION_TYPE_NULL("Action Type should not be null"),

    TIME_NULL("Time should not be null"),

    ACTION_POINT_NULL("Action Point should not be null"),

    DELIVERY_POINT_NULL("Delivery point should not be null"),

    PICKUP_POINT_NULL("Pickup point should not be null"),

    ACTION_POINT_LIST_NULL("Action Point list should not be null"),

    ACTION_POINT_LIST_NULL_ELEMENT("Action Point list's Elements should not be null"),

    ACTION_POINT_LIST_EMPTY("Action Point list should not be empty"),

    PICKUP_IS_DELIVERY("Pickup Point should not be Delivery Point"),

    POINT_NULL("Point should not be null"),

    POINT_NOT_IN_LIST("Point is not in the list"),

    POINT_LIST_NULL("List of points should not be null"),

    POINT_LIST_NULL_ELEMENT("List of points should not contain null Elements"),

    POINT_LIST_NEED_2_ELEMENT("List of points should contain at least 2 points"),

    NEGATIVE_LENGTH("Length should be positive"),

    LENGTH_ZERO("Length should not be equal to zero"),

    LATITUDE_IS_NULL("Latitude is null"),

    LATITUDE_TO_SMALL("Latitude is too small"),

    LATITUDE_TO_GREAT("Latitude is too great"),

    LONGITUDE_IS_NULL("Longitude is null"),

    LONGITUDE_TO_SMALL("Longitude is too small"),

    LONGITUDE_TO_GREAT("Longitude is too great"),

    NEGATIVE_ID("Id sould not be negative"),

    APPLICATION_NOT_OPENED("Application not opened"),

    MAP_NOT_LOADED("Map not loaded"),

    FILE_NULL("File should not be null"),

    TOUR_NOT_LOADED("Tour should be loaded"),

    TOUR_NOT_CALCULATED("Tour should be calculated"),

    TOUR_NULL("Tour should not be null"),

    DELIVERY_PROCESS_NULL("DeliveryProcess is null"),

    POINT_NOT_ON_MAP("Point does not belong to the map"),

    ACTION_POINT_NOT_IN_DELIVERY(" Action Point do not belong to any deliveryProcess"),

    NAME_NULL("Name is null"),

    GRAPH_NULL("Graph should not be null"),

    ID_NOT_IN_GRAPH("Id do not belong to the graph"),

    ID_NULL("Id should not be null"),

    JOURNEY_NULL("Journey should not be null"),

    JOURNEY_LIST_NULL("Journeys'list should not be null"),

    JOURNEY_LIST_EMPTY("Journeys'list should not be empty"),

    JOURNEY_LIST_NULL_ELEMENT("Journeys'list should not contain null elements"),

    POINT_NOT_ACTION_POINT("Point is not an Action Point"),

    LIST_NOT_SAME_SIZE("List should be of the same size"),

    NO_ENDPOINT("Point is not an Endpoint of the journey"),

    NO_STARTPOINT("Point is not a StartPoint of the journey"),

    IMPOSSIBLE_TO_REMOVE_DELIVERY("IT is not possible to delete the delivery " +
            "because the delivery Process does not exist"),

    ANOTHER_ACTION_IN_PROGRESS("There is another action in progress, the " +
            "changes can't be made yet."),

    FILE_CORRUPTED("The file you chose hasn't the right datas. Maybe you " +
            "should check the file"),

    IMPOSSIBLE_TOUR("Impossible to get from the start point to the arrival point"),

    DIJKSTRA_RES_NULL("Dijkstra result can't be null"),

    TSP_RES_NULL("Dijkstra result can't be null"),

    GRAPH_TOUR_INCOMPATIBLE("Graph and Tour are incompatible"),

    CANNOT_DELETE_BASE("Cannot delete Base Point");

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
