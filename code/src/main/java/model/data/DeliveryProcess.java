package model.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;

import java.sql.Time;

/**
 * This class represents one DeliveryProcess. For this it has two ActionPoints
 * one Pick Up point and one Delivery Point
 */
@Getter
@EqualsAndHashCode
public class DeliveryProcess implements GenData {

    /**
     * pickUp point.
     */
    private final ActionPoint pickUP;

    /**
     * Delivery point.
     */
    private final ActionPoint delivery;

    /**
     * Distance between the two Action Point inside of a calculated tour should
     * only be set after tour calculation.
     */
    @Setter
    private int distance;

    /**
     * Time it takes to go from the pick up to the delivery point. Should only
     * be set if tour was calculated.
     */
    @Setter
    private Time time;

    /**
     * Instantiates a Delivery process.
     *
     * @param pickUpPoint   pickUp point of the delivery.
     * @param deliveryPoint delivery point of the deliver.
     */
    public DeliveryProcess(final ActionPoint pickUpPoint,
                           final ActionPoint deliveryPoint) {
        Validate.notNull(pickUpPoint, "pickUp is null");
        Validate.notNull(deliveryPoint, "delivery is null");

        if (pickUpPoint == deliveryPoint) {
            throw new IllegalArgumentException("delivery is pickup");
        } else {
            this.pickUP = pickUpPoint;
            this.delivery = deliveryPoint;
        }
    }

    @Override
    public void accept(final GenDataVisitor genDataVisitor) {
        genDataVisitor.visit(this);
    }
}
