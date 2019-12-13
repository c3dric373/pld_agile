package view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import lombok.Getter;
import model.data.*;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Time;
import java.util.*;

public class DashBoardController implements Initializable,
        MapComponentInitializedListener {

    /**
     * Empty String.
     */
    private static final String EMPTY_STRING = "";

    /**
     * The style of our map.
     */
    private static String MAP_STYLE;

    static {
        try {
            MAP_STYLE = Files.readString(Path.of(DashBoardController.class.
                            getResource("map_style.txt").getPath()),
                    StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The {@link Polyline} used to represent the calculated {@link Tour} on
     * the map.
     */
    private Polyline poly;

    public void resetTour(){
        actionPointTableView.getItems().clear();
        startTime.setText("");
        numberDeliveries.setText("");
        arrivalTime.setText("");
        clearRectangleColor();
    }

    public void modifieDP() {
        int result = showModifieDeliveryDialog(deliveryProcessLoaded);
        int index = actionPointTableView.getSelectionModel().getFocusedIndex();
        if (result != -1) {
            List<ActionPoint> actionPoints = tourLoaded.getActionPoints();
            ActionPoint actionPoint = actionPoints.remove(index);
            actionPoints.add(result, actionPoint);
            this.mainApp.modifyOrder(actionPoints);
        }
    }

    //Enum Marker Types.
    @Getter
    public enum MarkerType {
        PICKUP("Pick-Up Point", "P", "icons/marker.png"),
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

    // Manage New DeliveryProcess
    private ActionPoint newPickUpActionPoint = null;
    private ActionPoint newDeliveryActionPoint = null;
    // Markers of new DeliveryProcess
    private Marker newPickUpPointMarker = null;
    private Marker newDeliveryPointMarker = null;

    @FXML
    private TableView<ActionPoint> actionPointTableView;

    @FXML
    private TableColumn<ActionPoint, String> deliveryRank;

    @FXML
    private TableColumn<ActionPoint, String> deliveryType;

    @FXML
    private TableColumn<ActionPoint, String> timeAtPoint;

    @FXML
    private Label labelPickUpCoordinates;

    @FXML
    private Label labelDeliveryCoordinates;

    @FXML
    public Label rectangle;

    @FXML
    private TextField inputDeliveryTimeH;

    @FXML
    private TextField inputDeliveryTimeM;

    @FXML
    private TextField inputPickUpTimeH;

    @FXML
    private TextField inputPickUpTimeM;

    @FXML
    private GoogleMapView mapView;

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
    private Label arrivalTime;

    @FXML
    private Label numberDeliveries;

    @FXML
    private Label startTime;

    // Reference to the main application
    private UserInterface mainApp;

    // List des ActionPoints en Observable pour la view
    private ObservableList<ActionPoint> actionPoints =
            FXCollections.observableArrayList();

    // Manage New DeliveryProcess
    private ActionPoint newPickUpActionPoint = null;

    private ActionPoint newDeliveryActionPoint = null;

    // Markers of new DeliveryProcess
    private Marker newPickUpPointMarker = null;

    private Marker newDeliveryPointMarker = null;

    private GoogleMap map;

    // Local Save of Tour.
    private Tour tourLoaded;

    private DeliveryProcess deliveryProcessLoaded;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Initialize the actionPoints table with the 3 columns.
        actionPointTableView.setItems(null);

        deliveryRank.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        deliveryType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getActionType().toString()));

        timeAtPoint.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassageTime()));

        actionPointTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handelTableSelection(newValue));

        mapView.addMapInializedListener(this);
        mapView.setKey("AIzaSyDJDcPFKsYMTHWJUxVzoP0W7ERsx3Bhdgc");
    }

    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        //TODO Create a graph center and pass it to display Map;
        mapOptions.center(new LatLong(45.771606, 4.880959)).styleString(MAP_STYLE).overviewMapControl(false).mapType(MapTypeIdEnum.ROADMAP).panControl(false).rotateControl(false).scaleControl(false).streetViewControl(false).zoomControl(false).zoom(13);
    }

    /**
     * Sets the loadedTour, to be able to make correct assessments of the
     * users interaction with the gui.
     *
     * @param tour the {@link} tour to be set.
     */
    public void setTour(final Tour tour) {
        tourLoaded = tour;
    }

    /**
     * Asks the user if he wants to delete a selected DeliveryProcess. If
     * confirmed the {@link UserInterface} is invoked.
     */
    public void deleteDp() {
        if (showConfirmationAlert("Are you sur you want to delete this " +
                "Delivery Process ?")) {
            this.mainApp.deleteDp(deliveryProcessLoaded);
        }
    }

    /**
     * Sets the actionPoints represented by the views. The table needs to be
     * cleaned before the new {@link ActionPoint}s can be displayed.
     *
     * @param tour the {@link} from which the {@link ActionPoint} should be
     *             displayed.
     */
    public void setActionPoints(final Tour tour) {
        actionPointTableView.getSelectionModel().clearSelection();
        actionPoints.remove(0, actionPoints.size());
        actionPoints.addAll(tour.getActionPoints());
    }

    /**
     * Interaction with the user when he desires to change the order of certain
     * ActionPoints. If the user chooses a valid number, the
     * {@link UserInterface} will be invoked.
     */
    public void modifyDeliveryOrder() {
        int result = showModifiedDeliveryDialog(deliveryProcessLoaded);
        int index = actionPointTableView.getSelectionModel().getFocusedIndex();
        if (result != -1) {
            List<ActionPoint> actionPoints = tourLoaded.getActionPoints();
            ActionPoint actionPoint = actionPoints.remove(index);
            actionPoints.add(result, actionPoint);
            this.mainApp.changeDeliveryOrder(actionPoints);
        }
    }

    /**
     * Sets the important labels on the top of the view.
     */
    private void setBigLabels() {
        numberDeliveries.setText(String.valueOf(tourLoaded.getDeliveryProcesses().size()));
        startTime.setText(tourLoaded.getStartTime().toString());

        // Checking whether the tour was calculated or not, if yes set the
        // corresponding labels if not set an empty string as a placeholder.
        if (tourLoaded.getCompleteTime() != null) {
            final List<Journey> journeys = tourLoaded.getJourneyList();
            final int journeysLength = journeys.size();
            arrivalTime.setText(journeys.get(journeysLength - 1).getFinishTime().toString());
        } else {
            arrivalTime.setText(EMPTY_STRING);
        }
    }

    /**
     * Clears the Information stored about the current delivery Process, to
     * reduce conflict with the future incoming information. Invokes the
     * {@link UserInterface} to calculate the tour.
     */
    public void calculateTour() {
        clearNewDeliveryProcess();
        this.mainApp.calculateTour();
    }

    /**
     * In order to display the ActionPoints of a Tour, before the latter was
     * optimized, we need to acces those. This is done by this methods. The Ac
     */
    public void createFakeActionPointList() {
        List<ActionPoint> listActionPoints = new ArrayList<>();

        // Create a BASE and END actionPoint.
        ActionPoint base = new ActionPoint(tourLoaded.getStartTime(),
                tourLoaded.getBase(), ActionType.BASE);
        ActionPoint end = new ActionPoint(tourLoaded.getStartTime(),
                tourLoaded.getBase(), ActionType.END);

        listActionPoints.add(base);
        listActionPoints.add(end);

        for (DeliveryProcess deliveryProcess :
                tourLoaded.getDeliveryProcesses()) {

            final ActionPoint pickUp =
                    new ActionPoint(deliveryProcess.getPickUP().getTime(),
                            deliveryProcess.getPickUP().getLocation(),
                            deliveryProcess.getPickUP().getActionType());
            pickUp.setId(deliveryProcess.getPickUP().getId());
            listActionPoints.add(pickUp);

            final ActionPoint delivery =
                    new ActionPoint(deliveryProcess.getDelivery().getTime(),
                            deliveryProcess.getDelivery().getLocation(),
                            deliveryProcess.getDelivery().getActionType());
            delivery.setId(deliveryProcess.getDelivery().getId());
            listActionPoints.add(delivery);
        }
        tourLoaded.setActionPoints(listActionPoints);
    }

    /**
     * Invokes the {@link UserInterface} after a click on the undo button was
     * made.
     */
    public void undo() {
        this.mainApp.undo();
    }

    /**
     * Create a {@link Marker} which will have all the necessary information
     * for the user stored on itself. This is done to highlight the necessary
     * information when the user passes his mouse over the point.
     *
     * @param actionPoint the given {@link ActionPoint} to create a marker for.
     * @param mType       the {@link MarkerType}.
     * @return the created {@link Marker}
     */
    Marker createMarker(final ActionPoint actionPoint, final MarkerType mType) {
        String label = mType.firstLetter;
        if (actionPoint.getActionType() != ActionType.BASE &&
                actionPoint.getActionType() != ActionType.END) {
            label += actionPoint.getId();
        }
        ;
        MarkerOptions markerPoint = new MarkerOptions();
        markerPoint.title(mType.getTitle() + " - " + label + "\n\r" +
                "Passage Time: " + actionPoint.getPassageTime() + "\n" +
                "Time of Action: " + actionPoint.getTime() + "\n").label(label).
                position(new LatLong(actionPoint.getLocation().getLatitude(),
                        actionPoint.getLocation().getLongitude()));
        return new Marker(markerPoint);
    }

    /**
     * Checks whether the necessary information to add a {@link DeliveryProcess}
     * to the {@code loadedTour}. If it is the case the {@link UserInterface} is
     * invoked, if not an error message is displayed.
     */
    public void addNewDeliveryProcess() {
        if (canAddDeliveryProcess()) {
            if (newPickUpActionPoint != null && newDeliveryActionPoint != null) {
                newPickUpActionPoint.setTime(Utils.parseStringToTime(inputPickUpTimeH.getText(), inputPickUpTimeM.getText()));
                newDeliveryActionPoint.setTime(Utils.parseStringToTime(inputDeliveryTimeH.getText(), inputDeliveryTimeM.getText()));
                this.mainApp.addDeliveryProcess(tourLoaded,
                        newPickUpActionPoint, newDeliveryActionPoint);
            } else {
                showAlert("Action Impossible", "Error :", "The Delivery " +
                        "Process is not created", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Action Impossible", "Error :", "All the fields to " +
                            "create a delivery process are " + "not completes",
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Displays a selected {@link DeliveryProcess} to the User. First useful
     * information is set and displayed.
     *
     * @param deliveryProcess
     */
    void showDeliveryProcess(final DeliveryProcess deliveryProcess) {
        deliveryProcessLoaded = deliveryProcess;
        final String pickUpDuration =
                deliveryProcess.getPickUP().getTime().toString();
        final String deliveryDuration =
                deliveryProcess.getDelivery().getTime().toString();
        final String pickUpPointName =
                deliveryProcess.getPickUP().getLocation().getSegments().get(0)
                        .getName();
        final String deliveryPointName =
                deliveryProcess.getDelivery().getLocation().getSegments().get(0)
                        .getName();
        dPPuPoint.setText(pickUpPointName);
        dPDPoint.setText(deliveryPointName);
        dpDDuration.setText(deliveryDuration);
        dpPUDuration.setText(pickUpDuration);
        if (tourLoaded.getJourneyList() != null) {
            if (deliveryProcess.getPickUP().getActionType()
                    == ActionType.BASE) {
                dpDuration.setText(tourLoaded.getCompleteTime().toString());
                dPDistance.setText(String.valueOf(tourLoaded.getTotalDistance())
                        + " m");
                List<Journey> journeyList = new ArrayList<Journey>();
                journeyList.add(tourLoaded.getJourneyList().get(0));
                displayMap(getSelectedActionPoint().getLocation());
                drawAllActionPoints();
                drawFullTour();
                clearRectangleColor();
                drawPolyline(getMCVPathFormJourneyListe(journeyList),
                        0.5, 2);
            } else {
                this.mainApp.getJourneyList(tourLoaded.getJourneyList(),
                        deliveryProcess);
                if (deliveryProcess.getTime() != null) {
                    dpDuration.setText(deliveryProcess.getTime().toString());
                }
                if (deliveryProcess.getDistance() != null) {
                    dPDistance.setText((deliveryProcess.getDistance()) + " m");
                }
            }
        }
    }

    public void handleLoadMap() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Map XML");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
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
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
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

    private void handelTableSelection(final ActionPoint newValue) {
        Utils.pointToColour(newValue);
        if (newValue != null && newValue.getActionType() == ActionType.END && tourLoaded.getJourneyList() != null) {
            // Manage the end of the tour.
            dpDuration.setText(tourLoaded.getCompleteTime().toString());
            dPDistance.setText(String.valueOf(tourLoaded.getTotalDistance()) + " m");
            List<Journey> journeyList = new ArrayList<Journey>();
            journeyList.add(tourLoaded.getJourneyList().get(tourLoaded.getJourneyList().size() - 1));
            displayMap(newValue.getLocation());
            drawAllActionPoints();
            drawFullTour();
            drawPolyline(getMCVPathFormJourneyListe(journeyList), 0.5, 3);
        } else {
            mainApp.getDeliveryProcessFromActionPoint(newValue, tourLoaded);
        }
    }

    /**
     * Displays the map by setting the correct Marker options.
     *
     * @param center
     */
    void displayMap(final Point center) {
        try {
            Thread.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //  Set new center for the map
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(center.getLatitude(),
                center.getLongitude())).styleString(MAP_STYLE).
                overviewMapControl(false).mapType(MapTypeIdEnum.ROADMAP).
                panControl(false).rotateControl(false).scaleControl(false).
                streetViewControl(false).zoomControl(false).zoom(12);

        // Add map to the view
        map = mapView.createMap(mapOptions);
    }

    /**
     * Displays the {@link DeliveryProcess}s of a given tour.
     */
    void displayTourWhenNotCalculated() {
        map.setCenter(new LatLong(tourLoaded.getBase().getLatitude(),
                tourLoaded.getBase().getLongitude()));
        setBigLabels();
        // Create a fake list of action Points To display.
        createFakeActionPointList();
        actionPoints.remove(0, actionPoints.size());
        actionPoints.addAll(tourLoaded.getActionPoints());

        System.out.println(actionPoints.size() + " action point size");
        // Add observable list data to the table
        actionPointTableView.setItems(actionPoints);
        drawAllActionPoints();
    }

    /**
     * Draws  a complete tour by clearing markers, drawing {@link ActionPoint}
     * and the necessary Polyline (connection the different Points).
     */
    void drawFullTour() {
        setBigLabels();
        map.clearMarkers();
        drawAllActionPoints();
        drawPolyline(getMCVPathFormJourneyListe(tourLoaded.getJourneyList()),
                0.4, 1);
    }

    /**
     * Draws all the {@link ActionPoint} of the loaded {@link Tour} on the map.
     * Sets the Marker according to the type of the {@link ActionPoint} and
     * it's {@code id}.
     */
    void drawAllActionPoints() {

        // First Action Point is the Base
        map.clearMarkers();
        if (poly != null) {
            poly.setVisible(false);
            clearRectangleColor();
        }
        map.addMarker(createMarker(actionPoints.get(0), MarkerType.BASE));

        //According to ActionType set the good MarkerType
        for (ActionPoint actionPoint : actionPoints) {
            if (actionPoint.getActionType() == ActionType.DELIVERY) {
                map.addMarker(createMarker(actionPoint, MarkerType.DELIVERY));
            } else if (actionPoint.getActionType() == ActionType.PICK_UP) {
                map.addMarker(createMarker(actionPoint, MarkerType.PICKUP));
            }
        }
    }

    /**
     * Draws a Polylyne on the map, following the path that is displayed in
     * the {@link MVCArray}.
     *
     * @param mvcArray The path to follow to draw the line.
     * @param opacity  opacity of the line.
     * @param type     int parameter to determine the colour of the line.
     */
    void drawPolyline(final MVCArray mvcArray, final double opacity,
                      final int type) {

        String color = "blue";

        switch (type) {
            // 1 = Draw Full tour
            case 1:
                color = "blue";
                break;
            // 2 = Draw First journey
            case 2:
                color = "red";
                break;
            // 3 = Draw End journey
            case 3:
                color = "cyan";
                break;
            // 4 = Draw with Auto color
            case 4:
                if (deliveryProcessLoaded != null)
                    color = Utils.pointToColour(deliveryProcessLoaded.getPickUP());
                break;
        }
        setRectangleColor(color);
        PolylineOptions polyOpts =
                new PolylineOptions().path(mvcArray).strokeColor(color).clickable(false).strokeOpacity(opacity).strokeWeight(4).visible(true);
        poly = new Polyline(polyOpts);
        poly.setVisible(true);
        map.addMapShape(poly);
    }

    /**
     * Displays an {@link ActionPoint} that was previosuly selected by the
     * user. It will also save the information about the coordinates of the
     * point, to later inform the model.
     *
     * @param actionPoint The actionPoint to save and draw.
     */
    void drawAndSaveNewActionPoint(final ActionPoint actionPoint) {
        //Clear past Markers
        map.clearMarkers();
        // Draw all tour Action point
        drawAllActionPoints();

        if (actionPoint.getActionType() == ActionType.PICK_UP) {
            newPickUpActionPoint = actionPoint;
            newPickUpPointMarker = createMarker(actionPoint, MarkerType.PICKUP);
            labelPickUpCoordinates.setText(Utils.pointToString(actionPoint.getLocation()));
        }
        if (actionPoint.getActionType() == ActionType.DELIVERY) {
            newDeliveryActionPoint = actionPoint;
            newDeliveryPointMarker = createMarker(actionPoint,
                    MarkerType.DELIVERY);
            labelDeliveryCoordinates.setText(Utils.pointToString(actionPoint.getLocation()));
        }

        // Eventually draw newPickUp and Delivery Point
        if (newPickUpPointMarker != null) map.addMarker(newPickUpPointMarker);
        if (newDeliveryPointMarker != null)
            map.addMarker(newDeliveryPointMarker);
    }

    void setRectangleColor(final String color) {
        rectangle.setStyle("-fx-background-color:" + color + ";" + "-fx" +
                "-opacity: 0.5;");
    }
    // Clear / Reset.

    /**
     * Sets the about the selected {@link DeliveryProcess} back to zero.
     */
    public void clearNewDeliveryProcess() {
        clearNewDeliveryPoint();
        clearNewPickUpPoint();
        inputDeliveryTimeM.setText("");
        inputPickUpTimeM.setText("");
        inputPickUpTimeH.setText("");
        inputDeliveryTimeH.setText("");
        newPickUpPointMarker = null;
        newDeliveryPointMarker = null;
        newPickUpActionPoint = null;
        newDeliveryActionPoint = null;
    }

    public void clearNewPickUpPoint() {
        labelPickUpCoordinates.setText("");
    }

    public void clearNewDeliveryPoint() {
        labelDeliveryCoordinates.setText("");
    }

    public void clearAll() {
        displayMap(tourLoaded.getBase());
        clearRectangleColor();
        clearNewDeliveryProcess();
    }

    public void clearRectangleColor() {
        rectangle.setStyle("-fx-background-color: #393e46;");
    }

    // Utils

    public MVCArray getMCVPathFormJourneyListe(final List<Journey> journeyList) {
        int count = 0;
        LinkedList<Point> fullListOfPoints = new LinkedList<Point>();
        for (Journey journey : journeyList) {
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
            LatLong latLong = new LatLong(point.getLatitude(),
                    point.getLongitude());
            ary[i++] = latLong;
        }
        MVCArray mvc = new MVCArray(ary);
        return mvc;
    }

    public void handelMouseClickOnPoint(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String id = btn.getId();
        System.out.println(id);
        map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
            LatLong latLong = event.getLatLong();
            if (id.contains("setPickUp") && editable(labelPickUpCoordinates)) {
                System.out.println("this is a test");
                this.mainApp.getNearestPoint(latLong.getLatitude(),
                        latLong.getLongitude(), ActionType.PICK_UP,
                        new Time(0, 0, 0));
            }
            if (id.contains("setDelivery") && editable(labelDeliveryCoordinates)) {
                this.mainApp.getNearestPoint(latLong.getLatitude(),
                        latLong.getLongitude(), ActionType.DELIVERY,
                        new Time(0, 0, 0));
            }
        });
    }

    /**
     * checks whether a label is empty or not
     *
     * @param label the label to check.
     * @return true if the label is
     */
    public Boolean editable(Label label) {
        return label.getText() == "";
    }

    public Boolean canAddDeliveryProcess() {
        if (inputPickUpTimeH.getText().equals("")) {
            inputPickUpTimeH.setText("0");
        }
        if (inputDeliveryTimeH.getText().equals("")) {
            inputDeliveryTimeH.setText("0");
        }
        return labelDeliveryCoordinates.getText() != "" && labelDeliveryCoordinates.getText() != "" && inputDeliveryTimeM.getText() != "" && inputPickUpTimeM.getText() != "";
    }

    /**
     * Alert Message to interact with the user and draw his attention to an
     * important message.
     *
     * @param title     title of the message.
     * @param header    header of the message.
     * @param msg       message itself.
     * @param alertType type of alert (ex.: logging or error)
     */
    void showAlert(final String title, final String header, final String msg,
                   final Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Shows the user a confiramtion alert to be able to make sure he wants
     * to delete the seleted {@link DeliveryProcess}.
     *
     * @param msg The message to  be shown to the user.
     * @return True if the user agrees to delete the {@link DeliveryProcess}
     * false otherwise.
     */
    private Boolean showConfirmationAlert(final String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(msg);

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel",
                ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Show a dialog to be able to change the order of the
     *
     * @param deliveryProcess
     * @return
     */
    private int showModifiedDeliveryDialog(final DeliveryProcess deliveryProcess) {
        List<Integer> choices = new ArrayList<>();
        for (int i = 1; i < tourLoaded.getActionPoints().size() - 1; i++) {
            choices.add(i);
        }

        ChoiceDialog<Integer> dialog =
                new ChoiceDialog<Integer>(actionPointTableView.getSelectionModel().
                        getFocusedIndex(), choices);
        dialog.setTitle("Modify Order");
        dialog.setHeaderText("Change Order");
        dialog.setContentText("Choose the new N°:");

        Optional<Integer> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        } else {
            return -1;
        }

    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp the mainApp.
     */
    public void setMainApp(final UserInterface mainApp) {
        Validate.notNull(mainApp);
        this.mainApp = mainApp;
    }

    /**
     * @return the selected actionPoint by the use
     */
    ActionPoint getSelectedActionPoint() {
        return actionPointTableView.getSelectionModel().getSelectedItem();
    }

}

//Enum Marker Types.
@Getter
enum MarkerType {
    PICKUP("Pick-Up Point", "P", "icons/marker.png"), DELIVERY("Delivery "
            + "Point", "D", "flag.png"), BASE("Base Point", "B", "home" +
            "-icon" + "-silhouette.png");

    private String title = "";
    public String firstLetter = "";
    private String iconPath = "";

    MarkerType(String title, String firstLetter, String iconPath) {
        this.title = title;
        this.firstLetter = firstLetter;
        this.iconPath = iconPath;
    }
}


