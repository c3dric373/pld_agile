package model.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.data.Point;
import model.data.Segment;
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

    public static void main(final String[] args) {
        ArrayList<Point> noeud = getGraphFromXml("grandPlan.xml");

        // WILL BE DELETED --
        //Check that the informations are correctly instancied
        for ( Point n : noeud){
            System.out.println(n.getId());
            for (Segment s : n.getNeighbourSegments() ){
                System.out.println(s.getName());
            }
        }
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
            final Document document = builder.parse(new File(".\\resource\\" + fileName));
            /**
             * Get the root Element
             */
            final Element root = document.getDocumentElement();
            /**
             * Get the nodes tag and display the number of nodes
             */
            final NodeList nodeList = root.getElementsByTagName("noeud");
            final int nbNodeElements = nodeList.getLength();
            System.out.println(nbNodeElements);

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
                float roadLength = Float.parseFloat(road.getAttribute("longueur"));
                String roadName = road.getAttribute("nomRue");
                long roadDeparture = Long.parseLong(road.getAttribute("origine"));
                Segment segment = new Segment(roadDeparture, roadArrival, roadLength, roadName);

                for (Point node : nodes){
                    if(node.getId() == roadDeparture ){
                        node.AddNeighbour(segment);
                    }
                }
            }

        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        } catch (final SAXException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        return nodes;
    }
}
