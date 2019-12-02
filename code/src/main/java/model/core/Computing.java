package model.core;

import model.data.*;
import model.io.XmlToGraph;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Computing {
    public static void main(String[] args) {
        Computing computing = new Computing();
        String file_graph = "/Users/noe/Desktop/ETUDE/semester 3/Agile/fichiersXML2019/moyenPlan.xml";
        String file_tour = "/Users/noe/Desktop/ETUDE/semester 3/Agile/fichiersXML2019/demandeMoyen5.xml";
        List<Point> points = XmlToGraph.getGraphFromXml(file_graph);
        Tour tour = XmlToGraph.getDeliveriesFromXml(file_tour);
        Graph graph = new Graph(points);

        TSP tsp2 = new TSP2();
        TSP1 tsp1 = new TSP1();
        int tpsLimite = Integer.MAX_VALUE;

        long start_time = System.currentTimeMillis();
        List<Journey> journeys = computing.getListJourney(tour, graph, tsp2, tpsLimite);
        long tsp2_time = System.currentTimeMillis() - start_time;
        start_time = System.currentTimeMillis();
        List<Journey> journeys2 = computing.getListJourney(tour, graph, tsp1, tpsLimite);
        long tsp1_time = System.currentTimeMillis() - start_time;

        System.out.printf("tsp1: %d\ntsp2: %d\n", tsp1_time, tsp2_time);
    }

    /**
     * Dijkstra shortest path
     * the shortest path from start point to other points
     *
     * @param graph    Map which contains a list of points with segments connect to each of them
     * @param id_start Id of start point
     * @return List of tuple which contains the previous point index and the distance in the shortest path
     * from the start point to each point
     */
    public List<tuple> dijkstra(final Graph graph, final long id_start) {
        Validate.notNull(graph, "graph can't be null");
        Map<Long, Integer> map = graph.getMap();
        Validate.notNull(map.get(id_start), "id_start not in graph");

        List<tuple> res = new ArrayList<>();

        int nb_points = graph.getNb_points();
        int start_index = map.get(id_start);
        // flag[i] represents whether we've already got the shortest path from start point to the i-th point
        boolean[] flag = new boolean[nb_points];
        List<Point> points = graph.getPoints();
        Point start_point = points.get(start_index);
        for (int i = 0; i < nb_points; i++) {
            flag[i] = (i == start_index);
            res.add(new tuple(start_index, start_point.getLengthTo(points.get(i).getId())));
        }
        int cur_index = start_index;
        for (int i = 1; i < nb_points; i++) {
            double min = Double.POSITIVE_INFINITY;
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
                tmp = (tmp == Double.POSITIVE_INFINITY) ? tmp : tmp + min;
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
     *
     * @param graph        Map which contains a list of points with segments connect to each of them
     * @param id_start     Id of the start point of the journey
     * @param id_arrive    Id of the arrival point of the journey
     * @param res_dijkstra Result of dijkstra(id_start)
     * @return Journey which represents the shortest path from the start point to arrival point
     */
    public Journey getShortestPath(final Graph graph, final long id_start, final long id_arrive, List<tuple> res_dijkstra) {
        List<Point> points = graph.getPoints();
        Map<Long, Integer> map = graph.getMap();
        int start_index = map.get(id_start);
        int arrive_index = map.get(id_arrive);

        List<Point> journey_points = new ArrayList<>();
        int cur_index = arrive_index;
        double min_length = res_dijkstra.get(arrive_index).getDist();
        if (min_length == Double.POSITIVE_INFINITY) return null;
        while (true) {
            journey_points.add(points.get(cur_index));
            if (cur_index == start_index) break;
            cur_index = res_dijkstra.get(cur_index).getPrev();
        }
        System.out.printf("Shortest Path from %d to %d in REVERSE order:\n", id_start, id_arrive);
        for (Point point : journey_points) {
            System.out.printf("  %d", point.getId());
        }
        System.out.println();
        return new Journey(journey_points, min_length);
    }

    /**
     * Apply dijkstra to all the points in the tour
     *
     * @param tour  A specific tour
     * @param graph Map which contains a list of points with segments connect to each of them
     * @return List of a number of lists of tuple which contains the previous point index and the distance
     * in the shortest path from the start point to each point
     */
    public List<List<tuple>> applyDijkstraToTour(final Tour tour, final Graph graph) {
        List<List<tuple>> res_dijkstra = new ArrayList<>();
        int nb = tour.getDeliveryProcesses().size();
        for (int i = 0; i < 2 * nb + 1; i++) {
            Point point;
            if (i == 0) {
                point = tour.getBase();
            } else if (i < nb + 1) {
                point = tour.getDeliveryProcesses().get(i - 1).getPickUP().getLocation();
            } else {
                point = tour.getDeliveryProcesses().get(i - nb - 1).getDelivery().getLocation();
            }
            res_dijkstra.add(dijkstra(graph, point.getId()));
        }
        return res_dijkstra;
    }

    /**
     * Get a matrix of costs between each two points of the tour
     *
     * @param tour         A specific tour
     * @param graph        Map which contains a list of points with segments connect to each of them
     * @param res_dijkstra Results of dijkstra for each point in the tour
     * @return A matrix of costs between each two points of the tour
     */
    public int[][] getCost(final Tour tour, final Graph graph, final List<List<tuple>> res_dijkstra) {
        Map<Long, Integer> map = graph.getMap();
        int nb = tour.getDeliveryProcesses().size();
        int[][] cost = new int[2 * nb + 1][2 * nb + 1];
        for (int i = 0; i < 2 * nb + 1; i++) {
            for (int j = 0; j < 2 * nb + 1; j++) {
                Point point;
                if (j == 0) {
                    point = tour.getBase();
                } else if (j < nb + 1) {
                    point = tour.getDeliveryProcesses().get(j - 1).getPickUP().getLocation();
                } else {
                    point = tour.getDeliveryProcesses().get(j - nb - 1).getDelivery().getLocation();
                }
                int index = map.get(point.getId());
                cost[i][j] = (int) res_dijkstra.get(i).get(index).getDist();
            }
        }
        return cost;
    }

    /**
     * Get the list of journeys for a tour
     *
     * @param tour      A specific tour
     * @param graph     Map which contains a list of points with segments connect to each of them
     * @param tsp       A specific TSP
     * @param tpsLimite Time limit for resolution
     * @return List of journeys for a tour
     */
    public List<Journey> getListJourney(final Tour tour, final Graph graph, final TSP tsp, final int tpsLimite) {
        List<List<tuple>> res_dijkstra = applyDijkstraToTour(tour, graph);
        int[][] cout = getCost(tour, graph, res_dijkstra);
        int nbSommets = tour.getDeliveryProcesses().size() * 2 + 1;
        int[] duree = new int[nbSommets];
        tsp.chercheSolution(tpsLimite, nbSommets, cout, duree);

        List<Journey> journeys = new ArrayList<>();
        for (int i = 0; i < nbSommets; i++) {
            int index_start_tour = tsp.getMeilleureSolution(i);
            int index_arrive_tour = tsp.getMeilleureSolution((i + 1) % nbSommets);
            long id_start;
            if (index_start_tour == 0) {
                id_start = tour.getBase().getId();
            } else if (index_start_tour < nbSommets / 2 + 1) {
                id_start = tour.getDeliveryProcesses().get(index_start_tour - 1).getPickUP().getLocation().getId();
            } else {
                id_start = tour.getDeliveryProcesses().get(index_start_tour - 1 - nbSommets / 2).getDelivery().getLocation().getId();
            }
            long id_arrive;
            if (index_arrive_tour == 0) {
                id_arrive = tour.getBase().getId();
            } else if (index_arrive_tour < nbSommets / 2 + 1) {
                id_arrive = tour.getDeliveryProcesses().get(index_arrive_tour - 1).getPickUP().getLocation().getId();
            } else {
                id_arrive = tour.getDeliveryProcesses().get(index_arrive_tour - 1 - nbSommets / 2).getDelivery().getLocation().getId();
            }
            Journey journey = getShortestPath(graph, id_start, id_arrive, res_dijkstra.get(index_start_tour));
            journeys.add(journey);
        }
        return journeys;
    }

    /**
     * Get the list of journeys for a delivery process
     *
     * @param journeys        List of journey for a tour
     * @param deliveryProcess A specific delivery process
     * @return List of journeys for a delivery process
     */
    public List<Journey> getJourneysForDeliveryProcess(final List<Journey> journeys, final DeliveryProcess deliveryProcess) {
        long id1 = deliveryProcess.getPickUP().getLocation().getId();
        long id2 = deliveryProcess.getDelivery().getLocation().getId();
        int index_journey1 = -1;
        int index_journey2 = -1;
        for (int i = 0; i < journeys.size(); i++) {
            if (index_journey1 != -1 && index_journey2 != -1) break;
            if (index_journey1 == -1 && index_journey2 == -1) {
                if (journeys.get(i).getStartPoint().getId() == id1) {
                    index_journey1 = i;
                } else if (journeys.get(i).getStartPoint().getId() == id2) {
                    index_journey2 = i;
                } else continue;
            }
            if (index_journey1 != -1) {
                if (journeys.get(i).getArrivePoint().getId() == id1) {
                    index_journey1 = i;
                } else if (journeys.get(i).getArrivePoint().getId() == id2) {
                    index_journey2 = i;
                }
            }
        }
        List<Journey> res = new ArrayList<>();
        for (int i = index_journey1; i <= index_journey2; i++) {
            res.add(journeys.get(i));
        }
        System.out.printf("Delivery process(id point pick up: %d, id point delivery: %d):\n", id1, id2);
        for (int i = 0; i < res.size(); i++) {
            System.out.printf("Journey %d:\n", i + 1);
            List<Point> points = res.get(i).getPoints();
            for (int j = points.size() - 1; j >= 0; j--) {
                Point point = points.get(j);
                System.out.printf("  id: %d, longitude: %f, latitude: %f\n", point.getId(), point.getLongitude(), point.getLatitude());
            }
        }
        return res;
    }

    /**
     * this class contains the previous point index and the distance in the shortest path from the start point to each point
     */
    class tuple {
        private int prev;
        private double dist;

        tuple(int prev, double dist) {
            this.prev = prev;
            this.dist = dist;
        }

        public int getPrev() {
            return prev;
        }

        public void setPrev(int prev) {
            this.prev = prev;
        }

        public double getDist() {
            return dist;
        }

        public void setDist(double dist) {
            this.dist = dist;
        }
    }
}
