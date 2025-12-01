package globalcafe;

public class CartItem {

    private String name;
    private double price;
    private String imagePath;
    private int quantity; // quantity for snacks or beverage units

    // Beverage customization fields
    private String size = null;
    private String shots = null;
    private String milk = null;
    private String syrup = null;

    private double extraPrice = 0;
    private double finalPrice;

    public CartItem(String name, double price, String imagePath) {
        this(name, price, imagePath, 1);   // default quantity = 1 (for snacks)
    }

    // Constructor for beverages (with customization text)
    public CartItem(String name, double price, String imagePath, String customizationText) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.quantity = 1;
        this.finalPrice = price;
    }

    // Constructor for snacks (no customization)
    public CartItem(String name, double price, String imagePath, int quantity) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.quantity = quantity;
        this.finalPrice = price * quantity;
    }

    // Beverage customization setters
    public void setSize(String size, double extra) {
        this.size = size;
        this.extraPrice += extra;
        updateFinalPrice();
    }

    public void setShots(String shots, double extra) {
        this.shots = shots;
        this.extraPrice += extra;
        updateFinalPrice();
    }

    public void setMilk(String milk, double extra) {
        this.milk = milk;
        this.extraPrice += extra;
        updateFinalPrice();
    }

    public void setSyrup(String syrup, double extra) {
        this.syrup = syrup;
        this.extraPrice += extra;
        updateFinalPrice();
    }

    private void updateFinalPrice() {
        this.finalPrice = (price + extraPrice) * quantity;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateFinalPrice();
    }

    public String getCustomizationText() {
        if (size == null && shots == null && milk == null && syrup == null) {
            return ""; // snacks have no customization
        }
        return "Size: " + size +
               "\nShots: " + shots +
               "\nMilk: " + milk +
               "\nSyrup: " + syrup;
    }
}
