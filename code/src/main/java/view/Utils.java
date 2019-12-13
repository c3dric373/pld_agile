package view;

import model.genData.ActionPoint;
import model.genData.Point;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;

import java.awt.*;
import java.sql.Time;
import java.text.DecimalFormat;

/**
 * Utility class for the {@link DashBoardController}.
 */
public class Utils {

    /**
     * Max number allowed for an hour.
     */
    private static final int MAX_HOURS = 23;

    /**
     * Max number allowed to be a minute.
     */
    private static final int MAX_MINUTES = 59;

    /**
     * Empty String.
     */
    private static final String EMPTY_STRING = "";

    /**
     * Random Multiplication Factor
     */
    private static final int RANDOM_MULTIPLICATION_FACTOR = 380000;

    /**
     * Transforms an actionPoint to a colour, returned in the form of a String.
     *
     * @param actionPoint the actionPoint to assign a colour.
     * @return the colour assigned to the {@link ActionPoint}.
     */
    static String pointToColour(ActionPoint actionPoint) {
        int numberFromId = (int) (actionPoint.getId()
                * RANDOM_MULTIPLICATION_FACTOR
                * Math.pow(2, actionPoint.getId()));
        Color color = new Color(numberFromId).brighter();
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(),
                color.getBlue());
    }

    /**
     * Represents the longitude and latitude of a point as a String.
     *
     * @param point the point to represent.
     * @return a String containing the latitude and longitude of the given
     * Point.
     */
    static String pointToString(final Point point) {
        if (point != null) {
            DecimalFormat numberFormat = new DecimalFormat("#.0000");
            return numberFormat.format(point.getLatitude()) + ", "
                    + numberFormat.format(point.getLongitude());
        } else {
            return EMPTY_STRING;
        }
    }

    /**
     * Parses a String to a {@link Time} object checking whether the given
     * input is correct.
     * Input is given by two strings, the first one representing th hours the
     * second one the minutes.
     *
     * @param hours   the hours input.
     * @param minutes the minutes input.
     * @return the {@link Time} object representing the input.
     */
    static Time parseStringToTime(final String hours, final String minutes) {
        Validate.notNull(hours, "hours null");
        Validate.notNull(minutes, "minutes null");
        Validate.isTrue(NumberUtils.isNumber(hours),
                "hours not a number");
        Validate.isTrue(NumberUtils.isNumber(minutes),
                "minutes not a number");
        Validate.isTrue(Integer.parseInt(hours)
                < MAX_HOURS && Integer.parseInt(hours) >= 0, "not an hour");
        Validate.isTrue(Integer.parseInt(minutes)
                < MAX_MINUTES && Integer.parseInt(minutes) >= 0, "not a " +
                "minute");
        final String toParse = hours + ":" + minutes + ":00";
        return Time.valueOf(toParse);
    }
}
