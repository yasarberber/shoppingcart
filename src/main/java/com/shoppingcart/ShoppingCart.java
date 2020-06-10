package com.shoppingcart;

import com.shoppingcart.calculator.DeliveryCostCalculator;
import com.shoppingcart.model.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

public class ShoppingCart {

    @Getter
    private List<ProductItem> productItemList = new ArrayList<>();

    @Getter
    private List<Campaign> campaigns = new ArrayList<>();

    @Getter
    private Coupon coupon;

    @Setter
    private DeliveryCostCalculator deliveryCostCalculator;

    public void addItem(Product product, int quantity) {
        if (Objects.nonNull(product) && quantity > 0 && product.isValid()) {
            this.productItemList.add(new ProductItem(product, quantity));
        }
    }

    public void applyDiscounts(Campaign... campaigns) {
        if (Objects.nonNull(campaigns)) {
            Arrays.stream(campaigns)
                    .filter(Objects::nonNull)
                    .filter(Campaign::isValid)
                    .collect(Collectors.toCollection(() -> this.campaigns));
        }
    }

    public void applyCoupon(Coupon coupon) {
        if (Objects.nonNull(coupon) && coupon.isValid()) {
            this.coupon = coupon;
        }
    }

    public double getTotalAmountAfterDiscounts() {
        double amount = getRemainingAmountAfterApplyingCampaign() - getCouponDiscount();
        return amount < 0 ? 0 : amount;
    }

    public double getRemainingAmountAfterApplyingCampaign() {
        double amount = getTotalAmount() - getCampaignDiscount();
        return amount < 0 ? 0 : amount;
    }

    private double getTotalAmount() {
        return this.productItemList.stream().mapToDouble(item -> (item.getProduct().getPrice() * item.getQuantity())).sum();
    }

    public double getCampaignDiscount() {
        return this.campaigns.stream()
                .mapToDouble(campaign -> campaign.calculateFor(this))
                .max()
                .orElse(0);
    }

    public double getCouponDiscount() {

        double couponDiscount = 0;
        if (Objects.nonNull(this.coupon)) {
            couponDiscount = this.coupon.calculateFor(this);
        }
        return couponDiscount;
    }

    public List<ProductItem> getProductsByCategory(Category category) {
        return this.productItemList.stream()
                .filter(productItem -> category.getCategories().contains(productItem.getProduct().getCategory()))
                .collect(Collectors.toList());
    }

    public int getTotalQuantities(List<ProductItem> productItems) {
        return productItems.stream().mapToInt(ProductItem::getQuantity).sum();
    }

    public double getDeliveryCost() {

        double deliveryCost = 0;
        if (!this.getProductItemList().isEmpty() && Objects.nonNull(this.deliveryCostCalculator)) {
            deliveryCost = this.deliveryCostCalculator.calculateFor(this);
        }
        return deliveryCost;
    }

    public int getNumberOfDelivers() {
        return this.productItemList.stream().map(productItem -> productItem.getProduct().getCategory()).collect(Collectors.toSet()).size();
    }

    public int getNumberOfProducts() {
        return this.productItemList.stream().map(ProductItem::getProduct).collect(Collectors.toSet()).size();
    }

    public String print() {

        StringBuilder msg = new StringBuilder();

        if (!this.productItemList.isEmpty()) {
            Map<Category, List<ProductItem>> productByCategories = this.productItemList.stream().collect(groupingBy(productItem -> productItem.getProduct().getCategory()));

            msg.append("\n\nProducts By Categories");
            msg.append("\n----------------------");
            productByCategories.forEach((category, productItems) -> {

                msg.append("\n\n* Category Name : ").append(category.getTitle());
                msg.append("\n** Products");

                Map<Product, Integer> products = productItems.stream().collect(groupingBy(ProductItem::getProduct, summingInt(ProductItem::getQuantity)));
                products.forEach((product, quantity) -> {
                    msg.append("\n--> Product Name :\t").append(product.getTitle());
                    msg.append("\n--- Quantity     :\t").append(quantity);
                    msg.append("\n--- Unit Price   :\t").append(product.getPrice());
                    msg.append("\n--- Total Price  :\t").append(quantity * product.getPrice());
                });
            });
            msg.append("\n----------------------");
            msg.append("\nTOTAL DISCOUNT  : ").append((getCampaignDiscount() + getCouponDiscount()));
            msg.append("\nTOTAL AMOUNT    : ").append(getTotalAmount());
            msg.append("\nDELIVERY COST   : ").append(getDeliveryCost());
        }

        return msg.toString();
    }
}
