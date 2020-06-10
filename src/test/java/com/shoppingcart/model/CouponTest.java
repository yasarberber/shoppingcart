package com.shoppingcart.model;

import com.shoppingcart.type.DiscountType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CouponTest {

    @Test
    public void isValid_shouldValidateMinAmountAndReturnFalse_whenMinAmountIsLessThanZero() {

        Coupon coupon = new Coupon(-1, 0, DiscountType.AMOUNT);

        boolean isValid = coupon.isValid();

        assertThat(isValid).isFalse();
    }

    @Test
    public void isValid_shouldValidateDiscountAmountAndReturnFalse_whenDiscountAmountIsLessThanZero() {

        Coupon coupon = new Coupon(0, -1, DiscountType.AMOUNT);

        boolean isValid = coupon.isValid();

        assertThat(isValid).isFalse();
    }

    @Test
    public void isValid_shouldValidateDiscountTypeAndReturnFalse_whenDiscountTypeIsNull() {

        Coupon coupon = new Coupon(0, 0, null);

        boolean isValid = coupon.isValid();

        assertThat(isValid).isFalse();
    }

    @Test
    public void isValid_shouldValidateAndReturnTrue_whenCouponHasValidValues() {

        Coupon coupon = new Coupon(0, 0, DiscountType.AMOUNT);

        boolean isValid = coupon.isValid();

        assertThat(isValid).isTrue();
    }
}
