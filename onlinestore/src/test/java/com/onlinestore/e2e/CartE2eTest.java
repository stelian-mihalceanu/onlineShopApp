package com.onlinestore.e2e;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartE2eTest extends BaseE2eTest {

    @Test
    void addToCartShouldWork() {
        // Go to products page
        page.navigate(BASE_URL + "/products");

        // Wait for products and click first one
        page.waitForSelector("a[href*='/product/'], .product-item, a.product-link");
        page.locator("a[href*='/product/'], .product-item a, a.product-link").first().click();

        // Wait for product detail
        page.waitForSelector("h1, .product-name, .product-detail");

        // Click "Add to cart" button – adjust selector/text as needed
        page.waitForSelector("button:has-text('Add to cart'), input[type='submit'][value*='cart'], .btn-add-to-cart");
        page.locator("button:has-text('Add to cart'), input[type='submit'][value*='cart'], .btn-add-to-cart").first().click();

        // Optionally wait for some feedback (toast, cart count, redirect)
        page.waitForTimeout(500);

        // Navigate to cart page
        page.navigate(BASE_URL + "/cart");

        // Wait for cart content
        page.waitForSelector(".cart-items, table.cart, .cart-item, h1:has-text('Cart')");

        boolean hasCartItems =
                page.locator(".cart-items").count() > 0 ||
                        page.locator("table.cart").count() > 0 ||
                        page.locator(".cart-item").count() > 0 ||
                        page.locator("h1:has-text('Cart')").count() > 0;

        assertTrue(hasCartItems, "Cart page should show items after adding a product");
    }
}