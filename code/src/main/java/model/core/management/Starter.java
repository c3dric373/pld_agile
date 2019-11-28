package model.core.management;

import view.UserInterface;
/**
 * Class responsible for starting the application.
 */
public class Starter {

    /**
     * Entry point of the application. Starts Deli'Velov.
     *
     * @param args Command line arguments that should be empty.
     */
    public static void main(String[] args) {
        ApplicationManager model = new ApplicationManagerImpl();
        UserInterface view = new UserInterface(model);

    }
}
