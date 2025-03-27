package dm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Adopter implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;

    private String preferredSpecies;
    private String preferredBreed;
    private int preferredAgeMin;
    private int preferredAgeMax;
    private String preferredGender;

    private Map<String, Double> preferences;

    public Adopter() {
        this.preferences = new HashMap<>();
        this.preferredAgeMin = 0;
        this.preferredAgeMax = 30;
    }

    public Adopter(String id, String name, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.preferences = new HashMap<>();
        this.preferredAgeMin = 0;
        this.preferredAgeMax = 30;
    }

    public void addPreference(String trait, Double value) {
        preferences.put(trait, value);
    }

    public Map<String, Double> getPreferencesMap() {
        Map<String, Double> preferencesMap = new HashMap<>(preferences);

        if (preferredSpecies != null && !preferredSpecies.isEmpty()) {
            preferencesMap.put("species:" + preferredSpecies, 5.0);
        }

        if (preferredBreed != null && !preferredBreed.isEmpty()) {
            preferencesMap.put("breed:" + preferredBreed, 5.0);
        }

        preferencesMap.put("age:min", (double) preferredAgeMin);
        preferencesMap.put("age:max", (double) preferredAgeMax);

        if (preferredGender != null && !preferredGender.isEmpty()) {
            preferencesMap.put("gender:" + preferredGender, 5.0);
        }

        return preferencesMap;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPreferredSpecies() { return preferredSpecies; }
    public void setPreferredSpecies(String preferredSpecies) { this.preferredSpecies = preferredSpecies; }

    public String getPreferredBreed() { return preferredBreed; }
    public void setPreferredBreed(String preferredBreed) { this.preferredBreed = preferredBreed; }

    public int getPreferredAgeMin() { return preferredAgeMin; }
    public void setPreferredAgeMin(int preferredAgeMin) { this.preferredAgeMin = preferredAgeMin; }

    public int getPreferredAgeMax() { return preferredAgeMax; }
    public void setPreferredAgeMax(int preferredAgeMax) { this.preferredAgeMax = preferredAgeMax; }

    public String getPreferredGender() { return preferredGender; }
    public void setPreferredGender(String preferredGender) { this.preferredGender = preferredGender; }
}