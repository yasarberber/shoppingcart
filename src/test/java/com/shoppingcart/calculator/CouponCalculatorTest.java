package com.shoppingcart.calculator;

import com.shoppingcart.ShoppingCart;
import com.shoppingcart.model.Campaign;
import com.shoppingcart.model.Category;
import com.shoppingcart.model.Coupon;
import com.shoppingcart.model.Product;
import com.shoppingcart.type.DiscountType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CouponCalculatorTest {

    @Test
    public void calculate_shouldReturnZero_whenCartHasNoItem() {

        ShoppingCart shoppingCart = new ShoppingCart();
        Coupon coupon = new Coupon(100, 10, DiscountType.AMOUNT);

        shoppingCart.applyCoupon(coupon);

        CouponCalculator couponCalculator = new CouponCalculator();
        double couponDiscount = couponCalculator.calculate(coupon, shoppingCart);

        assertThat(couponDiscount).isZero();
    }

    @Test
    public void calculate_shouldReturnZero_whenTotalAmountOfProductsIsLessThanMinAmountOfCoupon() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 100, category);

        Coupon coupon = new Coupon(110, 10, DiscountType.AMOUNT);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyCoupon(coupon);

        CouponCalculator couponCalculator = new CouponCalculator();
        double couponDiscount = couponCalculator.calculate(coupon, shoppingCart);

        assertThat(couponDiscount).isZero();
    }

    @Test
    public void calculate_shouldReturnZero_whenRemainingAmountOfProductsAfterApplyingCampaignsIsLessThanMinAmountOfCoupon() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 100, category);

        Campaign campaign = new Campaign(category, 20, 1, DiscountType.RATE);

        Coupon coupon = new Coupon(200, 20, DiscountType.AMOUNT);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 2);
        shoppingCart.applyDiscounts(campaign);
        shoppingCart.applyCoupon(coupon);

        CouponCalculator couponCalculator = new CouponCalculator();
        double couponDiscount = couponCalculator.calculate(coupon, shoppingCart);

        assertThat(couponDiscount).isZero();
    }

    @Test
    public void calculate_shouldReturnDiscountAmount_whenTotalAmountOfProductsIsNotLessThanDiscountMinimumAmount() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 100, category);

        Coupon coupon = new Coupon(90, 10, DiscountType.AMOUNT);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyCoupon(coupon);

        CouponCalculator couponCalculator = new CouponCalculator();
        double couponDiscount = couponCalculator.calculate(coupon, shoppingCart);

        assertThat(couponDiscount).isEqualTo(10);
    }

    @Test
    public void calculate_shouldReturnDiscountAmount_whenRemainingAmountOfProductsAfterApplyingCampaignsIsNotLessThanDiscountMinimumAmount() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 100, category);

        Campaign campaign = new Campaign(category, 20, 2, DiscountType.RATE);

        Coupon coupon = new Coupon(100, 10, DiscountType.AMOUNT);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyDiscounts(campaign);
        shoppingCart.applyCoupon(coupon);

        CouponCalculator couponCalculator = new CouponCalculator();
        double couponDiscount = couponCalculator.calculate(coupon, shoppingCart);

        assertThat(couponDiscount).isEqualTo(10);
    }

    @Test
    public void calculate_shouldReturnTotalAmountOfProducts_whenTotalAmountOfShoppingCartIsNotLessThanDiscountMinimumAmountAndDiscountAmountExceedsTotalAmountOfProducts() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 100, category);

        Coupon coupon = new Coupon(90, 150, DiscountType.AMOUNT);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyCoupon(coupon);

        CouponCalculator couponCalculator = new CouponCalculator();
        double couponDiscount = couponCalculator.calculate(coupon, shoppingCart);

        assertThat(couponDiscount).isEqualTo(100);
    }
}
