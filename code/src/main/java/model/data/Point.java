package model.data;


import lombok.Getter;

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
    private int latitude;

    /**
     * Longitude of Point
     */

    private int longitude;

    /**
     * Instantiates a Point
     * @param id id of point
     * @param  latitude latitude of point
     * @param longitude longitude of point
     */
    Point(final int id, final int latitude, final int longitude)
    {
        this.id =id;
        this.latitude=latitude;
        this.longitude=longitude;
    }

}
