package model.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang.Validate;

/**
 * This class represents one Deliveryprocess. For this it has two ActionPoints one Pick Up point and one Delivery Point
 */
@Getter
@EqualsAndHashCode
public class DeliveryProcess {

    /**
     * pickUp point
     */
    final ActionPoint pickUP;

    /**
     * Delivery point
     */
    final ActionPoint delivery;

    /**
     * Instatiates a Delivery process
     *
     * @param pickUP   pickUp point of the delivery
     * @param delivery delivery point of the deliver
     */
    public DeliveryProcess(final ActionPoint pickUP, final ActionPoint delivery) {
        Validate.notNull(pickUP, "pickUp is null");
        Validate.notNull(delivery, "delivery is null");

        if (pickUP == delivery) {
            throw new IllegalArgumentException("delivery is pickup");
        } else {
            this.pickUP = pickUP;
            this.delivery = delivery;
        }
    }

    public ActionPoint getPickUP() {
        return pickUP;
    }

    public ActionPoint getDelivery() {
        return delivery;
    }
}
