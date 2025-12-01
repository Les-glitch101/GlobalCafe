package globalcafe;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * CreateAccount - A JavaFX application for user account creation
 * Provides a GUI form for users to create new accounts with validation
 */
public class CreateAccount extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Main root container using StackPane for layered content
        StackPane root = new StackPane();
        root.setPrefSize(1366, 768); // Set preferred window size

        // ---------------------- Background Setup ----------------------
        Image bgImage = loadImage("/globalcafe/jakub-dziubak-XtUd5SiX464-unsplash.jpg");
        if (bgImage != null) {
            // Create background image that scales to fill the container
            BackgroundImage bg = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,  // No repeating in X direction
                    BackgroundRepeat.NO_REPEAT,  // No repeating in Y direction
                    BackgroundPosition.CENTER,   // Center the image
                    new BackgroundSize(1.0, 1.0, true, true, false, false) // Scale to fit
            );
            root.setBackground(new Background(bg));
        } else {
            // Fallback background color if image fails to load
            root.setStyle("-fx-background-color: #F5ECE0;");
        }

        // Main layout using BorderPane for structured positioning (top, center, bottom)
        BorderPane mainLayout = new BorderPane();

        // ---------------------- Logo Section in Top Left ----------------------
        Image logoImage = loadImage("/globalcafe/Rectangle 14.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(200);        // Set fixed width
        logoView.setPreserveRatio(true);  // Maintain aspect ratio

        StackPane logoContainer = new StackPane(logoView);
        logoContainer.setPadding(new Insets(20));  // Add padding around logo
        logoContainer.setAlignment(Pos.TOP_LEFT);  // Position in top-left corner

        // Place logo in top region of BorderPane
        mainLayout.setTop(logoContainer);

        // ---------------------- Center Form Section ----------------------
        VBox centerForm = new VBox(20);  // Vertical box with 20px spacing between elements
        centerForm.setAlignment(Pos.TOP_CENTER);  // Center align all content
        centerForm.setPadding(new Insets(10, 0, 0, 0));  // Reduced top padding to move content upward

        // Form header label
        Label header = new Label("Create Your Account");
        header.setFont(Font.font("Arial", 32));  // Set font and size
        header.setStyle("-fx-text-fill: black; -fx-effect: dropshadow(gaussian, white, 2, 0.5, 2, 2);");

        // Create form input fields
        TextField userIdField = createField("Enter User ID");
        TextField emailField = createField("Enter Email Address");
        TextField phoneField = createField("Enter Phone Number");
        TextField firstNameField = createField("Enter First Name");
        TextField lastNameField = createField("Enter Last Name");

        // Create styled buttons
        Button createBtn = styledButton("Create Account", "#F6CA7D", 738, 80, 28);
        Button backBtn = styledButton("Back to Login", "#EEDDAF", 300, 50, 18);

        // Add hover effects to buttons
        setupHover(createBtn, "#F6CA7D");
        setupHover(backBtn, "#EEDDAF");

        // ---------------------- Form Validation and Event Handling ----------------------
        createBtn.setOnAction(e -> {
            // Validate that all fields are filled
            if (userIdField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty() ||
                firstNameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty()) {

                showAlert("Error", "Please fill in all fields.");
                return;  // Stop execution if validation fails
            }

            // Validate email format
            if (!isValidEmail(emailField.getText())) {
                showAlert("Error", "Please enter a valid email address.");
                return;  // Stop execution if email is invalid
            }

            // Success case - show confirmation and clear form
            showAlert("Success",
                    "Account Created for: "
                            + firstNameField.getText() + " "
                            + lastNameField.getText()
            );

            // Clear all form fields after successful account creation
            userIdField.clear();
            emailField.clear();
            phoneField.clear();
            firstNameField.clear();
            lastNameField.clear();
        });

        // Back button action (placeholder functionality)
        backBtn.setOnAction(e -> showAlert("Info", "Returning to Login Page..."));

        // Arrange buttons vertically
        VBox buttonBox = new VBox(15, createBtn, backBtn);  // 15px spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);

        // Add all elements to the center form
        centerForm.getChildren().addAll(
                header,
                userIdField, emailField, phoneField,
                firstNameField, lastNameField,
                buttonBox
        );

        // Container for center content to control vertical positioning
        StackPane centerContainer = new StackPane(centerForm);
        centerContainer.setAlignment(Pos.TOP_CENTER);  // Position at top-center
        centerContainer.setPadding(new Insets(10, 0, 0, 0));  // Minimal top padding

        mainLayout.setCenter(centerContainer);

        // Add main layout to root stack pane
        root.getChildren().add(mainLayout);

        // ---------------------- Responsive Scaling ----------------------
        // Bind form scaling to window size for responsive behavior
        centerForm.scaleXProperty().bind(
                Bindings.min(root.widthProperty().divide(1366),
                        root.heightProperty().divide(768))
        );
        centerForm.scaleYProperty().bind(centerForm.scaleXProperty());

        // ---------------------- Scene and Stage Setup ----------------------
        Scene scene = new Scene(root, 1366, 768);  // Create scene with initial size
        primaryStage.setTitle("Global Cafe - Create Account");  // Window title
        primaryStage.setScene(scene);  // Set the scene for the stage
        primaryStage.show();  // Display the window
    }

    // ---------------------- Helper Methods ----------------------

    /**
     * Creates a styled text field with consistent appearance
     * @param text The prompt text to display when field is empty
     * @return Configured TextField object
     */
    private TextField createField(String text) {
        TextField tf = new TextField();
        tf.setPromptText(text);  // Hint text that disappears when user types
        tf.setFont(Font.font(20));  // Set font size
        tf.setMaxWidth(738);  // Maximum width
        tf.setPrefHeight(60);  // Preferred height
        tf.setStyle(
                "-fx-background-color: #EEDDAF; -fx-background-radius: 10;" +
                        "-fx-padding: 10; -fx-text-fill: black;"
        );
        return tf;
    }

    /**
     * Creates a styled button with consistent appearance
     * @param text Button display text
     * @color Button background color in hex format
     * @width Button width
     * @height Button height
     * @font Font size in pixels
     * @return Configured Button object
     */
    private Button styledButton(String text, String color, int width, int height, int font) {
        Button btn = new Button(text);
        btn.setPrefSize(width, height);  // Set preferred size
        btn.setStyle(
                "-fx-background-color: " + color +
                        "; -fx-font-size: " + font +
                        "px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-text-fill: black;"
        );
        return btn;
    }

    /**
     * Adds hover effects to buttons (color change and scale animation)
     * @param btn Button to apply hover effects to
     * @param originalColor Original button color in hex format
     */
    private void setupHover(Button btn, String originalColor) {
        final String originalStyle = btn.getStyle();
        String hoverColor = brighten(originalColor);  // Get brighter version of original color

        // Mouse enter event - apply hover effects
        btn.setOnMouseEntered(e -> {
            btn.setStyle(originalStyle.replace(originalColor, hoverColor));  // Change color
            btn.setScaleX(1.05);  // Slightly enlarge
            btn.setScaleY(1.05);
        });
        
        // Mouse exit event - restore original appearance
        btn.setOnMouseExited(e -> {
            btn.setStyle(originalStyle);  // Restore original color
            btn.setScaleX(1.0);  // Restore original size
            btn.setScaleY(1.0);
        });
    }

    /**
     * Returns a brighter version of the given hex color
     * @param hex Original color in hex format (e.g., "#F6CA7D")
     * @return Brighter color in hex format
     */
    private String brighten(String hex) {
        // Simple color brightening logic - returns predefined brighter versions
        if (hex.equals("#F6CA7D")) return "#F8D49C";  // Brighter version of create button color
        if (hex.equals("#EEDDAF")) return "#F4E8C7";  // Brighter version of back button color
        return "#FFE0A0";  // Default brighter color
    }

    /**
     * Loads an image from resources with error handling
     * @param resource Path to the image resource
     * @return Image object or null if loading fails
     */
    private Image loadImage(String resource) {
        try {
            return new Image(getClass().getResourceAsStream(resource));
        } catch (Exception e) {
            System.err.println("Failed to load image: " + resource);
            return null;  // Return null if image cannot be loaded
        }
    }

    /**
     * Basic email validation
     * @param email Email address to validate
     * @return true if email contains '@' and '.', false otherwise
     */
    private boolean isValidEmail(String email) {
        // Simple validation - in production, use more robust validation
        return email.contains("@") && email.contains(".");
    }

    /**
     * Displays an alert dialog to the user
     * @param title Alert window title
     * @param msg Message content to display
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);  // No header text
        alert.setContentText(msg);
        alert.showAndWait();  // Display and wait for user acknowledgment
    }

    /**
     * Main method - application entry point
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);  // Start the JavaFX application
    }
}