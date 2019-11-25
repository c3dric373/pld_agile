package model.data;


import lombok.Getter;

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
    private float latitude;

    /**
     * Longitude of Point
     */
    private float longitude;

    /**
     * list of road segment which contains this point
     */
    private List<Segment> list_segments;

    /**
     * Instantiates a Point
     * @param id id of point
     * @param latitude latitude of point
     * @param longitude longitude of point
     */
    Point(final int id, final float latitude, final float longitude) {
        this.id =id;
        this.latitude=latitude;
        this.longitude=longitude;
        this.list_segments = new ArrayList<Segment>();
    }

    /**
     * Add a road segment to the point
     * @param s road segment to add
     */
    void addSegment(Segment s) {
        list_segments.add(s);
    }

    /**
     * Get the distance from this point to another
     * @param id the id of the other point
     * @return the distance from this point to another
     */
    float getLengthTo(int id) {
        if (this.id == id) return 0;
        for (Segment s : list_segments) {
            if ((s.either() == this.id && s.other(this.id) == id) || s.either() == id && s.other(id) == this.id) return s.getLength();
        }
        return Float.POSITIVE_INFINITY;
    }

    public int getId() {
        return id;
    }
    public float getLatitude() {
        return latitude;
    }
    public float getLongitude() {
        return longitude;
    }
    public List<Segment> getList_segments() {
        return list_segments;
    }
}
