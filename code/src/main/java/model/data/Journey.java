package model.data;

import org.apache.commons.lang.Validate;

import java.util.List;

/**
 * This class represent the shortest path from one point to another
 */
public class Journey {
    /**
     * Id of the start point of the journey
     */
    private long id_start;
    /**
     * Id of the arrival point of the journey
     */
    private long id_arrive;
    /**
     * List of Ids of each point in the shortest path from the start point to the arrival point in the right order
     */
    private List<Long> ids;
    /**
     * The shortest length from the start point to the arrival point
     */
    private double min_length;

    /**
     * Instantiates a Journey
     * @param id_start Id of the start point of the journey
     * @param id_arrive Id of the arrival point of the journey
     * @param ids the List of Ids of each point in the shortest path from the start point to the arrival point in the right order
     * @param min_length The shortest length from the start point to the arrival point
     */
    public Journey(final long id_start, final long id_arrive, final List<Long> ids, final double min_length) {
        if (id_start < 0){
            throw new IllegalArgumentException("id_start is negative");
        }
        if (id_arrive < 0){
            throw new IllegalArgumentException("id_arrive is negative");
        }
        Validate.notNull(ids, "ids is null");
        if (min_length < 0){
            throw new IllegalArgumentException("min_length is negative");
        }
        this.id_start = id_start;
        this.id_arrive = id_arrive;
        this.ids = ids;
        this.min_length = min_length;
    }
}
