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
    TOUR_CALCULATED,

    /**
     * 1st state of the adding of a DeliveryProcess.
     * No points are selected, the user has to select the 1st point of the
     * DeliveryProcess.
     */
    ADD_DELIVERY_PROCESS,

    /**
     * 2nd state of the adding of a DeliveryProcess.
     * 1st point (PickUp) is selected and saved, the user
     * has to select the 2nd Point
     */
     ADD_DELIVERY_PROCESS_1stPoint,

    /**
     * 3rd and last state of the adding of a DeliveryProcess, the 2 points are
     * selected, the user just has to save in order to add the DeliveryProcess.
     */
    ADD_DELIVERY_PROCESS_2ndPoint,

    /**
     * State of the deleting of a DeliveryProcess (after clicking on the
     * Delete button).
     * The user has to select an ActionPoint in the list or on the map and
     * the corresponding ActionPoint will display. The user has to save to
     * delete the DeliveryProcess.
     */
     DELETE_DELIVERY_PROCESS,

    /**
     * 1st state of the modification of an ActionPoint of a DeliveryProcess.
     * Begins after the user click on the modify button.
     * The user has to click on an ActionPoint, then the user validates this
     * point by clicking on save. He clicks again on the map and we display
     * the nearest point on the map.
     */
    MODIFY_DELIVERY_PROCESS_POINT,

    /**
     * 2nd state of the modification of an ActionPoint of a DeliveryProcess.
     * The nearest point is showed on the map and the user has to click on
     * the save button to do the modification.
     */
    MODIFY_DELIVERY_PROCESS_POINT_END,

    /**
     * State of the modification of Delivery order.
     * Begins after the user click on the change order button.
     * The user then have to modify the order of the actionPoints and has to
     * save when he is done.
     */
     CHANGE_DELIVERY_ORDER;

    //TODO : Modify time on DeliveryProcess ?
}
