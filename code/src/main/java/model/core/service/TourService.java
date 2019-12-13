package model.core.service;

import model.data.*;
import org.apache.commons.lang.Validate;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

/**
 * Utility class providing helper functions for tasks closely related to
 * {@link Tour}.
 */
public class TourService {
    /**
     * Empty string.
     */
    private static final String EMPTY_STRING = "";

    /**
     * Division Factor
     */
    private static long DIVISION_FACTOR = 1000L;

    /**
     * Calculates from a tour the time at a given ActionPoint by searching
     * through the list of journeys.
     * If the list of journeys is null  or the loadedTour the method return "".
     *
     * @param tourLoaded the tour to search through
     */
    public static void calculateTimeAtPoint(final Tour tourLoaded) {
        if (tourLoaded == null || tourLoaded.getActionPoints() == null) {
            return;
        }
        final List<Journey> journeys = tourLoaded.getJourneyList();
        for (ActionPoint actionPoint : tourLoaded.getActionPoints()) {
            if (actionPoint.getActionType() == ActionType.BASE) {
                actionPoint.setPassageTime(
                        tourLoaded.getStartTime().toString());
            } else if (actionPoint.getActionType() == ActionType.END) {
                actionPoint.setPassageTime(journeys.get(journeys.size() - 1)
                        .getFinishTime().toString());
            } else {
                OptionalInt index = JourneyService.findIndexPointInJourneys(
                        journeys, actionPoint.getLocation(), true);
                if (index.isPresent()) {
                    actionPoint.setPassageTime(journeys.get(index.getAsInt())
                            .getFinishTime().toString());
                }
            }

        }

    }

    /**
     * Get the complete distance of a tour by adding the distance of each
     * {@link Journey} up.
     *
     * @param tour the {@code tour} from which we want to compute the total
     *             distance
     * @return the total distance.
     */
    public static int getCompleteDistance(final Tour tour) {
        int completeDistance = 0;
        for (Journey journey : tour.getJourneyList()) {
            completeDistance += journey.getMinLength();
        }
        return completeDistance;
    }

    /**
     * Get the complete time of a tour by adding the distance of each
     * {@link Journey} up.
     *
     * @param tour the {@code tour} from which we want to compute the total
     *             time.
     * @return the total time.
     */
    public static Time getCompleteTime(final Tour tour) {
        long firstFinishTime = tour.getStartTime().getTime();
        long secondFinishTime = tour.getJourneyList()
                .get(tour.getJourneyList().size() - 1)
                .getFinishTime().getTime();

        long journeyTime = Math.abs(firstFinishTime - secondFinishTime);
        journeyTime = journeyTime / DIVISION_FACTOR;

        return JourneyService.durationToTime(journeyTime);
    }

    /**
     * Deletes a {@link DeliveryProcess} in a Tour where the optimal tour was
     * not yet calculated. This method simply deletes the
     * {@link DeliveryProcess} from the list hol by the {@link Tour}
     * @param tour
     * @param deliveryProcess
     * @return
     */
    public static Tour deleteDpTourNotCalculated(final Tour tour
            , final DeliveryProcess deliveryProcess) {
        tour.getDeliveryProcesses().remove(deliveryProcess);
        if (tour.getActionPoints() != null) {
            tour.getActionPoints().remove(deliveryProcess.getPickUP());
            tour.getActionPoints().remove(deliveryProcess.getDelivery());
        }
        return tour;
    }

    public static Tour addDpTourNotCalculated(final Tour tour
            , final DeliveryProcess deliveryProcess) {
        tour.getDeliveryProcesses().add(deliveryProcess);
        if (tour.getActionPoints() != null) {
            tour.getActionPoints().add(deliveryProcess.getPickUP());
            tour.getActionPoints().add(deliveryProcess.getDelivery());
        }
        return tour;

    }

