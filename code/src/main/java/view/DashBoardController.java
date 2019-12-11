package view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.directions.*;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import lombok.Getter;
import model.core.service.JourneyService;
import model.core.service.TourService;
import model.data.*;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {

    //Map Style.
    private static final String mapStyle = "[{\"featureType\":\"administrative.neighborhood\",\"elementType\":\"all\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"poi\",\"elementType\":\"all\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"poi\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#747474\"},{\"lightness\":\"23\"}]},{\"featureType\":\"poi.attraction\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#f38eb0\"}]},{\"featureType\":\"poi.government\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#ced7db\"}]},{\"featureType\":\"poi.medical\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#ffa5a8\"}]},{\"featureType\":\"poi.park\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#c7e5c8\"}]},{\"featureType\":\"poi.place_of_worship\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#d6cbc7\"}]},{\"featureType\":\"poi.school\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#c4c9e8\"}]},{\"featureType\":\"poi.sports_complex\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#b1eaf1\"}]},{\"featureType\":\"road\",\"elementType\":\"all\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#343434\"}]},{\"featureType\":\"road\",\"elementType\":\"geometry\",\"stylers\":[{\"lightness\":\"100\"}]},{\"featureType\":\"road\",\"elementType\":\"labels\",\"stylers\":[{\"visibility\":\"off\"},{\"lightness\":\"100\"}]},{\"featureType\":\"road\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#8a8a8a\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#ffd4a5\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#6e6e6e\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#ffe9d2\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#6e6969\"}]},{\"featureType\":\"road.local\",\"elementType\":\"all\",\"stylers\":[{\"visibility\":\"simplified\"}]},{\"featureType\":\"road.local\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"weight\":\"3.00\"}]},{\"featureType\":\"road.local\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"weight\":\"0.30\"}]},{\"featureType\":\"road.local\",\"elementType\":\"labels.text\",\"stylers\":[{\"visibility\":\"on\"}]},{\"featureType\":\"road.local\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#464646\"},{\"lightness\":\"36\"}]},{\"featureType\":\"road.local\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"color\":\"#e9e5dc\"},{\"lightness\":\"30\"}]},{\"featureType\":\"transit\",\"elementType\":\"all\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"transit.line\",\"elementType\":\"geometry\",\"stylers\":[{\"visibility\":\"off\"},{\"lightness\":\"100\"}]},{\"featureType\":\"water\",\"elementType\":\"all\",\"stylers\":[{\"color\":\"#d2e7f7\"}]}]";

    private Tour tourLoaded;

    public void setTour(Tour tour) {
        tourLoaded = tour;

    }

    public void setActionPoints(final Tour tour) {
        actionPoints.remove(0, actionPoints.size());
        actionPoints.addAll(tour.getActionPoints());

    }


    //Enum Marker Types.
    @Getter
    public enum MarkerType {
        PICKUP("Pick-Up Point", "P", "flag.png"),
        DELIVERY("Delivery Point", "D", "flag.png"),
        BASE("Base Point", "B", "home-icon-silhouette.png");

        private String title = "";
        private String firstLetter = "";
        private String iconPath = "";

        MarkerType(String title, String firstLetter, String iconPath) {
            this.title = title;
            this.firstLetter = firstLetter;
            this.iconPath = iconPath;
        }
    }

    // Reference to the main application
    private UserInterface mainApp;

    // List des ActionPoints en Observable pour la view
    private ObservableList<ActionPoint> actionPoints = FXCollections.observableArrayList();


    // Direction Management
    protected DirectionsService directionsService;
    protected DirectionsPane directionsPane;


    @FXML
    private TableView<ActionPoint> actionPointTableView;

    @FXML
    private TableColumn<ActionPoint, String> deliveryRank;

    @FXML
    private TableColumn<ActionPoint, String> deliveryType;

    @FXML
    private TableColumn<ActionPoint, String> timeAtPoint;


    @FXML
    private Label baseLocation;

    @FXML
    private Label dpNumber;

    @FXML
    private Label dpDuration;

    @FXML
    private Label dPDistance;

    @FXML
    private Label dPPuPoint;

    @FXML
    private Label dPDPoint;

    @FXML
    private Label dpPUDuration;

    @FXML
    private Label dpDDuration;


    @FXML
    private GoogleMapView mapView;

    private GoogleMap map;

    public DashBoardController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Initialize the actionPoints table with the 3 columns.
        actionPointTableView.setItems(null);

        deliveryRank.setCellValueFactory(cellData -> new SimpleStringProperty
                (String.valueOf(actionPoints.indexOf(cellData.getValue()))));
        deliveryType.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getActionType().toString()));
        timeAtPoint.setCellValueFactory(
                cellData -> new SimpleStringProperty(TourService.calculateTimeAtPoint(tourLoaded, cellData.getValue())));
        actionPointTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mainApp.showDeliveryProcess(newValue, tourLoaded));

        mapView.addMapInializedListener(this);
        mapView.setKey("AIzaSyDJDcPFKsYMTHWJUxVzoP0W7ERsx3Bhdgc");
    }

    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        //TODO Create a graph center and pass it to display Map;
        mapOptions.center(new LatLong(45.771606, 4.880959))
                .styleString(mapStyle)
                .overviewMapControl(false)
                .mapType(MapTypeIdEnum.ROADMAP)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);

        /*
        InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
        infoWindowOptions.content("<h2>Fred Wilkie</h2>"
                + "Current Location: Safeway<br>"
                + "ETA: 45 minutes" );

        InfoWindow bobUnderwoodWindow = new InfoWindow(infoWindowOptions);
        bobUnderwoodWindow.open(map, bobUnderwoodMarker);
        map.addMarker( bobUnderwoodMarker );
         */

        map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
            LatLong latLong = event.getLatLong();
            System.out.println(latLong.getLatitude() + "coucou" + latLong.getLongitude());
        });

        //Set Direction Service
        directionsService = new DirectionsService();
        directionsPane = mapView.getDirec();
    }


    public void showDeliveryProcess(final DeliveryProcess deliveryProcess) {
        final String pickUpDuration = deliveryProcess.getPickUP().getTime().toString();
        final String deliveryDuration = deliveryProcess.getDelivery().getTime().toString();
        final String pickUpPointName = deliveryProcess.getPickUP().getLocation().getSegments().get(0).getName();
        final String deliveryPointName = deliveryProcess.getDelivery().getLocation().getSegments().get(0).getName();

        dPPuPoint.setText(pickUpPointName);
        dPDPoint.setText(deliveryPointName);
        dpDDuration.setText(deliveryDuration);
        dpPUDuration.setText(pickUpDuration);
        if(deliveryProcess.getTime() != null){
            dpDuration.setText(deliveryProcess.getTime().toString());
        }
        if (deliveryProcess.getDistance() != null){
            dPDistance.setText(String.valueOf(deliveryProcess.getDistance()));

        }


    }

    public void calculateTour() {
        this.mainApp.calculateTour();
    }

    public void handleLoadMap() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Map XML");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        final File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            if (selectedFile.getName().contains("xml")) {
                System.out.println("File selected: " + selectedFile.getName());
                this.mainApp.loadMap(selectedFile);
            } else {
                System.out.println("Error Loading not xml File");
            }
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    public void handleLoadTour() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Tour XML");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            if (selectedFile.getName().contains("xml")) {
                System.out.println("File selected: " + selectedFile.getName());
                this.mainApp.loadDeliveryRequest(selectedFile);
            } else {
                System.out.println("Error Loading not xml File");
            }
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    public void traceDirection() {
        drawPolyline(getMCVPathFormJourney(0), "red");
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(final UserInterface mainApp) {
        Validate.notNull(mainApp);
        this.mainApp = mainApp;
    }

    public void displayMap() {
        //  Set new center for the map
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(45.771606, 4.880959))
                .styleString(mapStyle)
                .overviewMapControl(false)
                .mapType(MapTypeIdEnum.ROADMAP)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);

        // Add map to the view
        map = mapView.createMap(mapOptions);
        directionsService = new DirectionsService();
        directionsPane = mapView.getDirec();
    }

    public void displayLoadedDeliveryProcess() {
        //TODO Commenter

        // Set The Base ID on the view
        baseLocation.setText(String.valueOf(tourLoaded.getBase().getId()));

        // Create a fake list of action Points To display.
        List<ActionPoint> fakeListActionPoints = createFakeActionPointList(tourLoaded.getDeliveryProcesses());

        actionPoints.addAll(fakeListActionPoints);

        // Add observable list data to the table
        actionPointTableView.setItems(actionPoints);
        drawAllActionPoints(fakeListActionPoints);
    }

    public List<ActionPoint> createFakeActionPointList(final List<DeliveryProcess> listDeliveryProcess) {
        List<ActionPoint> listActionPoints = new ArrayList<ActionPoint>();
        for (DeliveryProcess deliveryProcess : listDeliveryProcess) {
            listActionPoints.add(new ActionPoint(deliveryProcess.getPickUP()
                    .getTime(), deliveryProcess.getPickUP().getLocation(),
                    deliveryProcess.getPickUP().getActionType()));
            listActionPoints.add(new ActionPoint(deliveryProcess.getDelivery().getTime(), deliveryProcess.getDelivery().getLocation(), deliveryProcess.getDelivery().getActionType()));
        }
        return listActionPoints;
    }

    public void drawAllActionPoints(final List<ActionPoint> actionPoints) {
        // First Action Point is the Base
        map.clearMarkers();
        map.addMarker(createMarker(actionPoints.get(0), MarkerType.BASE));

        //TODO A améliorer Mettre en accord ActionType et Marker Type ?
        //According to ActionType set the good MarkerType
        for (ActionPoint actionPoint : actionPoints) {
            if (actionPoint.getActionType() == ActionType.DELIVERY) {
                map.addMarker(createMarker(actionPoint, MarkerType.DELIVERY));
            } else if (actionPoint.getActionType() == ActionType.PICK_UP) {
                map.addMarker(createMarker(actionPoint, MarkerType.PICKUP));
            }
        }

    }

    Marker createMarker(final ActionPoint actionPoints, final MarkerType mType) {
        /* //TODO Marche pas icon
        String path= MarkerImageFactory.createMarkerImage(mType.iconPath, "png");
        path = path.replace("(", "");
        path = path.replace(")", "");
         */

        MarkerOptions markerPoint = new MarkerOptions();
        //markerPoint.icon();
        markerPoint.title(mType.title)
                .label(mType.firstLetter)
                .position(new LatLong(actionPoints.getLocation().getLatitude(), actionPoints.getLocation().getLongitude()));
        return new Marker(markerPoint);
    }

    private void clearDirections() {
        new DirectionsRenderer(true, mapView.getMap(), directionsPane).clearDirections();
    }

    @Override
    public void directionsReceived(DirectionsResult results, DirectionStatus status) {
    }


    DirectionsWaypoint[] getDirectionWayPointsFromJourney(final Journey journey) {
        // Reverse List and Delete First and last elements, Start and End
        LinkedList<Point> newPointsList = new LinkedList<Point>();
        for (Point point : journey.getPoints()) {
            newPointsList.addFirst(point);
        }
        newPointsList.remove(0);
        newPointsList.remove(newPointsList.getLast());

        for (int i = 2; newPointsList.size() > 23; i += newPointsList.size() % 23) {
            newPointsList.remove(i);
        }

        // Creating a Direction Waypoints List from point list
        LinkedList<DirectionsWaypoint> path = new LinkedList<DirectionsWaypoint>();
        for (Point point : newPointsList) {
            DirectionsWaypoint stop = new DirectionsWaypoint(point.toString());
            stop.setStopOver(false);
            path.addFirst(stop);
        }

        return path.toArray(new DirectionsWaypoint[path.size()]);
    }

    public DirectionsWaypoint[] getDirectionWayPointsFromTour() {
        LinkedList<DirectionsWaypoint> path = new LinkedList<DirectionsWaypoint>();
        // On ajoute tous les points a la liste de points dans l'ordre inversse.
        for (Journey journey : tourLoaded.getJourneyList()) {
            for (Point point : journey.getPoints()) {
                DirectionsWaypoint stop = new DirectionsWaypoint(point.toString());
                stop.setStopOver(false);
                path.addFirst(stop);
            }
            path.remove(path.getLast());
        }
        path.remove(0);
        path.remove(path.getLast());
        return path.toArray(new DirectionsWaypoint[path.size()]);
    }

    void drawDirection(Point start, Point arrival, DirectionsWaypoint[] directionsWaypoints, Boolean clearMarkers, Boolean clearDirections) {
        // Clear all markers
        if (clearMarkers) map.clearMarkers();
        // Clear Past direction
        if (clearDirections) clearDirections();

        DirectionsRequest request = new DirectionsRequest(
                start.toString(),
                arrival.toString(),
                TravelModes.WALKING,
                directionsWaypoints);
        directionsService.getRoute(request, this, new DirectionsRenderer(false, mapView.getMap(), directionsPane));
    }

    void drawFullTour() {
        map.clearMarkers();
        //drawDirection(tourLoaded.getBase(),tourLoaded.getBase(), getDirectionWayPointsFromTour(),true,true);
        drawPolyline(getMCVPathFormTour(), "blue");
        drawAllActionPoints(tourLoaded.getActionPoints());
    }

    public void drawDeliveryProcess(int id) {
        Journey journey = tourLoaded.getJourneyList().get(id);
        DeliveryProcess deliveryProcess = tourLoaded.getDeliveryProcesses().get(id);
        drawDirection(deliveryProcess.getPickUP().getLocation(), deliveryProcess.getDelivery().getLocation(), getDirectionWayPointsFromJourney(tourLoaded.getJourneyList().get(id)), true, true);
        map.addMarker(createMarker(deliveryProcess.getPickUP(), MarkerType.PICKUP));
        map.addMarker(createMarker(deliveryProcess.getDelivery(), MarkerType.DELIVERY));
    }

    void drawPolyline(final MVCArray mvcArray, String color) {
        PolylineOptions polyOpts = new PolylineOptions()
                .path(mvcArray)
                .strokeColor(color)
                .clickable(false)
                .strokeOpacity(0.6)
                .strokeWeight(4);
        Polyline poly = new Polyline(polyOpts);
        map.addMapShape(poly);
    }

    MVCArray getMCVPathFormJourney(final int id) {
        Journey journey = tourLoaded.getJourneyList().get(id);
        // Reverse List
        LinkedList<Point> newPointsList = new LinkedList<Point>();
        for (Point point : journey.getPoints()) {
            newPointsList.addFirst(point);
        }

        LatLong[] ary = new LatLong[newPointsList.size()];
        int i = 0;
        for (Point point : newPointsList) {
            LatLong latLong = new LatLong(point.getLatitude(), point.getLongitude());
            ary[i++] = latLong;
        }
        MVCArray mvc = new MVCArray(ary);
        return mvc;
    }

    public MVCArray getMCVPathFormTour() {

        int count = 0;
        LinkedList<Point> fullListOfPoints = new LinkedList<Point>();
        for (Journey journey : tourLoaded.getJourneyList()) {
            // Reverse List
            LinkedList<Point> newPointsList = new LinkedList<Point>();
            for (Point point : journey.getPoints()) {
                newPointsList.addFirst(point);
            }
            fullListOfPoints.addAll(newPointsList);
        }
        LatLong[] ary = new LatLong[fullListOfPoints.size()];
        int i = 0;
        for (Point point : fullListOfPoints) {
            LatLong latLong = new LatLong(point.getLatitude(), point.getLongitude());
            ary[i++] = latLong;
        }
        MVCArray mvc = new MVCArray(ary);
        return mvc;
    }
}
