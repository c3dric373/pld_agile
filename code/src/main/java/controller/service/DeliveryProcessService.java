package controller.service;

import model.genData.*;
import org.apache.commons.lang.Validate;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class DeliveryProcessService {

    /**
     * Replaces an Action Point in a delivery Process and returns the new
     * delivery Process. Distinguishes well between a Action point of type
     * delivery or pick up.
     *
     * @param deliveryProcess the old delivery process.
     * @param newActionPoint  the new point to replace with.
     * @return the new delivery process.
     */
    public static DeliveryProcess replacePoint(final DeliveryProcess
                                                       deliveryProcess,
                                               final ActionPoint newActionPoint) {
        Validate.notNull(deliveryProcess, "delivery Process is null");
        Validate.notNull(newActionPoint, "newActionPoint is null");

        DeliveryProcess newDeliveryProcess;
        if (newActionPoint.getActionType() == ActionType.DELIVERY) {
            newDeliveryProcess = new DeliveryProcess(
                    deliveryProcess.getPickUP(), newActionPoint);
        } else {
            newDeliveryProcess = new DeliveryProcess(
                    newActionPoint, deliveryProcess.getDelivery());
        }
        return newDeliveryProcess;
    }

    /**
     * Finds an Action Point in a list of deliveryProcesses and returns it's
     * index.
     *
     * @param deliveryProcesses the list of deliveryProcesses.
     * @param actionPoint       the action point
     * @return returns the index of the action point
     */
    public static OptionalInt findActionPoint(final List<DeliveryProcess>
                                                      deliveryProcesses,
                                              final ActionPoint actionPoint) {
        for (final DeliveryProcess deliveryProcess : deliveryProcesses) {
            if (deliveryProcess.getDelivery().equals(actionPoint)
                    || deliveryProcess.getPickUP().equals(actionPoint)) {
                return OptionalInt.of(deliveryProcesses.indexOf(
                        deliveryProcess));
            }
        }
        return OptionalInt.empty();
    }

    /**
     * Adds a new delivery process to the delivery process list.
     *
     * @param deliveryProcesses current delivery process list. (can be null)
     * @param pickUpPoint       pickup Point of the new delivery process.
     * @param deliveryPoint     delivery Point of the new delivery process.
     * @return Returns the new delivery process list with the new delivery
     * process added.
     */

    public static List<DeliveryProcess> addNewDeliveryProcess(
            final List<DeliveryProcess> deliveryProcesses,
            final ActionPoint pickUpPoint, final ActionPoint deliveryPoint) {
        Validate.notNull(pickUpPoint, "pickUpPoint is null");
        Validate.notNull(deliveryPoint, "deliveryPoint is null");

        List<DeliveryProcess> newDeliveryProcesses = deliveryProcesses;
        newDeliveryProcesses.add(deliveryProcesses.size(),
                new DeliveryProcess(pickUpPoint,
                        deliveryPoint));
        return newDeliveryProcesses;
    }

    public static void setDpInfo(final Tour tour) {
        final List<Journey> journeys = tour.getJourneyList();
        for (DeliveryProcess deliveryProcess : tour.getDeliveryProcesses()) {
            final Point startPoint = deliveryProcess.getPickUP().getLocation();
            final Point endPoint = deliveryProcess.getDelivery().getLocation();
            final int distance = JourneyService.lengthPointToPoint(journeys,
                    startPoint, endPoint);
            final Time duration =
                    JourneyService.calculateTimePointToPoint(journeys,
                            startPoint, endPoint);
            deliveryProcess.setDistance(distance);
            deliveryProcess.setTime(duration);
        }

    }

    public static Optional<DeliveryProcess> createDpBase(final Tour tour) {
        ActionPoint endAp = null;
        ActionPoint base = null;
        for (ActionPoint actionPoint1 : tour.getActionPoints()) {
            if (actionPoint1.getActionType() == ActionType.END) {
                endAp = actionPoint1;
            } else if (actionPoint1.getActionType() == ActionType.BASE) {
                base = actionPoint1;
            }
        }
        if (endAp != null && base != null) {
            DeliveryProcess result = new DeliveryProcess(base, endAp);
            return Optional.of(result);
        }
        return Optional.empty();
    }

    public static void addDeliveryProcessIdTourNotCalc(final Tour tour,
                                                       final DeliveryProcess deliveryProcess) {
        deliveryProcess.getPickUP().setId(tour.getDeliveryProcesses().size());
        deliveryProcess.getDelivery().setId(tour.getDeliveryProcesses().size());
    }

    public static void delDeliveryProcessIdTourNotCalc(final Tour tour) {
        int i = 1;
        for (DeliveryProcess deliveryProcess : tour.getDeliveryProcesses()) {
            if (deliveryProcess.getPickUP().getActionType() == ActionType.BASE) {
                deliveryProcess.getPickUP().setId(0);
                deliveryProcess.getDelivery().setId(0);
            } else {
                deliveryProcess.getDelivery().setId(i);
                deliveryProcess.getPickUP().setId(i);
                i++;
            }

        }
    }

    public static void resetDeliveryProcessIdTourCalculated(final Tour tour) {
        int i = 1;
        for (final ActionPoint actionPoint : tour.getActionPoints()) {
            if (actionPoint.getActionType() == ActionType.PICK_UP) {
                actionPoint.setId(i);
                OptionalInt optionalInt =
                        DeliveryProcessService.findActionPoint(tour.getDeliveryProcesses(), actionPoint);
                if (optionalInt.isPresent()) {
                    int index = optionalInt.getAsInt();
                    final ActionPoint delivery =
                            tour.getDeliveryProcesses().get(index).getDelivery();
                    delivery.setId(i);
                }
                i++;
            }
        }
    }
}




