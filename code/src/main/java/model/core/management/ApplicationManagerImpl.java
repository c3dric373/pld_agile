package model.core.management;

import model.core.service.GraphService;
import model.core.service.JourneyService;
import model.core.service.TourService;
import model.data.*;
import model.io.XmlToGraph;
import org.apache.commons.lang.Validate;
import view.Observer;
import view.UserInterface;

import java.io.File;
import java.util.List;

public class ApplicationManagerImpl implements ApplicationManager {

    /**
     * Xml Converter for Project.
     */
    private XmlToGraph xmlToGraph;

    /**
     * Project Data wrapper for project.
     */
    private ProjectDataWrapper projectDataWrapper;

    /**
     * State of the Project.
     */
    private ProjectState projectState;

    /**
     * Graph Service of the Project.
     */
    private GraphService graphService;

    /**
     * JourneyService of the Project.
     */
    private JourneyService journeyService;

    /**
     * TourService of the Project.
     */

    private TourService tourService;


    ApplicationManagerImpl(){
    xmlToGraph = new XmlToGraph();
    projectState = ProjectState.INITIALIZED;
    journeyService = new JourneyService();
    graphService = new GraphService();
    tourService = new TourService();
    //projectDataWrapper = new ProjectDataWrapperImpl(newObserver);

    }

    @Override
    public void loadMap(final File file) {
        // TODO when graph takes nodes as input
        // List<Point> graph =  xmlToGraph.getGraphFromXml(file.getPath());
        final Graph graph = new Graph();
        projectDataWrapper.loadMap(graph);
    }

    @Override
    public void setObserver(final UserInterface userInterface) {

    }

    @Override
    public void loadTour(final File file) {
        // TODO when graph takes nodes as input
        Tour tour = xmlToGraph.getDeliveriesFromXml(file.getPath());
        projectDataWrapper.loadTour(tour);
    }

    @Override
    public void calculateTour() {
        Tour tour = projectData.getProject().getTour();
        projectDataWrapper.modifyTour(tour);
    }

    @Override
    public void addDeliveryProcess(final DeliveryProcess deliveryProcess) {
        projectDataWrapper.addDeliveryProcess(deliveryProcess);
    }

    @Override
    public void deleteDeliveryProcess(final DeliveryProcess deliveryProcess) {
        projectDataWrapper.deleteDeliveryProcess(deliveryProcess);
    }

    @Override
    public void changeDeliveryOrder(final List<ActionPoint> actionPoints) {
        if (projectState != ProjectState.TOUR_CALCULATED){
            throw new IllegalStateException("Tour not calculated");
        }
        Validate.notNull(actionPoints,"actionPoints null");
        Validate.notEmpty(actionPoints,"actionPointsEmpty");
        final Tour tour = projectDataWrapper.getProject().getTour();
        final Tour newTour = tourService.changeDeliverOrder(tour, actionPoints);
        projectDataWrapper.modifyTour(newTour);


    }

    @Override
    public void changePointPosition(final Point oldPoint, final Point newPoint) {

    }

    @Override
    public void findNearestPoint(final double latitude, final double longitude) {

    }


}
