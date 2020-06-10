package com.shoppingcart.model;

import com.shoppingcart.type.DiscountType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CampaignTest {

    @Test
    public void isValid_shouldValidateCategoryAndReturnFalse_whenCategoryIsNull() {

        Category category = null;
        Campaign campaign = new Campaign(category, 10, 1, DiscountType.AMOUNT);

        boolean isValid = campaign.isValid();

        assertThat(isValid).isFalse();
    }

    @Test
    public void isValid_shouldValidateDiscountAmountAndReturnFalse_whenDiscountAmountIsLessThanZero() {

        Category category = new Category("Test Category");
        Campaign campaign = new Campaign(category, -1, 1, DiscountType.AMOUNT);

        boolean isValid = campaign.isValid();

        assertThat(isValid).isFalse();
    }

    @Test
    public void isValid_shouldValidateNumberOfProductsAndReturnFalse_whenNumberOfProductsIsLessThanZero() {

        Category category = new Category("Test Category");
        Campaign campaign = new Campaign(category, 1, -1, DiscountType.AMOUNT);

        boolean isValid = campaign.isValid();

        assertThat(isValid).isFalse();
    }

    @Test
    public void isValid_shouldValidateDiscountTypeAndReturnFalse_whenDiscountTypeIsNull() {

        Category category = new Category("Test Category");
        Campaign campaign = new Campaign(category, 1, 1, null);

        boolean isValid = campaign.isValid();

        assertThat(isValid).isFalse();
    }

    @Test
    public void isValid_shouldValidateAndReturnTrue_whenCampaignHasValidValues() {

        Category category = new Category("Test Category");
        Campaign campaign = new Campaign(category, 1, 1, DiscountType.AMOUNT);

        boolean isValid = campaign.isValid();

        assertThat(isValid).isTrue();
    }
}
