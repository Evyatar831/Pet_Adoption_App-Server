package dao;


import dm.Adopter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdopterFileDao implements IDao<Adopter> {
    private final String filePath;
    private List<Adopter> adopters;

    public AdopterFileDao(String filePath) {
        this.filePath = filePath;
        this.adopters = new ArrayList<>();
        loadFromFile();
    }

    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Created new adopters file: " + filePath);
                this.adopters = new ArrayList<>();
                return;
            } catch (IOException e) {
                System.err.println("Failed to create adopters file: " + filePath);
                e.printStackTrace();
                this.adopters = new ArrayList<>();
                return;
            }
        }

        if (file.length() == 0) {
            System.out.println("Adopters file is empty: " + filePath);
            this.adopters = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            this.adopters = (List<Adopter>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading adopters from file: " + filePath);
            e.printStackTrace();
            this.adopters = new ArrayList<>();
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
            oos.writeObject(this.adopters);
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
    public void save(Adopter adopter) {

        for (int i = 0; i < adopters.size(); i++) {
            if (adopters.get(i).getId().equals(adopter.getId())) {
                adopters.set(i, adopter);
                saveToFile();
                return;
            }
        }


        adopters.add(adopter);
        saveToFile();
    }

    @Override
    public void update(Adopter adopter) {
        for (int i = 0; i < adopters.size(); i++) {
            if (adopters.get(i).getId().equals(adopter.getId())) {
                adopters.set(i, adopter);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void delete(String id) {
        adopters.removeIf(adopter -> adopter.getId().equals(id));
        saveToFile();
    }

    @Override
    public Adopter get(String id) {
        for (Adopter adopter : adopters) {
            if (adopter.getId().equals(id)) {
                return adopter;
            }
        }
        return null;
    }

    @Override
    public List<Adopter> getAll() {
        return new ArrayList<>(adopters);
    }


    private boolean ensureFileExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {

                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    if (!parent.mkdirs()) {
                        System.err.println("Failed to create directories for: " + filePath);
                        return false;
                    }
                }


                if (!file.createNewFile()) {
                    System.err.println("Failed to create file: " + filePath);
                    return false;
                }

                return true;
            } catch (IOException e) {
                System.err.println("Error creating file: " + filePath);
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
