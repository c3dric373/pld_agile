package model.io;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.data.Tour;
import model.data.Point;
import model.data.DeliveryProcess;
import model.data.ActionPoint;
import model.data.Segment;
import model.data.ActionType;

import org.apache.commons.lang.Validate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlToGraph {
    /**
     * Number of Seconds in a Hour.
     */
    static final int NB_SEC_IN_HOUR = 3600;
    /**
     * Number of Seconds in a minute.
     */
    static final int NB_SEC_IN_MIN = 60;
    /**
     * ArrayList that contains nodes that we'll send at the end of the reading.
     * Represents the graph
     */
    private static ArrayList<Point> nodes;
    /**
     * Tour that contains deliveryProcess.
     */
    private static Tour tour;
    /**
     * ArrayList that contains DeliveryProcess that we'll send
     * at the end of the reading.
     */
    private static ArrayList<DeliveryProcess> deliveries;

    // public static void main(final String[] args) {
 /*       ArrayList<Point> noeud = getGraphFromXml("resource/petitPlan.xml");

        // WILL BE DELETED --
        //Check that the informations are correctly instancied
        for ( Point n : noeud){
            System.out.println(n.getId());
            for (Segment s : n.getSegments() ){
                System.out.println(s.getName());
            }
        }
        ArrayList<Point> noeud = getGraphFromXml("resource/moyenPlan.xml");
        Tour Deliver = getDeliveriesFromXml("resource/demandeMoyen5.xml");
        List<DeliveryProcess> Dp = Deliver.getDeliveryProcesses();
        System.out.println( "------------------------");
        for (DeliveryProcess p : Dp){
            System.out.println( "id "+p.getDelivery().getPoint().getId());
            System.out.println( "lat "+p.getDelivery().getPoint()
                                                    .getLatitude());
        }
    */
    // }

    /**
     * Get the point with the provided id.
     *
     * @param idPoint id of the Point
     * @return the Point
     */
    public static Point getPointById(final long idPoint) {
        Point point = null;
        for (Point p : nodes) {
            if (p.getId() == idPoint) {
                point = p;
            }
        }
        if (point == null) {
            throw new IllegalArgumentException("Point doesn't exist");
        }
        return point;
    }

    /**
     * get the nodes parameter.
     *
     * @return nodes object
     */
    public static ArrayList<Point> getNodes() {
        return nodes;
    }

    /**
     * get the tour parameter.
     *
     * @return tour
     */
    public static Tour getTour() {
        return tour;
    }

    /**
     * get the List of DeliveryProcess.
     *
     * @return List of DeliveryProcess
     */
    public static ArrayList<DeliveryProcess> getDeliveries() {
        return deliveries;
    }

    /**
     * Create the list of nodes from the Xml file.
     *
     * @param path path to the Xml file
     * @return List of Point
     */
    public ArrayList<Point> getGraphFromXml(final String path) {
        Validate.notNull(path, "path is null");

        if (path.equals("")) {
            throw new IllegalArgumentException("path is empty");
        }

        nodes = new ArrayList<Point>();

        // Get an instance of class "DocumentBuilderFactory".
        final DocumentBuilderFactory factory;
        factory = DocumentBuilderFactory.newInstance();
        try {
            // Creation of a parser.
            final DocumentBuilder builder = factory.newDocumentBuilder();

            //Creation of a document.
            final Document document = builder.parse(new File(path));

            // Get the root Element.
            final Element root = document.getDocumentElement();

            // Get the nodes tag and display the number of nodes.
            final NodeList nodeList = root.getElementsByTagName("noeud");
            final int nbNodeElements = nodeList.getLength();
            System.out.println("nbNodes :" + nbNodeElements);

            // Reading of all nodes in the fil and addition to the ArrayList.
            for (int nodeIndex = 0; nodeIndex < nbNodeElements; nodeIndex++) {
                final Element node = (Element) nodeList.item(nodeIndex);
                long nodeId = Long.parseLong(node.getAttribute("id"));
                String latString = node.getAttribute("latitude");
                double nodeLat = Double.parseDouble(latString);
                String longString = node.getAttribute("longitude");
                double nodeLong = Double.parseDouble(longString);
                Point point = new Point(nodeId, nodeLat, nodeLong);
                nodes.add(point);
            }

            // Get the segments tag and display the number of segments.
            final NodeList roadList = root.getElementsByTagName("troncon");
            final int nbRoadElements = roadList.getLength();
            System.out.println("nbRoad :" + nbRoadElements);

            // Reading of all segments in the file.
            for (int segmentIndex = 0; segmentIndex < nbRoadElements;
                    segmentIndex++) {
                final Element road = (Element) roadList.item(segmentIndex);
                String arrivalString = road.getAttribute("destination");
                long roadArrival = Long.parseLong(arrivalString);
                String lengthString = road.getAttribute("longueur");
                double roadLength = Double.parseDouble(lengthString);
                String roadName = road.getAttribute("nomRue");
                String departureString = road.getAttribute("origine");
                long roadDeparture = Long.parseLong(departureString);
                Segment segment = new Segment(roadDeparture, roadArrival,
                                                    roadLength, roadName);

                for (Point node : nodes) {
                    if (node.getId() == roadDeparture) {
                        node.addNeighbour(segment);
                    }
                }
            }

        } catch (final ParserConfigurationException | SAXException
                | IOException | NumberFormatException e) {
            System.err.println(e.getMessage());
        }
        return nodes;
    }

    /**
     * Create the list of deliveries from the provided file.
     *
     * @param path path to the Xml file
     * @return List of deliveries
     */
    public Tour getDeliveriesFromXml(final String path) {
        Validate.notNull(path, "path is null");

        if (path.equals("")) {
            throw new IllegalArgumentException("path is empty");
        }

        deliveries = new ArrayList<DeliveryProcess>();

        // Get an instance of class "DocumentBuilderFactory".
        final DocumentBuilderFactory factory;
        factory = DocumentBuilderFactory.newInstance();
        try {
            // Creation of a parser.
            final DocumentBuilder builder = factory.newDocumentBuilder();

            // Creation of a document.
            final Document document = builder.parse(new File(path));

            // Get the root Element.
            final Element root = document.getDocumentElement();

            // Get the DeliveryProcess tag
            // and display the number of DeliveryProcess.
            final NodeList start = root.getElementsByTagName("entrepot");
            final Element startPoint = (Element) start.item(0);
            Long idBase = Long.parseLong(startPoint.getAttribute("adresse"));
            Point base = getPointById(idBase);
            System.out.println("entrepot :" + idBase);
            // Recup startTime
            String startTimeString = startPoint.getAttribute("heureDepart");
            Time startTime = Time.valueOf(startTimeString);
            System.out.println("startTime = " + startTime);

            // get list livraison
            final NodeList deliveryList;
            deliveryList = root.getElementsByTagName("livraison");
            final int nbDeliveryElements = deliveryList.getLength();
            System.out.println("nbdeliveryelements :" + nbDeliveryElements);

            // Reading of all DeliveryProcess in the file
            // and addition to the ArrayList.
            for (int deliveryIndex = 0; deliveryIndex < nbDeliveryElements;
                    deliveryIndex++) {
                final Element deliveryXml;
                deliveryXml = (Element) deliveryList.item(deliveryIndex);
                String pickupIdString;
                pickupIdString = deliveryXml.getAttribute("adresseEnlevement");
                Long pickupPointId = Long.parseLong(pickupIdString);
                System.out.println("idPick " + pickupPointId);
                Point pickupPoint = getPointById(pickupPointId);
                String deliveryIdString;
                deliveryIdString = deliveryXml.getAttribute("adresseLivraison");
                Long deliveryPointId = Long.parseLong(deliveryIdString);
                System.out.println("idDeliver "
                                    + deliveryPointId);
                Point deliveryPoint = getPointById(deliveryPointId);
                String pickupTimeString;
                pickupTimeString = deliveryXml.getAttribute("dureeEnlevement");
                int pickupTimeInt = Integer.parseInt(pickupTimeString);
                Time pickupTime = durationToTime(pickupTimeInt);
                String deliveryTimeString;
                deliveryTimeString = deliveryXml.getAttribute("dureeLivraison");
                int deliveryTimeInt = Integer.parseInt(deliveryTimeString);
                Time deliveryTime = durationToTime(deliveryTimeInt);
                ActionPoint pickupActionpoint;
                pickupActionpoint = new ActionPoint(pickupTime, pickupPoint,
                        ActionType.PICK_UP);
                ActionPoint deliveryActionpoint;
                deliveryActionpoint = new ActionPoint(deliveryTime,
                        deliveryPoint, ActionType.DELIVERY);
                DeliveryProcess deliv;
                deliv = new DeliveryProcess(pickupActionpoint,
                        deliveryActionpoint);
                deliveries.add(deliv);
            }
            tour = new Tour(deliveries, base, startTime);

        } catch (final ParserConfigurationException | SAXException
                | IOException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        return tour;
    }

    /**
     * transform a duration in a time.
     *
     * @param durationSec duration in Seconds
     * @return time object corresponding to durationSec
     */
    public Time durationToTime(final int durationSec) {
        int nbHour = durationSec / NB_SEC_IN_HOUR;
        int nbMin = (durationSec % NB_SEC_IN_HOUR) / NB_SEC_IN_MIN;
        int nbSec = (durationSec % NB_SEC_IN_MIN);
        String durationString;
        durationString = String.format("%d:%02d:%02d", nbHour, nbMin, nbSec);
        Time duration = Time.valueOf(durationString);
        System.out.println("duration = " + duration);
        return duration;
    }
}
