package com.shoppingcart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class Product {

    private String title;
    private int price;
    private Category category;

    public boolean isValid() {
        return this.getPrice() >= 0 && Objects.nonNull(this.getCategory());
    }
}
