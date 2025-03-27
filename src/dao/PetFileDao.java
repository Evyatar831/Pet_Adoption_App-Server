package dao;



import dm.Pet;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class PetFileDao implements IDao<Pet> {
    private final String filePath;
    private List<Pet> pets;

    public PetFileDao(String filePath) {
        this.filePath = filePath;
        this.pets = new ArrayList<>();
        loadFromFile();
    }


    @Override
    public void save(Pet pet) {
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getId().equals(pet.getId())) {
                pets.set(i, pet);
                boolean saved = saveToFile();
                System.out.println("Pet updated, save result: " + saved);
                return;
            }
        }

        pets.add(pet);
        boolean saved = saveToFile();
        System.out.println("New pet added, save result: " + saved);

        loadFromFile();
        System.out.println("After save verification - Pets in memory: " + pets.size());
        boolean found = false;
        for (Pet p : pets) {
            if (p.getId().equals(pet.getId())) {
                found = true;
                break;
            }
        }
        System.out.println("Saved pet found in reloaded data: " + found);
    }
    @Override
    public void update(Pet pet) {
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getId().equals(pet.getId())) {
                pets.set(i, pet);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void delete(String id) {
        System.out.println("PetFileDao: Deleting pet with ID: " + id);

        int sizeBefore = pets.size();
        pets.removeIf(pet -> pet.getId().equals(id));
        int sizeAfter = pets.size();

        boolean removed = sizeBefore > sizeAfter;
        System.out.println("PetFileDao: Pet removal from memory: " + (removed ? "SUCCESS" : "FAILED"));

        if (removed) {
            boolean saveSuccess = simpleSaveToFile();
            System.out.println("PetFileDao: Save to file result: " + saveSuccess);
        }
    }

    private boolean simpleSaveToFile() {
        System.out.println("PetFileDao: Saving " + pets.size() + " pets to file: " + filePath);

        try {
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try (FileOutputStream fos = new FileOutputStream(filePath);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(pets);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveToFile() {
        System.out.println("PetFileDao: Saving " + pets.size() + " pets to file: " + filePath);

        try {
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try (FileOutputStream fos = new FileOutputStream(filePath);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(pets);
                System.out.println("Successfully saved pets to file");
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void loadFromFile() {
        System.out.println("PetFileDao: Loading pets from file: " + filePath);

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist, creating empty pets list");
            this.pets = new ArrayList<>();
            return;
        }

        if (file.length() == 0) {
            System.out.println("File is empty, creating empty pets list");
            this.pets = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            this.pets = (List<Pet>) ois.readObject();
            System.out.println("PetFileDao: Successfully loaded " + pets.size() + " pets from file");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading pets from file: " + e.getMessage());
            e.printStackTrace();
            this.pets = new ArrayList<>();
        }
    }

    @Override
    public Pet get(String id) {
        for (Pet pet : pets) {
            if (pet.getId().equals(id)) {
                return pet;
            }
        }
        return null;
    }

    @Override
    public List<Pet> getAll() {
        return new ArrayList<>(pets);
    }

}
