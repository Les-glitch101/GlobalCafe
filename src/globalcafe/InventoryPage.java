package globalcafe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Map;

public class InventoryPage {

    private TableView<ItemStock> table;
    private ObservableList<ItemStock> inventoryData;

    public BorderPane getRoot(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFBE4;");
        root.setPrefSize(1366, 768);

        // ---------- TOP HEADER ----------
        Label title = new Label("Inventory Management");
        title.setFont(Font.font("Arial", 32));
        title.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
        HBox topBox = new HBox(title);
        topBox.setPadding(new Insets(20));
        topBox.setAlignment(Pos.CENTER);
        root.setTop(topBox);

        // ---------- TABLE ----------
        table = new TableView<>();
        
        TableColumn<ItemStock, String> nameCol = new TableColumn<>("Item Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        nameCol.setPrefWidth(300);

        TableColumn<ItemStock, Integer> qtyCol = new TableColumn<>("Stock Quantity");
        qtyCol.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        qtyCol.setPrefWidth(200);

        table.getColumns().addAll(nameCol, qtyCol);
        loadInventoryData();

        VBox tableContainer = new VBox(table);
        tableContainer.setPadding(new Insets(20));
        root.setCenter(tableContainer);

        // ---------- BOTTOM CONTROLS ----------
        VBox controlsContainer = new VBox(15);
        controlsContainer.setPadding(new Insets(20));
        controlsContainer.setAlignment(Pos.CENTER);

        // Update existing item section
        Label updateLabel = new Label("Update Existing Item:");
        updateLabel.setFont(Font.font("Arial", 16));
        updateLabel.setStyle("-fx-font-weight: bold;");

        HBox updateControls = new HBox(15);
        updateControls.setAlignment(Pos.CENTER);

        TextField itemField = new TextField();
        itemField.setPromptText("Item name");
        itemField.setPrefWidth(200);

        TextField qtyField = new TextField();
        qtyField.setPromptText("Quantity change (+/-)");
        qtyField.setPrefWidth(150);

        Button updateBtn = new Button("Update Stock");
        updateBtn.setStyle("-fx-background-color: #F6CA7D; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;");
        updateBtn.setOnAction(e -> updateStock(itemField, qtyField));

        updateControls.getChildren().addAll(itemField, qtyField, updateBtn);

        // Add new item section
        Label addLabel = new Label("Add New Item:");
        addLabel.setFont(Font.font("Arial", 16));
        addLabel.setStyle("-fx-font-weight: bold;");

        HBox addControls = new HBox(15);
        addControls.setAlignment(Pos.CENTER);

        TextField newItemField = new TextField();
        newItemField.setPromptText("New item name");
        newItemField.setPrefWidth(200);

        TextField newQtyField = new TextField();
        newQtyField.setPromptText("Initial quantity");
        newQtyField.setPrefWidth(150);

        Button addBtn = new Button("Add New Item");
        addBtn.setStyle("-fx-background-color: #A8D5BA; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;");
        addBtn.setOnAction(e -> addNewItem(newItemField, newQtyField));

        addControls.getChildren().addAll(newItemField, newQtyField, addBtn);

        // Action buttons
        HBox actionButtons = new HBox(20);
        actionButtons.setAlignment(Pos.CENTER);

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: #E5EB89; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;");
        refreshBtn.setOnAction(e -> loadInventoryData());

        

        actionButtons.getChildren().addAll(refreshBtn);

        controlsContainer.getChildren().addAll(updateLabel, updateControls, addLabel, addControls, actionButtons);
        root.setBottom(controlsContainer);

        return root;
    }

    private void updateStock(TextField itemField, TextField qtyField) {
        String item = itemField.getText().trim();
        if (item.isEmpty()) {
            showAlert("Error", "Please enter an item name!");
            return;
        }
        
        try {
            int qtyChange = Integer.parseInt(qtyField.getText().trim());
            InventoryManager.updateStock(item, qtyChange);
            loadInventoryData();
            itemField.clear();
            qtyField.clear();
            showAlert("Success", "Stock updated successfully!");
        } catch (NumberFormatException ex) {
            showAlert("Error", "Invalid quantity! Please enter a valid number.");
        }
    }

    private void addNewItem(TextField itemField, TextField qtyField) {
        String item = itemField.getText().trim();
        if (item.isEmpty()) {
            showAlert("Error", "Please enter an item name!");
            return;
        }
        
        try {
            int initialQuantity = Integer.parseInt(qtyField.getText().trim());
            InventoryManager.addNewItem(item, initialQuantity);
            loadInventoryData();
            itemField.clear();
            qtyField.clear();
            showAlert("Success", "New item added successfully!");
        } catch (NumberFormatException ex) {
            showAlert("Error", "Invalid quantity! Please enter a valid number.");
        }
    }

    private void loadInventoryData() {
        Map<String, Integer> inventory = InventoryManager.loadInventory();
        inventoryData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            inventoryData.add(new ItemStock(entry.getKey(), entry.getValue()));
        }
        table.setItems(inventoryData);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}