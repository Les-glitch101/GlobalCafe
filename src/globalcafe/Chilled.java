/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package globalcafe;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Chilled extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setPrefSize(1366, 768);

        // Background
        Image bgImage = loadImage("globalcafe/images/allison-christine-VgU1WYSSoWA-unsplash.jpg");
        if (bgImage != null) {
            BackgroundImage bg = new BackgroundImage(bgImage,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, true));
            root.setBackground(new Background(bg));
        } else {
            root.setStyle("-fx-background-color: #2c3e50;");
        }

        // Logo with opacity
        Image logoImage = loadImage("globalcafe/images/Rectangle 14.png");
        ImageView logoView = new ImageView();
        if (logoImage != null) {
            logoView.setImage(logoImage);
            logoView.setPreserveRatio(true);
            logoView.setFitWidth(200);
            logoView.setOpacity(0.5);
        }
        StackPane logoContainer = new StackPane(logoView);
        logoContainer.setAlignment(Pos.TOP_LEFT);
        logoContainer.setPadding(new Insets(20));

        // Back button
        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-background-color: #EEDDAF; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand; -fx-text-fill: black;");
        backButton.setOnAction(e -> openMoodSelectionPage(primaryStage));

        // Position back button near logo
        HBox topLeftBox = new HBox(10, backButton, logoContainer);
        topLeftBox.setAlignment(Pos.TOP_LEFT);
        topLeftBox.setPadding(new Insets(20));

        // Mood banner
        Image moodImage = loadImage("globalcafe/images/Rectangle 28.png"); // Energized image
        ImageView moodView = new ImageView();
        if (moodImage != null) {
            moodView.setImage(moodImage);
            moodView.setFitWidth(250);
            moodView.setPreserveRatio(true);
        }
        StackPane moodBanner = new StackPane(moodView);
        moodBanner.setAlignment(Pos.TOP_CENTER);
        moodBanner.setPadding(new Insets(80, 0, 0, 0));

        // Suggestions layout
        VBox suggestionsBox = new VBox(40);
        suggestionsBox.setAlignment(Pos.CENTER);

        // First suggestion block
        HBox suggestion1 = createSuggestionBlock(
                new String[]{"Ice Coffee", "Healthy Bowl", "3 Chocolate muffins"},
                new String[]{"globalcafe/images/icecoffee.png", "globalcafe/images/healthybowl.png", "globalcafe/images/muffins.png"},
                new int[]{25, 25, 25},
                75
        );

        // Second suggestion block
        HBox suggestion2 = createSuggestionBlock(
                new String[]{"Rooibos tea", "Bowl of nuts", "2 Slices of choc bread"},
                new String[]{"globalcafe/images/rooibos.png", "globalcafe/images/nuts.png", "globalcafe/images/chocbread.png"},
                new int[]{15, 25, 15},
                55
        );

        suggestionsBox.getChildren().addAll(suggestion1, suggestion2);

        VBox centerContent = new VBox(50, moodBanner, suggestionsBox);
        centerContent.setAlignment(Pos.CENTER);

        // Main layout - use topLeftBox instead of logoContainer
        StackPane mainLayout = new StackPane();
        mainLayout.getChildren().addAll(centerContent, topLeftBox);
        StackPane.setAlignment(centerContent, Pos.CENTER);
        StackPane.setAlignment(topLeftBox, Pos.TOP_LEFT);

        // Responsive scaling
        centerContent.scaleXProperty().bind(Bindings.min(root.widthProperty().divide(1366), root.heightProperty().divide(768)));
        centerContent.scaleYProperty().bind(centerContent.scaleXProperty());

        root.getChildren().add(mainLayout);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Global Cafe - Energized Suggestions");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Navigation method to return to mood selection page
     */
    private void openMoodSelectionPage(Stage stage) {
        MoodSelectionPage moodPage = new MoodSelectionPage();
        moodPage.start(stage);
    }

    private HBox createSuggestionBlock(String[] itemNames, String[] imagePaths, int[] prices, int totalPrice) {
        HBox block = new HBox(20);
        block.setAlignment(Pos.CENTER);

        // Items
        for (int i = 0; i < itemNames.length; i++) {
            VBox itemBox = new VBox(10);
            itemBox.setAlignment(Pos.CENTER);

            Image img = loadImage(imagePaths[i]);
            ImageView imgView = new ImageView();
            if (img != null) {
                imgView.setImage(img);
                imgView.setFitWidth(120);
                imgView.setFitHeight(120);
                imgView.setPreserveRatio(true);
            } else {
                // Fallback if image not found
                imgView.setFitWidth(120);
                imgView.setFitHeight(120);
                imgView.setStyle("-fx-background-color: #EEDDAF;");
            }

            Label name = new Label(itemNames[i]);
            name.setFont(Font.font("Arial", 18));
            name.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(gaussian, black, 2, 0.5, 2, 2);");

            Label price = new Label("R" + prices[i]);
            price.setFont(Font.font("Arial", 16));
            price.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(gaussian, black, 2, 0.5, 2, 2);");

            itemBox.getChildren().addAll(imgView, name, price);
            block.getChildren().add(itemBox);
        }

        // Total and button
        VBox totalBox = new VBox(10);
        totalBox.setAlignment(Pos.CENTER);

        Label totalLabel = new Label("Total R" + totalPrice);
        totalLabel.setFont(Font.font("Arial", 20));
        totalLabel.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(gaussian, black, 2, 0.5, 2, 2);");

        Button addToCart = new Button("+ Add to cart");
        addToCart.setPrefWidth(150);
        addToCart.setStyle("-fx-background-color: #A8D5BA; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand; -fx-text-fill: black;");

        // Store original style for hover effect
        final String originalStyle = addToCart.getStyle();
        addHoverEffect(addToCart, "#8FCFA0", originalStyle);

        totalBox.getChildren().addAll(totalLabel, addToCart);
        block.getChildren().add(totalBox);

        return block;
    }

    private void addHoverEffect(Button button, String hoverColor, String originalStyle) {
        button.setOnMouseEntered(e -> {
            button.setStyle(originalStyle.replace("#A8D5BA", hoverColor));
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

    public static void main(String[] args) {
        launch(args);
    }
}