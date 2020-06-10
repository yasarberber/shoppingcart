package com.shoppingcart.calculator;

import com.shoppingcart.ShoppingCart;
import com.shoppingcart.model.Campaign;
import com.shoppingcart.model.Category;
import com.shoppingcart.model.Product;
import com.shoppingcart.type.DiscountType;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CampaignCalculatorTest {

    @Test
    public void calculate_shouldReturnZero_whenCartHasNoProducts() {

        Category category = new Category("Test Category");
        Campaign campaign = new Campaign(category, 10, 1, DiscountType.AMOUNT);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.applyDiscounts(campaign);

        CampaignCalculator campaignCalculator = new CampaignCalculator();
        double campaignDiscount = campaignCalculator.calculate(campaign, shoppingCart);

        assertThat(campaignDiscount).isZero();
    }

    @Test
    public void calculate_shouldReturnZero_whenCartHasProductsNotRelatedWithCampaign() {

        Category category1 = new Category("Test Category 1");
        Category category2 = new Category("Test Category 2");

        Campaign campaign = new Campaign(category1, 10, 1, DiscountType.AMOUNT);

        Product product = new Product("Test Product", 100, category2);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyDiscounts(campaign);

        CampaignCalculator campaignCalculator = new CampaignCalculator();
        double campaignDiscount = campaignCalculator.calculate(campaign, shoppingCart);

        assertThat(campaignDiscount).isZero();
    }

    @Test
    public void calculate_shouldReturnAmountCalculatedWithRate_whenCartHasProductsRelatedWithCampaignCategoryAndDiscountTypeIsRate() {

        Category category = new Category("Test Category");

        Campaign campaign = new Campaign(category, 10, 1, DiscountType.RATE);

        Product product = new Product("Test Product", 100, category);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 2);
        shoppingCart.applyDiscounts(campaign);

        CampaignCalculator campaignCalculator = new CampaignCalculator();
        double campaignDiscount = campaignCalculator.calculate(campaign, shoppingCart);

        assertThat(campaignDiscount).isEqualTo(20);
    }

    @Test
    public void calculate_shouldReturnAmountCalculatedWithRate_whenCartHasProductsRelatedWithCampaignSubCategoryAndDiscountTypeIsRate() {

        Category subCategory = new Category("Test Subcategory");

        Category category = new Category("Test Category");
        category.add(subCategory);

        Campaign campaign = new Campaign(category, 10, 1, DiscountType.RATE);

        Product product1 = new Product("Test Product 1", 100, category);
        Product product2 = new Product("Test Product 2", 200, subCategory);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product1, 2);
        shoppingCart.addItem(product2, 1);
        shoppingCart.applyDiscounts(campaign);

        CampaignCalculator campaignCalculator = new CampaignCalculator();
        double campaignDiscount = campaignCalculator.calculate(campaign, shoppingCart);

        assertThat(campaignDiscount).isEqualTo(40);
    }

    @Test
    public void calculate_shouldReturnAmountCalculated_whenCartHasProductsRelatedWithCampaignCategoryAndDiscountTypeIsAmount() {

        Category category = new Category("Test Category");

        Campaign campaign = new Campaign(category, 10, 1, DiscountType.AMOUNT);

        Product product = new Product("Test Product", 100, category);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 2);
        shoppingCart.applyDiscounts(campaign);

        CampaignCalculator campaignCalculator = new CampaignCalculator();
        double campaignDiscount = campaignCalculator.calculate(campaign, shoppingCart);

        assertThat(campaignDiscount).isEqualTo(10);
    }


    @Test
    public void calculate_shouldReturnTotalAmountOfProducts_whenCartHasProductsRelatedWithCampaignsAndDiscountAmountExceedsTotalAmountOfProducts() {

        Category category = new Category("Test Category");

        Campaign campaign = new Campaign(category, 100, 1, DiscountType.AMOUNT);

        Product product = new Product("Test Product 1", 10, category);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 3);
        shoppingCart.applyDiscounts(campaign);

        CampaignCalculator campaignCalculator = new CampaignCalculator();
        double campaignDiscount = campaignCalculator.calculate(campaign, shoppingCart);

        assertThat(campaignDiscount).isEqualTo(30);
    }

    @Test
    public void calculate_shouldReturnZero_whenCartHasProductsRelatedWithCampaignCategoryAndDiscountTypeIsNotAmountOrRate() {

        Campaign campaign = Mockito.mock(Campaign.class);

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 100, category);

        when(campaign.isValid()).thenReturn(true);
        when(campaign.getCategory()).thenReturn(category);
        when(campaign.getDiscountType()).thenReturn(null);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 2);
        shoppingCart.applyDiscounts(campaign);

        CampaignCalculator campaignCalculator = new CampaignCalculator();
        double campaignDiscount = campaignCalculator.calculate(campaign, shoppingCart);

        assertThat(campaignDiscount).isZero();
    }
}
