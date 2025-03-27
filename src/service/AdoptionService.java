package service;

import dao.IDao;
import dm.Adoption;
import dm.Pet;
import dm.Adopter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdoptionService {
    private final IDao<Adoption> adoptionDao;
    private final IDao<Pet> petDao;
    private final IDao<Adopter> adopterDao;

    public AdoptionService(IDao<Adoption> adoptionDao, IDao<Pet> petDao, IDao<Adopter> adopterDao) {
        this.adoptionDao = adoptionDao;
        this.petDao = petDao;
        this.adopterDao = adopterDao;
    }

    public Adoption createAdoptionRequest(String petId, String adopterId) {
        Pet pet = petDao.get(petId);
        Adopter adopter = adopterDao.get(adopterId);

        if (pet == null || adopter == null) {
            throw new IllegalArgumentException("Pet or Adopter not found");
        }

        if (pet.getStatus() != null && "Adopted".equals(pet.getStatus())) {
            throw new IllegalStateException("Pet is already adopted");
        }

        String adoptionId = UUID.randomUUID().toString();
        Adoption adoption = new Adoption(adoptionId, petId, adopterId);

        adoptionDao.save(adoption);

        pet.setStatus("Pending");
        petDao.update(pet);

        return adoption;
    }

    public void updateAdoptionStatus(String adoptionId, String newStatus) {
        Adoption adoption = adoptionDao.get(adoptionId);
        if (adoption == null) {
            throw new IllegalArgumentException("Adoption not found with ID: " + adoptionId);
        }

        if (!isValidStatus(newStatus)) {
            throw new IllegalArgumentException("Invalid adoption status: " + newStatus);
        }

        if (!isValidStatusTransition(adoption.getStatus(), newStatus)) {
            throw new IllegalStateException("Invalid status transition from " +
                    adoption.getStatus() + " to " + newStatus);
        }

        Pet pet = petDao.get(adoption.getPetId());
        if (pet == null) {
            throw new IllegalStateException("Associated pet not found");
        }

        if ("Approved".equals(newStatus) && "Adopted".equals(pet.getStatus())) {
            throw new IllegalStateException("Cannot approve adoption for a pet that is already adopted");
        }

        adoption.setStatus(newStatus);
        adoptionDao.update(adoption);

        if ("Approved".equals(newStatus)) {
            pet.setStatus("Pending");
            petDao.update(pet);
        } else if ("Rejected".equals(newStatus)) {
            List<Adoption> otherAdoptions = getAdoptionsByPet(pet.getId()).stream()
                    .filter(a -> !a.getId().equals(adoptionId))
                    .filter(a -> "Pending".equals(a.getStatus()) || "Approved".equals(a.getStatus()))
                    .collect(Collectors.toList());

            if (otherAdoptions.isEmpty()) {
                pet.setStatus("Available");
                petDao.update(pet);
            }
        }
    }

    public void completeAdoption(String adoptionId) {
        Adoption adoption = adoptionDao.get(adoptionId);
        if (adoption == null) {
            throw new IllegalArgumentException("Adoption not found with ID: " + adoptionId);
        }

        if (!"Approved".equals(adoption.getStatus())) {
            throw new IllegalStateException("Cannot complete adoption that is not approved");
        }

        String petId = adoption.getPetId();
        Pet pet = petDao.get(petId);

        if (pet == null) {
            throw new IllegalStateException("Associated pet not found");
        }

        if ("Adopted".equals(pet.getStatus())) {
            throw new IllegalStateException("Pet is already adopted in another adoption request");
        }

        adoption.setStatus("Completed");
        adoption.setAdoptionDate(LocalDateTime.now());
        adoptionDao.update(adoption);

        pet.setStatus("Adopted");
        petDao.update(pet);

        List<Adoption> otherAdoptions = getAdoptionsByPet(petId);
        for (Adoption otherAdoption : otherAdoptions) {
            if (!otherAdoption.getId().equals(adoptionId) &&
                    ("Pending".equals(otherAdoption.getStatus()) || "Approved".equals(otherAdoption.getStatus()))) {
                otherAdoption.setStatus("Rejected");
                adoptionDao.update(otherAdoption);
            }
        }
    }

    public Adoption getAdoption(String adoptionId) {
        return adoptionDao.get(adoptionId);
    }

    public List<Adoption> getAllAdoptions() {
        return adoptionDao.getAll();
    }

    public List<Adoption> getAdoptionsByStatus(String status) {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid adoption status: " + status);
        }

        return adoptionDao.getAll().stream()
                .filter(adoption -> status.equals(adoption.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Adoption> getAdoptionsByAdopter(String adopterId) {
        return adoptionDao.getAll().stream()
                .filter(adoption -> adopterId.equals(adoption.getAdopterId()))
                .collect(Collectors.toList());
    }

    public List<Adoption> getAdoptionsByPet(String petId) {
        return adoptionDao.getAll().stream()
                .filter(adoption -> petId.equals(adoption.getPetId()))
                .collect(Collectors.toList());
    }

    private boolean isValidStatus(String status) {
        return "Pending".equals(status) ||
                "Approved".equals(status) ||
                "Rejected".equals(status) ||
                "Completed".equals(status);
    }

    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        switch (currentStatus) {
            case "Pending":
                return "Approved".equals(newStatus) || "Rejected".equals(newStatus);
            case "Approved":
                return "Completed".equals(newStatus) || "Pending".equals(newStatus);
            case "Rejected":
                return "Pending".equals(newStatus);
            case "Completed":
                return false;
            default:
                return false;
        }
    }
}