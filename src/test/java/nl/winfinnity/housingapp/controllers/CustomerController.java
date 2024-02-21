package nl.winfinnity.housingapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    public final static Logger LOG = LoggerFactory.getLogger(CustomerController.class);


    public CustomerController(){
        LOG.info("CustomerController created");
    }


}
