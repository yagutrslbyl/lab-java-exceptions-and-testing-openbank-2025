package com.example.demo.services;

import com.example.demo.models.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private List<Product> products = new ArrayList<>();


    public void addProduct(Product product) {
        products.add(product);
    }


    public List<Product> getAllProducts() {
        return products;
    }


    public Product getProductByName(String name) {

        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }

        throw new RuntimeException("Product not found");
    }


    public Product updateProduct(String name, Product updatedProduct) {

        for (Product product : products) {

            if (product.getName().equalsIgnoreCase(name)) {

                product.setName(updatedProduct.getName());
                product.setPrice(updatedProduct.getPrice());
                product.setCategory(updatedProduct.getCategory());
                product.setQuantity(updatedProduct.getQuantity());

                return product;
            }
        }

        throw new RuntimeException("Product not found");
    }


    public void deleteProduct(String name) {

        Product product = getProductByName(name);

        products.remove(product);
    }


    public List<Product> getProductsByCategory(String category) {

        List<Product> result = new ArrayList<>();

        for (Product product : products) {

            if (product.getCategory().equalsIgnoreCase(category)) {
                result.add(product);
            }
        }

        return result;
    }


    public List<Product> getProductsByPriceRange(double min, double max) {

        if (min > max) {
            throw new RuntimeException("Invalid price range");
        }

        List<Product> result = new ArrayList<>();

        for (Product product : products) {

            if (product.getPrice() >= min && product.getPrice() <= max) {
                result.add(product);
            }
        }

        return result;
    }
}