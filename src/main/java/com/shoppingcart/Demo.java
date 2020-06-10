package com.shoppingcart;

import com.shoppingcart.calculator.DeliveryCostCalculatorImpl;
import com.shoppingcart.model.Campaign;
import com.shoppingcart.model.Category;
import com.shoppingcart.model.Coupon;
import com.shoppingcart.model.Product;
import com.shoppingcart.type.DeliveryConstants;
import com.shoppingcart.type.DiscountType;

public class Demo {

    public static void main(String args[]) {

        ShoppingCart shoppingCart = new ShoppingCart();

        //Main category
        Category elektronik = new Category("Elektronik");

        //Categories & SubCategories
        //telefon
        Category telefon = new Category("Telefonu");
        Category tuslu = new Category("Tuşlu Telefon");
        Category cevirmeli = new Category("Çevirmeli Telefon");
        Category dokunmatik = new Category("Dokunmatik Telefon");

        telefon.add(tuslu);
        telefon.add(cevirmeli);
        telefon.add(dokunmatik);

        //televizyon
        Category televizyon = new Category("Televizyon");
        Category tuplu = new Category("Tüplü Televizyon");
        Category lcd = new Category("LCD Televizyon");

        televizyon.add(tuplu);
        televizyon.add(lcd);

        elektronik.add(telefon);
        elektronik.add(televizyon);

        //Products
        Product ankesor = new Product("Ankesör", 100, telefon);
        Product askerTel = new Product("Asker Tel", 135, tuslu);
        Product retroTel = new Product("Retro Tel", 200, cevirmeli);

        Product telecole = new Product("Televole", 450, televizyon);
        Product kosmoss = new Product("Kosmoss", 300, tuplu);
        Product elTv = new Product("El-Tv", 600, lcd);


        //Coupons
        Coupon kupon = new Coupon(1000, 100, DiscountType.AMOUNT);
        Coupon kuponGibiKupon = new Coupon(100, 30, DiscountType.RATE);


        //Campaigns
        Campaign neAlirsan = new Campaign(elektronik, 15, 2, DiscountType.RATE);
        Campaign askerDostu = new Campaign(tuslu, 20, 2, DiscountType.RATE);
        Campaign retroSever = new Campaign(cevirmeli, 20, 1, DiscountType.AMOUNT);
        Campaign yadigar = new Campaign(tuplu, 10, 1, DiscountType.AMOUNT);


        shoppingCart.addItem(askerTel, 5);
        shoppingCart.addItem(kosmoss, 2);
        shoppingCart.applyDiscounts(neAlirsan, askerDostu);
        shoppingCart.applyCoupon(kupon);

        shoppingCart.print();


        //******************************//

        shoppingCart.addItem(elTv, 1);
        shoppingCart.applyCoupon(kuponGibiKupon);
        shoppingCart.print();

        //******************************//
        shoppingCart.setDeliveryCostCalculator(new DeliveryCostCalculatorImpl(2,1, DeliveryConstants.FIXED_COST));
        shoppingCart.print();

    }
}
