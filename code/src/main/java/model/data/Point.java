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
    Point(final int id, final int latitude, final int longitude, final List segments)
    {
        this.id =id;
        this.latitude=latitude;
        this.longitude=longitude;
        this.segments = segments;
    }

}
