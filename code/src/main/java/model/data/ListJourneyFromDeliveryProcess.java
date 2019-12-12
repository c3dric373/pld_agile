package model.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListJourneyFromDeliveryProcess implements GenData{

    private List<Journey> journeyList;
    @Override
    public void accept(GenDataVisitor genDataVisitor) {
        genDataVisitor.visit(this);
    }
}
