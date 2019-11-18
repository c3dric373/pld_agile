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
    private int location;

    /**
     * Instantiates a Point
     * @param location location of the point on the map type int.
     */
    Point(int location){
        this.location=location;
    }

}
