package model.core.management;

import model.core.service.GraphService;
import model.core.service.JourneyService;
import model.core.service.TourService;
import model.data.*;
import model.io.XmlToGraph;
import org.apache.commons.lang.Validate;
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

    /**
     * Instantiates an Application Manager.
     */
    ApplicationManagerImpl() {
        xmlToGraph = new XmlToGraph();
        projectState = ProjectState.INITIALIZED;
        journeyService = new JourneyService();
        graphService = new GraphService();
        tourService = new TourService();
        projectDataWrapper = new ProjectDataWrapperImpl();

    }

    @Override
    public void loadMap(final File file) {
        if (projectState != ProjectState.INITIALIZED) {
            throw new IllegalStateException("Application not opened");
        }
        Validate.notNull(file, "file is null");

        List<Point> points =  xmlToGraph.getGraphFromXml(file.getPath());
        final Graph graph = new Graph(points);
        projectDataWrapper.loadMap(graph);
        projectState = ProjectState.MAP_LOADED;
    }

    @Override
    public void setObserver(final UserInterface userInterface) {
        projectDataWrapper.addObserver(userInterface);
    }

    @Override
    public void loadTour(final File file) {
        if (projectState != ProjectState.MAP_LOADED) {
            throw new IllegalStateException("Map not loaded");
        }
        Validate.notNull(file, "file is null");
        final Tour tour = xmlToGraph.getDeliveriesFromXml(file.getPath());
        projectDataWrapper.loadTour(tour);
        projectState = ProjectState.TOUR_LOADED;
    }

    @Override
    public void calculateTour() {
        if (projectState != ProjectState.TOUR_LOADED) {
            throw new IllegalStateException("tour not loaded");
        }
        final Tour tour = projectDataWrapper.getProject().getTour();
        final Tour newTour = graphService.calculateTour(tour);
        projectDataWrapper.modifyTour(newTour);
        projectState = ProjectState.TOUR_CALCULATED;
    }

    @Override
    public void addDeliveryProcess(final DeliveryProcess deliveryProcess) {
        if (projectState != ProjectState.TOUR_LOADED) {
            throw new IllegalStateException("Tour not calculated");
        }
        Validate.notNull(deliveryProcess, "deliveryProcess null");
        projectDataWrapper.addDeliveryProcess(deliveryProcess);
    }

    @Override
    public void deleteDeliveryProcess(final DeliveryProcess deliveryProcess) {
        if (projectState != ProjectState.TOUR_LOADED) {
            throw new IllegalStateException("Tour not calculated");
        }
        Validate.notNull(deliveryProcess, "deliveryProcess null");
        projectDataWrapper.deleteDeliveryProcess(deliveryProcess);
    }

    @Override
    public void changeDeliveryOrder(final List<ActionPoint> actionPoints) {
        if (projectState != ProjectState.TOUR_CALCULATED) {
            throw new IllegalStateException("Tour not calculated");
        }
        Validate.notNull(actionPoints, "actionPoints null");
        Validate.notEmpty(actionPoints, "actionPointsEmpty");
        final Tour tour = projectDataWrapper.getProject().getTour();
        final Tour newTour = tourService.changeDeliveryOrder(tour,
                actionPoints);
        projectDataWrapper.modifyTour(newTour);
    }

    @Override
    public void changePointPosition(final ActionPoint oldPoint, final Point newPoint) {
        if (projectState != ProjectState.TOUR_LOADED) {
            throw new IllegalStateException("Tour not loaded");
        }
        Validate.notNull(oldPoint, "oldPoint is null");
        Validate.notNull(newPoint, "newPoint is null");
        if (!GraphService.isInMap(newPoint)) {
            throw new IllegalArgumentException("newPoint not on map");
        }

        final Tour tour = projectDataWrapper.getProject().getTour();
        final Graph graph = projectDataWrapper.getProject().getGraph();
        final Tour newTour = tourService.changePointPosition
                (graph, tour, oldPoint, newPoint);
        projectDataWrapper.modifyTour(newTour);

    }

    @Override
    public void findNearestPoint(final double latitude, final double longitude) {
        Validate.notNull(latitude, "latitude is null");
        Validate.notNull(longitude, "longitude is null");
        final List<Point> pointList =
                projectDataWrapper.getProject().getPointList();
        final Point nearestPoint = graphService.findNearestPoint(pointList,
                longitude, latitude);
        projectDataWrapper.findNearestPoint(nearestPoint);
    }


}
