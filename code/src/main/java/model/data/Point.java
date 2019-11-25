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
    private long id;

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

    private ArrayList<Segment> neighbourSegments;

    /**
     * Instantiates a Point
     * @param id id of point
     * @param latitude latitude of point
     * @param longitude longitude of point
     * @param neighbourSegments List of neighbours of point
     */
    public Point(final long id, final float latitude, final float longitude)
    {
        this.id =id;
        this.latitude=latitude;
        this.longitude=longitude;
        this.neighbourSegments=new ArrayList<Segment>();
    }

    public void AddNeighbour(Segment segment)
    {
        neighbourSegments.add(segment);
    }

}
