package model.data;


import lombok.Getter;

import java.util.List;

/**
 * This class represents a specific point on the loaded map. Each intersection has an id. This id is stored in this class.
 */
@Getter
public class Point {

    /**
     * Location of the point on the map
     */
    private int id;

    /**
     * Latitude of Point
     */
    private double latitude;

    /**
     * Longitude of Point
     */

    private double longitude;

    /**
     * List of Adjacent Segments to point
     */
    private List<Segment> segments;

    /**
     * Instantiates a Point
     * @param id id of point
     * @param  latitude latitude of point
     * @param longitude longitude of point
     * @param segments list of adjacent segments to point
     */
    Point(final int id, final double latitude, final double longitude, final List<Segment> segments)
    {
        Validate.notNull(id, "id is null");
        if (latitude<-90){
            throw new IllegalArgumentException("latitude is too small");
        }
        if (latitude>90){
            throw new IllegalArgumentException("latitude is too great");
        }
        if (longitude<-180){
            throw new IllegalArgumentException("longitude is too small");
        }
        if (latitude>180){
            throw new IllegalArgumentException("longitude is too great");
        }
        this.id =id;
        this.latitude=latitude;
        this.longitude=longitude;
        this.segments = segments;
    }

}
