package model.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.data.Point;
import model.data.Segment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlToGraph {
    /*
     * ArrayList that contains nodes that we'll send at the end of the reading
     * Represents the graph
     */
    static ArrayList<Point> nodes;

    public static void main(final String[] args) {
        nodes=new ArrayList<Point>();
        /*
         * Step 1 : Recovery of an instance of class "DocumentBuilderFactory"
         */
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            /*
             * Step 2 : Creation of a parser
             */
            final DocumentBuilder builder = factory.newDocumentBuilder();
            /*
             * Step 3 : Creation of a document
             */
            final Document document = builder.parse(new File("C:\\Users\\colap\\Documents\\4IF\\pld_agile\\fichiersXML2019\\petitPlan.xml"));
            /*
             * Step 4 : Recovery of the root Element
             */
            final Element root = document.getDocumentElement();
            System.out.println("\n*************ROOT************");
            System.out.println(root.getNodeName());
            /*
             * Step 5 : Recovery of the nodes tag and display the number of nodes
             */
            final NodeList nodeList = root.getElementsByTagName("noeud");
            final int nbNodeElements = nodeList.getLength();
            System.out.println(nbNodeElements);

            /*
             * Step 6 : Reading of all nodes in the file and addition to the ArrayList
             */
            for (int nodeIndex = 0; nodeIndex < nbNodeElements; nodeIndex++) {
                final Element node = (Element) nodeList.item(nodeIndex);
                int nodeId = Integer.parseInt(node.getAttribute("id"));
                float nodeLat = Float.parseFloat(node.getAttribute("latitude"));
                float nodeLong = Float.parseFloat(node.getAttribute("longitude"));
                Point point = new Point (nodeId, nodeLat, nodeLong);
                nodes.add(point);
            }

            /*
             * Step 5 : Recovery of the segments tag and display the number of segments
             */
            final NodeList roadList = root.getElementsByTagName("troncon");
            final int nbRoadElements = roadList.getLength();
            System.out.println( "nbRoad :" + nbRoadElements);

            /*
             * Step 7 : Reading of all segments in the file
             */
            for (int segmentIndex = 0; segmentIndex < nbRoadElements; segmentIndex++) {
                final Element road = (Element) roadList.item(segmentIndex);
                int roadArrival = Integer.parseInt(road.getAttribute("destination"));
                float roadLength = Float.parseFloat(road.getAttribute("longueur"));
                String roadName = road.getAttribute("nom");
                int roadDeparture = Integer.parseInt(road.getAttribute("origine"));
                Segment segment = new Segment(roadDeparture, roadArrival, roadLength, roadName);
            }

        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        } catch (final SAXException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
