package service;

import AlgoClustering.IAlgoClustering;
import dao.IDao;
import dm.Adopter;
import dm.Pet;

import java.util.*;
import java.util.stream.Collectors;

public class PetMatchingService {
    private final IDao<Pet> petDao;
    private final IDao<Adopter> adopterDao;
    private final IAlgoClustering clusteringAlgorithm;

    public PetMatchingService(IDao<Pet> petDao, IDao<Adopter> adopterDao, IAlgoClustering clusteringAlgorithm) {
        this.petDao = petDao;
        this.adopterDao = adopterDao;
        this.clusteringAlgorithm = clusteringAlgorithm;


        try {
            List<Pet> allPets = petDao.getAll();
            System.out.println("Initializing clustering algorithm with " + allPets.size() + " pets");

            for (Pet pet : allPets) {
                if (pet.getCharacteristicsMap() != null && !pet.getCharacteristicsMap().isEmpty()) {
                    clusteringAlgorithm.addDataPoint(pet.getId(), this.enhanceCharacteristics(pet));
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing clustering algorithm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Map<String, Double> enhanceCharacteristics(Pet pet) {
        Map<String, Double> enhanced = new HashMap<>(pet.getCharacteristicsMap());

        if (pet.getSpecies() != null && !pet.getSpecies().isEmpty()) {
            enhanced.put("species:" + pet.getSpecies().toLowerCase(), 40.0);
        }

        if (pet.getBreed() != null && !pet.getBreed().isEmpty()) {
            enhanced.put("breed:" + pet.getBreed().toLowerCase(), 30.0);
        }

        enhanced.put("age", (double) pet.getAge());

        if (pet.getGender() != null && !pet.getGender().isEmpty()) {
            enhanced.put("gender:" + pet.getGender().toLowerCase(), 10.0);
        }

        return enhanced;
    }

    public void addPet(Pet pet) {
        petDao.save(pet);
        clusteringAlgorithm.addDataPoint(pet.getId(), enhanceCharacteristics(pet));
    }

    public void updatePet(Pet pet) {
        petDao.update(pet);
        clusteringAlgorithm.updateDataPoint(pet.getId(), enhanceCharacteristics(pet));
    }

    public void removePet(String id) {
        petDao.delete(id);
        clusteringAlgorithm.removeDataPoint(id);
    }

    public Pet getPet(String petId) {
        return petDao.get(petId);
    }

    public List<Pet> getAllPets() {
        return petDao.getAll();
    }

    public void addAdopter(Adopter adopter) {
        adopterDao.save(adopter);
    }

    public void updateAdopter(Adopter adopter) {
        adopterDao.update(adopter);
    }

    public void removeAdopter(String adopterId) {
        adopterDao.delete(adopterId);
    }

    public Adopter getAdopter(String adopterId) {
        return adopterDao.get(adopterId);
    }

    public List<Adopter> getAllAdopters() {
        return adopterDao.getAll();
    }

    public List<Pet> findPetMatchesForAdopter(String adopterId, int limit) {
        Adopter adopter = adopterDao.get(adopterId);
        if (adopter == null) {
            return new ArrayList<>();
        }

        List<Pet> availablePets = petDao.getAll().stream()
                .filter(pet -> !"Adopted".equals(pet.getStatus()))
                .collect(Collectors.toList());

        if (availablePets.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            Map<String, Double> enhancedPreferences = enhancePreferences(adopter);

            int numClusters = Math.min(3, availablePets.size());
            clusteringAlgorithm.processCluster(numClusters);

            List<String> matchingPetIds = clusteringAlgorithm.findSimilarDataPoints(
                    enhancedPreferences, Math.min(limit, availablePets.size()));


            return matchingPetIds.stream()
                    .map(petDao::get)
                    .filter(Objects::nonNull)
                    .filter(pet -> !"Adopted".equals(pet.getStatus()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Error in clustering: " + e.getMessage());
            e.printStackTrace();

            return null;

        }
    }

    private Map<String, Double> enhancePreferences(Adopter adopter) {
        Map<String, Double> enhanced = new HashMap<>(adopter.getPreferencesMap());

        if (adopter.getPreferredSpecies() != null && !adopter.getPreferredSpecies().isEmpty()) {
            enhanced.put("species:" + adopter.getPreferredSpecies().toLowerCase(), 40.0);
        }

        if (adopter.getPreferredBreed() != null && !adopter.getPreferredBreed().isEmpty()) {
            enhanced.put("breed:" + adopter.getPreferredBreed().toLowerCase(), 30.0);
        }

        enhanced.put("age:min", (double) adopter.getPreferredAgeMin());
        enhanced.put("age:max", (double) adopter.getPreferredAgeMax());

        if (adopter.getPreferredGender() != null && !adopter.getPreferredGender().isEmpty()) {
            enhanced.put("gender:" + adopter.getPreferredGender().toLowerCase(), 10.0);
        }

        return enhanced;
    }

}