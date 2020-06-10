package com.shoppingcart.model;

import com.shoppingcart.ShoppingCart;
import com.shoppingcart.calculator.DiscountCalculator;
import com.shoppingcart.type.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Discount {
    private double discount;
    private DiscountType discountType;

    private DiscountCalculator calculator;

    abstract boolean isValid();

    public double calculateFor(ShoppingCart cart) {
        return this.calculator.calculate(this, cart);
    }
}
