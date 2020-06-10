package com.shoppingcart.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class Category {

    private String title;

    private Set<Category> categoryList = new HashSet<>();

    public Category(String title) {
        this.title = title;
    }

    public void add(Category subCategory) {

        if (Objects.nonNull(subCategory) && !Objects.equals(this, subCategory)) {
            categoryList.add(subCategory);
        }
    }

    public Set<Category> getCategories() {

        Set<Category> allCategories = new HashSet<>();

        allCategories.add(this);
        allCategories.addAll(getSubCategories());

        return allCategories;
    }

    private Set<Category> getSubCategories() {
        Set<Category> allCategories = new HashSet<>();
        for (Category category : categoryList) {
            allCategories.addAll(category.getCategories());
        }

        return allCategories;
    }
}
