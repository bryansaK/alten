package com.example.alten.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.alten.entity.Product;

@Repository
public interface ProductReposiroty extends CrudRepository<Product, Long> {
    Optional<Product> findByInternalReference(String internalReference);
    List<Product> findByQuantityGreaterThan(Integer quantity);
    @Query("SELECT p FROM Product p WHERE p.inventoryStatus = 'INSTOCK' OR p.inventoryStatus = 'LOWSTOCK'")
    List<Product> findAvailableProducts();
    List<Product> findByInventoryStatus(String inventoryStatus);
    // todo je devrais créer des enums pour le status si j'ai le temps
    
}
