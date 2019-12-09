package view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import model.data.Point;
import org.apache.commons.lang.Validate;
import view.UserInterface;

import javax.xml.validation.Validator;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable, MapComponentInitializedListener {

    //Map Style.
    final String mapStyle = "[{\"featureType\":\"administrative.neighborhood\",\"elementType\":\"all\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"poi\",\"elementType\":\"all\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"poi\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#747474\"},{\"lightness\":\"23\"}]},{\"featureType\":\"poi.attraction\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#f38eb0\"}]},{\"featureType\":\"poi.government\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#ced7db\"}]},{\"featureType\":\"poi.medical\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#ffa5a8\"}]},{\"featureType\":\"poi.park\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#c7e5c8\"}]},{\"featureType\":\"poi.place_of_worship\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#d6cbc7\"}]},{\"featureType\":\"poi.school\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#c4c9e8\"}]},{\"featureType\":\"poi.sports_complex\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#b1eaf1\"}]},{\"featureType\":\"road\",\"elementType\":\"all\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#343434\"}]},{\"featureType\":\"road\",\"elementType\":\"geometry\",\"stylers\":[{\"lightness\":\"100\"}]},{\"featureType\":\"road\",\"elementType\":\"labels\",\"stylers\":[{\"visibility\":\"off\"},{\"lightness\":\"100\"}]},{\"featureType\":\"road\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#8a8a8a\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#ffd4a5\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#6e6e6e\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#ffe9d2\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#6e6969\"}]},{\"featureType\":\"road.local\",\"elementType\":\"all\",\"stylers\":[{\"visibility\":\"simplified\"}]},{\"featureType\":\"road.local\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"weight\":\"3.00\"}]},{\"featureType\":\"road.local\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"weight\":\"0.30\"}]},{\"featureType\":\"road.local\",\"elementType\":\"labels.text\",\"stylers\":[{\"visibility\":\"on\"}]},{\"featureType\":\"road.local\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#464646\"},{\"lightness\":\"36\"}]},{\"featureType\":\"road.local\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"color\":\"#e9e5dc\"},{\"lightness\":\"30\"}]},{\"featureType\":\"transit\",\"elementType\":\"all\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"transit.line\",\"elementType\":\"geometry\",\"stylers\":[{\"visibility\":\"off\"},{\"lightness\":\"100\"}]},{\"featureType\":\"water\",\"elementType\":\"all\",\"stylers\":[{\"color\":\"#d2e7f7\"}]}]";

    // Reference to the main application
    private UserInterface mainApp;

    @FXML
    private Label latitudeLabel;

    @FXML
    private Label longitudeLabel;

    @FXML
    private GoogleMapView mapView;

    private GoogleMap map;

    public DashBoardController() {}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);
        mapView.setKey("AIzaSyDJDcPFKsYMTHWJUxVzoP0W7ERsx3Bhdgc");
    }

    @Override
    public void mapInitialized() {
        LatLong bobUnderwoodLocation = new LatLong(45.76771270760567, 4.854260515750411);


        //Set the initial properties of the map.
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

        map = mapView.createMap(mapOptions);

        //Add markers to the map
        MarkerOptions markerOptions3 = new MarkerOptions();
        markerOptions3.position(bobUnderwoodLocation);
        markerOptions3.title("Coucou");

        Marker bobUnderwoodMarker = new Marker(markerOptions3);

        InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
        infoWindowOptions.content("<h2>Fred Wilkie</h2>"
                + "Current Location: Safeway<br>"
                + "ETA: 45 minutes" );

        InfoWindow bobUnderwoodWindow = new InfoWindow(infoWindowOptions);
        bobUnderwoodWindow.open(map, bobUnderwoodMarker);

        map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
            LatLong latLong = event.getLatLong();
            System.out.println(latLong.getLatitude() + "coucou" + latLong.getLongitude());
        });
        map.addMarker( bobUnderwoodMarker );
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    //@FXML
    //private void initialize() {
    // Initialize the person table with the two columns.
    //firstNameColumn.setCellValueFactory(
    //        cellData -> cellData.getValue().firstNameProperty());
    //lastNameColumn.setCellValueFactory(
    //        cellData -> cellData.getValue().lastNameProperty());

    // Clear person details.
    //showPersonDetails(null);

    // Listen for selection changes and show the person details when changed.
    //personTable.getSelectionModel().selectedItemProperty().addListener(
    //        (observable, oldValue, newValue) -> showPersonDetails(newValue));
    //}
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
            } else {
                System.out.println("Error Loading not xml File");
            }
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(final UserInterface mainApp) {
        Validate.notNull(mainApp);
        this.mainApp = mainApp;

        // Add observable list data to the table
        // tourTable.setItems(mainApp.getTour());
    }

    public void displayMapPoints(List<Point> points) {
        for( Point point: points) {
            //Add markers to the map
            MarkerOptions markerPoint = new MarkerOptions();
            markerPoint.position(new LatLong(point.getLatitude(), point.getLongitude()));
            Marker pointMarker = new Marker(markerPoint);
            map.addMarker( pointMarker );
        }
    }
}
