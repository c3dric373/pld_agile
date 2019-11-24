package model.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    /**
     * List of points in the map
     */
    private List<Point> list_points;
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
        list_points = new ArrayList<Point>();
        map = new HashMap<Integer, Integer>();
    }

    /**
     * Add a point to the map
     * @param id id of the point
     * @param longitude longitude of the point
     * @param latitude latitude of the point
     */
    void addPoint(int id, float longitude, float latitude) {
        list_points.add(new Point(id, longitude, latitude));
        map.put(id, nb_points++);
    }

    /**
     * Add a segment to the map
     * @param id_origin id of the origin point
     * @param id_end id of the end point
     * @param length distance between the two points
     * @param name name of the segment
     */
    void addSegment(int id_origin, int id_end, float length, String name) {
        if (map.get(id_origin) == null || map.get(id_end) == null) {
            System.err.println("Error segment: ");
            System.err.println("id_origin :" + id_origin);
            System.err.println("id_end :" + id_end);
            System.err.println("length :" + length);
            System.err.println("name :" + name);
        } else {
            int origin_index = map.get(id_origin);
            int end_index = map.get(id_end);
            Segment s = new Segment(id_origin, id_end, length, name);
            list_points.get(origin_index).addSegment(s);
            list_points.get(end_index).addSegment(s);
            nb_segments++;
        }
    }

    /**
     * Show the map
     */
    void show_map() {
        for (int i = 0; i< nb_points; i++) {
            System.out.print(list_points.get(i).getId()+"     ");
            for (Segment s:list_points.get(i).getList_segments()) {
                if (list_points.get(i).getId() == s.either())
                    System.out.print(s.other(s.either()) +" ");
                else
                    System.out.print(s.either() + " ");
            }
            System.out.println();
        }
        System.out.println("nb_nodes: "+ nb_points);
        System.out.println("nb_segments: "+nb_segments);
    }

    /**
     * Dijkstra shortest path
     * the shortest path from start point to other points
     * @param start_index the index of start point
     * @param prev a table which contains the previous point index in the shortest path for each point in the map
     * @param dist a table which contains the shortest distance from start point to all the points in the map
     */
    void dijkstra(int start_index, float[] prev, float[] dist) {
        boolean[] flag = new boolean[nb_points];
        for (int i = 0; i < nb_points; i++) {
            flag[i] = (i == start_index);
            prev[i] = 0;
            dist[i] = list_points.get(start_index).getLengthTo(list_points.get(i).getId());
        }
        int cur_index = start_index;
        for (int i = 1; i < nb_points; i++) {
            float min = Float.POSITIVE_INFINITY;
            for (int j = 0; j < nb_points; j++) {
                if (!flag[j] && dist[j] < min) {
                    min = dist[j];
                    cur_index = j;
                }
            }
            flag[cur_index] = true;
            Point cur_point = list_points.get(cur_index);
            for (int j = 0; j < nb_points; j++) {
                if (flag[j]) continue;
                float tmp = cur_point.getLengthTo(list_points.get(j).getId());
                tmp = (tmp == Float.POSITIVE_INFINITY) ? tmp : tmp + min;
                if (tmp < dist[j]) {
                    prev[j] = cur_index;
                    dist[j] = tmp;
                }
            }
        }
        int start_id = list_points.get(start_index).getId();
        System.out.printf("dijkstra(%d)\n", start_id);
        for (int i = 0; i < nb_points; i++) {
            System.out.printf("  shortest(%d, %d)=%f\n", start_id, list_points.get(i).getId(), dist[i]);
        }
    }

    public static void main(String[] args) {
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
        graph.dijkstra(0, prev, dist);
    }
}
