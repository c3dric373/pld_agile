package model.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Container class, who's whole purpose is to pass a list of {@link Journey}
 * s to the view.
 */
@Getter
@Setter
public class ListJourneyFromDeliveryProcess implements GenData {

    /**
     * List of journeys to be send of to the view.
     */
    private List<Journey> journeyList;

    @Override
    public void accept(GenDataVisitor genDataVisitor) {
        genDataVisitor.visit(this);
    }
}
