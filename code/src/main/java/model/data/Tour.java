package model.data;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang.Validate;

import java.sql.Time;
import java.util.List;

/** A tour consists of a list of DeliveryProcesses, a base point and a start time. The list of deliveryProcesses are all
 * the deliverys that the cyclists should complete
 */
@EqualsAndHashCode
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
    private Time startTime;

    /**
     * Instatiates a Tour
     * @param deliveryProcesses list of deliveries
     * @param base start point
     * @param startTime start tim
     */
    public Tour(List<DeliveryProcess> deliveryProcesses, Point base, Time startTime){
        Validate.notNull(deliveryProcesses, "deliveryProcess is null");
        Validate.notNull(base,"base is null");
        Validate.notNull(startTime,"startTime is null");
        /*if (startTime<0){
            throw new IllegalArgumentException("startTime is negative");
        }
        if (startTime>235959){
            throw new IllegalArgumentException("startTime is too great");
        }*/
        this.deliveryProcesses=deliveryProcesses;
        this.base=base;
        this.startTime = startTime;
    }

 public List<DeliveryProcess> getDeliveryProcesses(){
        return deliveryProcesses;
 }

    public Point getBase() {
        return base;
    }


}
