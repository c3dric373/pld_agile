package model.core.management;

import model.core.service.DeliveryProcessService;
import model.core.service.GraphService;
import model.core.service.JourneyService;
import model.core.service.TourService;
import model.data.*;
import model.io.XmlToGraph;
import org.apache.commons.lang.Validate;
import view.UserInterface;

import java.io.File;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

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
     * DeliveryProcessService of the Project
     */
    private DeliveryProcessService deliveryProcessService;

    /**
     * Instantiates an Application Manager.
     */
    ApplicationManagerImpl() {
        xmlToGraph = new XmlToGraph();
        projectState = ProjectState.INITIALIZED;
        journeyService = new JourneyService();
        graphService = new GraphService();
        tourService = new TourService();
        deliveryProcessService = new DeliveryProcessService();
        projectDataWrapper = new ProjectDataWrapperImpl();

    }

    @Override
    public void loadMap(final File file) {
        if (projectState != ProjectState.INITIALIZED &&
                projectState != ProjectState.MAP_LOADED) {
            throw new IllegalStateException("Application not opened");
        }
        Validate.notNull(file, "file is null");

        List<Point> points = xmlToGraph.getGraphFromXml(file.getPath());
        final Graph graph = new Graph(points);
        projectDataWrapper.loadMap(graph);
        setMapLoaded();
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
        setTourLoaded();
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
        int completeDistance = TourService.getCompleteDistance(newTour);
        Time completeTime = TourService.getCompleteTime(newTour);
        newTour.setCompleteTime(completeTime);
        newTour.setTotalDistance(completeDistance);
        DeliveryProcessService.setDpInfo(newTour);
        TourService.calculateTimeAtPoint(newTour);
        projectDataWrapper.modifyTour(newTour);
        setTourCalculated();
        mainProjectState = ProjectState.TOUR_CALCULATED;

    }

    @Override
    public void addDeliveryProcess(final Tour tour,
                                   final ActionPoint pickUpPoint,
                                   final ActionPoint deliveryPoint) {
        if(projectState != projectState.TOUR_LOADED
                && projectState != projectState.TOUR_CALCULATED) {
            throw new IllegalStateException("Another action is in progress");
        }
        setAddDeliveryProcess();
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
        if(projectState != projectState.TOUR_LOADED
                && projectState != projectState.TOUR_CALCULATED) {
            throw new IllegalStateException("Another action is in progress");
        }
        setDeleteDeliveryProcess();
        Validate.notNull(deliveryProcess, "deliveryProcess null");
        projectDataWrapper.deleteDeliveryProcess(deliveryProcess);
        projectState = mainProjectState;
    }

    @Override
    public void changeDeliveryOrder(final List<ActionPoint> actionPoints) {
        if(projectState != ProjectState.TOUR_CALCULATED)
        {
            throw new IllegalStateException("Another action is in progress");
        }
        setChangeDeliveryOrder();
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
        if (projectState != ProjectState.TOUR_LOADED
                && projectState != ProjectState.TOUR_CALCULATED)
        {
            throw new IllegalStateException("Another action is in progress");
        }
        setModifyDeliveryProcessPoint();
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
    public void findNearestPoint(final double latitude,
                                 final double longitude,
                                 ActionType actionType,
                                 Time actionTime) {
        Validate.notNull(latitude, "latitude is null");
        Validate.notNull(longitude, "longitude is null");
        Validate.notNull(actionType,"actionType is null");
        Validate.notNull(actionTime, "actionTime is null");
        final List<Point> pointList =
                projectDataWrapper.getProject().getPointList();
        final Point nearestPoint = graphService.findNearestPoint(pointList,
                longitude, latitude);
        final ActionPoint nearestActionPoint = new ActionPoint(actionTime,
                nearestPoint, actionType);
        projectDataWrapper.findNearestPoint(nearestActionPoint);
    }

    public void getDeliveryProcess(final List<DeliveryProcess> deliveryProcesses, final ActionPoint actionPoint) {
        //Validate.isTrue(projectState == ProjectState.TOUR_CALCULATED, "tour not calculated");
        if (actionPoint == null) {
            return;
        }
        OptionalInt index = deliveryProcessService.findActionPoint(deliveryProcesses, actionPoint);
        if (index.isPresent()) {
            DeliveryProcess deliveryProcess = deliveryProcesses.get(index.getAsInt());
            projectDataWrapper.selectDeliveryProcess(deliveryProcess);
        } else {
            Tour tour = projectDataWrapper.getProject().getTour();
            Optional<DeliveryProcess> completeDp = DeliveryProcessService.createDpBase(tour);
            completeDp.ifPresent(deliveryProcess -> projectDataWrapper.selectDeliveryProcess(deliveryProcess));
        }
    }

    public void setMapLoaded(){
        if(projectState != ProjectState.INITIALIZED &&
                projectState != ProjectState.MAP_LOADED){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.MAP_LOADED;
    }

    public void setTourLoaded() {
        if (projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.MAP_LOADED) {
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.TOUR_LOADED;
    }

    public void setTourCalculated() {
        if (projectState != ProjectState.TOUR_LOADED &&
                projectState != projectState.TOUR_CALCULATED) {
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = projectState.TOUR_CALCULATED;
    }

    public void setAddDeliveryProcess(){
        if(projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.TOUR_CALCULATED){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.ADD_DELIVERY_PROCESS;
    }

    public void setDeleteDeliveryProcess(){
        if(projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.TOUR_CALCULATED){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.DELETE_DELIVERY_PROCESS;
    }

    public void setModifyDeliveryProcessPoint() {
        if (projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.TOUR_CALCULATED) {
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.MODIFY_DELIVERY_PROCESS_POINT;
    }

    public void setChangeDeliveryOrder(){
        if(projectState != ProjectState.TOUR_CALCULATED){
            throw new IllegalStateException("Another action is in progress");
        }
        projectState = ProjectState.CHANGE_DELIVERY_ORDER;
    }


}
