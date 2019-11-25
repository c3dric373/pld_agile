package model.core;

import model.data.Graph;
import model.data.Point;
import model.data.Segment;

import java.util.ArrayList;
import java.util.List;

public class Computing {
    /**
     * a graph which contains the points and the segments
     */
    private Graph graph;

    /**
     * this class contains the previous point index and the distance in the shortest path from the start point to each point
     */
    public class tuple {
        private int prev;
        private double dist;
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
        int nb_points = graph.getNbPoints();
        if (start_index >= nb_points) {
            throw new IllegalArgumentException("start_index is too great");
        }
        List<tuple> res = new ArrayList<>();

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
                int index_other = graph.getMap().get(id_other);
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
}
