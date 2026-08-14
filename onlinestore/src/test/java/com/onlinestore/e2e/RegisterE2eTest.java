package com.onlinestore.e2e;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterE2eTest extends BaseE2eTest {

    @Test
    void registerUserShouldWork() {
        // Go to registration page – adjust path if different
        page.navigate(BASE_URL + "/register");

        // Wait for form
        page.waitForSelector("form#registerForm, form[name='register'], input[name='email'], input[type='email']");

        // Fill registration form – adjust field names/selectors to your HTML
        page.fill("input[name='email']", "testuser_" + System.currentTimeMillis() + "@example.com");
        page.fill("input[name='password']", "TestPass123!");
        page.fill("input[name='confirmPassword']", "TestPass123!");

        // Submit form
        page.locator("button:has-text('Register'), input[type='submit'][value*='Register'], .btn-register").first().click();

        // Wait for either success message or redirect (e.g. to login or home)
        page.waitForURL(BASE_URL + "/**");

        boolean success =
                page.locator(":has-text('registration successful'), :has-text('successfully registered'), " +
                        "h1:has-text('Welcome'), h2:has-text('registered')").count() > 0 ||
                        page.url().contains("/login") ||
                        page.url().equals(BASE_URL + "/");

        assertTrue(success, "Registration should complete successfully");
    }
}