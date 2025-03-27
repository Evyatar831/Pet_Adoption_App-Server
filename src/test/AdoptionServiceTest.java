package test;


import AlgoClustering.HierarchicalClusteringAlgo;
import dao.AdopterFileDao;
import dao.AdoptionFileDao;
import dao.PetFileDao;
import dm.Adopter;
import dm.Adoption;
import dm.Pet;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import service.AdoptionService;
import service.PetMatchingService;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;


public class AdoptionServiceTest {

    private static final String TEST_DIR = "test-resources";
    private static final String PETS_FILE_PATH = TEST_DIR + "/pets.txt";
    private static final String ADOPTERS_FILE_PATH = TEST_DIR + "/adopters.txt";
    private static final String ADOPTIONS_FILE_PATH = TEST_DIR + "/adoptions.txt";

    private PetFileDao petDao;
    private AdopterFileDao adopterDao;
    private AdoptionFileDao adoptionDao;
    private AdoptionService adoptionService;
    private PetMatchingService petMatchingService;

    private Pet testPet;
    private Adopter testAdopter;

    @BeforeClass
    public static void setupTestDirectory() {
        File testDir = new File(TEST_DIR);
        if (!testDir.exists()) {
            testDir.mkdir();
        }
    }

    @Before
    public void setup() {
        petDao = new PetFileDao(PETS_FILE_PATH);
        adopterDao = new AdopterFileDao(ADOPTERS_FILE_PATH);
        adoptionDao = new AdoptionFileDao(ADOPTIONS_FILE_PATH);

        HierarchicalClusteringAlgo clusteringAlgo = new HierarchicalClusteringAlgo(HierarchicalClusteringAlgo.LinkageType.AVERAGE);

        petMatchingService = new PetMatchingService(petDao, adopterDao, clusteringAlgo);
        adoptionService = new AdoptionService(adoptionDao, petDao, adopterDao);

        setupTestData();
    }

    @After
    public void cleanup() {
        if (testPet != null) {
            petDao.delete(testPet.getId());
        }
        if (testAdopter != null) {
            adopterDao.delete(testAdopter.getId());
        }

        List<Adoption> adoptions = adoptionDao.getAll();
        for (Adoption adoption : adoptions) {
            adoptionDao.delete(adoption.getId());
        }
    }

    private void setupTestData() {
        testPet = new Pet();
        testPet.setId(UUID.randomUUID().toString());
        testPet.setName("TestDog");
        testPet.setSpecies("Dog");
        testPet.setBreed("Labrador");
        testPet.setAge(3);
        testPet.setGender("Male");
        testPet.setStatus("Available");
        petDao.save(testPet);

        testAdopter = new Adopter();
        testAdopter.setId(UUID.randomUUID().toString());
        testAdopter.setName("Test Adopter");
        testAdopter.setEmail("testadopter@example.com");
        testAdopter.setPhone("555-123-4567");
        testAdopter.setAddress("123 Test St");
        adopterDao.save(testAdopter);
    }

    @Test
    public void testCreateAdoptionRequest() {
        Adoption adoption = adoptionService.createAdoptionRequest(testPet.getId(), testAdopter.getId());

        assertNotNull("Adoption should not be null", adoption);
        assertEquals("Adoption should have Pending status", "Pending", adoption.getStatus());
        assertEquals("Adoption should reference the correct pet", testPet.getId(), adoption.getPetId());
        assertEquals("Adoption should reference the correct adopter", testAdopter.getId(), adoption.getAdopterId());

        Pet updatedPet = petDao.get(testPet.getId());
        assertEquals("Pet status should be updated to Pending", "Pending", updatedPet.getStatus());
    }

    @Test
    public void testUpdateAdoptionStatus() {
        Adoption adoption = adoptionService.createAdoptionRequest(testPet.getId(), testAdopter.getId());

        adoptionService.updateAdoptionStatus(adoption.getId(), "Approved");

        Adoption updatedAdoption = adoptionDao.get(adoption.getId());
        assertEquals("Adoption status should be updated to Approved", "Approved", updatedAdoption.getStatus());

        Pet updatedPet = petDao.get(testPet.getId());
        assertEquals("Pet status should remain as Pending", "Pending", updatedPet.getStatus());
    }

