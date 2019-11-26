package model.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Graph {
    /**
     * List of points in the map
     */
    private List<Point> points;
    /**
     * Key: id point
     * Value: index in 'points'
     */
    private Map<Long, Integer> map;
    /**
     * Number of points in the map
     */
    private int nb_points;

    /**
     * Instantiates a Graph
     */
    public Graph(final List<Point> points) {
        this.nb_points = points.size();
        this.points = points;
        map = new HashMap<>();
        for (int i = 0; i < this.nb_points; i++) {
            map.put(points.get(i).getId(),i);
        }
    }

    /**
     * Add a point to the map
     * @param id id of the point
     * @param longitude longitude of the point
     * @param latitude latitude of the point
     */
//    void addPoint(final int id, double longitude, double latitude) {
//        list_points.add(new Point(id, longitude, latitude));
//        map.put(id, nb_points++);
//    }


    /**
     * Add a segment to the map
     * @param id_origin id of the origin point
     * @param id_end id of the end point
     * @param length distance between the two points
     * @param name name of the segment
     */
     /*
    void addSegment(final int id_origin, final int id_end, final float length, final String name) {
        Validate.notNull(name, "name is null");
        if (name.equals("")) {
            throw new IllegalArgumentException("name is empty");
        }
        if (length<0){
            throw new IllegalArgumentException("length is negative");
        }
        if (length==0){
            throw new IllegalArgumentException("length is zero");
        }
        if (id_origin < 0) {
            throw new IllegalArgumentException("id_origin is negative");
        }
        if (id_end < 0) {
            throw new IllegalArgumentException("id_end is negative");
        }
        if (map.get(id_origin) == null) {
            throw new IllegalArgumentException("point id_origin not included yet");
        }
        if (map.get(id_end) == null) {
            throw new IllegalArgumentException("point id_end not included yet");
        }
        int origin_index = map.get(id_origin);
        int end_index = map.get(id_end);
        Segment s = new Segment(id_origin, id_end, length, name);
        list_points.get(origin_index).addSegment(s);
        list_points.get(end_index).addSegment(s);
        nb_segments++;
    }
*/
    /**
     * Show the map
     */
    public void show_map() {
        for (int i = 0; i< nb_points; i++) {
            System.out.print(points.get(i).getId()+"     ");
            for (Segment s: points.get(i).getSegments()) {
                System.out.print(s.getId_end() + " ");
            }
            System.out.println();
        }
        System.out.println("nb_nodes: "+ nb_points);
    }

    public static void main(String[] args) {
      /*
        Graph graph = new Graph();
        graph.addPoint(1,0,0);
        graph.addPoint(2,0,0);
        graph.addPoint(3,0,0);
        graph.addPoint(4,0,0);
        graph.addPoint(5,0,0);
        graph.addSegment(1, 2, 1, "street_AB");
        graph.addSegment(1, 3, 2, "street_AC");
        graph.addSegment(2, 3, 3, "street_BC");
        graph.addSegment(1, 4, 4, "street_AD");
        graph.addSegment(4, 5, 5, "street_DE");
        graph.addSegment(2, 5, 6, "street_BE");
        graph.show_map();
        float[] prev = new float[5];
        float[] dist = new float[5];
        graph.dijkstra(0, prev, dist);*/
    }

    public List<Point> getPoints() {
        return points;
    }

    public Map<Long, Integer> getMap() {
        return map;
    }

    public int getNb_points() {
        return nb_points;
    }
}
