package model.data;

import org.apache.commons.lang.Validate;

import java.util.List;

/** A tour consists of a list of DeliveryProcesses, a base point and a start time. The list of deliveryProcesses are all
 * the deliverys that the cyclists should complete
 */
public class Tour {

    /**
     * List of all the deliveries the cyclist has to do
     */
    private List<DeliveryProcess> deliveryProcesses;

    /**
     *Start point of the delivery
     */
    private Point base;

    /**
     * Start time of the delivery
     */
    private int startTime;

    /**
     * Instatiates a Tour
     * @param deliveryProcesses list of deliveries
     * @param base start point
     * @param startTime start tim
     */
    Tour(List<DeliveryProcess> deliveryProcesses, Point base, int startTime){
        Validate.notNull(deliveryProcesses, "deliveryProcess is null");
        Validate.notNull(base,"base is null");
        if (startTime<0){
            throw new IllegalArgumentException("startTime is negative");
        }
        if (startTime>2359){
            throw new IllegalArgumentException("startTime is too great");
        }
        this.deliveryProcesses=deliveryProcesses;
        this.base=base;
        this.startTime = startTime;
    }





}
