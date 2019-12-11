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
     * Main State of the Project
     */
    private ProjectState mainProjectState;

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
        if (projectState != ProjectState.INITIALIZED  &&
                projectState != ProjectState.MAP_LOADED) {
            throw new IllegalStateException("Application not opened");
        }
        Validate.notNull(file, "file is null");

        List<Point> points =  xmlToGraph.getGraphFromXml(file.getPath());
        final Graph graph = new Graph(points);
        projectDataWrapper.loadMap(graph);
        projectState = ProjectState.MAP_LOADED;
        mainProjectState = ProjectState.MAP_LOADED;
    }

    @Override
    public void setObserver(final UserInterface userInterface) {
        projectDataWrapper.addObserver(userInterface);
    }

    @Override
    public void loadTour(final File file) {
        if (projectState != ProjectState.MAP_LOADED &&
                projectState != ProjectState.TOUR_LOADED) {
            throw new IllegalStateException("Map not loaded");
        }
        Validate.notNull(file, "file is null");
        final Tour tour = xmlToGraph.getDeliveriesFromXml(file.getPath());
        projectDataWrapper.loadTour(tour);
        projectState = ProjectState.TOUR_LOADED;
        mainProjectState = ProjectState.TOUR_LOADED;

    }

    @Override
    public void calculateTour() {
        if (projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.TOUR_CALCULATED) {
            throw new IllegalStateException("tour not loaded");
        }
        final Tour tour = projectDataWrapper.getProject().getTour();
        final Graph graph = projectDataWrapper.getProject().getGraph();
        final Tour newTour = graphService.calculateTour(tour, graph);
        projectDataWrapper.modifyTour(newTour);
        projectState = ProjectState.TOUR_CALCULATED;
        mainProjectState = ProjectState.TOUR_CALCULATED;

    }

    @Override
    public void addDeliveryProcess(final Tour tour,
                                   final ActionPoint pickUpPoint,
                                   final ActionPoint deliveryPoint) {
        if(projectState != projectState.ADD_DELIVERY_PROCESS_2ndPoint)
        {
            throw new IllegalStateException("Another action is in progress");
        }
        Validate.notNull(tour, "tour null");
        Validate.notNull(pickUpPoint, "pickUpPoint null");
        Validate.notNull(deliveryPoint, "deliveryPoint null");
        final Tour newTour;
        newTour = tourService.addNewDeliveryProcess(tour, pickUpPoint,
                deliveryPoint);
        projectDataWrapper.modifyTour(newTour);
        projectState = mainProjectState;
    }

    @Override
    public void deleteDeliveryProcess(final DeliveryProcess deliveryProcess) {
        if(projectState != projectState.DELETE_DELIVERY_PROCESS)
        {
            throw new IllegalStateException("Another action is in progress");
        }
        //TODO : review this method
        Validate.notNull(deliveryProcess, "deliveryProcess null");
        projectDataWrapper.deleteDeliveryProcess(deliveryProcess);
        projectState = mainProjectState;
    }

    @Override
    public void changeDeliveryOrder(final List<ActionPoint> actionPoints) {
        if(projectState != ProjectState.CHANGE_DELIVERY_ORDER)
        {
            throw new IllegalStateException("Another action is in progress");
        }
        Validate.notNull(actionPoints, "actionPoints null");
        Validate.notEmpty(actionPoints, "actionPointsEmpty");
        final Tour tour = projectDataWrapper.getProject().getTour();
        final Graph graph = projectDataWrapper.getProject().getGraph();
        final Tour newTour = tourService.changeDeliveryOrder(graph, tour,
                actionPoints);
        projectDataWrapper.modifyTour(newTour);
        projectState = mainProjectState;

    }

    @Override
    public void changePointPosition(final ActionPoint oldPoint, final Point newPoint) {
        if (projectState != ProjectState.MODIFY_DELIVERY_PROCESS_POINT_END)
        {
            throw new IllegalStateException("Another action is in progress");
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

        projectState = mainProjectState;
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

    public void setMAP_LOADED(){
        if(projectState != ProjectState.INITIALIZED &&
                projectState != ProjectState.MAP_LOADED){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.MAP_LOADED;
    }

    public void setTOUR_LOADED(){
        if(projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.MAP_LOADED){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.TOUR_LOADED;
    }

    public void setTOUR_CALCULATED(){
        if(projectState != ProjectState.TOUR_LOADED &&
                projectState != projectState.TOUR_CALCULATED){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = projectState.TOUR_CALCULATED;
    }

    public void setADD_DELIVERY_PROCESS() {
        if(projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.TOUR_CALCULATED){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.ADD_DELIVERY_PROCESS;
    }

    public void setADD_DELIVERY_PROCESS_1stPoint(){
        if(projectState != ProjectState.ADD_DELIVERY_PROCESS){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.ADD_DELIVERY_PROCESS_1stPoint;
    }

    public void setADD_DELIVERY_PROCESS_2ndPoint(){
        if(projectState != ProjectState.ADD_DELIVERY_PROCESS_1stPoint){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.ADD_DELIVERY_PROCESS_2ndPoint;
    }

    public void setDELETE_DELIVERY_PROCESS(){
        if(projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.TOUR_CALCULATED){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.DELETE_DELIVERY_PROCESS;
    }

    public void setMODIFY_DELIVERY_PROCESS_POINT(){
        if(projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.TOUR_CALCULATED){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.MODIFY_DELIVERY_PROCESS_POINT;
    }

    public void setMODIFY_DELIVERY_PROCESS_POINT_END(){
        if(projectState != ProjectState.MODIFY_DELIVERY_PROCESS_POINT){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.MODIFY_DELIVERY_PROCESS_POINT_END;
    }

    public void setCHANGE_DELIVERY_ORDER(){
        if(projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.TOUR_CALCULATED){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.CHANGE_DELIVERY_ORDER;
    }



}
