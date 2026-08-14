package com.onlinestore.e2e;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomePageE2eTest extends BaseE2eTest {

    @Test
    void homePageShouldLoad() {
        page.navigate(BASE_URL + "/");

        // Adjust selector to match your actual home page (e.g. h1, title, specific text)
        String title = page.title();
        assertTrue(title != null && !title.isEmpty(), "Page title should not be empty");

        // Example: check for a heading or key text on the home page
        // Change selector/text to match your UI
        boolean hasHeading = page.locator("h1").count() > 0;
        assertTrue(hasHeading, "Home page should have an h1 heading");
    }
}