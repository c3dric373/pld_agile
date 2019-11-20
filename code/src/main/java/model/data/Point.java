package model.data;


import lombok.Getter;

import java.util.ArrayList;

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
     * List of neighbours
     */

    private ArrayList<Segment> neighbourRoads;

    /**
     * Instantiates a Point
     * @param id id of point
     * @param  latitude latitude of point
     * @param longitude longitude of point
     */
    public Point(final int id, final float latitude, final float longitude)
    {
        this.id =id;
        this.latitude=latitude;
        this.longitude=longitude;
    }

}
