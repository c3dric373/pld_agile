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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class ApplicationManagerImpl implements ApplicationManager, UndoHandler {

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
     * List of Tours and Graphs to make undo possible.
     */
    private List<GenData> undoList;

    /**
     * Visitor to handle undo's. To distinguish between graphs and tours in
     * the list.
     */
    private GenDataVisitor undoVisitor;

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
        undoList = new ArrayList<>();
        undoVisitor = new UndoVisitor(this);

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
                projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.TOUR_CALCULATED) {
            throw new IllegalStateException("Map not loaded");
        }
        Validate.notNull(file, "file is null");
        final Tour tour = xmlToGraph.getDeliveriesFromXml(file.getPath());

        undoList.add(projectDataWrapper.getProject().getGraph());
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
        undoList.add(tour.deepClone());
        final Graph graph = projectDataWrapper.getProject().getGraph();
        final Tour newTour = graphService.calculateTour(tour, graph);
        updateInfo(newTour);
        projectDataWrapper.modifyTour(newTour);
        setTourCalculated();
        mainProjectState = ProjectState.TOUR_CALCULATED;

    }

    @Override
    public void addDeliveryProcess(final Tour tour,
                                   final ActionPoint pickUpPoint,
                                   final ActionPoint deliveryPoint) {
        if (projectState != ProjectState.TOUR_LOADED
                && projectState != ProjectState.TOUR_CALCULATED) {
            throw new IllegalStateException("Another action is in progress");
        }
        Validate.notNull(tour, "tour null");
        Validate.notNull(pickUpPoint, "pickUpPoint null");
        Validate.notNull(deliveryPoint, "deliveryPoint null");
        undoList.add(tour.deepClone());
        final Tour newTour;
        if (projectState == ProjectState.TOUR_LOADED) {
            setAddDeliveryProcess();
            DeliveryProcess deliveryProcess = new DeliveryProcess(pickUpPoint,
                    deliveryPoint);
            newTour = TourService.addDpTourNotCalculated(tour, deliveryProcess);
            DeliveryProcessService.addDeliveryProcessIdTourNotCalc(tour, deliveryProcess);
        } else {
            setAddDeliveryProcess();
            Graph graph = projectDataWrapper.getProject().getGraph();
            newTour = TourService.addNewDeliveryProcess(graph, tour, pickUpPoint,
                    deliveryPoint);
            updateInfo(newTour);
        }
        projectDataWrapper.modifyTour(newTour);
        projectState = mainProjectState;
    }

    @Override
    public void undoTour(final Tour tour) {
        System.out.println("test1");
        if (tour.getJourneyList() == null) {
            System.out.println("test2");
            projectState = ProjectState.TOUR_LOADED;
            projectDataWrapper.getProject().setTour(tour);
            projectDataWrapper.loadTour(tour);
        } else {
            projectState = ProjectState.TOUR_CALCULATED;
            projectDataWrapper.getProject().setTour(tour);
            projectDataWrapper.modifyTour(tour);
        }

    }

    @Override
    public void undoGraph(Graph graph) {

    }

    @Override
    public void deleteDeliveryProcess(final DeliveryProcess deliveryProcess) {
        if (projectState != ProjectState.TOUR_LOADED
                && projectState != ProjectState.TOUR_CALCULATED) {
            throw new IllegalStateException("Another action is in progress");
        }
        if (deliveryProcess.getPickUP().getActionType() == ActionType.BASE) {
            //TODO
            // projectDataWrapper.sendErrorMessage
            return;
        }
        Validate.notNull(deliveryProcess, "deliveryProcess null");
        Tour newTour;
        final Tour tour = projectDataWrapper.getProject().getTour();
        Tour clone = tour.deepClone();
        undoList.add(clone);
        System.out.println(clone.getActionPoints().size());
        if (projectState == ProjectState.TOUR_LOADED) {
            setDeleteDeliveryProcess();
            newTour = TourService.deleteDpTourNotCalculated(tour, deliveryProcess);
            DeliveryProcessService.delDeliveryProcessIdTourNotCalc(newTour);
            projectDataWrapper.loadTour(newTour);
        } else {
            setDeleteDeliveryProcess();
            final Graph graph = projectDataWrapper.getProject().getGraph();
            newTour = TourService.deleteDeliveryProcess(graph, tour, deliveryProcess);
            updateInfo(newTour);
            projectDataWrapper.loadTour(newTour);
        }
        projectState = mainProjectState;
        System.out.println(clone);
    }

    private void updateInfo(final Tour newTour) {
        DeliveryProcessService.setDpInfo(newTour);
        TourService.calculateTimeAtPoint(newTour);
        int completeDistance = TourService.getCompleteDistance(newTour);
        Time completeTime = TourService.getCompleteTime(newTour);
        newTour.setCompleteTime(completeTime);
        newTour.setTotalDistance(completeDistance);
        DeliveryProcessService.resetDeliveryProcessIdTourCalculated(newTour);
    }

    @Override
    public void changeDeliveryOrder(final List<ActionPoint> actionPoints) {
        if (projectState != ProjectState.TOUR_CALCULATED) {
            throw new IllegalStateException("Another action is in progress");
        }
        setChangeDeliveryOrder();
        Validate.notNull(actionPoints, "actionPoints null");
        Validate.notEmpty(actionPoints, "actionPointsEmpty");
        final Tour tour = projectDataWrapper.getProject().getTour();
        final Graph graph = projectDataWrapper.getProject().getGraph();
        final Tour newTour = tourService.changeDeliveryOrder(graph, tour,
                actionPoints);
        updateInfo(newTour);
        projectDataWrapper.modifyTour(newTour);
        undoList.add(tour.deepClone());
        projectState = mainProjectState;

    }

    @Override
    public void changePointPosition(final ActionPoint oldPoint, final Point newPoint) {
        if (projectState != ProjectState.TOUR_LOADED
                && projectState != ProjectState.TOUR_CALCULATED) {
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
        DeliveryProcessService.setDpInfo(newTour);
        TourService.calculateTimeAtPoint(newTour);
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
        Validate.notNull(actionType, "actionType is null");
        Validate.notNull(actionTime, "actionTime is null");
        final Graph graph =
                projectDataWrapper.getProject().getGraph();
        final Point nearestPoint = GraphService.findNearestPoint(graph,
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

    @Override
    public void getJourneyList(List<Journey> journeyList, DeliveryProcess deliveryProcess) {
        if (projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.TOUR_CALCULATED) {
            throw new IllegalStateException("Another action is in progress");
        }
        List<Journey> listJourneyFromDeliveryProcess = graphService.getJourneysForDeliveryProcess(journeyList, deliveryProcess);
        projectDataWrapper.getJourneyList(listJourneyFromDeliveryProcess);
    }

    @Override
    public void undo() {
        if (undoList.isEmpty()) {
            return;
        } else {
            final GenData genData = undoList.remove(undoList.size() - 1);
            genData.accept(undoVisitor);
        }
    }
    public void setMapLoaded(){
        if(projectState != ProjectState.INITIALIZED &&
                projectState != ProjectState.MAP_LOADED){
            sendMessage(ErrorMessage.ANOTHER_ACTION_IN_PROGRESS);
        } else {
            projectState = ProjectState.MAP_LOADED;
        }
    }

    public void setTourLoaded() {
        if (projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.MAP_LOADED) {
            sendMessage(ErrorMessage.ANOTHER_ACTION_IN_PROGRESS);
        } else {
            projectState = ProjectState.TOUR_LOADED;
        }
    }

    public void setTourCalculated() {
        if (projectState != ProjectState.TOUR_LOADED &&
                projectState != projectState.TOUR_CALCULATED) {
            sendMessage(ErrorMessage.ANOTHER_ACTION_IN_PROGRESS);
        } else {
            projectState = projectState.TOUR_CALCULATED;
        }
    }

    public void setAddDeliveryProcess() {
        if (projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.TOUR_CALCULATED) {
            sendMessage(ErrorMessage.ANOTHER_ACTION_IN_PROGRESS);
        } else {
            projectState = ProjectState.ADD_DELIVERY_PROCESS;
        }
    }

    public void setDeleteDeliveryProcess() {
        if (projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.TOUR_CALCULATED) {
            sendMessage(ErrorMessage.ANOTHER_ACTION_IN_PROGRESS);
        } else {
            projectState = ProjectState.DELETE_DELIVERY_PROCESS;
        }
    }

    public void setModifyDeliveryProcessPoint() {
        if (projectState != ProjectState.TOUR_LOADED &&
                projectState != ProjectState.TOUR_CALCULATED) {
            sendMessage(ErrorMessage.ANOTHER_ACTION_IN_PROGRESS);
        } else {
            projectState = ProjectState.MODIFY_DELIVERY_PROCESS_POINT;
        }
    }

    public void setChangeDeliveryOrder() {
        if (projectState != ProjectState.TOUR_CALCULATED) {
            sendMessage(ErrorMessage.ANOTHER_ACTION_IN_PROGRESS);
        } else {
            projectState = ProjectState.CHANGE_DELIVERY_ORDER;
        }
    }

    public static void sendMessage(ErrorMessage message){
        projectDataWrapper.sendErrorMessage(message);
    }

}
