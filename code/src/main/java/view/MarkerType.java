package view;

import lombok.Getter;

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