    private static boolean checkChangeOrder(
            final Tour tour,
            final List<ActionPoint> actionPoints) {

        for (DeliveryProcess deliveryProcess : tour.getDeliveryProcesses()) {
            if (actionPoints.indexOf(deliveryProcess.getDelivery()) < actionPoints.indexOf(deliveryProcess.getPickUP())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds 2 new action points to an ordered ActionPoint list.
     * The 2 points represent a deliveryProcess.
     *
     * @param tour          Current Tour.
     * @param pickUpPoint   New PickUp point to add.
     * @param deliveryPoint New DeliveryPoint to add.
     * @return Returns the new ActionPoint list with the new DeliveryProcess
     * added.
     */
    public static Tour addNewDeliveryProcess(final Graph graph, final Tour tour,
                                             final ActionPoint pickUpPoint,
                                             final ActionPoint deliveryPoint) {
        Validate.notNull(graph, "graph is null");
        Validate.notNull(tour, "tour is null");
        Validate.notNull(pickUpPoint, "pickUpPoint is null");
        Validate.notNull(deliveryPoint, "deliveryPoint is null");
        Validate.notNull(tour.getActionPoints(),
                "actionPoints of tour is null");

        Tour newTour = tour;
        List<DeliveryProcess> newDeliveryProcessList =
                tour.getDeliveryProcesses();
        List<ActionPoint> newActionPointList = tour.getActionPoints();
        newActionPointList
                .add(newActionPointList.size() - 1, pickUpPoint);
        newActionPointList
                .add(newActionPointList.size() - 1, deliveryPoint);
        newDeliveryProcessList.add(new DeliveryProcess(pickUpPoint,
                deliveryPoint));
        newTour.setDeliveryProcesses(newDeliveryProcessList);
        newTour.setActionPoints(newActionPointList);
        List<Journey> journeys = newTour.getJourneyList();
        Journey journey = journeys.get(journeys.size() - 1);
        Point pointBefore = journey.getStartPoint();
        Point pointAfter = journey.getArrivePoint();
        GraphService graphService = new GraphService();
        Journey journey1 = graphService.getShortestPath(
                graph, pointBefore.getId(), pickUpPoint.getLocation().getId(),
                null);
        Journey journey2 = graphService.getShortestPath(
                graph, pickUpPoint.getLocation().getId(),
                deliveryPoint.getLocation().getId(), null);
        Journey journey3 = graphService.getShortestPath(
                graph, deliveryPoint.getLocation().getId(), pointAfter.getId(),
                null);
        journeys.remove(journeys.size() - 1);
        journeys.add(journey1);
        journeys.add(journey2);
        journeys.add(journey3);
        final List<Journey> calculatedJourneys = JourneyService.
                calculateTime(journeys, tour.getActionPoints(),
                        tour.getStartTime());
        newTour.setJourneyList(calculatedJourneys);

        return newTour;

    }

    /**
     * delete the deliveryProcess from the tour.
     *
     * @param tour            Tour
     * @param deliveryProcess DeliveryProcess
     * @return the tour with the deliveryProcess removed
     */
    public static Tour deleteDeliveryProcess(final Graph graph, final Tour tour,
                                             final DeliveryProcess deliveryProcess) {
        Validate.notNull(graph, "graph is null");
        Validate.notNull(tour, "tour is null");
        Validate.notNull(tour.getJourneyList(),
                "journeyList of tour is null");
        Validate.notNull(deliveryProcess, "deliveryProcess is null");

        Tour newTour = tour;
        List<DeliveryProcess> deliveryProcessesList;
        deliveryProcessesList = tour.getDeliveryProcesses();
        List<ActionPoint> actionPointList = tour.getActionPoints();
        ActionPoint pickupPoint = deliveryProcess.getPickUP();
        ActionPoint deliveryPoint = deliveryProcess.getDelivery();

        actionPointList.remove(pickupPoint);
        actionPointList.remove(deliveryPoint);
        deliveryProcessesList.remove(deliveryProcess);
        // change deliveryProcessList
        newTour.setDeliveryProcesses(deliveryProcessesList);
        // change journeyList
        List<Journey> journeys = tour.getJourneyList();
        JourneyService journeyService = new JourneyService();
        GraphService graphService = new GraphService();
        int index1 = journeyService.findIndexPointInJourneys(
                journeys, pickupPoint.getLocation(), true).getAsInt();
        Point pointBefore1 = journeys.get(index1).getStartPoint();
        int index2 = journeyService.findIndexPointInJourneys(
                journeys, pickupPoint.getLocation(), false).getAsInt();
        Point pointAfter1 = journeys.get(index2).getArrivePoint();
        Journey journey1 = graphService.getShortestPath(
                graph, pointBefore1.getId(), pointAfter1.getId(),
                null);
        journeys.remove(index2);
        journeys.remove(index1);
        journeys.add(index1, journey1);
        index1 = journeyService.findIndexPointInJourneys(journeys,
                deliveryPoint.getLocation(), true).getAsInt();
        Point pointBefore2 = journeys.get(index1).getStartPoint();
        index2 = journeyService.findIndexPointInJourneys(journeys,
                deliveryPoint.getLocation(), false).getAsInt();
        Point pointAfter2 = journeys.get(index2).getArrivePoint();
        Journey journey2 = graphService.getShortestPath(graph,
                pointBefore2.getId(), pointAfter2.getId(), null);
        journeys.remove(index2);
        journeys.remove(index1);
        journeys.add(index1, journey2);
        // change actionPointList
        newTour.setActionPoints(actionPointList);

        final List<Journey> calculatedJourneys = JourneyService.
                calculateTime(journeys, tour.getActionPoints(),
                        tour.getStartTime());
        tour.setJourneyList(calculatedJourneys);

        return newTour;
    }

    public static void recalculateOrder(final Tour tour) {
        List<ActionPoint> actionPoints1 = tour.getActionPoints();
        List<ActionPoint> result = new ArrayList<>();
        for (Journey journey : tour.getJourneyList()) {
            ActionPoint actionPoint = JourneyService.findActionPoint(
                    journey.getStartPoint(), actionPoints1);
            result.add(actionPoint);
        }
        for (ActionPoint actionPoint : actionPoints1) {
            if (actionPoint.getActionType() == ActionType.END) {
                result.add(actionPoint);
            }
        }
        tour.setActionPoints(result);
    }

    /**
     * Changes the delivery order a Tour, re calculates the journeys for a new
     * list of action points. Does not optimize anything, only calculates the
     * shortest path from one point to another.
     *
     * @param graph        the map.
     * @param tour         the tour.
     * @param actionPoints the new action points.
     * @return a new Tour with the modified journeys.
     */
    public Tour changeDeliveryOrder(final Graph graph, final Tour tour,
                                    final List<ActionPoint> actionPoints) {
        Validate.notNull(graph, "graph can't be null");
        Validate.notNull(tour, "tour can't be null");
        Validate.notNull(tour.getActionPoints(),
                "list of actionPoints of tour can't be null");
        Validate.notNull(actionPoints,
                "actionPoints can't be null");
        Validate.notEmpty(actionPoints, "actionPoints can' be empty");
        Validate.noNullElements(actionPoints,
                "actionPoints can't contain null elements");
        final List<ActionPoint> oldActionPoints = tour.getActionPoints();
        if (oldActionPoints.size() != actionPoints.size()) {
            throw new IllegalArgumentException("actonPoints list not "
                    + "of same size");
        }
        if (!TourService.checkChangeOrder(tour, actionPoints)) {
            throw new IllegalArgumentException("illegal order change");
        }
        GraphService graphService = new GraphService();
        final List<Journey> newJourneys = new ArrayList<>();
        for (int i = 1; i < actionPoints.size(); i++) {
            final Point predecessorPoint;
            predecessorPoint = oldActionPoints.get(i - 1).getLocation();
            final Point successorPoint = oldActionPoints.get(i).getLocation();
            final Journey newJourney = graphService.
                    getShortestPath(graph, predecessorPoint.getId(),
                            successorPoint.getId(), null);
            newJourneys.add(newJourney);
        }
        final Time startTime = tour.getStartTime();
        final List<Journey> calculatedJourneys = JourneyService.
                calculateTime(newJourneys, actionPoints, startTime);
        tour.setJourneyList(calculatedJourneys);
        tour.setActionPoints(actionPoints);
        return tour;
    }

    /**
     * Changes the position of an Action point on a Tour.
     *
     * @param graph    the map on which the tour takes place.
     * @param tour     the tour.
     * @param oldPoint the old point.
     * @param newPoint the new Point.
     * @return a tour with the old point being replaced by the new one.
     */
    public Tour changePointPosition(final Graph graph, final Tour tour,
                                    final ActionPoint oldPoint,
                                    final Point newPoint) {
        Validate.notNull(graph, "graph can't be null");
        Validate.notNull(tour, "tour can't be null");
        Validate.notNull(tour.getActionPoints(),
                "list of actionPoints of tour can't be null");
        Validate.notNull(oldPoint, "oldPoint can't be null");
        Validate.notNull(newPoint, "newPoint can't be null");
        // If point not in delivery processes throw exception
        List<DeliveryProcess> oldDeliveryProcesses = tour.
                getDeliveryProcesses();
        final OptionalInt optionalOldIndexDP = DeliveryProcessService.
                findActionPoint(oldDeliveryProcesses, oldPoint);
        if (optionalOldIndexDP.isEmpty()) {
            throw new IllegalArgumentException("oldPoint not in "
                    + "delivery Processes");
        }

        // Finding the journeys from and to the old point
        final boolean IS_ENDPOINT = true;
        final List<Journey> oldJourneys = tour.getJourneyList();
        final OptionalInt optOldPredecessorJ = JourneyService.
                findIndexPointInJourneys(oldJourneys, oldPoint.getLocation(),
                        IS_ENDPOINT);
        final OptionalInt optOldSuccessorJ = JourneyService.
                findIndexPointInJourneys(oldJourneys, oldPoint.getLocation(),
                        !IS_ENDPOINT);
        if (optOldPredecessorJ.isEmpty()) {
            throw new IllegalArgumentException("Point isn't endPoint of any "
                    + "Journey");
        }
        if (optOldSuccessorJ.isEmpty()) {
            throw new IllegalArgumentException("Point isn't startPoint of any "
                    + "Journey");
        }
        // Find the actionPoints before and after the point to be moved

        final List<ActionPoint> actionPoints = tour.getActionPoints();
        final int oldPointIndex = actionPoints.indexOf(oldPoint);
        if (oldPointIndex == -1) {
            throw new IllegalArgumentException("oldPoint not in list");
        }
        final ActionPoint predecessorPoint = actionPoints.
                get(oldPointIndex - 1);
        final ActionPoint successorPoint = actionPoints.
                get(oldPointIndex + 1);
        // Create a new actionPoint and replace the old one in the list of
        // delivery processes

        int oldIndexDP = optionalOldIndexDP.getAsInt();
        final DeliveryProcess oldDeliveryProcess = oldDeliveryProcesses.
                get(oldIndexDP);
        final ActionPoint newActionPoint = new ActionPoint(oldPoint.getTime(),
                newPoint, oldPoint.getActionType());
        final DeliveryProcess newDeliveryProcess = DeliveryProcessService.
                replacePoint(oldDeliveryProcess, newActionPoint);
        oldDeliveryProcesses.set(oldIndexDP, newDeliveryProcess);

        // Calculate shortest path between predecessor and new Point and between
        // the successor and the new Point
        GraphService graphService = new GraphService();

        final Journey newPredecessorJourney = graphService.getShortestPath(
                graph, predecessorPoint.getLocation().getId(),
                newPoint.getId(), null);
        final Journey newSuccessorJourney = graphService.getShortestPath(graph,
                newPoint.getId(), successorPoint.getLocation().getId(),
                null);
        //Replacing the old Journeys with the newly calculated ones

        tour.getJourneyList().set(optOldPredecessorJ.getAsInt(),
                newPredecessorJourney);
        tour.getJourneyList().set(optOldSuccessorJ.getAsInt(),
                newSuccessorJourney);

        // Replace the oldPoint by the newPoint in the actionPoint list of
        // the tour
        tour.getActionPoints().set(oldPointIndex, newActionPoint);

        // Calculate the time it takes to calculate new Journey
        final List<Journey> newJourneys = JourneyService.calculateTime(
                tour.getJourneyList(), tour.getActionPoints(),
                tour.getStartTime());

        final List<Journey> calculatedJourneys = JourneyService.
                calculateTime(newJourneys, tour.getActionPoints(),
                        tour.getStartTime());
        tour.setJourneyList(calculatedJourneys);
        return tour;
    }
}
