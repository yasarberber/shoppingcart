package com.shoppingcart.model;

import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryTest {

    @Test
    public void add_shouldAddSubCategoryToCategoryList_whenSubCategoryIsNotNull() {

        Category category = new Category("Test category");
        Category subCategory = new Category("Test subCategory");

        category.add(subCategory);

        assertThat(category.getCategoryList()).hasSize(1);
        assertThat(category.getCategoryList()).containsOnly(subCategory);
    }

    @Test
    public void add_shoulNotdAddSubCategoryToCategoryListTwice_whenSubCategoryExistsInCategoryList() {

        Category category = new Category("Test category");
        Category subCategory = new Category("Test subCategory");

        category.add(subCategory);
        category.add(subCategory);

        assertThat(category.getCategoryList()).hasSize(1);
        assertThat(category.getCategoryList()).containsOnly(subCategory);
    }

    @Test
    public void add_shouldNotAddSubCategoryToCategoryList_whenSubCategoryIsNull() {

        Category category = new Category("Test category");
        Category subCategory = null;

        category.add(subCategory);

        assertThat(category.getCategoryList()).isEmpty();
    }

    @Test
    public void add_shouldNotAddSubCategoryToCategoryList_whenSubCategoryIsEqualToCategoryItself() {

        Category category = new Category("Test category");
        Category subCategory = category;

        category.add(subCategory);

        assertThat(category.getCategoryList()).isEmpty();
    }

    @Test
    public void getCategories_shouldReturnSetContainsOnlyCategoryItself_whenCategoryHasNoSubCategory() {

        Category category = new Category("Test category");

        Set<Category> categories = category.getCategories();

        assertThat(categories).containsOnly(category);
    }

    @Test
    public void getCategories_shouldReturnSetContainsCategoryWithSubCategories_whenCategoryHasAnySubCategory() {

        Category category = new Category("Test category");
        Category subCategory = new Category("Test subCategory");
        Category subSubCategory = new Category("Test subSubCategory");

        subCategory.add(subSubCategory);
        category.add(subCategory);

        Set<Category> categories = category.getCategories();

        assertThat(categories).containsOnly(category, subCategory, subSubCategory);
        assertThat(categories).extracting(Category::getTitle).containsOnly("Test category", "Test subCategory", "Test subSubCategory");
    }
}
