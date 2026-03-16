package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String customerJson;
    private String updatedCustomerJson;
    private String invalidCustomerJson;

    @BeforeEach
    void setUp() {
        customerJson = """
                {
                  "name": "Ali",
                  "email": "ali@mail.com",
                  "age": 25,
                  "address": "Baku"
                }
                """;

        updatedCustomerJson = """
                {
                  "name": "Ali Updated",
                  "email": "ali@mail.com",
                  "age": 30,
                  "address": "Ganja"
                }
                """;

        invalidCustomerJson = """
                {
                  "name": "",
                  "email": "wrong-email",
                  "age": 15,
                  "address": ""
                }
                """;
    }

    @Test
    void createCustomer_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "name": "Ali",
                          "email": "ali@mail.com",
                          "age": 25,
                          "address": "Baku"
                        }
                        """));
    }

    @Test
    void createCustomer_shouldReturnBadRequest_whenBodyInvalid() throws Exception {
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCustomerJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("email")));
    }

    @Test
    void getAllCustomers_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk());
    }

    @Test
    void getCustomerByEmail_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson));

        mockMvc.perform(get("/customers/ali@mail.com"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "name": "Ali",
                          "email": "ali@mail.com",
                          "age": 25,
                          "address": "Baku"
                        }
                        """));
    }

    @Test
    void updateCustomer_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson));

        mockMvc.perform(put("/customers/ali@mail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCustomerJson))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "name": "Ali Updated",
                          "email": "ali@mail.com",
                          "age": 30,
                          "address": "Ganja"
                        }
                        """));
    }

    @Test
    void deleteCustomer_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson));

        mockMvc.perform(delete("/customers/ali@mail.com"))
                .andExpect(status().isOk());
    }
}