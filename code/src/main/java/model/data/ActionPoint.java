package model.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;

import java.sql.Time;

@Getter
@EqualsAndHashCode
public class ActionPoint implements GenData{

    private final String EMPTY_STRING = "";

    @Setter
    private int id;

    /**
     * Time it takes to complete the action.
     */
    @Setter
    private Time time;

    /**
     * Time at which the cyclist arrives at this point should only be set when
     * a tour was calculated.
     */
    @Setter
    private String passageTime = EMPTY_STRING;

    /**
     * Location of the action.
     */
    private Point location;

    /**
     * Type of the action.
     */
    private ActionType actionType;

    /**
     * Instatiates an ActionPoint.
     *
     * @param actionTime     time of action.
     * @param actionLocation location of action.
     * @param type           type of action.
     */
    public ActionPoint(final Time actionTime, final Point actionLocation,
                       final ActionType type) {
        Validate.notNull(actionLocation, "location is null");
        Validate.notNull(type, "actionType is null");
        Validate.notNull(actionTime, "time is null");

        this.time = actionTime;
        this.location = actionLocation;
        this.actionType = type;
    }

    @Override
    public void accept(final GenDataVisitor genDataVisitor) {
        genDataVisitor.visit(this);
    }

}
