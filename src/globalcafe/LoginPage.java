package globalcafe;

import javafx.animation.FadeTransition;
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
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Stable, interaction-friendly LoginPage. - TextField appears on top of the
 * Enter ID button (stacked) - When hidden the TextField is mouseTransparent so
 * clicks reach the button - Hover effects are applied via a stable style helper
 * (no string replace) - Admin login with password protection
 */
public class LoginPage extends Application {

    private TextField userIdField;
    private PasswordField passwordField;
    private StackPane enterStack;
    private StackPane passwordStack;
    private Button enterIdBtn;
    private Button loginBtn;
    private boolean isAdminLogin = false;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin@2025";

    @Override
    public void start(Stage primaryStage) {
        // Root layout (prevents invisible panes intercepting events)
        BorderPane root = new BorderPane();
        root.setPrefSize(1366, 768);

        // Background image (optional)
        Image bg = loadImage("globalcafe/images/jakub-dziubak-XtUd5SiX464-unsplash.jpg");
        if (bg != null) {
            BackgroundImage bgImg = new BackgroundImage(
                    bg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, true)
            );
            root.setBackground(new Background(bgImg));
        } else {
            root.setStyle("-fx-background-color: #2c3e50;");
        }

        // Top-left logo
        Image logo = loadImage("globalcafe/images/Rectangle 14.png");
        ImageView logoView = new ImageView();
        if (logo != null) {
            logoView.setImage(logo);
            logoView.setPreserveRatio(true);
            logoView.setFitWidth(200);
        }
        HBox topLeft = new HBox(logoView);
        topLeft.setPadding(new Insets(20));
        topLeft.setAlignment(Pos.CENTER_LEFT);
        root.setTop(topLeft);

        // Center area
        VBox centerBox = new VBox(28);
        centerBox.setAlignment(Pos.CENTER);

        Label greeting = new Label("Hey — how you doing today?");
        greeting.setFont(Font.font(28));
        greeting.setStyle("-fx-text-fill: white;");

        // ---------- Buttons and popup text fields ----------
        enterIdBtn = new Button("Enter user ID");
        enterIdBtn.setPrefSize(600, 80);
        enterIdBtn.setStyle(btnStyle("#EEDDAF"));

        // User ID text field (stacked on top of the button)
        userIdField = new TextField();
        userIdField.setPromptText("Enter your user ID");
        userIdField.setFont(Font.font(20));
        userIdField.setMaxWidth(520);
        userIdField.setVisible(false);
        userIdField.setMouseTransparent(true);
        userIdField.setStyle(
                "-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 12; "
                + "-fx-padding: 8 12 8 12; -fx-border-color: rgba(0,0,0,0.2); -fx-border-radius: 12;"
        );

        // Password field (initially hidden)
        Button enterPasswordBtn = new Button("Enter password");
        enterPasswordBtn.setPrefSize(600, 80);
        enterPasswordBtn.setStyle(btnStyle("#A8D5BA"));
        enterPasswordBtn.setVisible(false);

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter admin password");
        passwordField.setFont(Font.font(20));
        passwordField.setMaxWidth(520);
        passwordField.setVisible(false);
        passwordField.setMouseTransparent(true);
        passwordField.setStyle(
                "-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 12; "
                + "-fx-padding: 8 12 8 12; -fx-border-color: rgba(0,0,0,0.2); -fx-border-radius: 12;"
        );

        // Stack them so the text fields can appear on top
        enterStack = new StackPane(enterIdBtn, userIdField);
        enterStack.setAlignment(Pos.CENTER);

        passwordStack = new StackPane(enterPasswordBtn, passwordField);
        passwordStack.setAlignment(Pos.CENTER);
        passwordStack.setVisible(false);

        // Login and create-account buttons
        loginBtn = new Button("Login");
        loginBtn.setPrefSize(600, 80);
        loginBtn.setStyle(btnStyle("#EEDDAF"));

        Button createAccountBtn = new Button("New here? Create an account");
        createAccountBtn.setPrefSize(600, 80);
        createAccountBtn.setStyle(btnStyle("#F6CA7D"));

        // Layout the buttons vertically
        VBox btns = new VBox(18, enterStack, passwordStack, loginBtn, createAccountBtn);
        btns.setAlignment(Pos.CENTER);

        centerBox.getChildren().addAll(greeting, btns);
        root.setCenter(centerBox);

        // ---------- Hover effects (stable) ----------
        applyHover(enterIdBtn, "#EEDDAF", "#e3d4a2");
        applyHover(enterPasswordBtn, "#A8D5BA", "#8FCFA0");
        applyHover(loginBtn, "#EEDDAF", "#e3d4a2");
        applyHover(createAccountBtn, "#F6CA7D", "#e3b56d");

        // ---------- Button actions ----------
        enterIdBtn.setOnAction(evt -> {
            showTextField(userIdField);
        });

