package com.example.demo;

import com.example.demo.controllers.ProductController;
import com.example.demo.models.Product;
import com.example.demo.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void addProduct_shouldReturnOk_whenApiKeyValid() throws Exception {
        String productJson = """
                {
                  "name": "Laptop",
                  "price": 1500,
                  "category": "Electronics",
                  "quantity": 5
                }
                """;

        mockMvc.perform(post("/products")
                        .header("API-Key", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk());
    }

    @Test
    void addProduct_shouldReturn5xx_whenApiKeyMissing() throws Exception {
        String productJson = """
                {
                  "name": "Laptop",
                  "price": 1500,
                  "category": "Electronics",
                  "quantity": 5
                }
                """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getAllProducts_shouldReturnOk() throws Exception {
        Product p = new Product("Electronics", "Laptop", 1500, 5);
        when(productService.getAllProducts()).thenReturn(List.of(p));

        mockMvc.perform(get("/products")
                        .header("API-Key", "123456"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductByName_shouldReturnOk() throws Exception {
        Product p = new Product("Electronics", "Laptop", 1500, 5);
        when(productService.getProductByName("Laptop")).thenReturn(p);

        mockMvc.perform(get("/products/Laptop")
                        .header("API-Key", "123456"))
                .andExpect(status().isOk());
    }

    @Test
    void updateProduct_shouldReturnOk() throws Exception {
        String productJson = """
                {
                  "name": "Laptop",
                  "price": 2000,
                  "category": "Electronics",
                  "quantity": 10
                }
                """;

        Product updated = new Product("Electronics", "Laptop", 2000, 10);
        when(productService.updateProduct(eq("Laptop"), any(Product.class))).thenReturn(updated);

        mockMvc.perform(put("/products/Laptop")
                        .header("API-Key", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProduct_shouldReturnOk() throws Exception {
        doNothing().when(productService).deleteProduct("Laptop");

        mockMvc.perform(delete("/products/Laptop")
                        .header("API-Key", "123456"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductsByCategory_shouldReturnOk() throws Exception {
        Product p = new Product("Electronics", "Laptop", 1500, 5);
        when(productService.getProductsByCategory("Electronics")).thenReturn(List.of(p));

        mockMvc.perform(get("/products/category/Electronics")
                        .header("API-Key", "123456"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductsByPrice_shouldReturnOk() throws Exception {
        Product p = new Product("Electronics", "Laptop", 1500, 5);
        when(productService.getProductsByPriceRange(100, 2000)).thenReturn(List.of(p));

        mockMvc.perform(get("/products/price")
                        .header("API-Key", "123456")
                        .param("min", "100")
                        .param("max", "2000"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductsByPrice_shouldReturn5xx_whenApiKeyInvalid() throws Exception {
        mockMvc.perform(get("/products/price")
                        .header("API-Key", "wrong-key")
                        .param("min", "100")
                        .param("max", "2000"))
                .andExpect(status().is5xxServerError());
    }
}