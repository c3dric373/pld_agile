package model.core;

import model.data.Graph;
import model.data.Journey;
import model.data.Point;
import model.data.Segment;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Computing {
    /**
     * this class contains the previous point index and the distance in the shortest path from the start point to each point
     */
    static class tuple {
        private int prev;
        private double dist;
        tuple(int prev, double dist) {
            if (prev < 0) {
                throw new IllegalArgumentException("prev is negative");
            }
            if (dist < 0) {
                throw new IllegalArgumentException("dist is negative");
            }
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
            if (prev < 0) {
                throw new IllegalArgumentException("prev is negative");
            }
            this.prev = prev;
        }
        public void setDist(double dist) {
            if (dist < 0) {
                throw new IllegalArgumentException("dist is negative");
            }
            this.dist = dist;
        }
    }

    /**
     * Dijkstra shortest path
     * the shortest path from start point to other points
     * @param graph Map which contains a list of points with segments connect to each of them
     * @param id_start Id of start point
     * @return List of tuple which contains the previous point index and the distance in the shortest path from the start point to each point
     */
    List<tuple> dijkstra(final Graph graph, final long id_start) {
        Map<Long,Integer> map = graph.getMap();
        if (map.get(id_start) == null) {
            throw new IllegalArgumentException("id_start not in graph");
        }
        List<tuple> res = new ArrayList<>();

        int nb_points = graph.getNbPoints();
        int start_index = map.get(id_start);
        // flag[i] represents whether we've already got the shortest path from start point to the i-th point
        boolean[] flag = new boolean[nb_points];
        List<Point> points = graph.getPoints();
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
                long id_other = s.getId_end();
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
        System.out.printf("dijkstra(%d)\n", id_start);
        for (int i = 0; i < nb_points; i++) {
            System.out.printf("  shortest(%d, %d)=%f\n", id_start, points.get(i).getId(), res.get(i).getDist());
        }
        return res;
    }

    /**
     * Create the shortest path from one point to another
     * @param graph Map which contains a list of points with segments connect to each of them
     * @param id_start Id of the start point of the journey
     * @param id_arrive Id of the arrival point of the journey
     * @param res_dijkstra Result of dijkstra(id_start)
     * @return Journey which represents the shortest path from the start point to arrival point
     */
    Journey getShortestPath(final Graph graph, final long id_start, final long id_arrive, List<tuple> res_dijkstra) {
        Map<Long,Integer> map = graph.getMap();
        if (map.get(id_start) == null) {
            throw new IllegalArgumentException("id_start not in graph");
        }
        if (map.get(id_arrive) == null) {
            throw new IllegalArgumentException("id_arrive not in graph");
        }
        Validate.notNull(res_dijkstra,"res_dijkstra is null");

        List<Point> points = graph.getPoints();
        int start_index = map.get(id_start);
        int arrive_index = map.get(id_arrive);
        List<Long> ids = new ArrayList<>();
        int cur_index = arrive_index;
        while (true) {
            ids.add(0,points.get(cur_index).getId());
            if (cur_index == start_index) break;
            cur_index = res_dijkstra.get(cur_index).getPrev();
        }
        double min_length = res_dijkstra.get(arrive_index).getDist();
        return new Journey(id_start,id_arrive,ids,min_length);
    }
}
