package com.shoppingcart.calculator;

import com.shoppingcart.ShoppingCart;

import java.util.Objects;

public class DeliveryCostCalculatorImpl implements DeliveryCostCalculator {

    private double costPerDelivery;
    private double costPerProduct;
    private double fixedCost;

    public DeliveryCostCalculatorImpl(double costPerDelivery, double costPerProduct, double fixedCost) {
        this.costPerDelivery = Math.max(costPerDelivery, 0);
        this.costPerProduct = Math.max(costPerProduct, 0);
        this.fixedCost = Math.max(fixedCost, 0);
    }

    public double calculateFor(ShoppingCart shoppingCart) {

        double deliveryCost = 0;

        if (Objects.nonNull(shoppingCart) && !shoppingCart.getProductItemList().isEmpty()) {

            deliveryCost = (shoppingCart.getNumberOfDelivers() * costPerDelivery)
                    + (shoppingCart.getNumberOfProducts() * costPerProduct)
                    + fixedCost;
        }

        return deliveryCost;
    }
}
