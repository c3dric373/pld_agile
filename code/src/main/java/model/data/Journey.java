package model.data;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;

import java.sql.Time;
import java.util.List;

/**
 * This class represent the shortest path from one point to another
 */
@Getter
public class Journey {

    /**
     * Start time of journey.
     */
    @Setter
    private Time startTime;
    /**
     * Start point of the journey.
     */
    private Point startPoint;
    /**
     * Arrival point of the journey.
     */
    private Point arrivePoint;
    /**
     * List of points in the shortest path from the start point to the arrival.
     * point in the REVERSE order
     */
    private List<Point> points;
    /**
     * The shortest length from the start point to the arrival point.
     */
    private double minLength;

    /**
     * Instantiates a Journey.
     *
     * @param newPoints    the List of points in the shortest path from the start
     *                  point to the arrival point in the REVERSE order.
     * @param newMinLength The shortest length from the start point to the arrival
     *                  point.
     */
    public Journey(final List<Point> newPoints, final double newMinLength) {
        Validate.notNull(newPoints, "point list of the journey can't "
                + "be null");
        if (points.size() < 2)
            throw new IllegalArgumentException("point list of the journey "
                    + "should have at least two points");
        Validate.noNullElements(points, "points of the journey can't "
                + "be null");
        Validate.isTrue(newMinLength > 0, "min length of the"
                + " journey should be positive");
        this.startPoint = points.get(points.size() - 1);
        this.arrivePoint = points.get(0);
        this.points = newPoints;
        this.minLength = newMinLength;
    }
}
