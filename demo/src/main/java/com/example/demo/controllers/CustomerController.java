package com.example.demo.controllers;

import com.example.demo.models.Customer;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private List<Customer> customers = new ArrayList<>();


    @PostMapping
    public Customer createCustomer(@Valid @RequestBody Customer customer) {

        customers.add(customer);
        return customer;
    }


    @GetMapping
    public List<Customer> getAllCustomers() {
        return customers;
    }


    @GetMapping("/{email}")
    public Customer getCustomerByEmail(@PathVariable String email) {

        for (Customer customer : customers) {

            if (customer.getEmail().equalsIgnoreCase(email)) {
                return customer;
            }
        }

        throw new RuntimeException("Customer not found");
    }


    @PutMapping("/{email}")
    public Customer updateCustomer(
            @PathVariable String email,
            @Valid @RequestBody Customer updatedCustomer) {

        for (Customer customer : customers) {

            if (customer.getEmail().equalsIgnoreCase(email)) {

                customer.setName(updatedCustomer.getName());
                customer.setAge(updatedCustomer.getAge());
                customer.setAddress(updatedCustomer.getAddress());
                customer.setEmail(updatedCustomer.getEmail());

                return customer;
            }
        }

        throw new RuntimeException("Customer not found");
    }


    @DeleteMapping("/{email}")
    public void deleteCustomer(@PathVariable String email) {

        Customer customer = getCustomerByEmail(email);

        customers.remove(customer);
    }
}