package com.shoppingcart.model;

import com.shoppingcart.calculator.CouponCalculator;
import com.shoppingcart.type.DiscountType;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Coupon extends Discount {

    private double minAmount;

    public Coupon(double minAmount, double discount, DiscountType discountType) {
        super(discount, discountType, new CouponCalculator());
        this.minAmount = minAmount;
    }

    @Override
    public boolean isValid() {
        return this.minAmount >= 0 && this.getDiscount() >= 0 && Objects.nonNull(this.getDiscountType());
    }
}
