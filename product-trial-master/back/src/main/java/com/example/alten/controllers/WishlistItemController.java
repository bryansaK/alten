package com.example.alten.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alten.dto.WishlistItemRequest;
import com.example.alten.entity.User;
import com.example.alten.entity.WishlistItem;
import com.example.alten.services.CustomUserDetailsService;
import com.example.alten.services.WishlistItemService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/wishlist")
public class WishlistItemController {
    private final WishlistItemService wishlistItemService;
    private final CustomUserDetailsService userDetailsService;

    public WishlistItemController(WishlistItemService wishlistItemService, CustomUserDetailsService userDetailsService) {
        this.wishlistItemService = wishlistItemService;
        this.userDetailsService = userDetailsService;
    }
    
    @GetMapping
    public List<WishlistItem> getWishlist() {
        User user = userDetailsService.getCurrentUser();
        return wishlistItemService.findByUser(user);
    }

    @PostMapping("/{productId}")
    public WishlistItem addToWishlist(@PathVariable Long productId) {
        User user = userDetailsService.getCurrentUser();
        WishlistItemRequest request = new WishlistItemRequest();
        request.setProductId(productId);
        return wishlistItemService.addToWishlist(user, request);
    }

    @DeleteMapping("/{id}")
    public void deleteWishlistItem(@PathVariable Long id) {
        User user = userDetailsService.getCurrentUser();
        wishlistItemService.deleteWishlistItem(id, user);
    }
}
