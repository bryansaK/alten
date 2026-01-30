package com.example.alten.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.alten.dto.ProductRequest;
import com.example.alten.entity.Product;
import com.example.alten.services.CustomUserDetailsService;
import com.example.alten.services.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CustomUserDetailsService userDetailsService;
    
    @Value("${admin.email:admin@example.com}")
    private String adminEmail;

    public ProductController(ProductService productService, CustomUserDetailsService userDetailsService) {
        this.productService = productService;
        this.userDetailsService = userDetailsService;
    }
    
    @GetMapping("/available")
    public List<Product> getAvailableProducts() {
        return productService.findAllAvailableProducts();
    }

    @PostMapping("/new")
    public Product createProduct(@RequestBody ProductRequest product) {
        if (!userDetailsService.getCurrentUser().getEmail().equalsIgnoreCase(adminEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin users can create products");
        }
        return productService.saveProduct(product);
    }

    @PatchMapping("/update/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductRequest product) {
        if (!userDetailsService.getCurrentUser().getEmail().equalsIgnoreCase(adminEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin users can update products");
        }
        return productService.updateProductById(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        if (!userDetailsService.getCurrentUser().getEmail().equalsIgnoreCase(adminEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin users can delete products");
        }
        productService.deleteProductById(id);
    }
}