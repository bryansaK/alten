package com.example.alten.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.alten.dto.CartItemRequest;
import com.example.alten.entity.CartItem;
import com.example.alten.entity.Product;
import com.example.alten.entity.User;
import com.example.alten.repository.CartItemRepository;
import com.example.alten.repository.ProductRepository;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> findByUser(User user) {
        return cartItemRepository.findByUser(user);
    }

    public CartItem addToCart(User user, CartItemRequest request) {
        Optional<Product> product = productRepository.findById(request.getProductId());
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Produit introuvable avec l'id: " + request.getProductId());
        }

        Optional<CartItem> existing = cartItemRepository.findByUserAndProductId(user, request.getProductId());
        if (existing.isPresent()) {
            CartItem cartItem = existing.get();
            cartItem.setQuantity(request.getQuantity());
            return cartItemRepository.save(cartItem);
        }

        CartItem cartItem = request.toEntity(user, product.get());
        return cartItemRepository.save(cartItem);
    }

    public CartItem updateCartItem(Long id, User user, CartItemRequest request) {
        Optional<CartItem> existing = cartItemRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Item du panier introuvable avec l'id: " + id);
        }
        CartItem cartItem = existing.get();
        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Impossible de modifier l'item d'un autre utilisateur");
        }
        request.applyTo(cartItem);
        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Long id, User user) {
        Optional<CartItem> existing = cartItemRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Item du panier introuvable avec l'id: " + id);
        }
        if (!existing.get().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Impossible de supprimer l'item d'un autre utilisateur");
        }
        cartItemRepository.deleteById(id);
    }
}
