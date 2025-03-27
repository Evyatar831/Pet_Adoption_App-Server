package controller;


import java.util.HashMap;
import java.util.Map;

public class ControllerFactory {
    private static ControllerFactory instance;
    private final Map<String, IController> controllers;

    private ControllerFactory() {
        controllers = new HashMap<>();
        initializeControllers();
    }

    public static synchronized ControllerFactory getInstance() {
        if (instance == null) {
            instance = new ControllerFactory();
        }
        return instance;
    }

    private void initializeControllers() {
        controllers.put("pet", new PetController());
        controllers.put("adopter", new AdopterController());
        controllers.put("adoption", new AdoptionController());
    }

    public IController getController(String controllerName) {
        return controllers.get(controllerName.toLowerCase());
    }
}
