package com.example.alten.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.alten.entity.Product;
import com.example.alten.repository.ProductReposiroty;

@Service
public class ProductService {

    private final CustomUserDetailsService userDetailsService;
    private final ProductReposiroty productRepository;

    @Autowired
    public ProductService(CustomUserDetailsService userDetailsService, ProductReposiroty productRepository) {
        this.userDetailsService = userDetailsService;
        this.productRepository = productRepository;
    }

    public List<Product> findAllAvailableProducts() {
        List<Product> products = productRepository.findAvailableProducts();
        return products;
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public Boolean deleteProduct(Product product) {
        productRepository.delete(product);
        return true;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
