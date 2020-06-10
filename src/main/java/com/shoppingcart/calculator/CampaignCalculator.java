package com.shoppingcart.calculator;

import com.shoppingcart.ShoppingCart;
import com.shoppingcart.model.Campaign;
import com.shoppingcart.model.Discount;
import com.shoppingcart.model.ProductItem;

import java.util.List;

public class CampaignCalculator extends DiscountCalculator {

    @Override
    public double calculate(Discount discount, ShoppingCart cart) {

        Campaign campaign = (Campaign) discount;
        double campaignAmount = 0;

        List<ProductItem> productItems = cart.getProductsByCategory(campaign.getCategory());

        if (cart.getTotalQuantities(productItems) >= campaign.getNumberOfProducts()) {

            double productsAmount = productItems.stream()
                    .mapToDouble(productItem -> productItem.getProduct().getPrice() * productItem.getQuantity())
                    .sum();

            campaignAmount = super.calculateDiscountAmount(productsAmount, campaign);
        }
        return campaignAmount;
    }
}
