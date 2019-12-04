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


    // Reference to the main application
    private UserInterface mainApp;

    @FXML
    private Label latitudeLabel;

    @FXML
    private Label longitudeLabel;

    @FXML
    private GoogleMapView mapView;

    private GoogleMap map;

    public DashBoardController() {

    }

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
                .styleString("[\n" +
                        "    {\n" +
                        "        \"featureType\": \"administrative.neighborhood\",\n" +
                        "        \"elementType\": \"all\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"visibility\": \"off\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"poi\",\n" +
                        "        \"elementType\": \"all\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"visibility\": \"off\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"poi\",\n" +
                        "        \"elementType\": \"labels.text.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#747474\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"lightness\": \"23\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"poi.attraction\",\n" +
                        "        \"elementType\": \"geometry.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#f38eb0\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"poi.government\",\n" +
                        "        \"elementType\": \"geometry.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#ced7db\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"poi.medical\",\n" +
                        "        \"elementType\": \"geometry.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#ffa5a8\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"poi.park\",\n" +
                        "        \"elementType\": \"geometry.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#c7e5c8\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"poi.place_of_worship\",\n" +
                        "        \"elementType\": \"geometry.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#d6cbc7\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"poi.school\",\n" +
                        "        \"elementType\": \"geometry.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#c4c9e8\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"poi.sports_complex\",\n" +
                        "        \"elementType\": \"geometry.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#b1eaf1\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road\",\n" +
                        "        \"elementType\": \"all\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"visibility\": \"on\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"color\": \"#343434\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road\",\n" +
                        "        \"elementType\": \"geometry\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"lightness\": \"100\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road\",\n" +
                        "        \"elementType\": \"labels\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"visibility\": \"off\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"lightness\": \"100\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road\",\n" +
                        "        \"elementType\": \"labels.text.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"visibility\": \"on\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"color\": \"#8a8a8a\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road.highway\",\n" +
                        "        \"elementType\": \"geometry.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#ffd4a5\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road.highway\",\n" +
                        "        \"elementType\": \"labels.text.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#6e6e6e\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road.arterial\",\n" +
                        "        \"elementType\": \"geometry.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#ffe9d2\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road.arterial\",\n" +
                        "        \"elementType\": \"labels.text.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#6e6969\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road.local\",\n" +
                        "        \"elementType\": \"all\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"visibility\": \"simplified\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road.local\",\n" +
                        "        \"elementType\": \"geometry.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"weight\": \"3.00\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road.local\",\n" +
                        "        \"elementType\": \"geometry.stroke\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"weight\": \"0.30\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road.local\",\n" +
                        "        \"elementType\": \"labels.text\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"visibility\": \"on\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road.local\",\n" +
                        "        \"elementType\": \"labels.text.fill\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#464646\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"lightness\": \"36\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"road.local\",\n" +
                        "        \"elementType\": \"labels.text.stroke\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#e9e5dc\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"lightness\": \"30\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"transit\",\n" +
                        "        \"elementType\": \"all\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"visibility\": \"off\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"transit.line\",\n" +
                        "        \"elementType\": \"geometry\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"visibility\": \"off\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"lightness\": \"100\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"featureType\": \"water\",\n" +
                        "        \"elementType\": \"all\",\n" +
                        "        \"stylers\": [\n" +
                        "            {\n" +
                        "                \"color\": \"#d2e7f7\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    }\n" +
                        "]")
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
                mainApp.loadMap(selectedFile);
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
    public void setMainApp(UserInterface mainApp) {
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
