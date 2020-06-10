package com.shoppingcart.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @Test
    public void isValid_shouldValidatePriceAndReturnFalse_whenPriceIsLessThanZero() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", -1, category);

        boolean isValid = product.isValid();

        assertThat(isValid).isFalse();
    }

    @Test
    public void isValid_shouldValidateCatalogAndReturnFalse_whenCategoryIsNull() {

        Category category = null;
        Product product = new Product("Test Product", 0, category);

        boolean isValid = product.isValid();

        assertThat(isValid).isFalse();
    }

    @Test
    public void isValid_shouldValidateAndReturnTrue_whenProductHasValidValues() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 0, category);

        boolean isValid = product.isValid();

        assertThat(isValid).isTrue();
    }
}
