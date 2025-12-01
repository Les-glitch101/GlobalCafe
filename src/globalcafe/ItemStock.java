package globalcafe;

import javafx.beans.property.*;

public class ItemStock {
    private final StringProperty name;
    private final IntegerProperty quantity;

    public ItemStock(String name, int quantity) {
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(Math.max(0, quantity)); // Prevent negative quantities
    }

    // Property getters
    public StringProperty nameProperty() { return name; }
    public IntegerProperty quantityProperty() { return quantity; }

    // Regular getters and setters
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    
    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int quantity) { this.quantity.set(Math.max(0, quantity)); }

    @Override
    public String toString() {
        return getName() + " - " + getQuantity() + " in stock";
    }
}