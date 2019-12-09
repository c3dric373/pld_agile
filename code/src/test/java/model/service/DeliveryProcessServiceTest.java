package model.service;

import model.core.service.DeliveryProcessService;
import model.data.ActionPoint;
import model.data.ActionType;
import model.data.DeliveryProcess;
import model.data.Point;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class DeliveryProcessServiceTest {


    private Time TEST_TIME = new Time(2234);
    private Point TEST_LOCATION = new Point(1, 0, 0);
    private ActionType TEST_ACTION_TYPE = ActionType.PICK_UP;
    private ActionType TEST_ACTION_TYPE2 = ActionType.DELIVERY;
    private ActionPoint TEST_PICK_UP =
            new ActionPoint(TEST_TIME, TEST_LOCATION, TEST_ACTION_TYPE);
    private ActionPoint TEST_DELIVERY =
            new ActionPoint(TEST_TIME, TEST_LOCATION, TEST_ACTION_TYPE2);
    private List<DeliveryProcess> TEST_DELIVERYPROCESS_LIST =
            new ArrayList<DeliveryProcess>();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void
    addNewDeliveryProcessTest_PickUpPointNull_throwsIllegalArgumentException(){
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("pickUpPoint is null");

        // Act
        DeliveryProcessService.addNewDeliveryProcess(TEST_DELIVERYPROCESS_LIST,
                null,TEST_DELIVERY);

        // Assert via annotation
    }

    @Test
    public void
    addNewDeliveryProcessTest_DeliveryPointNull_throwsIllegalArgumentException(){
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("deliveryPoint is null");

        // Act
        DeliveryProcessService.addNewDeliveryProcess(TEST_DELIVERYPROCESS_LIST,
                TEST_PICK_UP,null);

        // Assert via annotation
    }

    @Test
    public void addNewDeliveryProcessTest_validCall_validAnswer()
    {

    }
}
