package controller;


import dm.Pet;
import service.PetMatchingService;
import service.ServiceLocator;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PetController implements IController {
    private final PetMatchingService petService;

    public PetController() {
        this.petService = ServiceLocator.getPetMatchingService();
    }

    @Override
    public Object executeAction(String action, Object data) {
        System.out.println("PetController: Executing action '" + action + "' with data type: " +
                (data != null ? data.getClass().getName() : "null"));
        try {
            switch (action) {
                case "add":
                    return addPet((Pet) data);

                case "update":
                    return updatePet((Pet) data);

                case "get":
                    String getId = extractId(data);
                    if (getId != null) {
                        return getPet(getId);
                    }
                    return null;

                case "getAll":
                    return getAllPets();

                case "delete":
                    String deleteId = extractId(data);
                    if (deleteId != null) {
                        System.out.println("PetController: Deleting pet with ID: " + deleteId);
                        return removePet(deleteId);
                    }
                    return false;

                case "match":
                    if (data instanceof Map) {
                        Map<?, ?> map = (Map<?, ?>) data;
                        String adopterId = convertToString(map.get("adopterId"));
                        int limit = convertToInt(map.get("limit"), 5); // Default to 5 if not specified
                        return findMatches(adopterId, limit);
                    }
                    return null;

                default:
                    throw new IllegalArgumentException("Unknown action: " + action);
            }
        } catch (Exception e) {
            System.err.println("PetController: Exception in executeAction for action '" + action + "': " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private String extractId(Object data) {
        if (data instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) data;
            Object idObj = map.get("id");
            return idObj != null ? idObj.toString() : null;
        } else if (data instanceof Pet) {
            return ((Pet) data).getId();
        }
        return null;
    }

    private String convertToString(Object obj) {
        return obj != null ? obj.toString() : null;
    }

    private int convertToInt(Object obj, int defaultValue) {
        if (obj == null) return defaultValue;

        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }

        try {
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    private Pet addPet(Pet pet) {
        System.out.println("Server: Adding pet: " + pet.getId() + " - " + pet.getName());

        if (pet.getId() == null || pet.getId().isEmpty()) {
            System.err.println("Pet ID is missing");
            pet.setId(UUID.randomUUID().toString());
            System.out.println("Generated new ID: " + pet.getId());
        }

        if (pet.getName() == null || pet.getName().isEmpty()) {
            System.err.println("Pet name is missing");
            return null;
        }

        try {
            petService.addPet(pet);

            Pet added = petService.getPet(pet.getId());
            if (added == null) {
                System.err.println("Failed to verify pet was added: " + pet.getId());
                return null;
            }

            System.out.println("Pet added successfully: " + pet.getId() + " - " + pet.getName());
            return pet;
        } catch (Exception e) {
            System.err.println("Error adding pet: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private Pet updatePet(Pet pet) {

        Pet existingPet = petService.getPet(pet.getId());

        if (existingPet == null) {
            System.out.println("Pet with ID " + pet.getId() + " does not exist. Redirecting to add operation.");
            return addPet(pet);
        }

        try {
            petService.updatePet(pet);

            Pet updated = petService.getPet(pet.getId());
            if (updated == null) {
                System.err.println("Failed to verify pet was updated: " + pet.getId());
                return null;
            }

            System.out.println("Pet updated successfully: " + pet.getId() + " - " + pet.getName());
            return pet;
        } catch (Exception e) {
            System.err.println("Error updating pet: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private Pet getPet(String id) {
        return petService.getPet(id);
    }

    private List<Pet> getAllPets() {
        return petService.getAllPets();
    }

    private boolean removePet(String id) {
        System.out.println("Server: Removing pet with ID: " + id);


        if (id == null || id.isEmpty()) {
            System.err.println("Invalid pet ID for deletion: " + id);
            return false;
        }


        Pet existingPet = petService.getPet(id);
        if (existingPet == null) {
            System.err.println("Pet not found for deletion: " + id);
            return false;
        }


        System.out.println("Found pet to delete: " + existingPet.getName() + " (ID: " + existingPet.getId() + ")");

        try {

            petService.removePet(id);


            System.out.println("Pet removal operation completed successfully");
            return true;
        } catch (Exception e) {
            System.err.println("Exception during pet removal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private List<Pet> findMatches(String adopterId, int limit) {
        System.out.println("Pet findMatches completed successfully");

        return petService.findPetMatchesForAdopter(adopterId, limit);
    }
}
