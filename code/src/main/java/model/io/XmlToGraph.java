package model.io;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.data.*;
import org.apache.commons.lang.Validate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlToGraph {
    /**
     * ArrayList that contains nodes that we'll send at the end of the reading
     * Represents the graph
     */
    static ArrayList<Point> nodes;

    /**
     * ArrayList that contains DeliveryProcess that we'll send at the end of the reading
     */
    static ArrayList<DeliveryProcess> Deliveries;

    public static void main(final String[] args) {
 /*       ArrayList<Point> noeud = getGraphFromXml("petitPlan.xml");

        // WILL BE DELETED --
        //Check that the informations are correctly instancied
        for ( Point n : noeud){
            System.out.println(n.getId());
            for (Segment s : n.getSegments() ){
                System.out.println(s.getName());
            }
        }*/
        ArrayList<Point> noeud = getGraphFromXml("moyenPlan.xml");
        /*Tour Deliver = */getDeliveriesFromXml("demandeMoyen5.xml");
    }

    public static ArrayList<Point> getGraphFromXml(String fileName){
        Validate.notNull(fileName, "fileName is null");

        if (fileName.equals("")) {
            throw new IllegalArgumentException("fileName is empty");
        }

        nodes=new ArrayList<Point>();
        /**
         * Get an instance of class "DocumentBuilderFactory"
         */
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            /**
             * Creation of a parser
             */
            final DocumentBuilder builder = factory.newDocumentBuilder();
            /**
             * Creation of a document
             */
            final Document document = builder.parse(new File("resource" + File.separator + fileName));
            /**
             * Get the root Element
             */
            final Element root = document.getDocumentElement();
            /**
             * Get the nodes tag and display the number of nodes
             */
            final NodeList nodeList = root.getElementsByTagName("noeud");
            final int nbNodeElements = nodeList.getLength();
            System.out.println("nbNodes :"+nbNodeElements);

            /**
             * Reading of all nodes in the file and addition to the ArrayList
             */
            for (int nodeIndex = 0; nodeIndex < nbNodeElements; nodeIndex++) {
                final Element node = (Element) nodeList.item(nodeIndex);
                long nodeId = Long.parseLong(node.getAttribute("id"));
                double nodeLat = Double.parseDouble(node.getAttribute("latitude"));
                double nodeLong = Double.parseDouble(node.getAttribute("longitude"));
                Point point = new Point (nodeId, nodeLat, nodeLong);
                nodes.add(point);
            }

            /**
             * Get the segments tag and display the number of segments
             */
            final NodeList roadList = root.getElementsByTagName("troncon");
            final int nbRoadElements = roadList.getLength();
            System.out.println( "nbRoad :" + nbRoadElements);

            /**
             * Reading of all segments in the file
             */
            for (int segmentIndex = 0; segmentIndex < nbRoadElements; segmentIndex++) {
                final Element road = (Element) roadList.item(segmentIndex);
                long roadArrival = Long.parseLong(road.getAttribute("destination"));
                double roadLength = Double.parseDouble(road.getAttribute("longueur"));
                String roadName = road.getAttribute("nomRue");
                long roadDeparture = Long.parseLong(road.getAttribute("origine"));
                Segment segment = new Segment(roadDeparture, roadArrival, roadLength, roadName);

                for (Point node : nodes){
                    if(node.getId() == roadDeparture ){
                        node.addNeighbour(segment);
                    }
                }
            }

        } catch (final ParserConfigurationException | SAXException | IOException | NumberFormatException e) {
            System.err.println(e.getMessage());
        }
        return nodes;
    }

    public static void getDeliveriesFromXml(String fileName){
        Deliveries = new ArrayList<DeliveryProcess>();
        /**
         * Get an instance of class "DocumentBuilderFactory"
         */
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            /**
             * Creation of a parser
             */
            final DocumentBuilder builder = factory.newDocumentBuilder();
            /**
             * Creation of a document
             */
            final Document document = builder.parse(new File("resource" + File.separator + fileName));
            /**
             * Get the root Element
             */
            final Element root = document.getDocumentElement();
            /**
             * Get the DeliveryProcess tag and display the number of DeliveryProcess
             */
            final NodeList start = root.getElementsByTagName("entrepot");
            final Element startPoint = (Element) start.item(0);
            Long idBase = Long.parseLong(startPoint.getAttribute("adresse"));
            Point base = GetPointById(idBase);
            System.out.println("entrepot :"+idBase);
            // Recup startTime
            String startTimeString = startPoint.getAttribute("heureDepart");
            Time startTime = Time.valueOf(startTimeString);
            System.out.println("startTime = " + startTime);

            // get list livraison
            final NodeList deliveryList = root.getElementsByTagName("livraison");
            final int nbDeliveryElements = deliveryList.getLength();
            System.out.println("nbdeliveryelements :" +nbDeliveryElements);

            /**
             * Reading of all DeliveryProcess in the file and addition to the ArrayList
             */
            for (int deliveryIndex = 0; deliveryIndex < nbDeliveryElements; deliveryIndex++) {
                final Element deliveryXml = (Element) deliveryList.item(deliveryIndex);
                Long pickupPointId= Long.parseLong(deliveryXml.getAttribute("adresseEnlevement"));
                System.out.println("idPick " + pickupPointId);
                Point pickupPoint = GetPointById(pickupPointId);
                Long deliveryPointId = Long.parseLong(deliveryXml.getAttribute("adresseLivraison"));
                System.out.println("idDeliver " + deliveryPointId);
                Point deliveryPoint = GetPointById(deliveryPointId);
                int pickupTimeInt = Integer.parseInt(deliveryXml.getAttribute("dureeEnlevement"));
                Time pickupTime = DurationToTime(pickupTimeInt);
                int deliveryTimeString = Integer.parseInt(deliveryXml.getAttribute("dureeLivraison"));
                Time deliveryTime = DurationToTime(deliveryTimeString);
                ActionPoint pickupActionpoint = new ActionPoint(pickupTime, pickupPoint, ActionType.PICK_UP);
                ActionPoint deliveryActionpoint = new ActionPoint(deliveryTime,deliveryPoint, ActionType.DELIVERY);
                DeliveryProcess deliv = new DeliveryProcess(pickupActionpoint,deliveryActionpoint);
                Deliveries.add(deliv);
            }
        //Tour tour = new Tour(Deliveries, base, startTime);

        } catch (final ParserConfigurationException | SAXException | IOException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        //return new Tour();
    }

     public static Point GetPointById(long idPoint){
        for (Point p : nodes){
            if(p.getId() == idPoint) {
                return p;
            }
        }
        return new Point (120l,0.000,0.000);
    }

    public static Time DurationToTime (int durationSec){
        String durationString = String.format("%d:%02d:%02d", durationSec / 3600, (durationSec % 3600) / 60, (durationSec % 60));
        Time duration = Time.valueOf(durationString);
        System.out.println("duration = " + duration);
        return duration;
    }
}
