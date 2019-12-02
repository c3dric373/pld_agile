package model.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang.Validate;

/**
 * This class represents one DeliveryProcess. For this it has two ActionPoints
 * one Pick Up point and one Delivery Point
 */
@Getter
@EqualsAndHashCode
public class DeliveryProcess {

    /**
     * pickUp point.
     */
    private final ActionPoint pickUP;

    /**
     * Delivery point.
     */
    private final ActionPoint delivery;

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

}
