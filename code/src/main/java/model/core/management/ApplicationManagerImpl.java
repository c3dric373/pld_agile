package model.core.management;

import model.data.*;
import model.io.XmlToGraph;
import view.Observer;
import view.UserInterface;

import java.io.File;
import java.util.List;

public class ApplicationManagerImpl implements ApplicationManager {

    /**
     * Xml Converter for Project.
     */
    XmlToGraph xmlToGraph;

    /**
     * Project Data for Project.
     */
    ProjectData projectData;

    /**
     * Project Data wrapper for project.
     */
    ProjectDataWrapper projectDataWrapper;

    ApplicationManagerImpl(final Observer newObserver){
    xmlToGraph = new XmlToGraph();
    projectData = new ProjectDataImpl();
    projectDataWrapper = new ProjectDataWrapperImpl(newObserver);

    }

    @Override
    public void loadMap(final File file) {
        // TODO when graph takes nodes as input
        // List<Point> nodes =  xmlToGraph.getGraphFromXml(file.getPath());
        final Graph graph = new Graph();
        projectData.setGraph();
        projectDataWrapper.loadMap(graph);


    }

    @Override
    public void setObserver(final UserInterface userInterface) {

    }

    @Override
    public void loadTour(final File file) {

    }

    @Override
    public void calculateTour() {

    }

    @Override
    public void addDeliveryProcess(final DeliveryProcess deliveryProcess) {

    }

    @Override
    public void deleteDeliveryProcess(final DeliveryProcess deliveryProcess) {

    }

    @Override
    public void modifyTour() {

    }
}
