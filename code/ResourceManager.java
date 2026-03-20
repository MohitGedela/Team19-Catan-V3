package code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

class ResourceManager {

    private Map<ResourceType, Integer> resources;

    public ResourceManager() {
        this.resources = new HashMap<>();
    }

    public ResourceManager(Map<ResourceType, Integer> initialResources) {
        this.resources = new HashMap<>(initialResources);
    }

    public void add(ResourceType resource, int quantity) {
        resources.put(resource, resources.getOrDefault(resource, 0) + quantity);
    }

    public boolean remove(ResourceType resource, int quantity) {
        int have = resources.getOrDefault(resource, 0);
        if (have < quantity) {
            return false;
        }
        resources.put(resource, have - quantity);
        return true;
    }

    public boolean has(ResourceType resource, int quantity) {
        return resources.getOrDefault(resource, 0) >= quantity;
    }

    public int getTotal() {
        int total = 0;
        for (int amount : resources.values()) {
            total += amount;
        }
        return total;
    }

    public ResourceType getRandomResource(Random random) {
        List<ResourceType> total = new ArrayList<>();
        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                total.add(entry.getKey());
            }
        }
        return total.get(random.nextInt(total.size()));
    }

    public Map<ResourceType, Integer> getResourceMap() {
        return new HashMap<>(resources);
    }
}