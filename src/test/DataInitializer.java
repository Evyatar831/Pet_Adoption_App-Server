package test;

import dm.Pet;
import dm.Adopter;
import service.PetMatchingService;
import service.ServiceLocator;

import java.util.UUID;


public class DataInitializer {
    private final PetMatchingService petMatchingService;

    public DataInitializer() {
        this.petMatchingService = ServiceLocator.getPetMatchingService();
    }


    public void initializeSampleData() {
        clearExistingData();

        addSamplePets();

        addSampleAdopters();
    }


    private void clearExistingData() {
        System.out.println("Clearing existing data...");
    }


    private void addSamplePets() {
        Pet dog1 = new Pet(UUID.randomUUID().toString(), "Max", "Dog", "Golden Retriever", 3, "Male");
        dog1.setStatus("Available");
        petMatchingService.addPet(dog1);

        Pet dog2 = new Pet(UUID.randomUUID().toString(), "Luna", "Dog", "Labrador", 2, "Female");
        dog2.setStatus("Available");
        petMatchingService.addPet(dog2);

        Pet cat1 = new Pet(UUID.randomUUID().toString(), "Whiskers", "Cat", "Siamese", 5, "Female");
        cat1.setStatus("Available");
        petMatchingService.addPet(cat1);

        Pet cat2 = new Pet(UUID.randomUUID().toString(), "Mittens", "Cat", "Persian", 7, "Female");
        cat2.setStatus("Available");
        petMatchingService.addPet(cat2);

        Pet smallPet = new Pet(UUID.randomUUID().toString(), "Fluffy", "Rabbit", "Dwarf", 1, "Male");
        smallPet.setStatus("Available");
        petMatchingService.addPet(smallPet);

        System.out.println("Added 5 sample pets to the system.");
    }


    private void addSampleAdopters() {
        // Active lifestyle adopter
        Adopter activeOwner = new Adopter(UUID.randomUUID().toString(), "John Smith", "john@example.com", "555-1234", "123 Active Lane");
        activeOwner.setPreferredSpecies("Dog");
        activeOwner.setPreferredBreed("Golden Retriever");
        activeOwner.setPreferredAgeMin(1);
        activeOwner.setPreferredAgeMax(5);
        activeOwner.setPreferredGender("Male");
        petMatchingService.addAdopter(activeOwner);

        Adopter calmOwner = new Adopter(UUID.randomUUID().toString(), "Emily Davis", "emily@example.com", "555-5678", "456 Quiet Street");
        calmOwner.setPreferredSpecies("Cat");
        calmOwner.setPreferredBreed("Siamese");
        calmOwner.setPreferredAgeMin(4);
        calmOwner.setPreferredAgeMax(8);
        calmOwner.setPreferredGender("Female");
        petMatchingService.addAdopter(calmOwner);

        Adopter familyOwner = new Adopter(UUID.randomUUID().toString(), "Michael Johnson", "michael@example.com", "555-9012", "789 Family Road");
        familyOwner.setPreferredSpecies("Dog");
        familyOwner.setPreferredBreed("Labrador");
        familyOwner.setPreferredAgeMin(1);
        familyOwner.setPreferredAgeMax(4);
        familyOwner.setPreferredGender("Female");
        petMatchingService.addAdopter(familyOwner);

        Adopter apartmentOwner = new Adopter(UUID.randomUUID().toString(), "Sarah Lee", "sarah@example.com", "555-3456", "Downtown Apartment");
        apartmentOwner.setPreferredSpecies("Rabbit");
        apartmentOwner.setPreferredAgeMin(0);
        apartmentOwner.setPreferredAgeMax(3);
        petMatchingService.addAdopter(apartmentOwner);

        System.out.println("Added 4 sample adopters to the system.");
    }


    public static void main(String[] args) {
        DataInitializer initializer = new DataInitializer();
        initializer.initializeSampleData();
    }
}