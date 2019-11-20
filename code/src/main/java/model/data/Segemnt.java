package model.data;


import lombok.Getter;

/**
 * This class represents a specific road segment on the loaded map. Each road segment has a point destination, a point origin, the distance. This id is stored in this class.
 */
@Getter
public class Segment {

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
