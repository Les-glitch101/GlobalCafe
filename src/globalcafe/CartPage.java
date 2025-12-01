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

import java.util.Iterator;

public class CartPage {

    private Cart cart;
    private BorderPane root;
    private VBox itemsBox;           // will hold cart item rows and be refreshable
    private Label totalLabel;        // live-updated total
    private String previousMenu;     // for "Continue shopping" navigation

    // Default: returns to Coffee menu
    public CartPage(Cart cart) {
        this(cart, "coffee");
    }

    // New: allow caller to specify which menu to return to (e.g. "snacks", "coffee", "tea", "bakes")
    public CartPage(Cart cart, String previousMenu) {
        this.cart = cart;
        this.previousMenu = previousMenu == null ? "coffee" : previousMenu.toLowerCase();
    }

    public BorderPane getRoot(Stage stage) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFBE4;"); // Same background for entire page
        root.setPrefSize(1366, 768);

        // ---------- TOP LOGO ----------
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 0, 20, 0));
        topBox.setStyle("-fx-background-color: #FFFBE4;"); // Same background

        Image logo = safeImage("globalcafe/images/Rectangle 14.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(200);
        logoView.setPreserveRatio(true);

        Label title = new Label("Your Cart");
        title.setFont(Font.font("Arial", 32));
        title.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
        title.setPadding(new Insets(0, 0, 0, 20));

        topBox.getChildren().addAll(logoView, title);
        root.setTop(topBox);

        // ---------- CENTERED SLIP CONTAINER ----------
        VBox centerContainer = new VBox();
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.setPadding(new Insets(0));
        centerContainer.setStyle("-fx-background-color: #FFFBE4;"); // Same background

        // Create the slip background
        VBox slip = new VBox(20);
        slip.setAlignment(Pos.TOP_CENTER);
        slip.setPadding(new Insets(30, 40, 30, 40));
        slip.setMaxWidth(600);
        slip.setStyle("-fx-background-color: white; "
                + "-fx-border-color: #D3D3D3; "
                + "-fx-border-width: 1px; "
                + "-fx-background-radius: 10; "
                + "-fx-border-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");

        // Slip header
        Label slipTitle = new Label("ORDER RECEIPT");
        slipTitle.setFont(Font.font("Arial", 24));
        slipTitle.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

        // Separator line
        Region separator = new Region();
        separator.setPrefHeight(1);
        separator.setStyle("-fx-background-color: #D3D3D3;");
        separator.setMaxWidth(500);

        // Items container (refreshable)
        itemsBox = new VBox(15);
        itemsBox.setAlignment(Pos.TOP_CENTER);
        itemsBox.setPadding(new Insets(10, 0, 20, 0));

        // Populate items
        refreshItems();

        // Total section
        Region totalSeparator = new Region();
        totalSeparator.setPrefHeight(1);
        totalSeparator.setStyle("-fx-background-color: #D3D3D3;");
        totalSeparator.setMaxWidth(500);

        HBox totalBox = new HBox();
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        totalBox.setPadding(new Insets(10, 20, 0, 20));

        totalLabel = new Label("Total: R" + formatPrice(computeTotalFromCart()));
        totalLabel.setFont(Font.font("Arial", 22));
        totalLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

        totalBox.getChildren().add(totalLabel);

        // Assemble the slip
        slip.getChildren().addAll(slipTitle, separator, itemsBox, totalSeparator, totalBox);

        // Create ScrollPane for the slip
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(slip);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(450); // Maximum height for the scrollable area
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Apply CSS styling to the scroll pane (kept same look)
        scrollPane.setStyle("-fx-background: #FFFBE4;");

        centerContainer.getChildren().add(scrollPane);
        root.setCenter(centerContainer);

        // ---------- BOTTOM BUTTONS ----------
        HBox bottomButtons = new HBox(40);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setPadding(new Insets(20));
        bottomButtons.setStyle("-fx-background-color: #FFFBE4;"); // Same background

        Button continueBtn = new Button("Continue shopping");
        continueBtn.setPrefSize(300, 60);
        continueBtn.setStyle("-fx-background-color: #F6CA7D; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 15;");

        continueBtn.setOnAction(e -> {
            // Navigate back to the correct menu based on previousMenu value
            switch (previousMenu) {
                case "snacks":
                    SnacksMenu snacksMenu = new SnacksMenu(cart);
                    Scene snacksScene = new Scene(snacksMenu.getRoot(stage), 1366, 768);
                    stage.setScene(snacksScene);
                    break;
                case "tea":
                    TeaMenu teaMenu = new TeaMenu(cart);
                    Scene teaScene = new Scene(teaMenu.getRoot(stage), 1366, 768);
                    stage.setScene(teaScene);
                    break;
                case "bakes":
                    BakesMenu bakesMenu = new BakesMenu(cart);
                    Scene bakesScene = new Scene(bakesMenu.getRoot(stage), 1366, 768);
                    stage.setScene(bakesScene);
                    break;
                case "coffee":
                default:
                    CoffeeMenu menu = new CoffeeMenu(cart);
                    Scene menuScene = new Scene(menu.getRoot(stage), 1366, 768);
                    stage.setScene(menuScene);
                    break;
            }
        });

        Button payBtn = new Button("Proceed to pay");
        payBtn.setPrefSize(300, 60);
        payBtn.setStyle("-fx-background-color: #E5EB89; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 15;");

        //Correct payment page navigation
        payBtn.setOnAction(e -> {
            PaymentPage paymentPage = new PaymentPage(cart);
            Scene paymentScene = new Scene(paymentPage.getRoot(), 1366, 768);
            Stage currentStage = (Stage) payBtn.getScene().getWindow();
            currentStage.setScene(paymentScene);
        });

        bottomButtons.getChildren().addAll(continueBtn, payBtn);
        root.setBottom(bottomButtons);

        return root;
    }

    // Rebuilds the itemsBox from current cart items
    private void refreshItems() {
        itemsBox.getChildren().clear();

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            Label empty = new Label("Your cart is empty.");
            empty.setFont(Font.font("Arial", 16));
            itemsBox.getChildren().add(empty);

            if (totalLabel != null) {
                totalLabel.setText("Total: R0.00");
            }
            return;
        }

        // For each cart item add a row with image, info, quantity spinner and Remove button
        for (int idx = 0; idx < cart.getItems().size(); idx++) {
            CartItem item = cart.getItems().get(idx);
            HBox itemRow = buildCartItemRow(item);
            itemsBox.getChildren().add(itemRow);

            // Separator between items (except after last)
            if (idx < cart.getItems().size() - 1) {
                Region sep = new Region();
                sep.setPrefHeight(1);
                sep.setStyle("-fx-background-color: #E8E8E8;");
                sep.setMaxWidth(500);
                itemsBox.getChildren().add(sep);
            }
        }

        // Update total label
        if (totalLabel != null) {
            totalLabel.setText("Total: R" + formatPrice(computeTotalFromCart()));
        }
    }

    // Builds a cart item row containing image, name, price, customization, quantity spinner and remove button
    private HBox buildCartItemRow(CartItem item) {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(500);

        Image img;
        try {
            // attempt to load file path; fall back to resource
            img = new Image("file:" + item.getImagePath(), 100, 100, true, true);
        } catch (Exception ex) {
            img = safeImage("globalcafe/images/placeholder.png");
        }

        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(100);
        imgView.setFitHeight(100);

        VBox infoBox = new VBox(6);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label(item.getName());
        name.setFont(Font.font("Arial", 18));
        name.setStyle("-fx-font-weight: bold;");

        Label price = new Label("R" + formatPrice(item.getFinalPrice()));
        price.setFont(Font.font("Arial", 16));

        Label extra = new Label(item.getCustomizationText());
        extra.setFont(Font.font("Arial", 14));
        extra.setStyle("-fx-text-fill: #666666;");

        infoBox.getChildren().addAll(name, price, extra);

        // Quantity spinner
        Spinner<Integer> qtySpinner = new Spinner<>();
        int currentQty = Math.max(1, item.getQuantity());
        SpinnerValueFactory<Integer> valueFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, currentQty);
        qtySpinner.setValueFactory(valueFactory);
        qtySpinner.setPrefWidth(90);

        qtySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                return;
            }
            item.setQuantity(newVal);
            // update displayed price for this item
            price.setText("R" + formatPrice(item.getFinalPrice()));
            // update total
            if (totalLabel != null) {
                totalLabel.setText("Total: R" + formatPrice(computeTotalFromCart()));
            }
        });

        // Remove button
        Button removeBtn = new Button("Remove");
        removeBtn.setPrefHeight(32);
        removeBtn.setOnAction(e -> {
            // remove item from cart's list
            try {
                // Use iterator removal to avoid concurrent modification issues
                Iterator<CartItem> it = cart.getItems().iterator();
                while (it.hasNext()) {
                    CartItem ci = it.next();
                    if (ci == item) {
                        it.remove();
                        break;
                    }
                }
            } catch (Exception ex) {
                // fallback: try list remove
                cart.getItems().remove(item);
            }

            // refresh UI
            refreshItems();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox controls = new HBox(10, qtySpinner, removeBtn);
        controls.setAlignment(Pos.CENTER_RIGHT);

        box.getChildren().addAll(imgView, infoBox, spacer, controls);
        return box;
    }

    // Compute total price from cart items directly (independent of Cart.getTotal())
    private double computeTotalFromCart() {
        double total = 0;
        if (cart == null || cart.getItems() == null) {
            return 0;
        }
        for (CartItem item : cart.getItems()) {
            // Use final price which should account for extras and quantity
            total += item.getFinalPrice();
        }
        return total;
    }

    // Format to two decimals
    private String formatPrice(double value) {
        return String.format("%.2f", value);
    }

    // Safe image loader: if resource missing, return a 1x1 blank image to avoid crashes
    private Image safeImage(String path) {
        try {
            return new Image(path);
        } catch (Exception e) {
            // return a tiny transparent image as a last resort
            return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABAQMAAAAl21bKAAAAA1BMVEUAAACnej3aAAAAAXRSTlMAQObYZgAAAApJREFUCNdjYAAAAAIAAeIhvDMAAAAASUVORK5CYII=");
        }
    }
}