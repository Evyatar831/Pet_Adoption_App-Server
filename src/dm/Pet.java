package dm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Pet implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String species;
    private String breed;
    private int age;
    private String gender;
    private String status; // "Available", "Pending", "Adopted"

    // for clustering algorithm compatibility
    private Map<String, Double> characteristics;

    public Pet() {
        this.status = "Available";
        this.characteristics = new HashMap<>();
    }

    public Pet(String id, String name, String species, String breed, int age, String gender) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.status = "Available";
        this.characteristics = new HashMap<>();
    }


    public Map<String, Double> getCharacteristicsMap() {
        Map<String, Double> characteristicsMap = new HashMap<>(characteristics);

        if (species != null && !species.isEmpty()) {
            characteristicsMap.put("species:" + species, 5.0);
        }

        if (breed != null && !breed.isEmpty()) {
            characteristicsMap.put("breed:" + breed, 5.0);
        }

        characteristicsMap.put("age", (double) age);

        if (gender != null && !gender.isEmpty()) {
            characteristicsMap.put("gender:" + gender, 5.0);
        }

        return characteristicsMap;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}