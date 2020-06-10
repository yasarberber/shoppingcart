package com.shoppingcart.calculator;

import com.shoppingcart.ShoppingCart;
import com.shoppingcart.model.Coupon;
import com.shoppingcart.model.Discount;

public class CouponCalculator extends DiscountCalculator {
    @Override
    public double calculate(Discount discount, ShoppingCart cart) {

        Coupon coupon = (Coupon) discount;

        double couponDiscount = 0;
        double cartRemainingAmount = cart.getRemainingAmountAfterApplyingCampaign();

        if (cartRemainingAmount >= coupon.getMinAmount()) {
            couponDiscount = calculateDiscountAmount(cartRemainingAmount, coupon);
        }

        return couponDiscount;
    }
}
