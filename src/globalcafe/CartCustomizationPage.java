package globalcafe;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class CartCustomizationPage {

    private BorderPane root;
    private Cart cart;
    private String itemName;
    private double basePrice;
    private String imagePath;
    private Stage stage;

    // Toggle groups for capturing selections
    private ToggleGroup sizeGroup;
    private ToggleGroup shotsGroup;
    private ToggleGroup milkGroup;
    private ToggleGroup sweetenerGroup;

    public CartCustomizationPage(Cart cart, String itemName, double basePrice, String imagePath, Stage stage) {
        this.cart = cart;
        this.itemName = itemName;
        this.basePrice = basePrice;
        this.imagePath = imagePath;
        this.stage = stage;
        this.root = createContent();
    }

    public BorderPane getRoot() {
        return root;
    }

    private BorderPane createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(1366, 768);
        root.setStyle("-fx-background-color: #FFFBE4;");

        // ✅ TOP SECTION
        HBox topBox = new HBox(20);
        topBox.setPadding(new Insets(0));
        topBox.setAlignment(Pos.CENTER_LEFT);

        Image logoImage = loadImage("globalcafe/images/Rectangle 14.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label cartLabel = new Label("Cart (" + cart.getItemCount() + ")");
        cartLabel.setFont(Font.font("Arial", 20));
        cartLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

        topBox.getChildren().addAll(logoView, spacer, cartLabel);
        root.setTop(topBox);

        // ✅ CENTER SECTION
        GridPane centerGrid = new GridPane();
        centerGrid.setPadding(new Insets(30));
        centerGrid.setHgap(50);
        centerGrid.setVgap(30);
        centerGrid.setAlignment(Pos.CENTER);
        centerGrid.setStyle(
                "-fx-background-color: #F7EFD7;"
                + "-fx-background-radius: 15;"
                + "-fx-border-color: #F7EFD7;"
                + "-fx-border-radius: 15;"
                + "-fx-border-width: 2;"
                + "-fx-padding: 30;"
        );

        // Item image and name
        VBox itemBox = new VBox(10);
        itemBox.setAlignment(Pos.CENTER);

        Image itemImage = loadImage(imagePath);
        ImageView itemView = new ImageView(itemImage);
        itemView.setFitWidth(200);
        itemView.setPreserveRatio(true);

        Label itemNameLabel = new Label(itemName + " R" + basePrice);
        itemNameLabel.setFont(Font.font("Arial", 22));
        itemNameLabel.setStyle("-fx-text-fill: black;");

        itemBox.getChildren().addAll(itemView, itemNameLabel);
        centerGrid.add(itemBox, 0, 0);

        // Size options
        VBox sizeBox = createOptionGroup("Size:", new String[]{"Small R25", "Medium R30", "Large R35", "Mega R40"}, "size");
        centerGrid.add(sizeBox, 1, 0);

        // Shots options
        VBox shotsBox = createOptionGroup("Shots:", new String[]{"1 shot", "2 shots +R2", "3 shots +R4", "4 shots +R6"}, "shots");
        centerGrid.add(shotsBox, 2, 0);

        // Milk options
        VBox milkBox = createOptionGroup("Type of milk:", new String[]{"Whole milk", "Oat milk +R2", "Soy milk +R2", "Almond milk +R2", "Coconut milk +R2"}, "milk");
        centerGrid.add(milkBox, 0, 1);

        // Sweeteners options
        VBox sweetBox = createOptionGroup("Sweeteners and syrup:", new String[]{"Vanilla +R5", "Caramel +R5", "Hazelnut +R5", "Mocha +R5"}, "sweetener");
        centerGrid.add(sweetBox, 1, 1);

        root.setCenter(centerGrid);

        // ✅ BOTTOM SECTION
        Button addToCartBtn = new Button("Add to cart");
        addToCartBtn.setPrefWidth(400);
        addToCartBtn.setPrefHeight(60);
        addToCartBtn.setStyle("-fx-background-color: #D3D3D3; -fx-font-size: 22px; -fx-font-weight: bold; -fx-background-radius: 15;");

        addToCartBtn.setOnAction(e -> addItemToCart());
        addHoverEffect(addToCartBtn, "#C0C0C0");

        VBox bottomBox = new VBox(addToCartBtn);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));
        root.setBottom(bottomBox);

        return root;
    }

    private void addItemToCart() {
        try {
            // Create cart item
            CartItem item = new CartItem(itemName, basePrice, imagePath);

            // Apply customizations with price adjustments
            applyCustomizations(item);

            // Add to cart
            cart.addItem(item);

            // Show confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Item Added");
            alert.setHeaderText(null);
            alert.setContentText(itemName + " has been added to your cart!\nTotal: R" + item.getFinalPrice());
            alert.showAndWait();

            // Navigate back to CoffeeMenu
            CoffeeMenu menuPage = new CoffeeMenu(cart);
            Scene menuScene = new Scene(menuPage.getRoot(stage), 1366, 768);
            stage.setScene(menuScene);

        } catch (Exception e) {
            System.out.println("Error adding item to cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void applyCustomizations(CartItem item) {
        // Apply size customization
        if (sizeGroup.getSelectedToggle() != null) {
            RadioButton selected = (RadioButton) sizeGroup.getSelectedToggle();
            String sizeText = selected.getText();
            double extra = 0;

            if (sizeText.contains("Medium")) {
                extra = 5;
            } else if (sizeText.contains("Large")) {
                extra = 10;
            } else if (sizeText.contains("Mega")) {
                extra = 15;
            }

            item.setSize(sizeText.split(" R")[0], extra);
        }

        // Apply shots customization
        if (shotsGroup.getSelectedToggle() != null) {
            RadioButton selected = (RadioButton) shotsGroup.getSelectedToggle();
            String shotsText = selected.getText();
            double extra = 0;

            if (shotsText.contains("+R2")) {
                extra = 2;
            } else if (shotsText.contains("+R4")) {
                extra = 4;
            } else if (shotsText.contains("+R6")) {
                extra = 6;
            }

            item.setShots(shotsText.split(" \\+")[0], extra);
        }

        // Apply milk customization
        if (milkGroup.getSelectedToggle() != null) {
            RadioButton selected = (RadioButton) milkGroup.getSelectedToggle();
            String milkText = selected.getText();
            double extra = milkText.contains("+R2") ? 2 : 0;

            item.setMilk(milkText.split(" \\+")[0], extra);
        }

        // Apply syrup customization
        if (sweetenerGroup.getSelectedToggle() != null) {
            RadioButton selected = (RadioButton) sweetenerGroup.getSelectedToggle();
            String syrupText = selected.getText();
            double extra = syrupText.contains("+R5") ? 5 : 0;

            item.setSyrup(syrupText.split(" \\+")[0], extra);
        }
    }

    private VBox createOptionGroup(String title, String[] options, String type) {
        VBox box = new VBox(10);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 20));
        titleLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

        ToggleGroup group = new ToggleGroup();

        // Store the toggle group based on type
        switch (type) {
            case "size":
                sizeGroup = group;
                break;
            case "shots":
                shotsGroup = group;
                break;
            case "milk":
                milkGroup = group;
                break;
            case "sweetener":
                sweetenerGroup = group;
                break;
        }

        for (String opt : options) {
            RadioButton rb = new RadioButton(opt);
            rb.setFont(Font.font("Arial", 18));
            rb.setStyle("-fx-text-fill: black;");
            rb.setToggleGroup(group);

            // Select first option by default
            if (group.getToggles().isEmpty()) {
                rb.setSelected(true);
            }

            box.getChildren().add(rb);
        }

        box.getChildren().add(0, titleLabel);
        return box;
    }

    private void addHoverEffect(Button button, String hoverColor) {
        String originalStyle = button.getStyle();
        button.setOnMouseEntered(e -> {
            button.setStyle(originalStyle.replaceFirst("#[A-Fa-f0-9]{6}", hoverColor));
            button.setScaleX(1.05);
            button.setScaleY(1.05);
        });
        button.setOnMouseExited(e -> {
            button.setStyle(originalStyle);
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
    }

    private Image loadImage(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return new Image(new FileInputStream(file));
            }

            InputStream stream = getClass().getResourceAsStream("/" + filePath);
            if (stream != null) {
                return new Image(stream);
            }

            System.out.println("Image not found: " + filePath);
            return null;
        } catch (Exception e) {
            System.out.println("Error loading image: " + filePath + " - " + e.getMessage());
            return null;
        }
    }
}