        enterPasswordBtn.setOnAction(evt -> {
            showTextField(passwordField);
        });

        loginBtn.setOnAction(evt -> {
            handleLogin(primaryStage);
        });

        createAccountBtn.setOnAction(evt -> {
            try {
                CreateAccount createAccount = new CreateAccount();
                Stage createStage = new Stage();
                createAccount.start(createStage);
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Failed to open account creation page.");
            }
        });

        // ---------- Scene ----------
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Global Cafe - Login");
        primaryStage.show();
    }

    private void showTextField(TextField field) {
        if (!field.isVisible()) {
            field.setVisible(true);
            field.setMouseTransparent(false);
            field.requestFocus();
            field.setOpacity(0);
            FadeTransition ft = new FadeTransition(Duration.millis(180), field);
            ft.setToValue(1);
            ft.play();
        } else {
            if (field.getText().isEmpty()) {
                field.requestFocus();
            }
        }
    }

    private void handleLogin(Stage primaryStage) {
        String username = userIdField.getText().trim();
        
        if (username.isEmpty()) {
            showAlert("Please enter your User ID before logging in.");
            showTextField(userIdField);
            return;
        }

        // Check if admin login
        if (username.equalsIgnoreCase(ADMIN_USERNAME)) {
            if (!isAdminLogin) {
                // First time admin detected - show password field
                isAdminLogin = true;
                passwordStack.setVisible(true);
                showTextField(passwordField);
                showAlert("Admin login detected. Please enter your password.");
                return;
            } else {
                // Verify admin password
                String password = passwordField.getText().trim();
                if (password.isEmpty()) {
                    showAlert("Please enter the admin password.");
                    showTextField(passwordField);
                    return;
                }
                
                if (!password.equals(ADMIN_PASSWORD)) {
                    showAlert("Invalid admin password. Please try again.");
                    passwordField.clear();
                    showTextField(passwordField);
                    return;
                }
                
                // Admin login successful - open inventory page
                try {
                    InventoryPage inventoryPage = new InventoryPage();
                    Scene inventoryScene = new Scene(inventoryPage.getRoot(primaryStage), 1366, 768);
                    primaryStage.setScene(inventoryScene);
                    primaryStage.setTitle("Global Cafe - Inventory Management");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("Failed to load inventory management page.");
                }
                return;
            }
        }

        // Regular user login
        if (isAdminLogin) {
            // Reset admin login state if user changed from admin to regular user
            isAdminLogin = false;
            passwordStack.setVisible(false);
            passwordField.clear();
        }

        try {
            // Open MoodSelectionPage for regular users
            MoodSelectionPage moodPage = new MoodSelectionPage();
            Stage moodStage = new Stage();
            moodPage.start(moodStage);
            primaryStage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Failed to load mood selection page.");
        }
    }

    // Helper: consistent button base style
    private String btnStyle(String color) {
        return "-fx-background-color: " + color + ";"
                + " -fx-background-radius: 12;"
                + " -fx-font-size: 20px;"
                + " -fx-font-weight: bold;"
                + " -fx-cursor: hand;"
                + " -fx-text-fill: black;";
    }

    // Stable hover: set full style on enter/exit (no regex)
    private void applyHover(Button btn, String normalColor, String hoverColor) {
        btn.setOnMouseEntered(e -> {
            btn.setStyle(btnStyle(hoverColor));
            btn.setScaleX(1.04);
            btn.setScaleY(1.04);
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle(btnStyle(normalColor));
            btn.setScaleX(1.0);
            btn.setScaleY(1.0);
        });
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Info");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    //image loader with better error handling
    private Image loadImage(String path) {
        try {
            //direct file path first
            File file = new File(path);
            if (file.exists()) {
                System.out.println("Loading image from file: " + path);
                return new Image(new FileInputStream(file));
            }
            
            //as resource
            String resourcePath = path.startsWith("/") ? path.substring(1) : path;
            InputStream stream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (stream != null) {
                System.out.println("Loading image from resource: " + resourcePath);
                return new Image(stream);
            }
            
            //different path variations
            String[] pathVariations = {
                path,
                "/" + path,
                "src/main/resources/" + path,
                "resources/" + path
            };
            
            for (String variation : pathVariations) {
                file = new File(variation);
                if (file.exists()) {
                    System.out.println("Loading image from variation: " + variation);
                    return new Image(new FileInputStream(file));
                }
                
                //resource for each variation
                String cleanVariation = variation.startsWith("/") ? variation.substring(1) : variation;
                stream = getClass().getClassLoader().getResourceAsStream(cleanVariation);
                if (stream != null) {
                    System.out.println("Loading image from resource variation: " + cleanVariation);
                    return new Image(stream);
                }
            }
            
            System.err.println("Image not found: " + path);
            return null;
            
        } catch (Exception e) {
            System.err.println("Error loading image: " + path + " - " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}