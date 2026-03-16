package com.example.demo.services;

import com.example.demo.models.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private List<Customer> customers = new ArrayList<>();


    public void addCustomer(Customer customer) {
        customers.add(customer);
    }


    public List<Customer> getAllCustomers() {
        return customers;
    }


    public Customer getCustomerByEmail(String email) {

        for (Customer customer : customers) {

            if (customer.getEmail().equalsIgnoreCase(email)) {
                return customer;
            }
        }

        throw new RuntimeException("Customer not found");
    }


    public Customer updateCustomer(String email, Customer updatedCustomer) {

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


    public void deleteCustomer(String email) {

        Customer customer = getCustomerByEmail(email);

        customers.remove(customer);
    }
}