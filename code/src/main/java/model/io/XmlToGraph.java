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
        ArrayList<Point> noeud = getGraphFromXml("C:\\Users\\colap\\Documents\\4IF\\pld_agile\\fichiersXML2019\\petitPlan.xml");
        for (Point n: noeud ){
            System.out.println(n.getId());
            ArrayList<Segment> g= n.getNeighbourSegments();
            System.out.println("----------");
            for ( Segment f :g){
                System.out.println(f.getName());
            }
            System.out.println("----------");
        }
    }

    public static ArrayList<Point> getGraphFromXml(String url){
        nodes=new ArrayList<Point>();
        /*
         * Get an instance of class "DocumentBuilderFactory"
         */
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            /*
             * Creation of a parser
             */
            final DocumentBuilder builder = factory.newDocumentBuilder();
            /*
             * Creation of a document
             */
            final Document document = builder.parse(new File(url));
            /*
             * Get the root Element
             */
            final Element root = document.getDocumentElement();
            /*
             * Get the nodes tag and display the number of nodes
             */
            final NodeList nodeList = root.getElementsByTagName("noeud");
            final int nbNodeElements = nodeList.getLength();
            System.out.println(nbNodeElements);

            /*
             * Reading of all nodes in the file and addition to the ArrayList
             */
            for (int nodeIndex = 0; nodeIndex < nbNodeElements; nodeIndex++) {
                final Element node = (Element) nodeList.item(nodeIndex);
                Long nodeId = Long.parseLong(node.getAttribute("id"));
                float nodeLat = Float.parseFloat(node.getAttribute("latitude"));
                float nodeLong = Float.parseFloat(node.getAttribute("longitude"));
                Point point = new Point (nodeId, nodeLat, nodeLong);
                nodes.add(point);
            }

            /*
             * Get the segments tag and display the number of segments
             */
            final NodeList roadList = root.getElementsByTagName("troncon");
            final int nbRoadElements = roadList.getLength();
            System.out.println( "nbRoad :" + nbRoadElements);

            /*
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
            System.err.println("NumberFormatException: " + e.getMessage());
        }
        return nodes;
    }
}