    @Test
    public void testCompleteAdoption() {
        Adoption adoption = adoptionService.createAdoptionRequest(testPet.getId(), testAdopter.getId());

        adoptionService.updateAdoptionStatus(adoption.getId(), "Approved");

        adoptionService.completeAdoption(adoption.getId());

        Adoption completedAdoption = adoptionDao.get(adoption.getId());
        assertEquals("Adoption status should be Completed", "Completed", completedAdoption.getStatus());

        Pet adoptedPet = petDao.get(testPet.getId());
        assertEquals("Pet status should be Adopted", "Adopted", adoptedPet.getStatus());
    }

    @Test
    public void testRejectedToApprovedTransition() {
        Adoption adoption = adoptionService.createAdoptionRequest(testPet.getId(), testAdopter.getId());

        adoptionService.updateAdoptionStatus(adoption.getId(), "Rejected");

        Adoption rejectedAdoption = adoptionDao.get(adoption.getId());
        assertEquals("Adoption status should be Rejected", "Rejected", rejectedAdoption.getStatus());

        adoptionService.updateAdoptionStatus(adoption.getId(), "Pending");

        Adoption pendingAdoption = adoptionDao.get(adoption.getId());
        assertEquals("Adoption status should be back to Pending", "Pending", pendingAdoption.getStatus());

        adoptionService.updateAdoptionStatus(adoption.getId(), "Approved");

        Adoption approvedAdoption = adoptionDao.get(adoption.getId());
        assertEquals("Adoption status should be Approved", "Approved", approvedAdoption.getStatus());
    }

    @Test
    public void testGetAdoptionByStatus() {
        Adoption adoption1 = adoptionService.createAdoptionRequest(testPet.getId(), testAdopter.getId());

        Pet testPet2 = new Pet();
        testPet2.setId(UUID.randomUUID().toString());
        testPet2.setName("TestCat");
        testPet2.setSpecies("Cat");
        testPet2.setBreed("Siamese");
        testPet2.setAge(2);
        testPet2.setGender("Female");
        testPet2.setStatus("Available");
        petDao.save(testPet2);

        Adoption adoption2 = new Adoption(UUID.randomUUID().toString(), testPet2.getId(), testAdopter.getId());
        adoption2.setStatus("Approved");
        adoptionDao.save(adoption2);

        List<Adoption> pendingAdoptions = adoptionService.getAdoptionsByStatus("Pending");
        assertTrue("Should find at least one pending adoption", pendingAdoptions.size() >= 1);

        List<Adoption> approvedAdoptions = adoptionService.getAdoptionsByStatus("Approved");
        assertTrue("Should find at least one approved adoption", approvedAdoptions.size() >= 1);

        petDao.delete(testPet2.getId());
    }

    @Test
    public void testMultipleAdoptionsForSamePet() {
        Adoption adoption1 = adoptionService.createAdoptionRequest(testPet.getId(), testAdopter.getId());

        adoptionService.updateAdoptionStatus(adoption1.getId(), "Approved");

        Adopter testAdopter2 = new Adopter();
        testAdopter2.setId(UUID.randomUUID().toString());
        testAdopter2.setName("Second Adopter");
        testAdopter2.setEmail("second@example.com");
        testAdopter2.setPhone("555-987-6543");
        testAdopter2.setAddress("456 Test Ave");
        adopterDao.save(testAdopter2);

        Adoption adoption2 = null;
        try {
            adoption2 = adoptionService.createAdoptionRequest(testPet.getId(), testAdopter2.getId());
        } catch (IllegalStateException e) {
        }

        adoptionService.completeAdoption(adoption1.getId());

        Pet updatedPet = petDao.get(testPet.getId());
        assertEquals("Pet should be marked as Adopted", "Adopted", updatedPet.getStatus());

        adopterDao.delete(testAdopter2.getId());
    }
}
