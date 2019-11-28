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
     * @param startPoint Start point of the journey
     * @param arrivePoint Arrival point of the journey
     * @param points the List of points in the shortest path from the start point to the arrival point in the REVERSE order
     * @param minLength The shortest length from the start point to the arrival point
     */
    public Journey(final Point startPoint, final Point arrivePoint, final List<Point> points, final double minLength) {
        this.startPoint = startPoint;
        this.arrivePoint = arrivePoint;
        this.points = points;
        this.minLength = minLength;
    }

}
