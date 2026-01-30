package com.example.alten.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.alten.dto.ProductRequest;
import com.example.alten.entity.Product;
import com.example.alten.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAllAvailableProducts() {
        List<Product> products = productRepository.findAvailableProducts();
        return products;
    }

    public Product updateProductById(Long id, ProductRequest request) {
        Optional<Product> existing = productRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        Product product = existing.get();
        request.applyTo(product);
        return productRepository.save(product);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public Product saveProduct(ProductRequest request) {
        Product product = request.toEntity();
        return productRepository.save(product);
    }

}
