package globalcafe;

/**
 * ComboItem is a convenience subclass of CartItem that groups three CartItems:
 * a drink, a snack and a bake. It appears as a single entry in the cart but
 * sums the prices of the parts.
 */
public class ComboItem extends CartItem {
    private final CartItem drink;
    private final CartItem snack;
    private final CartItem bake;

    public ComboItem(String name, CartItem drink, CartItem snack, CartItem bake) {
        super(name, drink.getPrice() + snack.getPrice() + bake.getPrice(),
                // use drink image as representative (you can change)
                drink.getImagePath());
        this.drink = drink;
        this.snack = snack;
        this.bake = bake;
    }

    public CartItem getDrink() { return drink; }
    public CartItem getSnack() { return snack; }
    public CartItem getBake() { return bake; }

    @Override
    public double getFinalPrice() {
        // If CartItem.getFinalPrice() takes quantity and customizations into account,
        // you might prefer to override differently. Here we rely on base price * quantity.
        return super.getFinalPrice();
    }
}
