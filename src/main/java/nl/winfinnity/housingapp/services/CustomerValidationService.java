package nl.winfinnity.housingapp.services;

import nl.winfinnity.housingapp.exceptions.InvalidInputException;
import nl.winfinnity.housingapp.models.Customer;
import org.springframework.stereotype.Service;


@Service
public class CustomerValidationService {

    public Customer validateCustomer(Customer customer) {
        if (customer.getFirstname() == null || customer.getFirstname().isEmpty()) {
            throw new InvalidInputException("First name cannot be empty");
        }

        if (customer.getLastname() == null || customer.getLastname().isEmpty()) {
            throw new InvalidInputException("Last name cannot be empty");
        }

        if (customer.getEmail() == null || !customer.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidInputException("Invalid email format");
        }

        if (customer.getAge() < 18) {
            throw new InvalidInputException("Age must be 18 or older");
        }
        if(!customer.eMailOrAddressPresent()) {
            throw new InvalidInputException("Either email or address must be present");
        }
        return customer;
    }


}
