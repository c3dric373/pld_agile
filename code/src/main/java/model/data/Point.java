package model.data;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a specific point on the loaded map. Each intersection
 * has an id. This id is stored in this class.
 */
@Getter
@EqualsAndHashCode
public class Point implements GenData {

    /**
     * Location of the point on the map.
     */
    private long id;

    /**
     * Latitude of Point.
     */
    private double latitude;

    /**
     * Longitude of Point.
     */
    private double longitude;

    /**
     * List of Adjacent Segments to point.
     */
    private List<Segment> segments;

    /**
     * Smallest possible latitude.
     */
    static final int MIN_LATITUDE = -90;

    /**
     * Greatest possible latitude.
     */
    static final int MAX_LATITUDE = 90;

    /**
     * Smallest possible latitude.
     */
    static final int MIN_LONGITUDE = -180;

    /**
     * Greatest possible latitude.
     */
    static final int MAX_LONGITUDE = 180;

    /**
     * Smallest possible id.
     */
    static final int MIN_ID = 0;


    /**
     * Instantiates a Point.
     *
     * @param pointId        id of point
     * @param pointLatitude  latitude of point
     * @param pointLongitude longitude of point
     */
    public Point(final long pointId, final double pointLatitude,
                 final double pointLongitude) {
        Validate.notNull(pointId, "id is null");
        if (pointLatitude < MIN_LATITUDE) {
            throw new IllegalArgumentException("latitude is too small");
        }
        if (pointLatitude > MAX_LATITUDE) {
            throw new IllegalArgumentException("latitude is too great");
        }
        if (pointLongitude < MIN_LONGITUDE) {
            throw new IllegalArgumentException("longitude is too small");
        }
        if (pointLongitude > MAX_LONGITUDE) {
            throw new IllegalArgumentException("longitude is too great");
        }
        if (pointId < MIN_ID) {
            throw new IllegalArgumentException("id is negative");
        }
        this.id = pointId;
        this.latitude = pointLatitude;
        this.longitude = pointLongitude;
        this.segments = new ArrayList<>();
    }

    /**
     * Adds a segment to the list of segments.
     *
     * @param segment The {@link Segment} to be add.
     */
    public void addNeighbour(final Segment segment) {
        segments.add(segment);
    }


    /**
     * Get the distance from this point to another
     * if it's reachable via one segments.
     *
     * @param pointId the id of the other point
     * @return the distance from this point to another
     */
    public double getLengthTo(final long id) {
        if (this.id == id) return 0;
        for (final Segment s : segments) {
            if (s.getId_end() == id) return s.getLength();
        }
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public void accept(final GenDataVisitor genDataVisitor) {
        genDataVisitor.visit(this);
    }
}
