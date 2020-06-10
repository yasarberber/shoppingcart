package com.shoppingcart.calculator;

import com.shoppingcart.ShoppingCart;
import com.shoppingcart.model.Discount;
import com.shoppingcart.type.DiscountType;

import java.util.Objects;

public abstract class DiscountCalculator {

    public double calculate(Discount discount, ShoppingCart cart){
        double discountAmount = 0;

        if(Objects.nonNull(discount) && Objects.nonNull(cart)){
            discountAmount = calculateFor(discount,cart);
        }
        return discountAmount;
    }

    public abstract double calculateFor(Discount discount, ShoppingCart cart);

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
