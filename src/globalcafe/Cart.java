/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package globalcafe;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        items.add(item);
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getFinalPrice).sum();
    }

    public int getItemCount() {
        return items.size();
    }

    public double getTotalAmount() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public void clearCart() {
        items.clear();
    }

}
