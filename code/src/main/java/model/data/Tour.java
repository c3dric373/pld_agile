package model.data;

import com.rits.cloning.Cloner;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.Validate;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * A tour consists of a list of DeliveryProcesses, a base point and a start
 * time. The list of deliveryProcesses are all
 * the deliveries that the cyclists should complete.
 */
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class Tour implements GenData {

    /**
     * List of all the action points delivered in this journey
     * IN ORDER OF DELIVERY.
     */
    private List<ActionPoint> actionPoints;

    /**
     * List of all Journeys.
     */
    private List<Journey> journeyList;

    /**
     * List of all the deliveries the cyclist has to do.
     */
    private List<DeliveryProcess> deliveryProcesses;

    /**
     * Start point of the delivery.
     */
    private Point base;

    /**
     * Start time of the delivery.
     */
    private Time startTime;

    /**
     * Time it takes to complete the tour.
     */
    private Time completeTime;

    /**
     * Total distance of the tour.
     */
    private int totalDistance;

    /**
     * Instantiates an empty Tour.
     */
    public Tour() {
        this.deliveryProcesses = new ArrayList<DeliveryProcess>();
        this.base = new Point(0, 1, 1);
        this.startTime = new Time(0);
    }

    /**
     * Instantiates a Tour.
     *
     * @param deliveryProcessesList list of deliveries
     * @param basePoint             start point
     * @param time                  start time
     */
    public Tour(final List<DeliveryProcess> deliveryProcessesList,
                final Point basePoint, final Time time) {
        Validate.notNull(deliveryProcessesList, "deliveryProcess"
                + " is null");
        Validate.notNull(basePoint, "base is null");
        Validate.notNull(time, "startTime is null");
        this.deliveryProcesses = deliveryProcessesList;
        this.base = basePoint;
        this.startTime = time;
    }

    @Override
    public void accept(final GenDataVisitor genDataVisitor) {
        genDataVisitor.visit(this);
    }

    /**
     * Deep copy of a {@link Tour}, need for undo process.
     *
     * @return the deep copy.
     */
    public Tour deepClone() {
        Cloner cloner = new Cloner();
        return cloner.deepClone(this);
    }

}

