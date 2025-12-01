package globalcafe;

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

public class PaymentPage {

    private Cart cart;
    private BorderPane root;
    private Label cartLabel;

    public PaymentPage(Cart cart) {
        this.cart = cart;
        this.root = createContent();
    }

    public BorderPane getRoot() {
        return root;
    }

    private BorderPane createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(1366, 768);
        root.setStyle("-fx-background-color: #FFFBE4;");

        // --- Top section: Logo + Cart ---
        HBox topBox = new HBox(20);
        topBox.setPadding(new Insets(20));
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setStyle("-fx-background-color: #EEDDAF;");

        Image logoImage = loadImage("globalcafe/images/Rectangle 14.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);

        cartLabel = new Label("Cart (" + cart.getItemCount() + ")");
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

        topBox.getChildren().addAll(logoView, spacer, cartLabel, profileBtn);
        root.setTop(topBox);

        // --- Center content ---
        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(30));
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setStyle("-fx-background-color: #EEDDAF;");

        Label heading = new Label("Payment");
        heading.setFont(Font.font("Arial", 36));
        heading.setStyle("-fx-font-weight: bold;");

        // --- Cart summary ---
        VBox summaryBox = new VBox(10);
        summaryBox.setAlignment(Pos.CENTER_LEFT);
        summaryBox.setPadding(new Insets(10));
        summaryBox.setStyle("-fx-background-color: #FFFBE4; -fx-border-color: #A8D5BA; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;");

        Label summaryLabel = new Label("Order Summary:");
        summaryLabel.setFont(Font.font("Arial", 24));
        summaryLabel.setStyle("-fx-font-weight: bold;");

        for (CartItem item : cart.getItems()) {
            Label itemLabel = new Label(item.getName() + " x" + item.getQuantity() + " - R" + (item.getPrice() * item.getQuantity()));
            itemLabel.setFont(Font.font("Arial", 18));
            summaryBox.getChildren().add(itemLabel);
        }

        Label totalLabel = new Label("Total: R" + cart.getTotalAmount());
        totalLabel.setFont(Font.font("Arial", 28));
        totalLabel.setStyle("-fx-font-weight: bold;");

        // --- Payment methods ---
        VBox paymentBox = new VBox(20);
        paymentBox.setAlignment(Pos.CENTER_LEFT);

        Label paymentMethodLabel = new Label("Select Payment Method:");
        paymentMethodLabel.setFont(Font.font("Arial", 20));

        ToggleGroup paymentGroup = new ToggleGroup();
        RadioButton cardOption = new RadioButton("Credit / Debit Card");
        RadioButton cashOption = new RadioButton("Cash on Delivery");
        RadioButton upiOption = new RadioButton("Mobile Payment");

        cardOption.setFont(Font.font("Arial", 18));
        cashOption.setFont(Font.font("Arial", 18));
        upiOption.setFont(Font.font("Arial", 18));

        cardOption.setToggleGroup(paymentGroup);
        cashOption.setToggleGroup(paymentGroup);
        upiOption.setToggleGroup(paymentGroup);

        paymentBox.getChildren().addAll(paymentMethodLabel, cardOption, cashOption, upiOption);

        // --- Buttons ---
        HBox buttonsBox = new HBox(30);
        buttonsBox.setAlignment(Pos.CENTER);

        Button payBtn = new Button("Pay Now");
        payBtn.setPrefWidth(200);
        payBtn.setPrefHeight(50);
        payBtn.setStyle("-fx-background-color: #A8D5BA; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 10;");
        addHoverEffect(payBtn, "#E5EB89");
        payBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment Successful");
            alert.setHeaderText(null);
            alert.setContentText("Thank you! Your payment has been processed successfully.");
            alert.showAndWait();

            cart.clearCart();
            cartLabel.setText("Cart (" + cart.getItemCount() + ")");

            // Navigate to LoginWindow
            Stage stage = (Stage) root.getScene().getWindow();
            LoginWindow loginWindow = new LoginWindow();
            try {
                loginWindow.start(stage); // Reuse the same stage
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(200);
        cancelBtn.setPrefHeight(50);
        cancelBtn.setStyle("-fx-background-color: #E5EB89; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 10;");
        addHoverEffect(cancelBtn, "#A8D5BA");
        cancelBtn.setOnAction(e -> {
            Stage stage = (Stage) root.getScene().getWindow();
            CartPage cartPage = new CartPage(cart);
            Scene scene = new Scene(cartPage.getRoot(stage), 1366, 768);
            stage.setScene(scene);
        });

        buttonsBox.getChildren().addAll(payBtn, cancelBtn);

        mainContent.getChildren().addAll(heading, summaryBox, totalLabel, paymentBox, buttonsBox);
        root.setCenter(mainContent);

        return root;
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
