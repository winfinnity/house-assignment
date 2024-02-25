package nl.winfinnity.housingapp.services;

import nl.winfinnity.housingapp.exceptions.InvalidInputException;
import nl.winfinnity.housingapp.models.Customer;
import nl.winfinnity.housingapp.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerValidationService customerValidationService;
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerValidationService customerValidationService) {
        this.customerRepository = customerRepository;
        this.customerValidationService = customerValidationService;
    }

    public Page<Customer> findAll(Pageable pageable) {
        LOG.info("Getting all customers with pagination, page: {} and pageSize: {}", pageable.getPageNumber(), pageable.getPageSize());
        var customers = customerRepository.findAll(pageable);
        return customers;
    }

    public Customer saveCustomer(Customer customer) {
        LOG.info("Saving customer: {} ", customer.getEmail());
        return customerRepository.save(customerValidationService.validateCustomer(customer));
    }

    public Customer updateCustomer(Customer customer, Long id) {
        LOG.info("Updating customer: {} ", customer.getEmail());
        if (findCustomerByEmail(customer.getEmail()) != null) {
            throw new InvalidInputException("Email already exists in the database. Please use a different email.");
        }
        var updatedCustomer = customerRepository.findById(id).orElseThrow(() -> new InvalidInputException("Customer not found with id: " + id));
        updatedCustomer.setFirstname(customer.getFirstname());
        updatedCustomer.setLastname(customer.getLastname());
        updatedCustomer.setEmail(customer.getEmail());
        updatedCustomer.setAge(customer.getAge());
        updatedCustomer.setAddresses(customer.getAddresses());
        return customerRepository.save(customerValidationService.validateCustomer(updatedCustomer));
    }

    public Page<Customer> findCustomersByFirstname(String firstname, Pageable pageable) {
        LOG.info("Finding customers by firstname: {}", firstname);
        return customerRepository.findByFirstnameContaining(firstname, pageable);
    }

    public Page<Customer> findCustomersByLastname(String lastname, Pageable pageable) {
        LOG.info("Finding customers by lastname: {}", lastname);
        return customerRepository.findByLastnameContaining(lastname, pageable);
    }

    private Customer findCustomerByEmail(String email) {
        LOG.info("Finding customer by email: {}", email);
        return customerRepository.findByEmail(email).orElse(null);
    }
}
