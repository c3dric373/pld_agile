package model.core.service;

import model.core.TSP;
import model.core.TSP3;
import model.core.TSP4;
import model.data.*;
import org.apache.commons.lang.Validate;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphService {


    public static Point findNearestPoint(final Graph graph,
                                         final double longitude,
                                         final double latitude) {
        Validate.notNull(graph, "graph null");
        Point nearestPoint = null;
        double difference = Double.POSITIVE_INFINITY;
        for (final Point p : graph.getPoints()) {
            double differenceLat = p.getLatitude() - latitude;
            double differenceLong = p.getLongitude() - longitude;
            if (differenceLat * differenceLat + differenceLong * differenceLong < difference) {
                difference = differenceLat * differenceLat + differenceLong * differenceLong;
                nearestPoint = p;
            }
        }
        Validate.notNull(nearestPoint, "findNearestPoint can't return null");
        return nearestPoint;
    }


    /**
     * Find the center of the graph thanks to itself
     *
     * @param graph The graph
     * @return the center for the points
     */
    public static Point addGraphCenter(final Graph graph) {
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

        double latitudeCenter = (latitudeMax + latitudeMin) / 2;
        double longitudeCenter = (longitudeMax + longitudeMin) / 2;
        Point centerPoint = new Point(1, latitudeCenter, longitudeCenter);
        System.out.println(latitudeCenter + "  " + longitudeCenter);

        return new Point(1, latitudeCenter, longitudeCenter);
    }


    public static boolean isInMap(final Point newPoint) {
        //todo
        return false;
    }

    /**
     * Dijkstra shortest path
     * the shortest path from start point to other points
     *
     * @param graph   Map which contains a list of points with segments connect to each of them
     * @param idStart Id of start point
     * @return List of tuple which contains the previous point index and the distance in the shortest path
     * from the start point to each point
     */
    public List<Tuple> dijkstra(final Graph graph,
                                final long idStart) {
        Validate.notNull(graph, "graph can't be null");
        Map<Long, Integer> map = graph.getMap();
        Validate.notNull(map.get(idStart), "idStart not in graph");

        // tuples[i] contains the previous point index and the distance in the shortest path from the start point to i-th point
        List<Tuple> tuples = new ArrayList<>();
        int nbPoints = graph.getNbPoints();
        int startIndex = map.get(idStart);
        // flag[i] represents whether we've already got the shortest path from start point to the i-th point
        boolean[] flag = new boolean[nbPoints];
        List<Point> points = graph.getPoints();
        Point startPoint = points.get(startIndex);
        // initialize flag and tuples
        for (int i = 0; i < nbPoints; i++) {
            flag[i] = (i == startIndex);
            tuples.add(new Tuple(startIndex, startPoint.getLengthTo(points.get(i).getId())));
        }
        // calculate for each point, the index of its previous point and the shortest distance from the start point to this point
        int curIndex = startIndex;
        for (int i = 1; i < nbPoints; i++) {
            // search for the shortest distance which has not been 'flag' yet
            double min = Double.POSITIVE_INFINITY;
            for (int j = 0; j < nbPoints; j++) {
                if (!flag[j] && tuples.get(j).getDist() < min) {
                    min = tuples.get(j).getDist();
                    curIndex = j;
                }
            }
            // 'flag' this point
            flag[curIndex] = true;
            // verify for each point not 'flag', whether it's better to go through the current point
            Point curPoint = points.get(curIndex);
            for (Segment s : curPoint.getSegments()) {
                long idOther = s.getIdEnd();
                int indexOther = map.get(idOther);
                if (flag[indexOther]) continue;
                double tmp = curPoint.getLengthTo(idOther);
                tmp = (tmp == Double.POSITIVE_INFINITY) ? tmp : tmp + min;
                if (tmp < tuples.get(indexOther).getDist()) {
                    tuples.get(indexOther).setPrev(curIndex);
                    tuples.get(indexOther).setDist(tmp);
                }
            }
        }
        return tuples;
    }

    /**
     * Create the shortest path from one point to another
     *
     * @param graph       Map which contains a list of points with segments connect to each of them
     * @param idStart     Id of the start point of the journey
     * @param idArrive    Id of the arrival point of the journey
     * @param resDijkstra Result of dijkstra(idStart)
     * @return Journey which represents the shortest path from the start point to arrival point
     */
    public Journey getShortestPath(final Graph graph, final long idStart, final long idArrive, List<Tuple> resDijkstra) {
        Validate.notNull(graph, "graph can't be null");
        Map<Long, Integer> map = graph.getMap();
        Validate.notNull(map.get(idStart), "idStart not in graph");
        Validate.notNull(map.get(idArrive), "idArrive not in graph");

        if (resDijkstra == null) {
            resDijkstra = dijkstra(graph, idStart);
        }
        List<Point> points = graph.getPoints();
        int startIndex = map.get(idStart);
        int arriveIndex = map.get(idArrive);

        List<Point> journeyPoints = new ArrayList<>();
        int curIndex = arriveIndex;
        double minLength = resDijkstra.get(arriveIndex).getDist();
        // if it is impossible to get from the start point to the arrival point, return null
        //if(minLength == Double.POSITIVE_INFINITY){
          //  ApplicationManagerImpl.sendMessage(ErrorMessage.CANT_REACH_POINT);
        //}
        //Validate.isTrue(minLength != Double.POSITIVE_INFINITY, "can't reach from point " + idStart + " to point " + idArrive);
        // get the REVERSE order of the path one by one (start with the arrival point)
        while (true) {
            journeyPoints.add(points.get(curIndex));
            if (curIndex == startIndex) break;
            curIndex = resDijkstra.get(curIndex).getPrev();
        }
//        System.out.printf("Shortest Path from %d to %d in REVERSE order:\n", idStart, idArrive);
//        for (Point point : journeyPoints) {
//            System.out.printf("  %d", point.getId());
//        }
//        System.out.println();
        if (journeyPoints.size() == 1) {
            journeyPoints.add(journeyPoints.get(0));
        }
        return new Journey(journeyPoints, minLength);
    }

    /**
     * Apply dijkstra to all the points in the tour
     *
     * @param tour  A specific tour
     * @param graph Map which contains a list of points with segments connect to each of them
     * @return List of a number of lists of tuple which contains the previous point index and the distance
     * in the shortest path from the start point to each point
     */
    public List<List<Tuple>> applyDijkstraToTour(final Tour tour,
                                                 final Graph graph) {
        Validate.notNull(tour, "tour can't be null");
        Validate.notNull(graph, "graph can't be null");
        List<List<Tuple>> resDijkstra = new ArrayList<>();
        int nb = tour.getDeliveryProcesses().size();
        for (int i = 0; i < 2 * nb + 1; i++) {
            Point point;
            if (i == 0) {
                // point base
                point = tour.getBase();
            } else if (i < nb + 1) {
                // points pick up
                point = tour.getDeliveryProcesses().get(i - 1).getPickUP().getLocation();
            } else {
                // points delivery
                point = tour.getDeliveryProcesses().get(i - nb - 1).getDelivery().getLocation();
            }
            resDijkstra.add(dijkstra(graph, point.getId()));
        }
        return resDijkstra;
    }

    /**
     * Get a matrix of costs between each two points of the tour
     *
     * @param tour        A specific tour
     * @param graph       Map which contains a list of points with segments connect to each of them
     * @param resDijkstra Results of dijkstra for each point in the tour
     * @return A matrix of costs between each two points of the tour
     */
    public int[][] getCost(final Tour tour,
                           final Graph graph,
                           final List<List<Tuple>> resDijkstra) {
        Validate.notNull(tour, "tour can't be null");
        Validate.notNull(graph, "graph can't be null");
        Validate.notNull(resDijkstra, "resDijkstra can't be null");
        Map<Long, Integer> map = graph.getMap();
        int nbDeliveryProcesses = tour.getDeliveryProcesses().size();
        int[][] cost = new int[2 * nbDeliveryProcesses + 1][2 * nbDeliveryProcesses + 1];
        for (int i = 0; i < 2 * nbDeliveryProcesses + 1; i++) {
            for (int j = 0; j < 2 * nbDeliveryProcesses + 1; j++) {
                Point point;
                if (j == 0) {
                    // point base
                    point = tour.getBase();
                } else if (j < nbDeliveryProcesses + 1) {
                    // points pick up
                    point = tour.getDeliveryProcesses().get(j - 1).getPickUP().getLocation();
                } else {
                    // points delivery
                    point = tour.getDeliveryProcesses().get(j - nbDeliveryProcesses - 1).getDelivery().getLocation();
                }
                int index = map.get(point.getId());
                double distance = resDijkstra.get(i).get(index).getDist();
                cost[i][j] = (distance == Double.POSITIVE_INFINITY) ? Integer.MAX_VALUE : (int) (resDijkstra.get(i).get(index).getDist() / JourneyService.TRAVEL_SPEED);
            }
        }
        return cost;
    }

    /**
     * Get a list of duration for each point of the tour
     *
     * @param tour A specific tour
     * @return
     */
    public int[] getDuration(final Tour tour) {
        Validate.notNull(tour, "tour can't be null");
        int nb = tour.getDeliveryProcesses().size();
        int[] duration = new int[2 * nb + 1];
        Time referenceTime = Time.valueOf("0:0:0");
        for (int i = 1; i < 2 * nb + 1; i++) {
            Time time;
            if (i < nb + 1) {
                // points pick up
                time = tour.getDeliveryProcesses().get(i - 1).getPickUP().getTime();
            } else {
                // points delivery
                time = tour.getDeliveryProcesses().get(i - nb - 1).getDelivery().getTime();
            }
            duration[i] = (int) ((time.getTime() - referenceTime.getTime()) / 1000);
        }

        return duration;
    }

    /**
     * Get the list of journeys for a tour
     *
     * @param tour      A specific tour
     * @param graph     Map which contains a list of points with segments connect to each of them
     * @param tsp       A specific TSP
     * @param timeLimit Time limit for resolution
     * @return List of journeys for a tour
     */
    public List<Journey> getListJourney(final Tour tour, final Graph graph, final TSP tsp, final int timeLimit) {
        Validate.notNull(tour, "tour can't be null");
        Validate.notNull(graph, "graph can't be null");
        Validate.notNull(tsp, "tsp can't be null");

        List<List<Tuple>> resDijkstra = applyDijkstraToTour(tour, graph);
        int[][] cost = getCost(tour, graph, resDijkstra);
        int nbNode = tour.getDeliveryProcesses().size() * 2 + 1;
        int[] duration = getDuration(tour);
        // apply the given tsp
        tsp.searchSolution(timeLimit, nbNode, cost, duration);

        // create a list of journey based on the result of tsp
        List<Journey> journeys = new ArrayList<>();
        for (int i = 0; i < nbNode; i++) {
            int indexStartTour = tsp.getBestSolution(i);
            int indexArriveTour = tsp.getBestSolution((i + 1) % nbNode);
            long idStart;
            if (indexStartTour == 0) {
                // point base
                idStart = tour.getBase().getId();
            } else if (indexStartTour < nbNode / 2 + 1) {
                // points pick up
                idStart = tour.getDeliveryProcesses().get(indexStartTour - 1).getPickUP().getLocation().getId();
            } else {
                // points delivery
                idStart = tour.getDeliveryProcesses().get(indexStartTour - 1 - nbNode / 2).getDelivery().getLocation().getId();
            }
            long idArrive;
            if (indexArriveTour == 0) {
                // point base
                idArrive = tour.getBase().getId();
            } else if (indexArriveTour < nbNode / 2 + 1) {
                // points pick up
                idArrive = tour.getDeliveryProcesses().get(indexArriveTour - 1).getPickUP().getLocation().getId();
            } else {
                // points delivery
                idArrive = tour.getDeliveryProcesses().get(indexArriveTour - 1 - nbNode / 2).getDelivery().getLocation().getId();
            }
            Journey journey = getShortestPath(graph, idStart, idArrive, resDijkstra.get(indexStartTour));
            journeys.add(journey);
        }
        return journeys;
    }

    /**
     * Calculate the shortest path for the the tour
     *
     * @param tour  A specific tour
     * @param graph Map which contains a list of points with segments connect to each of them
     * @return A tour with a list of Journey and a list of actionPoint
     */
    public Tour calculateTour(final Tour tour,
                              final Graph graph) {
        Validate.notNull(tour, "tour can't be null");
        Validate.notNull(graph, "graph can't be null");
        Validate.isTrue(tour.getDeliveryProcesses().size() <= 15,
                "calculateTour can't take more than 15 delivery process");

        // if we catch an IllegalArgumentException, it means tour and graph are incompatible
        TSP tsp = new TSP4();
//        int timeLimit = Integer.MAX_VALUE;
        List<Journey> journeys = getListJourney(tour, graph, tsp, 30000);
        List<Point> points = new ArrayList<>();
        for (int i = 1; i < journeys.size(); i++)
            points.add(journeys.get(i).getStartPoint());
        List<ActionPoint> actionPoints = new ArrayList<>();
        actionPoints.add(new ActionPoint(Time.valueOf("0:0:0"), tour.getBase(), ActionType.BASE));
        for (Point point : points) {
            boolean notFound = true;
            for (DeliveryProcess deliveryProcess : tour.getDeliveryProcesses()) {
                if (deliveryProcess.getDelivery().getLocation() == point) {
                    actionPoints.add(deliveryProcess.getDelivery());
                    notFound = false;
                } else if (deliveryProcess.getPickUP().getLocation() == point) {
                    actionPoints.add(deliveryProcess.getPickUP());
                    notFound = false;
                }
                if (!notFound) break;
            }
        }
        actionPoints.add(new ActionPoint(Time.valueOf("0:0:0"), tour.getBase(), ActionType.END));
        tour.setActionPoints(actionPoints);
        // Calculate the finish time of each ActionPoints of each journeys
        List<Journey> journeys1 = JourneyService.calculateTime(journeys, actionPoints, tour.getStartTime());
        tour.setJourneyList(journeys1);
        return tour;
    }

    /**
     * Get the list of journeys for a delivery process
     *
     * @param journeys        List of journey for a tour
     * @param deliveryProcess A specific delivery process
     * @return List of journeys for a delivery process
     */
    public List<Journey> getJourneysForDeliveryProcess(final List<Journey> journeys, final DeliveryProcess deliveryProcess) {
        Validate.notNull(journeys, "journeys can't be null");
        Validate.notNull(deliveryProcess, "deliveryProcess can't be null");
        long id1 = deliveryProcess.getPickUP().getLocation().getId();
        long id2 = deliveryProcess.getDelivery().getLocation().getId();
        int indexJourney1 = -1;
        int indexJourney2 = -1;
        // search for the index of the start journey and the end journey
        for (int i = 0; i < journeys.size(); i++) {
            if (indexJourney1 != -1 && indexJourney2 != -1) break;
            // tsp1 makes no difference between points pick up and points delivery
            if (indexJourney1 == -1 && indexJourney2 == -1) {
                if (journeys.get(i).getStartPoint().getId() == id1) {
                    indexJourney1 = i;
                } else if (journeys.get(i).getStartPoint().getId() == id2) {
                    indexJourney2 = i;
                } else continue;
            }
            if (indexJourney1 != -1) {
                if (journeys.get(i).getArrivePoint().getId() == id1) {
                    indexJourney1 = i;
                } else if (journeys.get(i).getArrivePoint().getId() == id2) {
                    indexJourney2 = i;
                }
            }
        }
        List<Journey> res = new ArrayList<>();
        for (int i = indexJourney1; i <= indexJourney2; i++) {
            res.add(journeys.get(i));
        }
//        System.out.printf("Delivery process(id point pick up: %d, id point delivery: %d):\n", id1, id2);
//        for (int i = 0; i < res.size(); i++) {
//            System.out.printf("Journey %d:\n", i + 1);
//            List<Point> points = res.get(i).getPoints();
//            for (int j = points.size() - 1; j >= 0; j--) {
//                Point point = points.get(j);
//                System.out.printf("  id: %d, longitude: %f, latitude: %f\n", point.getId(), point.getLongitude(), point.getLatitude());
//            }
//        }
        return res;
    }

    /**
     * this class contains the previous point index and the distance in the shortest path from the start point to each point
     */
    class Tuple {
        private int prev;
        private double dist;

        Tuple(int prev, double dist) {
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

//    public static void main(String[] args) {
//        XmlToGraph xmlToGraph = new XmlToGraph();
//        GraphService graphService = new GraphService();
//        String fileGraph = "resource/grandPlan.xml";
//        String fileTour = "resource/demandeGrand7.xml";
//        List<Point> points = xmlToGraph.getGraphFromXml(fileGraph);
//        Tour tour = xmlToGraph.getDeliveriesFromXml(fileTour);
//        Graph graph = new Graph(points);
//        graphService.calculateTour(tour, graph);
//    }
}
