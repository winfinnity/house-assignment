package nl.winfinnity.housingapp.services;

import nl.winfinnity.housingapp.exceptions.InvalidInputException;
import nl.winfinnity.housingapp.models.Customer;
import nl.winfinnity.housingapp.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        return customerRepository.findAll(pageable);
    }

    public Customer saveCustomer(Customer customer) {
        LOG.info("Saving new customer");
        var existingCustomer = findCustomerByEmail(customer.getEmail());
        if (existingCustomer.isPresent()) {
            throw new InvalidInputException("Email already in use by another customer");
        }
        return customerRepository.save(customerValidationService.validateCustomer(customer));
    }

    public Customer updateCustomer(Customer customer, Long id) {
        LOG.info("Updating customer: {} ", customer.getId());
        var updatedCustomer = customerRepository.findById(id).orElseThrow(() -> new InvalidInputException("Customer not found"));
        updatedCustomer.setFirstname(customer.getFirstname());
        updatedCustomer.setLastname(customer.getLastname());
        updatedCustomer.setAge(customer.getAge());
        updatedCustomer.setAddresses(customer.getAddresses());
        if(updatedCustomer.getEmail().equalsIgnoreCase(customer.getEmail())) {
            updatedCustomer.setEmail(customer.getEmail());
        }
        else {
            var existingCustomer = findCustomerByEmail(customer.getEmail());
            if (existingCustomer.isPresent() && (existingCustomer.get().getId() != updatedCustomer.getId())) {
                throw new InvalidInputException("Email already in use by another customer");
            }
            updatedCustomer.setEmail(customer.getEmail());
        }
        return customerRepository.save(customerValidationService.validateCustomer(updatedCustomer));
    }

    public Customer deleteCustomer(Long id) {
        LOG.info("Deleting customer with id: {}", id);
        var customer = customerRepository.findById(id).orElseThrow(() -> new InvalidInputException("Customer not found"));
        customerRepository.delete(customer);
        return customer;
    }

    public Page<Customer> findCustomersByFirstnameAndLastname(String firstname, String lastname, Pageable pageable) {
        LOG.info("Finding customers by firstname: {} and lastname: {}", firstname, lastname);
        return customerRepository.findByFirstnameLikeIgnoreCaseAndLastnameLikeIgnoreCase(firstname, lastname, pageable);
    }

    private Optional<Customer> findCustomerByEmail(String email) {
        LOG.info("Finding customer by email: {}", email);
        return customerRepository.findByEmailIgnoreCase(email);
    }
}
