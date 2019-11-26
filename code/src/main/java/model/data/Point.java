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
    private long id;

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
     */
    public Point(final long id, final double latitude, final double longitude)
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
        this.segments =new ArrayList<>();
    }

    public void AddNeighbour(Segment segment)
    {
        segments.add(segment);
    }


    /**
     * Get the distance from this point to another if it's reachable via one segments
     * @param id the id of the other point
     * @return the distance from this point to another
     */
    double getLengthTo(final long id) {
        if (this.id == id) return 0;
        for (final Segment s : segments) {
            if ((s.either() == this.id && s.other(this.id) == id) || s.either() == id && s.other(id) == this.id) return s.getLength();
        }
        throw new IllegalArgumentException("point not reachable via one segment");
    }

    public long getId() {
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
