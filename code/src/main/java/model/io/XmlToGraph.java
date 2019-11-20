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

    static ArrayList<Point> nodes;
    public static void main(final String[] args) {
        /*
         * Etape 1 : récupération d'une instance de la classe "DocumentBuilderFactory"
         */
        nodes=new ArrayList<Point>();
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            /*
             * Etape 2 : création d'un parseur
             */
            final DocumentBuilder builder = factory.newDocumentBuilder();

            /*
             * Etape 3 : création d'un Document
             */
            final Document document = builder.parse(new File("C:\\Users\\colap\\Documents\\4IF\\pld_agile\\fichiersXML2019\\petitPlan.xml"));


            /*
             * Etape 4 : récupération de l'Element racine
             */
            final Element racine = document.getDocumentElement();

            //Affichage de l'élément racine
            System.out.println("\n*************RACINE************");
            System.out.println(racine.getNodeName());

            /*
             * Etape 5 : récupération des personnes
             */
            final NodeList racineNoeuds = racine.getChildNodes();
            final int nbRacineNoeuds = racineNoeuds.getLength();
            System.out.println(nbRacineNoeuds);


            /*
             * Etape 7 : récupération des numéros de téléphone
             */
            final NodeList nodeList = racine.getElementsByTagName("noeud");
            final int nbNodeElements = nodeList.getLength();
            System.out.println(nbNodeElements);

            for (int nodeIndex = 0; nodeIndex < nbNodeElements; nodeIndex++) {
                final Element node = (Element) nodeList.item(nodeIndex);
                int nodeId = Integer.parseInt(node.getAttribute("id"));
                float nodeLat = Float.parseFloat(node.getAttribute("latitude"));
                float nodeLong = Float.parseFloat(node.getAttribute("longitude"));
                Point point = new Point (nodeId, nodeLat, nodeLong);
                nodes.add(point);
            }

            final NodeList roadList = racine.getElementsByTagName("troncon");
            final int nbRoadElements = roadList.getLength();
            //System.out.println( "nbRoad :" + nbRoadElements);
            for (int segmentIndex = 0; segmentIndex < nbRoadElements; segmentIndex++) {
                final Element road = (Element) roadList.item(segmentIndex);
                int roadArrival = Integer.parseInt(road.getAttribute("destination"));
                float roadLength = Float.parseFloat(road.getAttribute("longueur"));
                String roadName = road.getAttribute("nom");
                int roadDeparture = Integer.parseInt(road.getAttribute("origine"));
                Segment segment = new Segment(roadDeparture, roadArrival, roadLength, roadName);
                //System.out.println("troncon : destination : " + road.getAttribute("destination") + "  longueur : "+ road.getAttribute("longueur") );
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
