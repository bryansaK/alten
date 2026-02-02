package com.example.alten.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alten.dto.CartItemRequest;
import com.example.alten.entity.CartItem;
import com.example.alten.entity.User;
import com.example.alten.services.CartItemService;
import com.example.alten.services.CustomUserDetailsService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/cart")
public class CartItemController {

    private final CartItemService cartItemService;
    private final CustomUserDetailsService userDetailsService;

    public CartItemController(CartItemService cartItemService, CustomUserDetailsService userDetailsService) {
        this.cartItemService = cartItemService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public List<CartItem> getCart() {
        User user = userDetailsService.getCurrentUser();
        return cartItemService.findByUser(user);
    }

    @PostMapping("/{productId}")
    public CartItem addToCart(@PathVariable Long productId) {
        User user = userDetailsService.getCurrentUser();
        CartItemRequest request = new CartItemRequest();
        request.setProductId(productId);
        request.setQuantity(1); // Default quantity
        return cartItemService.addToCart(user, request);
    }

    @PatchMapping("/{id}")
    public CartItem updateCartItem(@PathVariable Long id, @RequestBody CartItemRequest request) {
        User user = userDetailsService.getCurrentUser();
        return cartItemService.updateCartItem(id, user, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        User user = userDetailsService.getCurrentUser();
        cartItemService.deleteCartItem(id, user);
    }
}
