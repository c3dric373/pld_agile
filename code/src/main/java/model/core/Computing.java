package model.core;

import model.data.*;
import model.io.XmlToGraph;
import org.apache.commons.lang.Validate;

import javax.swing.event.ListDataListener;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Computing {
    /**
     * this class contains the previous point index and the distance in the shortest path from the start point to each point
     */
    class tuple {
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
    public List<tuple> dijkstra(final Graph graph, final long id_start) {
        Map<Long,Integer> map = graph.getMap();
        if (map.get(id_start) == null) {
            throw new IllegalArgumentException("id_start not in graph");
        }
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
     * @param graph Map which contains a list of points with segments connect to each of them
     * @param id_start Id of the start point of the journey
     * @param id_arrive Id of the arrival point of the journey
     * @param res_dijkstra Result of dijkstra(id_start)
     * @return Journey which represents the shortest path from the start point to arrival point
     */
    public Journey getShortestPath(final Graph graph, final long id_start, final long id_arrive, List<tuple> res_dijkstra) {
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
        double min_length = res_dijkstra.get(arrive_index).getDist();
        if (min_length == Double.POSITIVE_INFINITY) return null;
        while (true) {
            ids.add(points.get(cur_index).getId());
            if (cur_index == start_index) break;
            cur_index = res_dijkstra.get(cur_index).getPrev();
        }
        System.out.printf("Shortest Path from %d to %d in REVERSE order:\n", id_start, id_arrive);
        for (long id : ids) {
            System.out.printf("  %d", id);
        }
        System.out.println();
        return new Journey(id_start,id_arrive,ids,min_length);
    }

    public List<List<tuple>> applyDijkstraToTour(final Tour tour, final Graph graph) {
        List<List<tuple>> res_dijkstra = new ArrayList<>();
        int nb = tour.getDeliveryProcesses().size();
        for (int i = 0; i < 2*nb+1; i++) {
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

    public int[][] getCost(final Tour tour, final Graph graph, final List<List<tuple>> res_dijkstra) {
        Map<Long,Integer> map = graph.getMap();
        int nb = tour.getDeliveryProcesses().size();
        int[][] cost = new int[2*nb+1][2*nb+1];
        for (int i = 0; i < 2*nb+1; i++) {
            for (int j = 0; j < 2 * nb + 1; j++) {
                Point point;
                if (j==0) {
                    point = tour.getBase();
                } else if (j < nb+1) {
                    point = tour.getDeliveryProcesses().get(j-1).getPickUP().getLocation();
                } else {
                    point = tour.getDeliveryProcesses().get(j-nb-1).getDelivery().getLocation();
                }
                int index = map.get(point.getId());
                cost[i][j] = (int)res_dijkstra.get(i).get(index).getDist();
            }
        }
        return cost;
    }

    public List<Journey> getListJourney(final Tour tour, final Graph graph, final TSP tsp, final int tpsLimite) {
        List<List<tuple>> res_dijkstra = applyDijkstraToTour(tour, graph);
        int[][] cout = getCost(tour, graph, res_dijkstra);
        int nbSommets = tour.getDeliveryProcesses().size()*2+1;
        int[] duree = new int[nbSommets];
        tsp.chercheSolution(tpsLimite,nbSommets,cout,duree);

        List<Journey> journeys = new ArrayList<>();
        for (int i = 0; i < nbSommets; i++) {
            int index_start_tour = tsp.getMeilleureSolution(i);
            int index_arrive_tour = tsp.getMeilleureSolution((i+1)%nbSommets);
            long id_start;
            if (index_start_tour == 0) {
                id_start = tour.getBase().getId();
            } else if (index_start_tour < nbSommets/2+1) {
                id_start = tour.getDeliveryProcesses().get(index_start_tour-1).getPickUP().getLocation().getId();
            } else {
                id_start = tour.getDeliveryProcesses().get(index_start_tour-1-nbSommets/2).getDelivery().getLocation().getId();
            }
            long id_arrive;
            if (index_arrive_tour == 0) {
                id_arrive = tour.getBase().getId();
            } else if (index_arrive_tour < nbSommets/2+1) {
                id_arrive = tour.getDeliveryProcesses().get(index_arrive_tour-1).getPickUP().getLocation().getId();
            } else {
                id_arrive = tour.getDeliveryProcesses().get(index_arrive_tour-1-nbSommets/2).getDelivery().getLocation().getId();
            }
            Journey journey = getShortestPath(graph,id_start,id_arrive,res_dijkstra.get(index_start_tour));
            journeys.add(journey);
        }
        return journeys;
    }

    public List<List<Point>> getPointsFromJourneys(final Graph graph, final List<Journey> journeys) {
        List<List<Point>> list_points = new ArrayList<>();
        Map<Long,Integer> map = graph.getMap();
        for (Journey journey : journeys) {
            List<Point> points = new ArrayList<>();
            for (Long id: journey.getIds()) {
                int index_point = map.get(id);
                Point point = graph.getPoints().get(index_point);
                points.add(point);
            }
            list_points.add(points);
        }
        for (int i = 0; i < list_points.size(); i++) {
            System.out.printf("Journey %d:\n", i+1);
            List<Point> points = list_points.get(i);
            for (int j = points.size()-1; j >= 0 ; j--) {
                Point point = points.get(j);
                System.out.printf("  id: %d, longitude: %f, latitude: %f\n", point.getId(), point.getLongitude(), point.getLatitude());
            }
        }
        return list_points;
    }

    public List<List<Point>> getJourneysForDeliveryProcess(final List<Journey> journeys, final List<List<Point>> list_points, final DeliveryProcess deliveryProcess) {
        long id1 = deliveryProcess.getPickUP().getLocation().getId();
        long id2 = deliveryProcess.getDelivery().getLocation().getId();
        int index_journey1 = -1;
        int index_journey2 = -1;
        for (int i = 0; i < journeys.size(); i++) {
            if (index_journey1 !=-1 && index_journey2 != -1) break;
            if (index_journey1 == -1 && index_journey2 ==-1) {
                if (journeys.get(i).getId_start() == id1) {
                    index_journey1 = i;
                } else if (journeys.get(i).getId_start() == id2) {
                    index_journey2 = i;
                } else continue;
            }
            if (index_journey1 != -1 && index_journey2 == -1) {
                if (journeys.get(i).getId_arrive() == id1) {
                    index_journey1 = i;
                } else if (journeys.get(i).getId_arrive() == id2) {
                    index_journey2 = i;
                }
            }
        }
        List<List<Point>> res = new ArrayList<>();
        for (int i = index_journey1; i <= index_journey2; i++) {
            res.add(list_points.get(i));
        }
        System.out.printf("Delivery process(id point pick up: %d, id point delivery: %d):\n",id1,id2);
        for (int i = 0; i < res.size(); i++) {
            System.out.printf("Journey %d:\n", i+1);
            List<Point> points = res.get(i);
            for (int j = points.size()-1; j >= 0 ; j--) {
                Point point = points.get(j);
                System.out.printf("  id: %d, longitude: %f, latitude: %f\n", point.getId(), point.getLongitude(), point.getLatitude());
            }
        }
        return res;
    }

    public static void main(String[] args) {
        Computing computing = new Computing();
        XmlToGraph xmlToGraph = new XmlToGraph();
        List<Point> points = xmlToGraph.getGraphFromXml("moyenPlan.xml");
        Graph graph = new Graph(points);
        graph.show_map();

        TSP tsp1 = new TSP1();
		int tpsLimite = Integer.MAX_VALUE;
		List<DeliveryProcess> deliveryProcesses = new ArrayList<>();
		DeliveryProcess deliveryProcess1 = new DeliveryProcess(new ActionPoint(1,new Point(26121686,0,0), ActionType.PICK_UP), new ActionPoint(1,new Point(191134392,0,0),ActionType.DELIVERY));
        DeliveryProcess deliveryProcess2 = new DeliveryProcess(new ActionPoint(1,new Point(55444018,0,0), ActionType.PICK_UP), new ActionPoint(1,new Point(26470086,0,0),ActionType.DELIVERY));
        DeliveryProcess deliveryProcess3 = new DeliveryProcess(new ActionPoint(1,new Point(27362899,0,0), ActionType.PICK_UP), new ActionPoint(1,new Point(505061101,0,0),ActionType.DELIVERY));
		deliveryProcesses.add(deliveryProcess1);
        deliveryProcesses.add(deliveryProcess2);
        deliveryProcesses.add(deliveryProcess3);
		Tour tour = new Tour(deliveryProcesses, new Point(1349383079,0,0),1);

        List<Journey> journeys = computing.getListJourney(tour,graph,tsp1,tpsLimite);
        List<List<Point>> list_points = computing.getPointsFromJourneys(graph,journeys);
        List<List<Point>> part_list_points = computing.getJourneysForDeliveryProcess(journeys,list_points,deliveryProcess2);
    }
}
