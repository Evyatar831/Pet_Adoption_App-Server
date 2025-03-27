package controller;


import dm.Adopter;
import service.PetMatchingService;
import service.ServiceLocator;

import java.util.List;
import java.util.Map;

public class AdopterController implements IController {
    private final PetMatchingService petMatchingService;

    public AdopterController() {
        this.petMatchingService = ServiceLocator.getPetMatchingService();
    }

    @Override
    public Object executeAction(String action, Object data) {
        switch (action) {
            case "add":
                return addAdopter((Adopter) data);
            case "update":
                return updateAdopter((Adopter) data);
            case "get":
                if (data instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) data;
                    return getAdopter((String) map.get("id"));
                }
                return null;
            case "getAll":
                return getAllAdopters();
            case "delete":
                if (data instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) data;
                    return removeAdopter((String) map.get("id"));
                }
                return false;
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
    }

    private Adopter addAdopter(Adopter adopter) {
        petMatchingService.addAdopter(adopter);
        return adopter;
    }

    private Adopter updateAdopter(Adopter adopter) {
        petMatchingService.updateAdopter(adopter);
        return adopter;
    }

    private Adopter getAdopter(String id) {
        return petMatchingService.getAdopter(id);
    }

    private List<Adopter> getAllAdopters() {
        return petMatchingService.getAllAdopters();
    }

    private boolean removeAdopter(String id) {
        petMatchingService.removeAdopter(id);
        return true;
    }
}