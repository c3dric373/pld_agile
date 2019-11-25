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
     * Value: index in 'list_point'
     */
    private Map<Integer, Integer> map;
    /**
     * Number of segments in the map
     */
    private int nb_segments;
    /**
     * Number of points in the map
     */
    private int nb_points;

    /**
     * Instantiates a Graph
     */
    Graph() {
        nb_points = 0;
        nb_segments = 0;
        points = new ArrayList<Point>();
        map = new HashMap<Integer, Integer>();
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
    void show_map() {
        for (int i = 0; i< nb_points; i++) {
            System.out.print(points.get(i).getId()+"     ");
            for (Segment s: points.get(i).getSegments()) {
                if (points.get(i).getId() == s.either())
                    System.out.print(s.other(s.either()) +" ");
                else
                    System.out.print(s.either() + " ");
            }
            System.out.println();
        }
        System.out.println("nb_nodes: "+ nb_points);
        System.out.println("nb_segments: "+nb_segments);
    }

    class tuple {
        private int prev = 0;
        private double dist = Float.POSITIVE_INFINITY;
        tuple(int prev, double dist) {
            this.prev = prev;
            this.dist = dist;
        }

        public int getPrev() {
            return prev;
        }
        public double getDist() {
            return dist;
        }
        public void setPrev(int prev) {
            this.prev = prev;
        }
        public void setDist(double dist) {
            this.dist = dist;
        }
    }

    /**
     * Dijkstra shortest path
     * the shortest path from start point to other points
     * @param start_index the index of start point
     * @return list of tuple which contains the previous point index and the distance in the shortest path from the start point to each point
     */
    List<tuple> dijkstra(int start_index) {
        if (start_index < 0) {
            throw new IllegalArgumentException("start_index is too small");
        }
        if (start_index >= nb_points) {
            throw new IllegalArgumentException("start_index is too great");
        }
        List<tuple> res = new ArrayList<>();

        boolean[] flag = new boolean[nb_points];
        Point start_point = points.get(start_index);
        for (int i = 0; i < nb_points; i++) {
            flag[i] = (i == start_index);
            res.add(new tuple(0, start_point.getLengthTo(points.get(i).getId())));
        }
        int cur_index = start_index;
        for (int i = 1; i < nb_points; i++) {
            double min = Float.POSITIVE_INFINITY;
            for (int j = 0; j < nb_points; j++) {
                if (!flag[j] && res.get(j).getDist() < min) {
                    min = res.get(j).getDist();
                    cur_index = j;
                }
            }
            flag[cur_index] = true;
            Point cur_point = points.get(cur_index);
            for (Segment s : cur_point.getSegments()) {
                long id_other = s.other(cur_point.getId());
                int index_other = map.get(id_other);
                if (flag[index_other]) continue;
                double tmp = cur_point.getLengthTo(id_other);
                tmp = (tmp == Float.POSITIVE_INFINITY) ? tmp : tmp + min;
                if (tmp < res.get(index_other).getDist()) {
                    res.get(index_other).setPrev(cur_index);
                    res.get(index_other).setDist(tmp);
                }
            }
        }
        long start_id = points.get(start_index).getId();
        System.out.printf("dijkstra(%d)\n", start_id);
        for (int i = 0; i < nb_points; i++) {
            System.out.printf("  shortest(%d, %d)=%f\n", start_id, points.get(i).getId(), res.get(i).getDist());
        }
        return res;
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

    public int getNbPoints() {
        return this.nb_points;
    }

    public int getNb_segments() {
        return this.nb_segments;
    }

    public List<Point> getPoints() {
        return this.points;
    }

    public Map<Integer, Integer> getMap() {
        return this.map;
    }
}
