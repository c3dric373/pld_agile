package model.core.service;

import model.core.TSP;
import model.core.TSP3;
import model.data.*;
import model.io.XmlToGraph;
import org.apache.commons.lang.Validate;
import org.checkerframework.checker.units.qual.C;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphService {


    public Point findNearestPoint(final List<Point> pointList,
                                  final double longitude,
                                  final double latitude) {
        final Point nearestPoint;
        long nearestId = 0;
        double nearestLong = 0.0, nearestLat = 0.0;
        double differenceLong = 100.0, differenceLat = 100.0;
        for (final Point p : pointList) {
            if ((Math.abs(p.getLatitude() - latitude) < differenceLat) &&
                    (Math.abs(p.getLongitude() - longitude) < differenceLong)) {
                differenceLat = Math.abs(p.getLatitude() - latitude);
                differenceLong = Math.abs(p.getLongitude() - longitude);
                nearestLat = p.getLatitude();
                nearestLong = p.getLongitude();
                nearestId = p.getId();
            }
        }
        /*
        System.out.println("nearestLong : " + nearestLong + " nearestLat : "
        + nearestLat);
        System.out.println("nearestID : " + nearestId);
        System.out.println("differenceLong : "+differenceLong+" differenceLat"
        +differenceLat);
        */
        nearestPoint = new Point(nearestId, nearestLat, nearestLong);
        return nearestPoint;
    }

    /**
     * Find the center of the graph thanks to itself
     * @param graph
     * @return
     */
    public static Point addGraphCenter (final Graph graph) {
        Validate.notNull(graph, "graph can't be null");
        double longitudeMax = graph.getPoints().get(0).getLongitude();
        double longitudeMin = graph.getPoints().get(0).getLongitude();
        double latitudeMax = graph.getPoints().get(0).getLatitude();
        double latitudeMin = graph.getPoints().get(0).getLatitude();

        for (Point point : graph.getPoints()) {
            if (longitudeMax < point.getLongitude()) {
                longitudeMax = point.getLongitude();
            }
            if (longitudeMin > point.getLongitude()) {
                longitudeMin = point.getLongitude();
            }
            if (latitudeMax < point.getLatitude()) {
                latitudeMax = point.getLatitude();
            }
            if (latitudeMin > point.getLatitude()) {
                latitudeMin = point.getLatitude();
            }
        }

        double latitudeCenter = (latitudeMax+latitudeMin)/2;
        double longitudeCenter = (longitudeMax+longitudeMin)/2;
        Point centerPoint = new Point(1, latitudeCenter, longitudeCenter);

        System.out.println("Latitude Center : " + latitudeCenter);
        System.out.println("Longitude Center : " + longitudeCenter);

        return centerPoint;
    }

    public Tour calculateTour(final Tour tour, final Graph graph) {
        Validate.notNull(tour, "tour can't be null");
        Validate.notNull(graph, "graph can't be null");

        Tour res = tour;
        TSP tsp3 = new TSP3();
        int tpsLimite = Integer.MAX_VALUE;
        List<Journey> journeys = getListJourney(tour, graph, tsp3, tpsLimite);

        List<Point> points = new ArrayList<>();
        for (int i = 1; i < journeys.size(); i++) {
            points.add(journeys.get(i).getStartPoint());
        }
        List<ActionPoint> actionPoints = new ArrayList<>();
        actionPoints.add(new ActionPoint(Time.valueOf("0:0:0"), tour.getBase(), ActionType.BASE));
        for (Point point : points) {
            boolean notFound = true;
            for (DeliveryProcess deliveryProcess :
                    tour.getDeliveryProcesses()) {
                if (deliveryProcess.getDelivery().getLocation() == point) {
                    actionPoints.add(deliveryProcess.getDelivery());
                    notFound = false;
                } else if (deliveryProcess.getPickUP().getLocation() == point) {
                    actionPoints.add(deliveryProcess.getPickUP());
                    notFound = false;
                }
                if (!notFound) break;;
            }
        }
        actionPoints.add(new ActionPoint(Time.valueOf("0:0:0"), tour.getBase(), ActionType.BASE));
        res.setActionPoints(actionPoints);

        JourneyService journeyService = new JourneyService();
        List<Journey> journeys1 = journeyService.calculateTime(journeys, actionPoints, tour.getStartTime());
        res.setJourneys(journeys1);

        return res;
    }

    public static boolean isInMap(final Point newPoint) {
        //todo
        return false;
    }


    public static Journey shortestPath(final Graph graph, final Point point1,
                                       final Point point2) {
        //todo
        return null;
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

        // tuples[i] contains the previous point index and the distance in the shortest path from the start point to i-th point
        List<tuple> tuples = new ArrayList<>();

        int nb_points = graph.getNbPoints();
        int start_index = map.get(id_start);
        // flag[i] represents whether we've already got the shortest path from start point to the i-th point
        boolean[] flag = new boolean[nb_points];
        List<Point> points = graph.getPoints();
        Point start_point = points.get(start_index);
        // initialize flag and tuples
        for (int i = 0; i < nb_points; i++) {
            flag[i] = (i == start_index);
            tuples.add(new tuple(start_index, start_point.getLengthTo(points.get(i).getId())));
        }
        // calculate for each point, the index of its previous point and the shortest distance from the start point to this point
        int cur_index = start_index;
        for (int i = 1; i < nb_points; i++) {
            // search for the shortest distance which has not been 'flag' yet
            double min = Double.POSITIVE_INFINITY;
            for (int j = 0; j < nb_points; j++) {
                if (!flag[j] && tuples.get(j).getDist() < min) {
                    min = tuples.get(j).getDist();
                    cur_index = j;
                }
            }
            // 'flag' this point
            flag[cur_index] = true;
            // verify for each point no 'flag', whether it's better to
            Point cur_point = points.get(cur_index);
            for (Segment s : cur_point.getSegments()) {
                long id_other = s.getIdEnd();
                int index_other = map.get(id_other);
                if (flag[index_other]) continue;
                double tmp = cur_point.getLengthTo(id_other);
                tmp = (tmp == Double.POSITIVE_INFINITY) ? tmp : tmp + min;
                if (tmp < tuples.get(index_other).getDist()) {
                    tuples.get(index_other).setPrev(cur_index);
                    tuples.get(index_other).setDist(tmp);
                }
            }
        }
//        System.out.printf("dijkstra(%d)\n", id_start);
//        for (int i = 0; i < nb_points; i++) {
//            System.out.printf("  shortest(%d, %d)=%f\n", id_start, points.get(i).getId(), res.get(i).getDist());
//        }
        return tuples;
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

    public static void main(String[] args) {
        XmlToGraph xmlToGraph = new XmlToGraph();
        Computing computing = new Computing();
        String file_graph = "resource/grandPlan.xml";
        String file_tour = "resource/demandeGrand7.xml";
        List<Point> points = xmlToGraph.getGraphFromXml(file_graph);
        Tour tour = xmlToGraph.getDeliveriesFromXml(file_tour);
        Graph graph = new Graph(points);
//        GraphService.calculateTour(tour, graph);
        GraphService.addGraphCenter(graph);
    }
}
