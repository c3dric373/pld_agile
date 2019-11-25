package model.data;

import org.apache.commons.lang.Validate;
import lombok.Getter;

/**
 * This class represents a specific road segment on the loaded map.
 * Each Segment has a origin point, a end point, the distance between them and the name of the segment.
 */
@Getter
public class Segment {

    /**
     * Id of the origin Point on the map
     */
    private int id_origin;
    /**
     * Id of the end Point on the map
     */
    private int id_end;
    /**
     * Distance between the origin Point and the end Point
     */
    private float length;
    /**
     * Name of the Segment
     */
    private String name;

    /**
     * Instantiates a Segment
     * @param id_origin Id of origin Point
     * @param id_end Id of end Point
     * @param length Distance between the origin Point and the end Point
     * @param name Name of the Segment
     */
    Segment(final int id_origin, final int id_end, final float length, final String name)
    {
        Validate.notNull(name, "name is null");
        if (name.equals("")) {
            throw new IllegalArgumentException("name is empty");
        }
        if (length < 0) {
            throw new IllegalArgumentException("length is negative");
        }
        if (length == 0) {
            throw new IllegalArgumentException("length is zero");
        }
        if (id_origin < 0) {
            throw new IllegalArgumentException("id_origin is negative");
        }
        if (id_end < 0) {
            throw new IllegalArgumentException("id_end is negative");
        }
        this.id_origin = id_origin;
        this.id_end = id_end;
        this.length = length;
        this.name = name;
    }

    /**
     * Get one of the point id of the segment
     * @return one of the point id of the segment
     */
    int either() {
        return id_origin;
    }

    /**
     * Get the other point id of the segment
     * @param id a point id of the segment
     * @return the other point id of the segment
     */
    int other(int id){
        if(id == id_origin){
            return id_end;
        }else if(id == id_end){
            return id_origin;
        }else{
            throw new IllegalArgumentException("segment has no such point");
        }
    }

    public int getId_origin() {
        return id_origin;
    }
    public int getId_end() {
        return id_end;
    }
    public float getLength() {
        return length;
    }
    public String getName() {
        return name;
    }
}
