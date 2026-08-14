package com.onlinestore.e2e;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductE2eTest extends BaseE2eTest {

    @Test
    void productListAndDetailShouldWork() {
        // Navigate to products page – adjust path if different (e.g. /products)
        page.navigate(BASE_URL + "/products");

        // Wait for at least one product link/card to appear
        page.waitForSelector("a[href*='/product/'], .product-item, a.product-link");

        // Click the first product
        page.locator("a[href*='/product/'], .product-item a, a.product-link").first().click();

        // Wait for product detail page to load (adjust selector as needed)
        page.waitForSelector("h1, .product-name, .product-detail");

        boolean hasProductDetail =
                page.locator("h1").count() > 0 ||
                        page.locator(".product-name").count() > 0 ||
                        page.locator(".product-detail").count() > 0;

        assertTrue(hasProductDetail, "Product detail page should show product info");
    }
}
