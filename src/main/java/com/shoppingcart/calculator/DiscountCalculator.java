package com.shoppingcart.calculator;

import com.shoppingcart.ShoppingCart;
import com.shoppingcart.model.Discount;
import com.shoppingcart.type.DiscountType;

public abstract class DiscountCalculator {

    //addValidate

    public abstract double calculate(Discount discount, ShoppingCart cart);

    public double calculateDiscountAmount(double amount, Discount discount) {

        double discountAmount = 0;

        if (discount.getDiscountType() == DiscountType.RATE) {
            discountAmount = amount * (discount.getDiscount() / 100);
        } else if (discount.getDiscountType() == DiscountType.AMOUNT) {
            discountAmount = discount.getDiscount();
        }

        return Math.min(discountAmount, amount);
    }

}
