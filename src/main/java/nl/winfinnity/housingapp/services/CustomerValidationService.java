package nl.winfinnity.housingapp.services;

import nl.winfinnity.housingapp.exceptions.InvalidInputException;
import nl.winfinnity.housingapp.models.Customer;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;


@Service
public class CustomerValidationService {


    private static final String EMAILPATTTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern pattern = Pattern.compile(EMAILPATTTERN);

    public Customer validateCustomer(Customer customer) {
        if (customer.getFirstname() == null || customer.getFirstname().isEmpty()) {
            throw new InvalidInputException("First name cannot be empty");
        }

        if (customer.getLastname() == null || customer.getLastname().isEmpty()) {
            throw new InvalidInputException("Last name cannot be empty");
        }

        if (customer.getEmail() !=null && !pattern.matcher(customer.getEmail()).matches()) {
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
