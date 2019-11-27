package model.data;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang.Validate;
import lombok.Getter;

/**
 * This class represents a specific road segment on the loaded map.
 * Each Segment has a origin point, a end point, the distance between them and
 * the name of the segment.
 */
@Getter
@EqualsAndHashCode
public class Segment {

    /**
     * Id of the origin Point on the map.
     */
    private long idOrigin;
    /**
     * Id of the end Point on the map.
     */
    private long idEnd;
    /**
     * Distance between the origin Point and the end Point.
     */
    private double length;
    /**
     * Name of the Segment.
     */
    private String name;

    /**
     * Smallest possible id.
     */
    private final int MIN_ID = 0;

    /**
     * Biggest not allowed length.
     */
    private final int MAX_NOT_ALLOWED_LENGTH = 0;

    /**
     * Instantiates a Segment.
     *
     * @param idOrigin Id of origin Point.
     * @param idEnd    Id of end Point.
     * @param length   Distance between the origin Point and the end Point.
     * @param name     Name of the Segment.
     */
    public Segment(final long idOrigin, final long idEnd, final double length,
                   final String name) {
        Validate.notNull(name, "name is null");
       /* if (name.equals("")) {
            throw new IllegalArgumentException("name is empty");
        }*/
        if (length < MAX_NOT_ALLOWED_LENGTH) {
            throw new IllegalArgumentException("length is negative");
        }
        if (length == MAX_NOT_ALLOWED_LENGTH) {
            throw new IllegalArgumentException("length is zero");
        }
        if (idOrigin < MIN_ID) {
            throw new IllegalArgumentException("id_origin is negative");
        }
        if (idEnd < MIN_ID) {
            throw new IllegalArgumentException("id_end is negative");
        }
        this.idOrigin = idOrigin;
        this.idEnd = idEnd;
        this.length = length;
        this.name = name;
    }

    /**
     * Get one of the point id of the segment.
     *
     * @return one of the point id of the segment.
     */
    long either() {
        return idOrigin;
    }

    /**
     * Get the other point id of the segment.
     *
     * @param id a point id of the segment.
     * @return the other point id of the segment.
     */
    long other(final long id) {
        if (id == idOrigin) {
            return idEnd;
        } else if (id == idEnd) {
            return idOrigin;
        } else {
            throw new IllegalArgumentException("segment has no such point");
        }
    }
}
