package model.core.management;

import org.apache.commons.lang.Validate;

/**
 * Extra main needed to start the application from Intellij.
 */
public class NewMain {
    /**
     * Main method that start the application.
     *
     * @param args Command line arguments, should be empty.
     */
    public static void main(final String[] args) {
        Validate.notNull(args, "args null");
        Starter.main(args);
    }
}
