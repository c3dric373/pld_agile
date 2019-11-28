package model.data;

import lombok.Getter;
import org.apache.commons.lang.Validate;

import java.util.List;

/**
 * This class represent the shortest path from one point to another
 */
@Getter
public class Journey {
    /**
     * Start point of the journey
     */
    private Point startPoint;
    /**
     * Arrival point of the journey
     */
    private Point arrivePoint;
    /**
     * List of points in the shortest path from the start point to the arrival point in the REVERSE order
     */
    private List<Point> points;
    /**
     * The shortest length from the start point to the arrival point
     */
    private double minLength;

    /**
     * Instantiates a Journey
     *
     * @param points    the List of points in the shortest path from the start point to the arrival point in the REVERSE order
     * @param minLength The shortest length from the start point to the arrival point
     */
    public Journey(final List<Point> points, final double minLength) {
        Validate.notNull(points, "point list of the journey can't be null");
        if (points.size() < 2)
            throw new IllegalArgumentException("point list of the journey should have at least two points");
        Validate.noNullElements(points, "points of the journey can't be null");
        Validate.isTrue(minLength > 0, "min length of the journey should be positive");
        this.startPoint = points.get(points.size() - 1);
        this.arrivePoint = points.get(0);
        this.points = points;
        this.minLength = minLength;
    }

}
