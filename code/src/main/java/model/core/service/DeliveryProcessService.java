package model.core.service;

import model.data.ActionPoint;
import model.data.ActionType;
import model.data.DeliveryProcess;
import org.apache.commons.lang.Validate;

import java.util.List;
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
    static DeliveryProcess replacePoint(final DeliveryProcess
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
    static OptionalInt findActionPoint(final List<DeliveryProcess>
                                               deliveryProcesses,
                                       final ActionPoint actionPoint) {
        for (final DeliveryProcess deliveryProcess : deliveryProcesses) {
            if (deliveryProcess.getDelivery() == actionPoint
                    || deliveryProcess.getPickUP() == actionPoint) {
                return OptionalInt.of(deliveryProcesses.indexOf(
                        deliveryProcess));
            }

        }
        return OptionalInt.empty();
    }

    /**
     * Adds a new delivery process to the delivery process list.
     * @param deliveryProcesses current delivery process list. (can be null)
     * @param pickUpPoint pickup Point of the new delivery process.
     * @param deliveryPoint delivery Point of the new delivery process.
     * @return Returns the new delivery process list with the new delivery
     * process added.
     */

    public static List<DeliveryProcess> addNewDeliveryProcess(
            final List<DeliveryProcess> deliveryProcesses,
            final ActionPoint pickUpPoint, final ActionPoint deliveryPoint){
        Validate.notNull(pickUpPoint,"PickUp Point is null");
        Validate.notNull(deliveryPoint, "Delivery Point is null");

        List<DeliveryProcess> newDeliveryProcesses = deliveryProcesses;
        newDeliveryProcesses.add(new DeliveryProcess(pickUpPoint,
                deliveryPoint));
        return newDeliveryProcesses;
    }
}




