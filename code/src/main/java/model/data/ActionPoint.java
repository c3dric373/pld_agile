package model.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang.Validate;

import java.sql.Time;

@Getter
@EqualsAndHashCode
public class ActionPoint {

    /**
     * Time it takes to complete the action
     */
    private Time time;

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
    public ActionPoint(Time time, Point location, ActionType actionType){
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
