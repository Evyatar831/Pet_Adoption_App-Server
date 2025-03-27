package service;


import AlgoClustering.IAlgoClustering;
import AlgoClustering.KMeansClusteringAlgo;
import dao.AdopterFileDao;
import dao.AdoptionFileDao;
import dao.IDao;
import dao.PetFileDao;
import dm.Adopter;
import dm.Adoption;
import dm.Pet;

import java.io.File;

public class ServiceLocator {

    private static final String DATA_DIR = "resources";
    private static final String PET_FILE = DATA_DIR +  "/datasource_pets.txt";
    private static final String ADOPTER_FILE = DATA_DIR +  "/datasource_adopters.txt";
    private static final String ADOPTION_FILE = DATA_DIR +  "/datasource_adoptions.txt";

    private static IDao<Pet> petDao;
    private static IDao<Adopter> adopterDao;
    private static IDao<Adoption> adoptionDao;
    private static IAlgoClustering clusteringAlgo;
    private static PetMatchingService petMatchingService;
    private static AdoptionService adoptionService;

    static {
        initialize();
    }

    private static void initialize() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            boolean created = dataDir.mkdirs();
            if (!created) {
                System.err.println("ERROR: Failed to create data directory: " + DATA_DIR);
            } else {
                System.out.println("Created data directory: " + DATA_DIR);
            }
        }

        petDao = new PetFileDao(PET_FILE);
        adopterDao = new AdopterFileDao(ADOPTER_FILE);
        adoptionDao = new AdoptionFileDao(ADOPTION_FILE);

        clusteringAlgo = new KMeansClusteringAlgo(100);

        petMatchingService = new PetMatchingService(petDao, adopterDao, clusteringAlgo);
        adoptionService = new AdoptionService(adoptionDao, petDao, adopterDao);
    }

    public static PetMatchingService getPetMatchingService() {
        return petMatchingService;
    }

    public static AdoptionService getAdoptionService() {
        return adoptionService;
    }
}
