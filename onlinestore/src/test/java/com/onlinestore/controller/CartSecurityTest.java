package com.onlinestore.controller;

import com.onlinestore.security.JwtUtil;
import com.onlinestore.service.CartService;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void shouldBlockRequestWithoutToken() throws Exception {

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isForbidden());
    }
}