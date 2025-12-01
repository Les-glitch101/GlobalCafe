package globalcafe;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MoodSelectionPage extends Application {

    private static final double PREF_WIDTH = 1366;
    private static final double PREF_HEIGHT = 768;

    private Cart cart;
    private BorderPane root;   // ✅ ADD ROOT NODE

    // Required empty constructor for Application
    public MoodSelectionPage() {}

    public MoodSelectionPage(Cart cart) {
        this.cart = cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    // ---------------------------------------------------------
    //  ⭐ NEW: getRoot() so navigation works
    // ---------------------------------------------------------
    public BorderPane getRoot() {
        if (root == null) {
            buildUI(); 
        }
        return root;
    }

    // ---------------------------------------------------------
    //  MAIN UI BUILDER (used by BOTH start() and navigation)
    // ---------------------------------------------------------
    private void buildUI() {

        root = new BorderPane();
        root.setPrefSize(PREF_WIDTH, PREF_HEIGHT);

        // Background
        Image bgImage = loadResourceImage("allison-christine-VgU1WYSSoWA-unsplash.jpg");
        if (bgImage != null) {
            BackgroundImage bImg = new BackgroundImage(
                    bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            root.setBackground(new Background(bImg));
        }

        // TOP: Logo
        Image logoImg = loadResourceImage("Rectangle 14.png");
        ImageView logoView = new ImageView();
        if (logoImg != null) {
            logoView.setImage(logoImg);
            logoView.setFitWidth(200);
            logoView.setPreserveRatio(true);
            logoView.setOpacity(0.85);
        }
        StackPane top = new StackPane(logoView);
        StackPane.setAlignment(logoView, Pos.TOP_LEFT);
        root.setTop(top);

        // Greetings
        Label g1 = new Label("How are you feeling today Les? Maybe");
        Label g2 = new Label("I could help you find some combination");
        g1.setFont(Font.font("Montserrat", 36));
        g2.setFont(Font.font("Montserrat", 28));
        g1.setTextFill(Color.WHITE);
        g2.setTextFill(Color.WHITE);

        VBox greetingBox = new VBox(5, g1, g2);
        greetingBox.setAlignment(Pos.CENTER);

        // Grid tiles
        String[] labels = {"Energized", "Cozy", "Sweet tooth", "Focused", "Chilled", "Browse menu"};
        String[] images = {
            "Rectangle 23.png",
            "Rectangle 26.png",
            "Rectangle 25.png",
            "Rectangle 24.png",
            "Rectangle 28.png",
            "Rectangle 27.png"
        };

        GridPane grid = new GridPane();
        grid.setHgap(36);
        grid.setVgap(36);
        grid.setAlignment(Pos.CENTER);

        int idx = 0;
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 3; c++) {
                StackPane tile = createImageTile(labels[idx], images[idx]);
                grid.add(tile, c, r);
                idx++;
            }
        }

        VBox centerBox = new VBox(30, greetingBox, grid);
        centerBox.setAlignment(Pos.CENTER);

        root.setCenter(centerBox);

        // Responsive scaling
        centerBox.scaleXProperty().bind(
                Bindings.min(root.widthProperty().divide(PREF_WIDTH),
                             root.heightProperty().divide(PREF_HEIGHT))
        );
        centerBox.scaleYProperty().bind(centerBox.scaleXProperty());
    }

    // ---------------------------------------------------------
    // START() for running as an actual Application (launch)
    // ---------------------------------------------------------
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(getRoot(), PREF_WIDTH, PREF_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Global Cafe - Mood Selection");
        stage.show();
    }


    /**
     * Create a single image tile (acts like a button): Image with rounded
     * corners and an overlaid label.
     */
    private StackPane createImageTile(String text, String imageFile) {
        // container size
        double tileW = 300;
        double tileH = 150;

        // image
        Image img = loadResourceImage(imageFile);
        ImageView iv = new ImageView();
        if (img != null) {
            iv.setImage(img);
            iv.setFitWidth(tileW);
            iv.setFitHeight(tileH);
            iv.setPreserveRatio(false);
            iv.setSmooth(true);
        } else {
            // fallback solid color as a plain region
            Region fallback = new Region();
            fallback.setPrefSize(tileW, tileH);
            fallback.setStyle("-fx-background-color: linear-gradient(#6a5acd, #4b0082);");
            StackPane.setMargin(fallback, new Insets(0));
            // create label and return
            Label lbFall = createTileLabel(text);
            StackPane fallbackTile = new StackPane(fallback, lbFall);
            fallbackTile.setMaxSize(tileW, tileH);
            fallbackTile.setPrefSize(tileW, tileH);
            applyTileClip(fallbackTile, tileW, tileH, 18);
            applyHoverEffect(fallbackTile);
            fallbackTile.setOnMouseClicked(e -> handleTileClick(text, (Stage) fallbackTile.getScene().getWindow()));
            return fallbackTile;
        }

        // overlay label
        Label lbl = createTileLabel(text);

        // rounded corners using clip on the ImageView
        Rectangle clip = new Rectangle(tileW, tileH);
        clip.setArcWidth(22);
        clip.setArcHeight(22);
        iv.setClip(clip);

        // add subtle border using another rectangle (optional)
        Rectangle border = new Rectangle(tileW, tileH);
        border.setArcWidth(22);
        border.setArcHeight(22);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.rgb(0, 0, 0, 0.15));

        StackPane tile = new StackPane(iv, lbl, border);
        tile.setMaxSize(tileW, tileH);
        tile.setPrefSize(tileW, tileH);
        applyTileClip(tile, tileW, tileH, 18);
        applyHoverEffect(tile);

        tile.setOnMouseClicked((MouseEvent e) -> handleTileClick(text, (Stage) tile.getScene().getWindow()));

        return tile;
    }

    /**
     * Handle tile clicks and navigate to appropriate pages
     */
    private void handleTileClick(String text, Stage stage) {
        switch (text) {
            case "Energized":
                openEnergizedPage(stage);
                break;
            case "Cozy":
                openCozyPage(stage);
                break;
            case "Sweet tooth":
                openSweetToothPage(stage);
                break;
            case "Focused":
                openFocusedPage(stage);
                break;
            case "Chilled":
                openChilledPage(stage);
                break;
            case "Browse menu":
                openBrowseMenuPage(stage);
                break;
        }
    }

    private void openEnergizedPage(Stage stage) {
    try {
        if (cart == null) {
            System.out.println("MoodSelectionPage: Cart was null, creating new cart");
            cart = new Cart();
        } else {
            System.out.println("MoodSelectionPage: Passing cart with " + cart.getItems().size() + " items to EnergizedPage");
        }

        EnergizedPage energizedPage = new EnergizedPage(cart);
        Scene scene = new Scene(energizedPage.getRoot(), 1366, 768);
        stage.setScene(scene);
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Failed to open Energized page: " + e.getMessage());
    }
}


    private void openCozyPage(Stage stage) {
    openPage(CozyPage.class, stage);
    }

    private void openSweetToothPage(Stage stage) {
        openPage(SweetTooth.class, stage);
    }

    private void openFocusedPage(Stage stage) {
        openPage(Focused.class, stage);
    }

    private void openChilledPage(Stage stage) {
        openPage(Chilled.class, stage);
    }

    private void openBrowseMenuPage(Stage stage) {
        try {
            // Create cart if it doesn't exist
            if (cart == null) {
                cart = new Cart();
            }
            CoffeeMenu coffeeMenu = new CoffeeMenu(cart);
            Scene menuScene = new Scene(coffeeMenu.getRoot(stage), 1366, 768);
            stage.setScene(menuScene);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to open Coffee Menu: " + e.getMessage());
        }
    }

    /**
     * Generic helper: attempt to instantiate and open arbitrary page class while passing the same Cart where possible.
     *
     * Usage: openPage(SomePage.class, stage);
     *
     * It will:
     *  - try new SomePage(Cart)
     *  - else try new SomePage()
     *  - if instance has setCart(Cart) method, call it
     *  - then call instance.start(Stage) if available (as most of your pages use start(stage))
     */
    private void openPage(Class<?> pageClass, Stage stage) {
        try {
            Object pageInstance = null;

            // Try constructor with Cart
            try {
                Constructor<?> ctor = pageClass.getConstructor(Cart.class);
                pageInstance = ctor.newInstance(cart != null ? cart : new Cart());
            } catch (NoSuchMethodException ignored) {
                // No constructor(Cart) - will try no-arg next
            }

            // Try no-arg ctor if we still don't have an instance
            if (pageInstance == null) {
                try {
                    Constructor<?> noArg = pageClass.getConstructor();
                    pageInstance = noArg.newInstance();
                } catch (NoSuchMethodException ex) {
                    throw new RuntimeException("No suitable constructor found for " + pageClass.getName());
                }
            }

            // If there's a setCart(Cart) method, call it
            try {
                Method setCartMethod = pageClass.getMethod("setCart", Cart.class);
                setCartMethod.invoke(pageInstance, cart != null ? cart : new Cart());
            } catch (NoSuchMethodException ignored) {
                
            }

            // Try to call start(Stage) if it exists
            try {
                Method startMethod = pageClass.getMethod("start", Stage.class);
                startMethod.invoke(pageInstance, stage);
                return;
            } catch (NoSuchMethodException ignored) {
                // maybe the page exposes getRoot(Stage) instead (like CartPage/CoffeeMenu),
                // try to invoke getRoot(Stage) -> Node and set Scene manually
            }

            // Fallback: look for getRoot(Stage) then set Scene
            try {
                Method getRootMethod = pageClass.getMethod("getRoot", Stage.class);
                Object rootNode = getRootMethod.invoke(pageInstance, stage);
                if (rootNode instanceof javafx.scene.Parent) {
                    Scene scene = new Scene((javafx.scene.Parent) rootNode, 1366, 768);
                    stage.setScene(scene);
                    return;
                }
            } catch (NoSuchMethodException ignored) {
                // nothing else to try
            }

            // If we reach here nothing sensible worked
            throw new RuntimeException("Could not open page: " + pageClass.getName());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to open page: " + pageClass.getName() + " -> " + e.getMessage());
        }
    }

    private Label createTileLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Montserrat", 22));
        lbl.setTextFill(Color.WHITE);
        lbl.setStyle("-fx-font-weight: 700; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 4, 0.5, 0, 1);");
        lbl.setWrapText(true);
        lbl.setAlignment(Pos.CENTER);
        return lbl;
    }

    private void applyTileClip(Region r, double w, double h, double arc) {
        Rectangle clip = new Rectangle(w, h);
        clip.setArcWidth(arc);
        clip.setArcHeight(arc);
        r.setClip(clip);
    }

    private void applyHoverEffect(Region region) {
        // simple scale transition and opacity change
        ScaleTransition stEnter = new ScaleTransition(Duration.millis(160), region);
        stEnter.setToX(1.06);
        stEnter.setToY(1.06);

        ScaleTransition stExit = new ScaleTransition(Duration.millis(160), region);
        stExit.setToX(1.0);
        stExit.setToY(1.0);

        region.setOnMouseEntered(e -> {
            region.setOpacity(0.95);
            stExit.stop();
            stEnter.playFromStart();
        });
        region.setOnMouseExited(e -> {
            region.setOpacity(1.0);
            stEnter.stop();
            stExit.playFromStart();
        });
    }

    /**
     * Load images from classpath resources located in /globalcafe/ folder.
     */
    private Image loadResourceImage(String filename) {
        try {
            String path = "/globalcafe/images/" + filename;
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null) {
                return new Image(is);
            } else {
                System.out.println("Resource not found: " + path);
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Failed to load resource image " + filename + " -> " + ex.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
