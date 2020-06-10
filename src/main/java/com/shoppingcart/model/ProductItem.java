package com.shoppingcart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductItem {
    private Product product;
    private int quantity;
}
