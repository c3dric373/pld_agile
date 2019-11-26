package model.data;

import lombok.Getter;
import org.apache.commons.lang.Validate;

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
        Validate.notNull(location, "location is null");
        Validate.notNull(actionType, "actionType is null");
        if (time<0){
            throw new IllegalArgumentException("time is negative");
        }
        if (time>=2400){
            throw new IllegalArgumentException("time is too great");
        }
        this.time = time;
        this.location = location;
        this.actionType=actionType;
    }
}
