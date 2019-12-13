package model.data;

import lombok.Getter;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Graph implements GenData {
    /**
     * List of points in the map.
     */
    private List<Point> points;
    /**
     * Key: id point.
     * Value: index in 'points'
     */
    private Map<Long, Integer> map;
    /**
     * Number of points in the map.
     */
    private int nbPoints;

    /**
     * Instantiates a Graph.
     *
     * @param pointlist list of points
     */
    public Graph(final List<Point> pointlist) {
        Validate.notNull(pointlist, "point list of the graph can't be null");
        Validate.noNullElements(pointlist, "points of the graph can't be null");
        this.nbPoints = points.size();
        this.points = pointlist;
        map = new HashMap<>();
        for (int i = 0; i < this.nbPoints; i++) {
            map.put(points.get(i).getId(), i);
        }
    }

    @Override
    public void accept(final GenDataVisitor genDataVisitor) {
        genDataVisitor.visit(this);
    }
}
