package com.shoppingcart.calculator;

import com.shoppingcart.ShoppingCart;
import com.shoppingcart.model.Category;
import com.shoppingcart.model.Product;
import com.shoppingcart.model.ProductItem;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class DeliveryCostCalculatorImplTest {

    @Test
    public void calculateFor_shouldReturnZero_whenShoppingCartIsNull() {

        ShoppingCart shoppingCart = null;
        DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculatorImpl(1, 2, 3);

        double deliveryCost = deliveryCostCalculator.calculateFor(shoppingCart);

        assertThat(deliveryCost).isZero();

    }

    @Test
    public void calculateFor_shouldReturnZero_whenShoppingCartHasNotProduct() {

        ShoppingCart shoppingCart = new ShoppingCart();
        DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculatorImpl(1, 2, 3);

        double deliveryCost = deliveryCostCalculator.calculateFor(shoppingCart);

        assertThat(deliveryCost).isZero();

    }

    @Test
    public void calculateFor_shouldReturnCalculatedDeliveryCost_whenShoppingCartHasAnyProduct() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 50, category);

        ShoppingCart shoppingCart = Mockito.mock(ShoppingCart.class);

        when(shoppingCart.getProductItemList()).thenReturn(Collections.singletonList(new ProductItem(product, 2)));
        when(shoppingCart.getNumberOfDelivers()).thenReturn(2);
        when(shoppingCart.getNumberOfProducts()).thenReturn(3);

        DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculatorImpl(1, 2, 3);
        double deliveryCost = deliveryCostCalculator.calculateFor(shoppingCart);

        assertThat(deliveryCost).isEqualTo(11);

    }
}
