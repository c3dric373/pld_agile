package model.data;

import org.apache.commons.lang.Validate;

/**
 * This class represents one Deliveryprocess. For this it has two ActionPoints one Pick Up point and one Delivery Point
 */
public class DeliveryProcess {

    /**
     * pickUp point
     */
    ActionPoint pickUP;

    /**
     * Delivery point
     */
    ActionPoint delivery;

    /**
     * Instatiates a Delivery process
     * @param pickUP pickUp point of the delivery
     * @param delivery delivery point of the deliver
     */
    DeliveryProcess(ActionPoint pickUP, ActionPoint delivery){
        Validate.notNull(pickUP, "pickUp is null");
        Validate.notNull(delivery, "delivery is null");

        if(pickUP == delivery) {
            throw new IllegalArgumentException("delivery is pickup");
        } else {
            this.pickUP = pickUP;
            this.delivery = delivery;
        }
    }

}
