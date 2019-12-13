package view;

import lombok.Getter;

/**
 * Enum to distinguish between the different possible Markers on our map.
 * These Markers will be used to display {@link model.data.ActionPoint}s.
 */
@Getter
enum MarkerType {
    /**
     * Type Pick Up.
     */
    PICKUP("Pick-Up Point", "P", "icons/marker.png"),
    /**
     * Type Delivery.
     */
    DELIVERY("Delivery Point", "D", "flag.png"),
    /**
     * Type Base.
     */
    BASE("Base Point", "B", "home-icon"
            + "-silhouette.png");
    /**
     * Title.
     */
    private String title = "";
    /**
     * First Letter.
     */
    public String firstLetter = "";
    /**
     * IconPath.
     */
    private String iconPath = "";

    /**
     * Construction
     *
     * @param newTitle       title of marker
     * @param newFirstLetter firstLetter of Marker
     * @param newIconPath    iconPath of Marker
     */
    MarkerType(final String newTitle, final String newFirstLetter,
               final String newIconPath) {
        this.title = newTitle;
        this.firstLetter = newFirstLetter;
        this.iconPath = newIconPath;
    }
}
