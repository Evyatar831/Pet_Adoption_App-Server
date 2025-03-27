package controller;

import dm.Adoption;
import service.AdoptionService;
import service.ServiceLocator;

import java.util.List;
import java.util.Map;

public class AdoptionController implements IController {
    private final AdoptionService adoptionService;

    public AdoptionController() {
        this.adoptionService = ServiceLocator.getAdoptionService();
    }

    @Override
    public Object executeAction(String action, Object data) {
        System.out.println("AdoptionController: executing action '" + action + "' with data type: " +
                (data != null ? data.getClass().getName() : "null"));

        switch (action) {
            case "create":
                // Handle when data is already an Adoption object
                if (data instanceof Adoption) {
                    Adoption adoption = (Adoption) data;
                    return createAdoptionRequest(adoption.getPetId(), adoption.getAdopterId());
                }
                // Handle when data is a Map
                else if (data instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) data;
                    String petId = (String) map.get("petId");
                    String adopterId = (String) map.get("adopterId");

                    System.out.println("Create adoption parameters - petId: " + petId + ", adopterId: " + adopterId);

                    if (petId != null && adopterId != null) {
                        return createAdoptionRequest(petId, adopterId);
                    } else {
                        System.err.println("Missing required parameters for adoption creation");
                    }
                } else {
                    System.err.println("Unexpected data type for adoption/create");
                }
                return null;

            case "update":
                // Handle when data is already an Adoption object
                if (data instanceof Adoption) {
                    Adoption adoption = (Adoption) data;
                    return updateAdoptionStatus(adoption.getId(), adoption.getStatus());
                }
                // Handle when data is a Map
                else if (data instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) data;
                    String id = (String) map.get("id");
                    String status = (String) map.get("status");

                    System.out.println("Update adoption parameters - id: " + id + ", status: " + status);

                    if (id != null && status != null) {
                        return updateAdoptionStatus(id, status);
                    } else {
                        System.err.println("Missing required parameters for adoption update");
                    }
                } else {
                    System.err.println("Unexpected data type for adoption/update");
                }
                return null;

            case "complete":
                // Handle when data is already an Adoption object
                if (data instanceof Adoption) {
                    Adoption adoption = (Adoption) data;
                    return completeAdoption(adoption.getId());
                }
                // Handle when data is a Map
                else if (data instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) data;
                    String id = (String) map.get("id");

                    System.out.println("Complete adoption parameter - id: " + id);

                    if (id != null) {
                        return completeAdoption(id);
                    } else {
                        System.err.println("Missing id parameter for adoption completion");
                    }
                } else {
                    System.err.println("Unexpected data type for adoption/complete");
                }
                return null;

            case "get":
                // Handle when data is already an Adoption object
                if (data instanceof Adoption) {
                    Adoption adoption = (Adoption) data;
                    return getAdoption(adoption.getId());
                }
                // Handle when data is a Map
                else if (data instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) data;
                    String id = (String) map.get("id");

                    System.out.println("Get adoption parameter - id: " + id);

                    if (id != null) {
                        return getAdoption(id);
                    } else {
                        System.err.println("Missing id parameter for get adoption");
                    }
                } else {
                    System.err.println("Unexpected data type for adoption/get");
                }
                return null;

            case "getAll":
                System.out.println("Retrieving all adoptions");
                return getAllAdoptions();

            case "getByStatus":
                // Handle when data is already an Adoption object
                if (data instanceof Adoption) {
                    Adoption adoption = (Adoption) data;
                    return getAdoptionsByStatus(adoption.getStatus());
                }
                // Handle when data is a Map
                else if (data instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) data;
                    String status = (String) map.get("status");

                    System.out.println("Get adoptions by status parameter - status: " + status);

                    if (status != null) {
                        return getAdoptionsByStatus(status);
                    } else {
                        System.err.println("Missing status parameter for getByStatus");
                    }
                } else {
                    System.err.println("Unexpected data type for adoption/getByStatus");
                }
                return null;

            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
    }

    private Adoption createAdoptionRequest(String petId, String adopterId) {
        System.out.println("Creating adoption request for pet: " + petId + " and adopter: " + adopterId);
        return adoptionService.createAdoptionRequest(petId, adopterId);
    }

    private Adoption updateAdoptionStatus(String id, String status) {
        System.out.println("Updating adoption status for: " + id + " to: " + status);
        adoptionService.updateAdoptionStatus(id, status);
        return adoptionService.getAdoption(id);
    }

    private Adoption completeAdoption(String id) {
        System.out.println("Completing adoption for: " + id);
        adoptionService.completeAdoption(id);
        return adoptionService.getAdoption(id);
    }

    private Adoption getAdoption(String id) {
        System.out.println("Retrieving adoption with id: " + id);
        return adoptionService.getAdoption(id);
    }

    private List<Adoption> getAllAdoptions() {
        System.out.println("Retrieving all adoptions");
        return adoptionService.getAllAdoptions();
    }

    private List<Adoption> getAdoptionsByStatus(String status) {
        System.out.println("Retrieving adoptions with status: " + status);
        return adoptionService.getAdoptionsByStatus(status);
    }
}