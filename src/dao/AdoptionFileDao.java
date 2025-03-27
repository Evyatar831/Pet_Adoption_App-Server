package dao;


import dm.Adoption;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdoptionFileDao implements IDao<Adoption> {
    private final String filePath;
    private List<Adoption> adoptions;

    public AdoptionFileDao(String filePath) {
        this.filePath = filePath;
        this.adoptions = new ArrayList<>();
        loadFromFile();
    }

    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Created new adoptions file: " + filePath);
                this.adoptions = new ArrayList<>();
                return;
            } catch (IOException e) {
                System.err.println("Failed to create adoptions file: " + filePath);
                e.printStackTrace();
                this.adoptions = new ArrayList<>();
                return;
            }
        }

        if (file.length() == 0) {
            System.out.println("Adoptions file is empty: " + filePath);
            this.adoptions = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            this.adoptions = (List<Adoption>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading adoptions from file: " + filePath);
            e.printStackTrace();
            this.adoptions = new ArrayList<>();
        }
    }

    private void saveToFile() {
        File file = new File(filePath);
        File parentDir = file.getParentFile();


        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                System.err.println("ERROR: Failed to create parent directory for: " + filePath);
            }
        }

        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.adoptions);
            System.out.println("Successfully saved data to: " + filePath);
        } catch (IOException e) {
            System.err.println("ERROR saving data to file: " + filePath);
            e.printStackTrace();


            System.err.println("File exists: " + file.exists());
            System.err.println("File can write: " + file.canWrite());
            System.err.println("Free disk space: " + file.getFreeSpace() + " bytes");
        }
    }

    @Override
    public void save(Adoption adoption) {

        for (int i = 0; i < adoptions.size(); i++) {
            if (adoptions.get(i).getId().equals(adoption.getId())) {
                adoptions.set(i, adoption);
                saveToFile();
                return;
            }
        }


        adoptions.add(adoption);
        saveToFile();
    }

    @Override
    public void update(Adoption adoption) {
        for (int i = 0; i < adoptions.size(); i++) {
            if (adoptions.get(i).getId().equals(adoption.getId())) {
                adoptions.set(i, adoption);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void delete(String id) {
        adoptions.removeIf(adoption -> adoption.getId().equals(id));
        saveToFile();
    }

    @Override
    public Adoption get(String id) {
        for (Adoption adoption : adoptions) {
            if (adoption.getId().equals(id)) {
                return adoption;
            }
        }
        return null;
    }

    @Override
    public List<Adoption> getAll() {
        return new ArrayList<>(adoptions);
    }


}
