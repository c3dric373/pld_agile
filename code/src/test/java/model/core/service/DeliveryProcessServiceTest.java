package model.core.service;

import model.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryProcessServiceTest {

    private DeliveryProcessService deliveryProcessService;
    private Point point1;
    private Point point2;
    private Point point3;
    private Point point4;
    private ActionPoint pickUp;
    private ActionPoint delivery;
    private ActionPoint pickUp1;
    private ActionPoint delivery1;
    private ActionPoint base;
    private ActionPoint end;
    private DeliveryProcess deliveryProcess;
    private DeliveryProcess deliveryProcess1;
    private List<DeliveryProcess> deliveryProcesses;
    Tour tour;

    @BeforeEach
    void setUp() {
        deliveryProcessService = new DeliveryProcessService();
        point1 = new Point(1, 20.0, 12.0);
        point2 = new Point(2, 20.0, 12.0);
        point3 = new Point(3, 20.0, 12.0);
        point4 = new Point(4, 20.0, 12.0);
        pickUp = new ActionPoint(Time.valueOf("0:0:0"), point1, ActionType.PICK_UP);
        delivery = new ActionPoint(Time.valueOf("0:1:0"), point2, ActionType.DELIVERY);
        pickUp1 = new ActionPoint(Time.valueOf("0:2:0"), point3, ActionType.PICK_UP);
        delivery1 = new ActionPoint(Time.valueOf("0:3:0"), point4, ActionType.DELIVERY);
        base = new ActionPoint(Time.valueOf("0:0:0"), point1, ActionType.BASE);
        end = new ActionPoint(Time.valueOf("0:0:0"), point1, ActionType.END);
        deliveryProcess = new DeliveryProcess(pickUp, delivery);
        deliveryProcess1 = new DeliveryProcess(pickUp1, delivery1);
        deliveryProcesses = new ArrayList<>();
        deliveryProcesses.add(deliveryProcess);
        deliveryProcesses.add(deliveryProcess1);
        tour = new Tour(deliveryProcesses, base.getLocation(), Time.valueOf("0:0:0"));
    }

    @Nested
    class replacePoint {
        @Test
        void nullParameter() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.replacePoint(null, pickUp1)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.replacePoint(deliveryProcess, null))
            );
        }

        @Test
        void incompatibleParameter() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.replacePoint(deliveryProcess, base)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.replacePoint(deliveryProcess, end))
            );
        }

        @Test
        void correctUsage() {
            DeliveryProcess res = deliveryProcessService.replacePoint(deliveryProcess, delivery1);
            DeliveryProcess finalRes = deliveryProcessService.replacePoint(deliveryProcess, pickUp1);
            assertAll(
                    () -> assertEquals(pickUp1, finalRes.getPickUP(),
                            "replacePoint should replace the right pick up point"),
                    () -> assertEquals(delivery1, res.getDelivery(),
                            "replacePoint should replace the right delivery point")
            );
        }
    }

    @Nested
    class findActionPoint {
        @Test
        void nullParameter() {
            List<DeliveryProcess> deliveryProcesses1 = new ArrayList<>();
            deliveryProcesses1.add(null);
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.findActionPoint(null, pickUp)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.findActionPoint(deliveryProcesses, null)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.findActionPoint(deliveryProcesses1, pickUp))
            );
        }

        @Test
        void correctUsage() {
            OptionalInt res = deliveryProcessService.findActionPoint(deliveryProcesses, delivery1);
            OptionalInt res1 = deliveryProcessService.findActionPoint(deliveryProcesses, base);
            assertAll(
                    () -> assertEquals(1, res.getAsInt(),
                            "findActionPoint should return the right index"),
                    () -> assertTrue(res1.isEmpty(),
                            "replacePoint should return empty is action point not found")
            );
        }
    }

    @Nested
    class addNewDeliveryProcess {
        @Test
        void nullParameter() {
            List<DeliveryProcess> deliveryProcesses1 = new ArrayList<>();
            deliveryProcesses1.add(null);
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.addNewDeliveryProcess(null, pickUp, delivery)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.addNewDeliveryProcess(deliveryProcesses, null, delivery)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.addNewDeliveryProcess(deliveryProcesses1, pickUp, delivery)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.addNewDeliveryProcess(deliveryProcesses, pickUp, null))
            );
        }

        @Test
        void incompatibleParameter() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.addNewDeliveryProcess(deliveryProcesses, base, delivery)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.addNewDeliveryProcess(deliveryProcesses, pickUp, base))
            );
        }

        @Test
        void correctUsage() {
            List<DeliveryProcess> res = deliveryProcessService.addNewDeliveryProcess(deliveryProcesses, pickUp, delivery);
            assertAll(
                    () -> assertEquals(pickUp, res.get(2).getPickUP(),
                            "addNewDeliveryProcess should add a new deliveryProcess"),
                    () -> assertEquals(delivery, res.get(2).getDelivery(),
                            "addNewDeliveryProcess should add a new deliveryProcess")
            );

        }
    }

    @Nested
    class setDpInfo {
        @Test
        void nullParameter() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.setDpInfo(null)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.setDpInfo(tour))
            );
        }

        @Test
        void correctUsage() {
            List<DeliveryProcess> deliveryProcesses1 = new ArrayList<>();
            deliveryProcesses1.add(deliveryProcess);
            Tour tour = new Tour(deliveryProcesses1, base.getLocation(), Time.valueOf("0:0:0"));
            List<Point> points1 = new ArrayList<>();
            points1.add(point1);
            points1.add(base.getLocation());
            Journey journey1 = new Journey(points1, 5);
            journey1.setFinishTime(Time.valueOf("0:0:2"));
            List<Point> points2 = new ArrayList<>();
            points2.add(point2);
            points2.add(point1);
            Journey journey2 = new Journey(points2, 5);
            journey2.setFinishTime(Time.valueOf("0:0:3"));
            List<Point> points3 = new ArrayList<>();
            points3.add(end.getLocation());
            points3.add(point2);
            Journey journey3 = new Journey(points3, 5);
            journey3.setFinishTime(Time.valueOf("0:0:4"));
            List<Journey> journeys = new ArrayList<>();
            journeys.add(journey1);
            journeys.add(journey2);
            journeys.add(journey3);
            tour.setJourneyList(journeys);
            deliveryProcessService.setDpInfo(tour);
            assertAll(
                    () -> assertEquals(5, tour.getDeliveryProcesses().get(0).getDistance(),
                            "setDpInfo should set the right distance for deliveryProcesses"),
                    () -> assertEquals(-3599000.0, tour.getDeliveryProcesses().get(0).getTime().getTime(),
                            "setDpInfo should set the right duration for deliveryProcesses")
            );
        }
    }

    @Nested
    class createDpBase {
        @Test
        void nullParameter() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.createDpBase(null)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> deliveryProcessService.createDpBase(tour))
            );
        }

        @Test
        void correctUsage() {
            List<DeliveryProcess> deliveryProcesses1 = new ArrayList<>();
            deliveryProcesses1.add(deliveryProcess);
            Tour tour = new Tour(deliveryProcesses1, base.getLocation(), Time.valueOf("0:0:0"));
            List<ActionPoint> actionPoints = new ArrayList<>();
            actionPoints.add(base);
            actionPoints.add(pickUp);
            actionPoints.add(delivery);
            actionPoints.add(end);
            tour.setActionPoints(actionPoints);
            Optional<DeliveryProcess> res = deliveryProcessService.createDpBase(tour);
            assertAll(
                    () -> assertEquals(base, res.get().getPickUP(),
                            "createDpBase should return a fake deliveryProcesses with base as pickUp point"),
                    () -> assertEquals(end, res.get().getDelivery(),
                            "createDpBase should return a fake deliveryProcesses with end as delivery point")
            );
        }
    }
}