package com.shoppingcart;

import com.shoppingcart.calculator.DeliveryCostCalculator;
import com.shoppingcart.model.*;
import com.shoppingcart.type.DiscountType;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShoppingCartTest {

    @Test
    public void addItem_shouldNotAddProduct_whenProductIsNull() {

        Product product = null;

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);

        assertThat(shoppingCart.getProductItemList()).isEmpty();
    }

    @Test
    public void addItem_shouldNotAddProduct_whenQuantityIsLessThanZero() {

        Category category = new Category("Test category");
        Product product = new Product("Test product", 0, category);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, -1);

        assertThat(shoppingCart.getProductItemList()).isEmpty();
    }

    @Test
    public void addItem_shouldNotAddProduct_whenQuantityIsZero() {

        Category category = new Category("Test category");
        Product product = new Product("Test product", 0, category);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 0);

        assertThat(shoppingCart.getProductItemList()).isEmpty();
    }

    @Test
    public void addItem_shouldNotAddProduct_whenProductIsInvalid() {

        Product product = Mockito.mock(Product.class);

        when(product.isValid()).thenReturn(false);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);

        assertThat(shoppingCart.getProductItemList()).isEmpty();
    }

    @Test
    public void addItem_shouldAddProduct_whenProductIsValid() {

        Category category = new Category("Category");
        Product product = new Product("Product", 10, category);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);

        assertThat(shoppingCart.getProductItemList()).hasSize(1);

        ProductItem productItem = shoppingCart.getProductItemList().get(0);
        assertThat(productItem.getProduct()).isEqualToComparingFieldByField(product);
        assertThat(productItem.getQuantity()).isEqualTo(1);
    }

    @Test
    public void applyDiscounts_shouldNotApplyAnyCampaign_whenInputIsNull() {

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.applyDiscounts(null);

        assertThat(shoppingCart.getCampaigns()).isEmpty();
    }

    @Test
    public void applyDiscounts_shouldNotApplyAnyNullCampaign() {

        Campaign validCampaign = mock(Campaign.class);
        Campaign nullCampaign = null;

        when(validCampaign.isValid()).thenReturn(true);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.applyDiscounts(nullCampaign, validCampaign);

        assertThat(shoppingCart.getCampaigns()).hasSize(1);
        assertThat(shoppingCart.getCampaigns()).containsOnly(validCampaign);
    }

    @Test
    public void applyDiscounts_shouldNotApplyAnyInvalidCampaign() {

        Campaign inValidCampaign = mock(Campaign.class);
        Campaign vslidCampaign = mock(Campaign.class);

        when(inValidCampaign.isValid()).thenReturn(false);
        when(vslidCampaign.isValid()).thenReturn(true);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.applyDiscounts(inValidCampaign, vslidCampaign);

        assertThat(shoppingCart.getCampaigns()).hasSize(1);
        assertThat(shoppingCart.getCampaigns()).containsOnly(vslidCampaign);
    }

    @Test
    public void applyDiscounts_shouldApplyAnyValidCampaigns() {

        Category category = new Category("Test Category");
        Campaign campaign = new Campaign(category, 1, 1, DiscountType.AMOUNT);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.applyDiscounts(campaign);

        assertThat(shoppingCart.getCampaigns()).hasSize(1);

        Campaign appliedCampaign = shoppingCart.getCampaigns().get(0);
        assertThat(appliedCampaign).isEqualToComparingFieldByField(campaign);
    }

    @Test
    public void applyCoupon_shouldNotApply_whenCouponIsNull() {

        Coupon nullCoupon = null;

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.applyCoupon(nullCoupon);

        assertThat(shoppingCart.getCoupon()).isNull();
    }

    @Test
    public void applyCoupon_shouldNotApply_whenCouponIsInvalid() {

        Coupon invalidCoupon = Mockito.mock(Coupon.class);

        when(invalidCoupon.isValid()).thenReturn(false);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.applyCoupon(invalidCoupon);

        assertThat(shoppingCart.getCoupon()).isNull();
    }

    @Test
    public void applyCoupon_shouldApply_whenCouponIsValid() {

        Coupon coupon = new Coupon(0, 0, DiscountType.AMOUNT);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.applyCoupon(coupon);

        assertThat(shoppingCart.getCoupon()).isEqualToComparingFieldByField(coupon);
    }

    @Test
    public void getCampaignDiscount_shouldReturnZero_whenShoppingCartHasNoCampaign() {

        ShoppingCart shoppingCart = new ShoppingCart();

        double campaignDiscount = shoppingCart.getCampaignDiscount();

        assertThat(campaignDiscount).isZero();
    }

    @Test
    public void getCampaignDiscount_shouldReturnZero_whenShoppingCartHasNoProducts() {

        Category category = new Category("Test Category");
        Campaign campaign = new Campaign(category, 10, 1, DiscountType.AMOUNT);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.applyDiscounts(campaign);

        double campaignDiscount = shoppingCart.getCampaignDiscount();

        assertThat(campaignDiscount).isZero();
    }

    @Test
    public void getCampaignDiscount_shouldReturnCalculatedAmount_whenShoppingCartHasOneCampaign() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 100, category);

        Campaign campaign = Mockito.mock(Campaign.class);

        when(campaign.isValid()).thenReturn(true);
        when(campaign.calculateFor(any(ShoppingCart.class))).thenReturn(Double.valueOf(20));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 2);
        shoppingCart.applyDiscounts(campaign);

        double campaignDiscount = shoppingCart.getCampaignDiscount();

        assertThat(campaignDiscount).isEqualTo(20);
    }

    @Test
    public void getCampaignDiscount_shouldReturnMaxAmountOfCalculatedDiscounts_whenShoppingCartHasMoreThanOneCampaignRelatedWithProducts() {

        Category category = new Category("Test Category 1");
        Product product = new Product("Test Product", 100, category);

        Campaign campaign1 = mock(Campaign.class);
        Campaign campaign2 = mock(Campaign.class);

        when(campaign1.isValid()).thenReturn(true);
        when(campaign2.isValid()).thenReturn(true);

        when(campaign1.calculateFor(any(ShoppingCart.class))).thenReturn(Double.valueOf(20));
        when(campaign2.calculateFor(any(ShoppingCart.class))).thenReturn(Double.valueOf(40));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 4);
        shoppingCart.applyDiscounts(campaign1, campaign2);

        double campaignDiscount = shoppingCart.getCampaignDiscount();

        assertThat(campaignDiscount).isEqualTo(40);
    }

    @Test
    public void getCouponDiscount_shouldReturnZero_whenShoppingCartHasNoCoupon() {

        ShoppingCart shoppingCart = new ShoppingCart();

        double couponDiscount = shoppingCart.getCouponDiscount();

        assertThat(couponDiscount).isZero();
    }

    @Test
    public void getCouponDiscount_shouldReturnZero_whenShoppingCartHasNoItem() {

        ShoppingCart shoppingCart = new ShoppingCart();
        Coupon coupon = new Coupon(100, 10, DiscountType.AMOUNT);

        shoppingCart.applyCoupon(coupon);

        double couponDiscount = shoppingCart.getCouponDiscount();

        assertThat(couponDiscount).isZero();
    }

    @Test
    public void getCouponDiscount_shouldReturnCalculatedAmount_whenShoppingCartHasCoupon() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 100, category);

        Coupon coupon = Mockito.mock(Coupon.class);

        when(coupon.isValid()).thenReturn(true);
        when(coupon.calculateFor(any(ShoppingCart.class))).thenReturn(Double.valueOf(20));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyCoupon(coupon);

        double couponDiscount = shoppingCart.getCouponDiscount();

        assertThat(couponDiscount).isEqualTo(20);
    }

    @Test
    public void getTotalAmountAfterDiscounts_shouldReturnZero_whenShoppingCartHasNoProduct() {

        ShoppingCart shoppingCart = new ShoppingCart();

        double totalAmountAfterDiscount = shoppingCart.getTotalAmountAfterDiscounts();

        assertThat(totalAmountAfterDiscount).isZero();
    }

    @Test
    public void getTotalAmountAfterDiscounts_shouldRemainingAmountAfterApplyingCouponDiscount_whenShoppingCartHasCouponOnly() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 100, category);

        Coupon coupon = Mockito.mock(Coupon.class);

        when(coupon.isValid()).thenReturn(true);
        when(coupon.calculateFor(any(ShoppingCart.class))).thenReturn(Double.valueOf(20));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyCoupon(coupon);

        double totalAmountAfterDiscount = shoppingCart.getTotalAmountAfterDiscounts();

        assertThat(totalAmountAfterDiscount).isEqualTo(80);
    }

    @Test
    public void getTotalAmountAfterDiscounts_shouldReturnRemainingAmountAfterApplyingCampaign_whenShoppingCartHasAnyCampaignRelatedWithProducts() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 125, category);

        Campaign campaign = Mockito.mock(Campaign.class);

        when(campaign.isValid()).thenReturn(true);
        when(campaign.calculateFor(any(ShoppingCart.class))).thenReturn(Double.valueOf(20));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyDiscounts(campaign);

        double couponDiscount = shoppingCart.getTotalAmountAfterDiscounts();

        assertThat(couponDiscount).isEqualTo(105);
    }

    @Test
    public void getTotalAmountAfterDiscounts_shouldReturnRemainingAmountAfterApplyingDiscounts_whenShoppingCartHasCouponAndAnyCampaignRelatedWithProducts() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 100, category);

        Campaign campaign = Mockito.mock(Campaign.class);

        Coupon coupon = Mockito.mock(Coupon.class);

        when(campaign.isValid()).thenReturn(true);
        when(campaign.calculateFor(any(ShoppingCart.class))).thenReturn(Double.valueOf(20));
        when(coupon.isValid()).thenReturn(true);
        when(coupon.calculateFor(any(ShoppingCart.class))).thenReturn(Double.valueOf(20));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyDiscounts(campaign);
        shoppingCart.applyCoupon(coupon);

        double couponDiscount = shoppingCart.getTotalAmountAfterDiscounts();

        assertThat(couponDiscount).isEqualTo(60);
    }

    @Test
    public void getTotalAmountAfterDiscounts_shouldReturnZeroAfterApplyingDiscounts_whenTotalCalculatedDiscountAmountExceedsTotalAmountOfProducts() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 100, category);

        Campaign campaign = Mockito.mock(Campaign.class);

        Coupon coupon = Mockito.mock(Coupon.class);

        when(campaign.isValid()).thenReturn(true);
        when(campaign.calculateFor(any(ShoppingCart.class))).thenReturn(Double.valueOf(60));
        when(coupon.isValid()).thenReturn(true);
        when(coupon.calculateFor(any(ShoppingCart.class))).thenReturn(Double.valueOf(70));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyDiscounts(campaign);
        shoppingCart.applyCoupon(coupon);

        double couponDiscount = shoppingCart.getTotalAmountAfterDiscounts();

        assertThat(couponDiscount).isZero();
    }

    @Test
    public void getRemainingAmountAfterApplyingCampaign_shouldReturnZero_whenShoppingCartHasNoProduct() {

        ShoppingCart shoppingCart = new ShoppingCart();

        double totalAmountAfterDiscount = shoppingCart.getRemainingAmountAfterApplyingCampaign();

        assertThat(totalAmountAfterDiscount).isZero();
    }

    @Test
    public void getRemainingAmountAfterApplyingCampaign_shouldReturnRemainingAmountAfterApplyingCampaign_whenShoppingCartHasAnyCampaignRelatedWithProducts() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 125, category);

        Campaign campaign = Mockito.mock(Campaign.class);

        when(campaign.isValid()).thenReturn(true);
        when(campaign.calculateFor(any(ShoppingCart.class))).thenReturn(Double.valueOf(20));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyDiscounts(campaign);

        double couponDiscount = shoppingCart.getRemainingAmountAfterApplyingCampaign();

        assertThat(couponDiscount).isEqualTo(105);
    }

    @Test
    public void getTotalAmountAfterDiscounts_shouldReturnZeroAfterApplyingCampaign_whenCalculatedDiscountAmountExceedsTotalAmountOfProducts() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 50, category);

        Campaign campaign = Mockito.mock(Campaign.class);

        when(campaign.isValid()).thenReturn(true);
        when(campaign.calculateFor(any(ShoppingCart.class))).thenReturn(Double.valueOf(60));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyDiscounts(campaign);

        double couponDiscount = shoppingCart.getRemainingAmountAfterApplyingCampaign();

        assertThat(couponDiscount).isZero();
    }

    @Test
    public void getProductsByCategory_shouldReturnEmptyList_whenShoppingCartHasNoProduct() {

        Category category = new Category("Test Category");

        ShoppingCart shoppingCart = new ShoppingCart();

        List<ProductItem> productList = shoppingCart.getProductsByCategory(category);

        assertThat(productList).isEmpty();
    }

    @Test
    public void getProductsByCategory_shouldReturnEmptyList_whenShoppingCartHasProductsNotRelatedWithCategory() {

        Category category1 = new Category("Test Category 1");
        Category category2 = new Category("Test Category 2");

        Product product = new Product("Test Product", 50, category2);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 2);

        List<ProductItem> productList = shoppingCart.getProductsByCategory(category1);

        assertThat(productList).isEmpty();
    }

    @Test
    public void getProductsByCategory_shouldReturnProductList_whenShoppingCartHasProductsRelatedWithCategory() {

        Category category = new Category("Test Category");

        Product product = new Product("Test Product", 50, category);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 2);

        List<ProductItem> productList = shoppingCart.getProductsByCategory(category);

        assertThat(productList).hasSize(1);
        assertThat(productList).extracting(ProductItem::getProduct).containsOnly(product);
        assertThat(productList).extracting(ProductItem::getQuantity).containsOnly(2);
    }

    @Test
    public void getProductsByCategory_shouldReturnProductList_whenShoppingCartHasProductsRelatedWithCategoryAndSubCategories() {

        Category subCategory = new Category("Test Subcategory");
        Category category = new Category("Test Category");
        category.add(subCategory);

        Product product1 = new Product("Test Product 1", 50, category);
        Product product2 = new Product("Test Product 2", 50, subCategory);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product1, 2);
        shoppingCart.addItem(product2, 1);

        List<ProductItem> productList = shoppingCart.getProductsByCategory(category);

        assertThat(productList).hasSize(2);
        assertThat(productList).extracting(ProductItem::getProduct).containsOnly(product1, product2);
        assertThat(productList).extracting(ProductItem::getQuantity).containsOnly(2, 1);
    }

    @Test
    public void getTotalQuantities_shouldReturnZero_whenShoppingCartHasNoProduct() {

        ShoppingCart shoppingCart = new ShoppingCart();

        int totalQuantity = shoppingCart.getTotalQuantities(shoppingCart.getProductItemList());

        assertThat(totalQuantity).isZero();
    }

    @Test
    public void getTotalQuantities_shouldReturnTotalProductQuantity_whenShoppingCartHasProducts() {

        Category subCategory = new Category("Test Subcategory");
        Category category = new Category("Test Category");
        category.add(subCategory);

        Product product1 = new Product("Test Product 1", 50, category);
        Product product2 = new Product("Test Product 2", 50, subCategory);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product1, 2);
        shoppingCart.addItem(product2, 1);
        shoppingCart.addItem(product1, 2);

        int totalQuantity = shoppingCart.getTotalQuantities(shoppingCart.getProductItemList());

        assertThat(totalQuantity).isEqualTo(5);
    }

    @Test
    public void getDeliveryCost_shouldReturnZero_whenShoppingCartHasNoProduct() {

        ShoppingCart shoppingCart = new ShoppingCart();

        double deliveryCost = shoppingCart.getDeliveryCost();

        assertThat(deliveryCost).isZero();
    }

    @Test
    public void getDeliveryCost_shouldReturnZero_whenShoppingCartHasProductButDeliveryCostCalculatorIsNotSet() {

        Category category = new Category("Test Category");

        Product product = new Product("Test Product", 50, category);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 2);

        double deliveryCost = shoppingCart.getDeliveryCost();

        assertThat(deliveryCost).isZero();
    }

    @Test
    public void getDeliveryCost_shouldReturnCalculatedDeliveryCost_whenShoppingCartHasProductAndDeliveryCostCalculatorIsSet() {

        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 50, category);

        DeliveryCostCalculator deliveryCostCalculator = Mockito.mock(DeliveryCostCalculator.class);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 2);
        shoppingCart.setDeliveryCostCalculator(deliveryCostCalculator);

        when(deliveryCostCalculator.calculateFor(shoppingCart)).thenReturn(Double.valueOf(10));

        double deliveryCost = shoppingCart.getDeliveryCost();

        assertThat(deliveryCost).isEqualTo(10);
    }

    @Test
    public void getNumberOfDelivers_shouldReturnZero_whenShoppingCartHasNoProduct() {

        ShoppingCart shoppingCart = new ShoppingCart();

        double deliveryCost = shoppingCart.getNumberOfDelivers();

        assertThat(deliveryCost).isZero();
    }

    @Test
    public void getNumberOfDelivers_shouldReturnDistinctNumberOfCategoriesOfProducts() {

        Category category1 = new Category("Test Category");
        Product product1 = new Product("Test Product", 50, category1);

        Category category2 = new Category("Test Category");
        Product product2 = new Product("Test Product", 50, category2);

        Product product3 = new Product("Test Product", 50, category2);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product1, 3);
        shoppingCart.addItem(product2, 2);
        shoppingCart.addItem(product3, 1);

        double deliveryCost = shoppingCart.getNumberOfDelivers();

        assertThat(deliveryCost).isEqualTo(2);
    }

    @Test
    public void getNumberOfProducts_shouldReturnZero_whenShoppingCartHasNoProduct() {

        ShoppingCart shoppingCart = new ShoppingCart();

        double deliveryCost = shoppingCart.getNumberOfProducts();

        assertThat(deliveryCost).isZero();
    }

    @Test
    public void getNumberOfProducts_shouldReturnDistinctNumberOfCategoriesOfProducts() {

        Category category1 = new Category("Test Category");
        Product product1 = new Product("Test Product", 50, category1);

        Category category2 = new Category("Test Category");
        Product product2 = new Product("Test Product", 50, category2);

        Product product3 = new Product("Test Product", 50, category2);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product1, 3);
        shoppingCart.addItem(product2, 2);
        shoppingCart.addItem(product3, 1);

        double deliveryCost = shoppingCart.getNumberOfProducts();

        assertThat(deliveryCost).isEqualTo(3);
    }

    @Test
    public void print_shouldReturnEmptyString_whenShoppingCartHasNoProduct() {

        ShoppingCart shoppingCart = new ShoppingCart();

        String printMessage = shoppingCart.print();

        assertThat(printMessage).isEmpty();
    }

    @Test
    public void print_shouldReturnNotEmptyString_whenShoppingCartHasProduct() {

        Category category1 = new Category("Test Category");
        Product product1 = new Product("Test Product", 50, category1);

        Category category2 = new Category("Test Category");
        Product product2 = new Product("Test Product", 50, category2);

        Product product3 = new Product("Test Product", 50, category2);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product1, 3);
        shoppingCart.addItem(product2, 2);
        shoppingCart.addItem(product3, 1);

        String printMessage = shoppingCart.print();

        assertThat(printMessage).isNotEmpty();
    }
}
