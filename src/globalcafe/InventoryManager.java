package globalcafe;
import java.io.*;
import java.util.*;

public class InventoryManager {
    private static final String FILE_PATH = "inventory.txt";

    // Initialize with default inventory if file doesn't exist
    static {
        initializeDefaultInventory();
    }

    public static Map<String, Integer> loadInventory() {
        Map<String, Integer> inventory = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    try {
                        String itemName = parts[0].trim();
                        int quantity = Integer.parseInt(parts[1].trim());
                        inventory.put(itemName, quantity);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid quantity format in inventory file for line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading inventory: " + e.getMessage());
            // Return empty map instead of crashing
        }
        return inventory;
    }

    public static void updateStock(String item, int change) {
        Map<String, Integer> inventory = loadInventory();
        int currentQuantity = inventory.getOrDefault(item, 0);
        int newQuantity = currentQuantity + change;
        
        // Prevent negative stock
        if (newQuantity < 0) {
            newQuantity = 0;
        }
        
        inventory.put(item, newQuantity);
        saveInventory(inventory);
    }

    public static void setStock(String item, int quantity) {
        Map<String, Integer> inventory = loadInventory();
        inventory.put(item, Math.max(0, quantity)); // Prevent negative quantities
        saveInventory(inventory);
    }

    public static int getStock(String item) {
        Map<String, Integer> inventory = loadInventory();
        return inventory.getOrDefault(item, 0);
    }

    private static void saveInventory(Map<String, Integer> inventory) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                bw.write(entry.getKey() + "=" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }

    private static void initializeDefaultInventory() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            Map<String, Integer> defaultInventory = new LinkedHashMap<>();
            // Add default items
            defaultInventory.put("Coffee Beans", 100);
            defaultInventory.put("Milk", 50);
            defaultInventory.put("Sugar", 200);
            defaultInventory.put("Tea Leaves", 75);
            defaultInventory.put("Croissants", 30);
            defaultInventory.put("Chocolate", 40);
            defaultInventory.put("Ice", 100);
            defaultInventory.put("Cups", 200);
            
            saveInventory(defaultInventory);
            System.out.println("Default inventory created.");
        }
    }

    // Method to add new items to inventory
    public static void addNewItem(String item, int initialQuantity) {
        Map<String, Integer> inventory = loadInventory();
        if (!inventory.containsKey(item)) {
            inventory.put(item, Math.max(0, initialQuantity));
            saveInventory(inventory);
        }
    }
}