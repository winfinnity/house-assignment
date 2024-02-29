package nl.winfinnity.housingapp.services;

import nl.winfinnity.housingapp.models.Customer;
import nl.winfinnity.housingapp.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
public class DatabasePreloadService {

    private static final Logger LOG = LoggerFactory.getLogger(DatabasePreloadService.class);
    private final CustomerRepository customerRepository;

    public DatabasePreloadService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void preloadDatabase(int numberOfCustomers) {
        LOG.info("Preloading database with {} customers",numberOfCustomers);
        List<String> firstNames = Arrays.asList("John", "Jane", "Mike", "Emily", "Robert", "Anna", "James", "Jessica", "David", "Sarah");
        List<String> lastNames = Arrays.asList("Smith", "Johnson", "Williams", "Brown", "Jones", "Miller", "Davis", "Garcia", "Rodriguez", "Wilson");
        Random random = new Random();
        IntStream.rangeClosed(1, numberOfCustomers).forEach(i -> {
            Customer customer = new Customer();
            customer.setAge(18 + i % 83); // Set age between 18 and 100
            String firstName = firstNames.get(random.nextInt(firstNames.size()));
            String lastName = lastNames.get(random.nextInt(lastNames.size()));
            customer.setFirstname(firstName);
            customer.setLastname(lastName);
            customer.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + i + "@winfinnity.com");
            customerRepository.save(customer);
        });

    }

    public void clear() {
        customerRepository.deleteAll();
    }
}
