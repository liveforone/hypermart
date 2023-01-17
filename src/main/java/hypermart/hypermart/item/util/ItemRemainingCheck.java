package hypermart.hypermart.item.util;

import hypermart.hypermart.basket.model.Basket;

import java.util.List;

public class ItemRemainingCheck {

    public static boolean isSoldOut(int remaining) {
        return remaining <= 0;
    }

    public static boolean isOverRemaining(int remaining, int orderQuantity) {
        return remaining - orderQuantity < 0;
    }

    public static boolean isSoldOutForMultiple(List<Basket> baskets) {
        for (Basket basket : baskets) {
            int remaining = basket.getItem().getRemaining();
            if (remaining <= 0) {
                return true;
            }
        }
        return false;
    }
}
