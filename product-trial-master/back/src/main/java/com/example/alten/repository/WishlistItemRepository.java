package com.example.alten.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.alten.entity.User;
import com.example.alten.entity.WishlistItem;

@Repository
public interface WishlistItemRepository extends CrudRepository<WishlistItem, Long> {
    List<WishlistItem> findByUser(User user);
    Optional<WishlistItem> findByUserAndProductId(User user, Long productId);
}
