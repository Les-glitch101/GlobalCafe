package globalcafe;

import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class EnergizedPage {

    private Cart cart;
    private StackPane root;

    // ----------------------------------------------------------
    //  Constructor – receives SAME cart from MoodSelectionPage
    // ----------------------------------------------------------
    public EnergizedPage(Cart cart) {
    System.out.println("EnergizedPage constructor CALLED");
    this.cart = (cart != null) ? cart : new Cart();
    this.root = createContent();
}


    public StackPane getRoot() {
        return root;
    }

    // ----------------------------------------------------------
    //  Create UI
    // ----------------------------------------------------------
    private StackPane createContent() {
        root = new StackPane();
        root.setPrefSize(1366, 768);

        // Background
        Image bgImage = loadImage("globalcafe/images/allison-christine-VgU1WYSSoWA-unsplash.jpg");
        if (bgImage != null) {
            BackgroundImage bg = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, true)
            );
            root.setBackground(new Background(bg));
        } else {
            root.setStyle("-fx-background-color: #2c3e50;");
            System.out.println("Background image not found");
        }

        // Logo
        Image logoImage = loadImage("globalcafe/images/Rectangle 14.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(200);
        logoView.setPreserveRatio(true);
        logoView.setOpacity(0.5);

        StackPane logoContainer = new StackPane(logoView);
        logoContainer.setAlignment(Pos.TOP_LEFT);
        logoContainer.setPadding(new Insets(0));

        // Back button
        Button backButton = new Button("←");
        backButton.setStyle(
                "-fx-background-color: #EEDDAF; -fx-font-size: 14px; " +
                "-fx-font-weight: bold; -fx-background-radius: 10;"
        );
        backButton.setOnAction(e -> {
            System.out.println("Back button clicked");
            openMoodSelectionPage();
        });

        HBox topBar = new HBox(10, backButton, logoContainer);
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setPadding(new Insets(20));

        // Mood banner
        Image moodImage = loadImage("globalcafe/images/Rectangle 23.png");
        ImageView moodView = new ImageView(moodImage);
        moodView.setFitWidth(250);
        moodView.setPreserveRatio(true);

        StackPane moodBanner = new StackPane(moodView);
        moodBanner.setAlignment(Pos.TOP_CENTER);

        // Suggestions
        VBox suggestions = new VBox(40);
        suggestions.setAlignment(Pos.CENTER);

        HBox suggestion1 = createSuggestionBlock(
                new String[]{"Ice Coffee", "Healthy Bowl", "3 Chocolate muffins"},
                new String[]{
                        "globalcafe/images/icecoffee.png",
                        "globalcafe/images/healthybowl.png",
                        "globalcafe/images/muffins.png"
                },
                new int[]{25, 25, 25},
                75,
                "energized",
                1
        );

        HBox suggestion2 = createSuggestionBlock(
                new String[]{"Rooibos tea", "Bowl of nuts", "2 Slices of choc bread"},
                new String[]{
                        "globalcafe/images/rooibos.png",
                        "globalcafe/images/nuts.png",
                        "globalcafe/images/chocbread.png"
                },
                new int[]{15, 25, 15},
                55,
                "energized",
                2
        );

        suggestions.getChildren().addAll(suggestion1, suggestion2);

        VBox content = new VBox(50, moodBanner, suggestions);
        content.setAlignment(Pos.CENTER);

        StackPane main = new StackPane(content, topBar);
        StackPane.setAlignment(content, Pos.CENTER);
        StackPane.setAlignment(topBar, Pos.TOP_LEFT);

        // Responsive scaling
        content.scaleXProperty().bind(
                Bindings.min(
                        root.widthProperty().divide(1366),
                        root.heightProperty().divide(768)
                )
        );
        content.scaleYProperty().bind(content.scaleXProperty());

        root.getChildren().add(main);
        return root;
    }

    // ----------------------------------------------------------
    //  Creates suggestion block with working Add-To-Cart
    // ----------------------------------------------------------
    private HBox createSuggestionBlock(
            String[] itemNames,
            String[] imagePaths,
            int[] prices,
            int totalPrice,
            String menu,
            int blockNumber
    ) {
        HBox block = new HBox(20);
        block.setAlignment(Pos.CENTER);

        // Item tiles
        for (int i = 0; i < itemNames.length; i++) {
            VBox box = new VBox(10);
            box.setAlignment(Pos.CENTER);

            Image img = loadImage(imagePaths[i]);
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(120);
            imgView.setFitHeight(120);
            imgView.setPreserveRatio(true);

            Label name = new Label(itemNames[i]);
            name.setFont(Font.font("Arial", 18));
            name.setStyle("-fx-text-fill: white;");

            Label price = new Label("R" + prices[i]);
            price.setFont(Font.font("Arial", 16));
            price.setStyle("-fx-text-fill: white;");

            box.getChildren().addAll(imgView, name, price);
            block.getChildren().add(box);
        }

        // Total + Add button
        VBox totalBox = new VBox(10);
        totalBox.setAlignment(Pos.CENTER);

        Label totalLabel = new Label("Total R" + totalPrice);
        totalLabel.setFont(Font.font("Arial", 20));
        totalLabel.setStyle("-fx-text-fill: white;");

        Button add = new Button("+ Add to cart");
        add.setPrefWidth(150);
        add.setStyle(
                "-fx-background-color: #A8D5BA; -fx-font-size: 16px; " +
                "-fx-font-weight: bold; -fx-cursor: hand;"
        );

        // Hover effect
        add.setOnMouseEntered(e ->
                add.setStyle(
                        "-fx-background-color: #90C9A7; -fx-font-size: 16px; " +
                        "-fx-font-weight: bold; -fx-cursor: hand;"
                )
        );
        add.setOnMouseExited(e ->
                add.setStyle(
                        "-fx-background-color: #A8D5BA; -fx-font-size: 16px; " +
                        "-fx-font-weight: bold; -fx-cursor: hand;"
                )
        );

        // Click handler
        add.setOnAction(e -> {
            System.out.println("Add to cart button clicked for block " + blockNumber);

            // Add all items to cart
            for (int i = 0; i < itemNames.length; i++) {
                CartItem item = new CartItem(itemNames[i], prices[i], imagePaths[i]);
                cart.addItem(item);
                System.out.println("Added: " + itemNames[i] + " R" + prices[i]);
            }

            System.out.println("Cart now has " + cart.getItemCount() + " items");
            System.out.println("Cart total: R" + cart.getTotal());

            // Show alert without waiting (auto-close)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Added to Cart");
            alert.setContentText("Your items were added successfully!");
            alert.show();

            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(ev -> {
                alert.close();
                System.out.println("Alert closed, redirecting to cart page...");
                openCartPage(menu);
            });
            delay.play();
        });

        // Add children ONCE
        totalBox.getChildren().addAll(totalLabel, add);
        System.out.println("TotalBox children: " + totalBox.getChildren());

        block.getChildren().add(totalBox);
        return block;
    }

    // ----------------------------------------------------------
    //  Debug-safe image loader
    // ----------------------------------------------------------
    private Image loadImage(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                System.out.println("Loading image from file: " + path);
                return new Image(new FileInputStream(file));
            }

            InputStream s = getClass().getResourceAsStream("/" + path);
            if (s != null) {
                System.out.println("Loading image from resource: /" + path);
                return new Image(s);
            }

            s = getClass().getResourceAsStream(path);
            if (s != null) {
                System.out.println("Loading image from resource: " + path);
                return new Image(s);
            }

            System.out.println("WARNING: Image not found: " + path);
            return createPlaceholderImage();
        } catch (Exception e) {
            System.out.println("ERROR loading image: " + e.getMessage());
            return createPlaceholderImage();
        }
    }

    private Image createPlaceholderImage() {
        // 1×1 transparent PNG (base64)
        return new Image(
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==",
                120, 120, false, false
        );
    }

    // ----------------------------------------------------------
    //  Back → MoodSelectionPage (same cart)
    // ----------------------------------------------------------
    private void openMoodSelectionPage() {
        try {
            Stage stage = (Stage) root.getScene().getWindow();
            MoodSelectionPage page = new MoodSelectionPage(cart);
            page.start(stage);
        } catch (Exception e) {
            System.out.println("Error opening MoodSelectionPage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------
    //  Add to Cart → CartPage
    // ----------------------------------------------------------
    private void openCartPage(String returnMenu) {
        try {
            System.out.println("Opening cart page with menu: " + returnMenu);
            Stage stage = (Stage) root.getScene().getWindow();
            CartPage cp = new CartPage(cart, returnMenu);
            Scene scene = new Scene(cp.getRoot(stage), 1366, 768);
            stage.setScene(scene);
            System.out.println("Cart page opened successfully");
        } catch (Exception e) {
            System.out.println("Error opening CartPage: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
