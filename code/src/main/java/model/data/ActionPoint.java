package model.data;

import lombok.Getter;

@Getter
public class ActionPoint {

    /**
     * Time it takes to complete the action
     */
    private int time;

    /**
     * Location of the action
     */
    private Point location;

    /**
     * Type of the action
     */
    private ActionType actionType;

    /**
     * Instatiates an ActionPoint
     * @param time time of action
     * @param location location of action
     * @param actionType type of action
     */
    ActionPoint(int time, Point location, ActionType actionType){
        this.time = time;
        this.location = location;
        this.actionType=actionType;
    }
}
