package globalcafe;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class SnacksMenu extends Application {

    private Cart cart; // ✅ Shared cart instance
    private BorderPane root;
    private Label cartLabel;

    // Default constructor for Application launch
    public SnacksMenu() {
        this.cart = new Cart(); // Create new cart when launched as Application
    }

    // Constructor for navigation with existing cart
    public SnacksMenu(Cart cart) {
        this.cart = cart; // Use existing cart when navigating back
    }

    @Override
    public void start(Stage primaryStage) {
        this.root = createContent(primaryStage);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Global Cafe - Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createContent(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(1366, 768);
        root.setStyle("-fx-background-color: #FFFBE4;");

        VBox mainContent = new VBox(20); // spacing between topSection and scrollPane
        mainContent.setPadding(new Insets(20, 0, 0, 0));

        mainContent.setStyle("-fx-background-color: #EEDDAF;");
        mainContent.setAlignment(Pos.TOP_CENTER);

        VBox topSection = new VBox(15);
        topSection.setPadding(new Insets(0));

        // Logo + Cart info
        HBox logoCartBox = new HBox(20);
        logoCartBox.setAlignment(Pos.CENTER_LEFT);

        Image logoImage = loadImage("globalcafe/images/Rectangle 14.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);

        cartLabel = new Label("Cart (" + cart.getItemCount() + ")");
        cartLabel.setFont(Font.font("Arial", 20));
        cartLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

        cartLabel.setFont(Font.font("Arial", 20));
        cartLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

        Image profileIcon = loadImage("globalcafe/images/User.png");
        ImageView profileView = new ImageView(profileIcon);
        profileView.setFitWidth(36);
        profileView.setFitHeight(36);
        Button profileBtn = new Button();
        profileBtn.setGraphic(profileView);
        profileBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        profileBtn.setPrefSize(40, 40);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        logoCartBox.getChildren().addAll(logoView, spacer, cartLabel, profileBtn);

        Label greeting = new Label("Hey Les, What are you feeling today???");
        greeting.setFont(Font.font("Arial", 28));
        greeting.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(600);
        searchField.setStyle("-fx-font-size: 18px; -fx-background-radius: 10;");

        HBox categoryBox = new HBox(30);
        categoryBox.setAlignment(Pos.CENTER);
        Button coffeeBtn = new Button("Coffee");
        Button teaBtn = new Button("Tea");
        Button snacksBtn = new Button("Snacks");
        Button bakesBtn = new Button("Bakes");

// Style all buttons
        for (Button btn : new Button[]{coffeeBtn, teaBtn, snacksBtn, bakesBtn}) {
            btn.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");
        }

// Highlight active tab
        snacksBtn.setStyle("-fx-background-color: #E5EB89; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

// Navigation actions
        coffeeBtn.setOnAction(e -> stage.setScene(new CoffeeMenu(cart).getRoot(stage).getScene()));
        teaBtn.setOnAction(e -> stage.setScene(new TeaMenu(cart).getRoot(stage).getScene()));
        snacksBtn.setOnAction(e -> stage.setScene(new SnacksMenu(cart).getRoot(stage).getScene()));
        bakesBtn.setOnAction(e -> stage.setScene(new BakesMenu(cart).getRoot(stage).getScene()));

        categoryBox.getChildren().addAll(coffeeBtn, teaBtn, snacksBtn, bakesBtn);
        topSection.getChildren().addAll(logoCartBox, greeting, searchField, categoryBox);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent;");

        GridPane itemGrid = new GridPane();
        itemGrid.setPadding(new Insets(20));
        itemGrid.setHgap(30);
        itemGrid.setVgap(30);
        itemGrid.setAlignment(Pos.TOP_CENTER);

        String[] itemNames = {
            "Bowl of nuts", "Bowl of popcorn", "Bowl of fruits", "Bowl of Biltong",
            "Bowl of samussa", "Healthy Bowl", "Bowl of chips", "Bowl of sweets"
        };
        int[] prices = {25, 25, 20, 20, 20, 25, 25, 35};
        String[] imagePaths = {
            "globalcafe/images/bowlofnuts.png", "globalcafe/images/bowlofpopcorn.png", "globalcafe/images/bowloffruit.png", "globalcafe/images/bowlofbiltong.png",
            "globalcafe/images/bowlofsamussa.png", "globalcafe/images/healthybowl.png", "globalcafe/images/bowlofchips.png", "globalcafe/images/bowlofsweets.png"
        };

        int col = 0, row = 0;
        for (int i = 0; i < itemNames.length; i++) {
            VBox itemBox = createMenuItem(itemNames[i], prices[i], imagePaths[i], stage);
            itemGrid.add(itemBox, col, row);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        scrollPane.setContent(itemGrid);
        mainContent.getChildren().addAll(topSection, scrollPane);
        root.setCenter(mainContent);

        HBox bottomBox = new HBox(30);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));

        Button viewCartBtn = new Button("View Cart (" + cart.getItemCount() + ")");
        viewCartBtn.setPrefWidth(300);
        viewCartBtn.setPrefHeight(60);
        viewCartBtn.setStyle("-fx-background-color: #F6CA7D; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 15;");

        viewCartBtn.setOnAction(e -> {
            CartPage cartPage = new CartPage(cart);
            Scene cartScene = new Scene(cartPage.getRoot(stage), 1366, 768);
            stage.setScene(cartScene);
        });

        Button checkoutBtn = new Button("Checkout");
        checkoutBtn.setPrefWidth(300);
        checkoutBtn.setPrefHeight(60);
        checkoutBtn.setStyle("-fx-background-color: #E5EB89; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 15;");

        bottomBox.getChildren().addAll(viewCartBtn, checkoutBtn);
        root.setBottom(bottomBox);
        
                coffeeBtn.setOnAction(e -> {
    CoffeeMenu coffeeMenu = new CoffeeMenu(cart);
    Scene scene = new Scene(coffeeMenu.getRoot(stage), 1366, 768);
    stage.setScene(scene);
});

        teaBtn.setOnAction(e -> {
    TeaMenu teaMenu = new TeaMenu(cart);
    Scene scene = new Scene(teaMenu.getRoot(stage), 1366, 768);
    stage.setScene(scene);
});

snacksBtn.setOnAction(e -> {
    SnacksMenu snacksMenu = new SnacksMenu(cart);
    Scene scene = new Scene(snacksMenu.getRoot(stage), 1366, 768);
    stage.setScene(scene);
});

bakesBtn.setOnAction(e -> {
    BakesMenu bakesMenu = new BakesMenu(cart);
    Scene scene = new Scene(bakesMenu.getRoot(stage), 1366, 768);
    stage.setScene(scene);
});

        return root;
    }

    private VBox createMenuItem(String name, int price, String imagePath, Stage stage) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        Image img = loadImage(imagePath);
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(150);
        imgView.setFitHeight(150);
        imgView.setPreserveRatio(true);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", 18));
        nameLabel.setStyle("-fx-text-fill: black;");

        Label priceLabel = new Label("R" + price);
        priceLabel.setFont(Font.font("Arial", 16));
        priceLabel.setStyle("-fx-text-fill: black;");

        Button addBtn = new Button("+ Add to cart");
        addBtn.setStyle("-fx-background-color: #A8D5BA; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10;");
        addHoverEffect(addBtn, "#E5EB89");

        addBtn.setOnAction(e -> {
            // Items go straight to cart
            CartItem item = new CartItem(name, price, imagePath);
            cart.addItem(item);

            // Update cart label at the top
            cartLabel.setText("Cart (" + cart.getItemCount() + ")");

            // Show confirmation popup
            showAddedPopup(name);
        });

        box.getChildren().addAll(imgView, nameLabel, priceLabel, addBtn);
        return box;
    }

    private void showAddedPopup(String itemName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Added to Cart");
        alert.setHeaderText(null);
        alert.setContentText(itemName + " has been added to your cart.");
        alert.showAndWait();
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

    public BorderPane getRoot(Stage stage) {
        if (root == null) {
            this.root = createContent(stage);
        }
        return root;
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

    public static void main(String[] args) {
        launch(args);
    }
}
