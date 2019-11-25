package model.data;


import lombok.Getter;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
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
     * @param latitude latitude of point
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
        if (longitude>180){
            throw new IllegalArgumentException("longitude is too great");
        }
        if (id < 0){
            throw new IllegalArgumentException("id is negative");
        }
        this.id =id;
        this.latitude=latitude;
        this.longitude=longitude;
        this.segments = segments;
    }

    /**
     * Add a road segment to the point
     * @param s road segment to add
     */
    void addSegment(Segment s) {
        segments.add(s);
    }

    /**
     * Get the distance from this point to another if it's reachable via one segments
     * @param id the id of the other point
     * @return the distance from this point to another
     */
    double getLengthTo(final int id) {
        if (this.id == id) return 0;
        for (final Segment s : segments) {
            if ((s.either() == this.id && s.other(this.id) == id) || s.either() == id && s.other(id) == this.id) return s.getLength();
        }
        throw new IllegalArgumentException("point not reachable via one segment");
    }

    public int getId() {
        return id;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public List<Segment> getSegments() {
        return segments;
    }
}
