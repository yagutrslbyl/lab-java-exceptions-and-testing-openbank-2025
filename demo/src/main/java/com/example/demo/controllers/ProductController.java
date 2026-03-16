package com.example.demo.controllers;

import com.example.demo.models.Product;
import com.example.demo.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    private void checkApiKey(String apiKey) {

        if (apiKey == null || !apiKey.equals("123456")) {
            throw new RuntimeException("Invalid API Key");
        }
    }


    @PostMapping
    public Product addProduct(
            @RequestHeader(value = "API-Key", required = false) String apiKey,
            @Valid @RequestBody Product product) {

        checkApiKey(apiKey);
        productService.addProduct(product);
        return product;
    }


    @GetMapping
    public List<Product> getAllProducts(
            @RequestHeader(value = "API-Key", required = false) String apiKey) {

        checkApiKey(apiKey);
        return productService.getAllProducts();
    }


    @GetMapping("/{name}")
    public Product getProductByName(
            @RequestHeader(value = "API-Key", required = false) String apiKey,
            @PathVariable String name) {

        checkApiKey(apiKey);
        return productService.getProductByName(name);
    }


    @PutMapping("/{name}")
    public Product updateProduct(
            @RequestHeader(value = "API-Key", required = false) String apiKey,
            @PathVariable String name,
            @Valid @RequestBody Product product) {

        checkApiKey(apiKey);
        return productService.updateProduct(name, product);
    }


    @DeleteMapping("/{name}")
    public void deleteProduct(
            @RequestHeader(value = "API-Key", required = false) String apiKey,
            @PathVariable String name) {

        checkApiKey(apiKey);
        productService.deleteProduct(name);
    }


    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(
            @RequestHeader(value = "API-Key", required = false) String apiKey,
            @PathVariable String category) {

        checkApiKey(apiKey);
        return productService.getProductsByCategory(category);
    }


    @GetMapping("/price")
    public List<Product> getProductsByPrice(
            @RequestHeader(value = "API-Key", required = false) String apiKey,
            @RequestParam double min,
            @RequestParam double max) {

        checkApiKey(apiKey);
        return productService.getProductsByPriceRange(min, max);
    }
}