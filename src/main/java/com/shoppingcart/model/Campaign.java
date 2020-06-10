package com.shoppingcart.model;

import com.shoppingcart.calculator.CampaignCalculator;
import com.shoppingcart.type.DiscountType;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Campaign extends Discount {

    private Category category;
    private int numberOfProducts;

    public Campaign(Category category, double amount, int numberOfProducts, DiscountType discountType) {
        super(amount, discountType, new CampaignCalculator());
        this.category = category;
        this.numberOfProducts = numberOfProducts;
    }

    @Override
    public boolean isValid() {
        return Objects.nonNull(this.category) && (this.getDiscount() >= 0) && (this.numberOfProducts >= 0) && Objects.nonNull(this.getDiscountType());
    }
}
