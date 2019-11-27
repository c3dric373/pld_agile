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
public class Point {

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
    private final int MIN_LATITUDE = -90;

    /**
     * Greatest possible latitude.
     */
    private final int MAX_LATITUDE = -90;

    /**
     * Smallest possible latitude.
     */
    private final int MIN_LONGITUDE = -180;

    /**
     * Greatest possible latitude.
     */
    private final int MAX_LONGITUDE = 180;

    /**
     * Smallest possible id.
     */
    private final int MIN_ID = 0;


    /**
     * Instantiates a Point.
     *
     * @param id        id of point
     * @param latitude  latitude of point
     * @param longitude longitude of point
     */
    public Point(final long id, final double latitude, final double longitude) {
        Validate.notNull(id, "id is null");
        if (latitude < MIN_LATITUDE) {
            throw new IllegalArgumentException("latitude is too small");
        }
        if (latitude > MAX_LATITUDE) {
            throw new IllegalArgumentException("latitude is too great");
        }
        if (longitude < MIN_LONGITUDE) {
            throw new IllegalArgumentException("longitude is too small");
        }
        if (longitude > MAX_LONGITUDE) {
            throw new IllegalArgumentException("longitude is too great");
        }
        if (id < MIN_ID) {
            throw new IllegalArgumentException("id is negative");
        }
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
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
     * Get the distance from this point to another if it's reachable via one segments
     *
     * @param id the id of the other point
     * @return the distance from this point to another
     */
    double getLengthTo(final long id) {
        if (this.id == id) {
            return 0;
        }
        for (final Segment s : segments) {
            if ((s.either() == this.id && s.other(this.id) == id) || s.either()
                    == id && s.other(id) == this.id) {
                return s.getLength();
            }
        }
        throw new IllegalArgumentException("point not reachable via one segment"
        );
    }

}
