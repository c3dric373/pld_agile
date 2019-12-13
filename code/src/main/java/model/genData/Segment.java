package model.genData;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang.Validate;

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
    static final int MIN_ID = 0;

    /**
     * Biggest not allowed length.
     */
    static final int MAX_NOT_ALLOWED_LENGTH = 0;

    /**
     * Instantiates a Segment.
     *
     * @param idOriginPoint Id of origin Point.
     * @param idEndPoint    Id of end Point.
     * @param segmentLength Distance between the origin Point and the end Point.
     * @param segmentName   Name of the Segment.
     */
    public Segment(final long idOriginPoint, final long idEndPoint,
                   final double segmentLength, final String segmentName) {
        Validate.notNull(segmentName, "name is null");
        if (segmentLength < MAX_NOT_ALLOWED_LENGTH) {
            throw new IllegalArgumentException("length is negative");
        }
        if (segmentLength == MAX_NOT_ALLOWED_LENGTH) {
            throw new IllegalArgumentException("length is zero");
        }
        if (idOriginPoint < MIN_ID) {
            throw new IllegalArgumentException("id_origin is negative");
        }
        if (idEndPoint < MIN_ID) {
            throw new IllegalArgumentException("id_end is negative");
        }
        this.idOrigin = idOriginPoint;
        this.idEnd = idEndPoint;
        this.length = segmentLength;
        this.name = segmentName;
    }

}
