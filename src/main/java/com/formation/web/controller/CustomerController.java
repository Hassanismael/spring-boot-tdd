package com.formation.web.controller;

import com.formation.service.CustomerService;
import com.formation.web.error.BadRequestException;
import com.formation.web.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping
  public List<Customer> getCustomers() {
    return this.customerService.getAllCustomers();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Customer addCustomer(@RequestBody Customer customer) {
    return this.customerService.addCustomer(customer);
  }

  @GetMapping("/{id}")
  public Customer getCustomer(@PathVariable("id") String id) {
    return this.customerService.getCustomer(id);
  }

  @PutMapping("/{id}")
  public Customer updateCustomer(@PathVariable("id") String id, @RequestBody Customer customer) {
    if (!id.equals(customer.getCustomerId())) {
      throw new BadRequestException("path variable must match incoming request id");
    }
    return this.customerService.updateCustomer(customer);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.RESET_CONTENT)
  public void deleteCustomer(@PathVariable("id") String id) {
    this.customerService.deleteCustomer(id);
  }

}
