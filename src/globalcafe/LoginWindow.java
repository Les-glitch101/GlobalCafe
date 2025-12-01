package globalcafe;

import javafx.application.Application;
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

public class LoginWindow extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Root 
        BorderPane root = new BorderPane();
        root.setPrefSize(1366, 768);

        // Background image
        Image bgImage = loadImage("jakub-dziubak-XtUd5SiX464-unsplash.jpg");

        if (bgImage != null) {
            BackgroundImage bg = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, true)
            );
            root.setBackground(new Background(bg));
        }

        // Top-left logo
        Image logo = loadImage("Rectangle 14.png");
        ImageView logoView = new ImageView(logo);
        logoView.setPreserveRatio(true);
        logoView.setFitWidth(200);

        StackPane topLeft = new StackPane(logoView);
        topLeft.setAlignment(Pos.TOP_LEFT);
        topLeft.setPadding(new Insets(0));

        root.setTop(topLeft);

        // --- Center content ---
        VBox centerContent = new VBox(30);
        centerContent.setAlignment(Pos.CENTER);

        Label greeting = new Label("Hey how you doing today?");
        greeting.setFont(Font.font("Arial", 28));
        greeting.setStyle("-fx-text-fill: white;");

        Button smileBtn = new Button("😊");
        Button sadBtn = new Button("🙁");
        smileBtn.setFont(Font.font(28));
        sadBtn.setFont(Font.font(28));
        smileBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        sadBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        HBox emojis = new HBox(10, greeting, smileBtn, sadBtn);
        emojis.setAlignment(Pos.CENTER);

        // Buttons
        Button loginBtn = new Button("Login");
        loginBtn.setPrefSize(500, 80);
        loginBtn.setStyle("-fx-background-color: #EEDDAF; -fx-background-radius: 20; -fx-font-size: 28; -fx-font-weight: bold;");

        Button guestBtn = new Button("Continue as guest");
        guestBtn.setPrefSize(500, 80);
        guestBtn.setStyle("-fx-background-color: #F6CA7D; -fx-background-radius: 20; -fx-font-size: 28; -fx-font-weight: bold;");

        centerContent.getChildren().addAll(emojis, loginBtn, guestBtn);
        root.setCenter(centerContent);

        // ---------- Hover Effects (Perfectly Working) ----------
        applyHover(loginBtn, "#EEDDAF", "#e8d79c");
        applyHover(guestBtn, "#F6CA7D", "#e9b56d");

        // Emoji hover
        smileBtn.setOnMouseEntered(e -> smileBtn.setScaleX(1.2));
        smileBtn.setOnMouseEntered(e -> smileBtn.setScaleY(1.2));
        smileBtn.setOnMouseExited(e -> {
            smileBtn.setScaleX(1);
            smileBtn.setScaleY(1);
        });

        sadBtn.setOnMouseEntered(e -> sadBtn.setScaleX(1.2));
        sadBtn.setOnMouseEntered(e -> sadBtn.setScaleY(1.2));
        sadBtn.setOnMouseExited(e -> {
            sadBtn.setScaleX(1);
            sadBtn.setScaleY(1);
        });

        // ---------- BUTTON ACTION (NOW WORKING) ----------
        loginBtn.setOnAction(e -> {
            try {
                LoginPage loginPage = new LoginPage();
                Stage loginStage = new Stage();
                loginPage.start(loginStage);
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        guestBtn.setOnAction(e -> System.out.println("Guest login"));

        // Scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Global Café");
        primaryStage.show();
    }

    // Hover effect
    private void applyHover(Button button, String normalColor, String hoverColor) {
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: " + hoverColor +
                        "; -fx-background-radius: 20; -fx-font-size: 28; -fx-font-weight: bold;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: " + normalColor +
                        "; -fx-background-radius: 20; -fx-font-size: 28; -fx-font-weight: bold;"
        ));
    }

    // Load image
    private Image loadImage(String path) {
        try {
            return new Image(new FileInputStream(new File(path)));
        } catch (Exception e) {
            System.out.println("Cannot load image: " + path);
            return null;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}