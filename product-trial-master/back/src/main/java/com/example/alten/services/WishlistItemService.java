package com.example.alten.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.alten.dto.WishlistItemRequest;
import com.example.alten.entity.Product;
import com.example.alten.entity.User;
import com.example.alten.entity.WishlistItem;
import com.example.alten.repository.ProductRepository;
import com.example.alten.repository.WishlistItemRepository;

@Service
public class WishlistItemService {

    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public WishlistItemService(WishlistItemRepository wishlistItemRepository, ProductRepository productRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
        this.productRepository = productRepository;
    }

    public List<WishlistItem> findByUser(User user) {
        return wishlistItemRepository.findByUser(user);
    }

    public WishlistItem addToWishlist(User user, WishlistItemRequest request) {
        Optional<Product> product = productRepository.findById(request.getProductId());
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Produit introuvable avec l'id: " + request.getProductId());
        }

        Optional<WishlistItem> existing = wishlistItemRepository.findByUserAndProductId(user, request.getProductId());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Produit déjà présent dans la wishlist");
        }

        WishlistItem wishlistItem = request.toEntity(user, product.get());
        return wishlistItemRepository.save(wishlistItem);
    }

    public void deleteWishlistItem(Long id, User user) {
        Optional<WishlistItem> existing = wishlistItemRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Item de wishlist introuvable avec l'id: " + id);
        }
        if (!existing.get().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Impossible de supprimer l'item d'un autre utilisateur");
        }
        wishlistItemRepository.deleteById(id);
    }
}
