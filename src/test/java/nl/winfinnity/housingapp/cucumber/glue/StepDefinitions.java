package nl.winfinnity.housingapp.cucumber.glue;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import jakarta.annotation.PostConstruct;
import nl.winfinnity.housingapp.models.CustomPageImpl;
import nl.winfinnity.housingapp.models.Customer;
import nl.winfinnity.housingapp.services.DatabasePreloadService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.logging.log4j.LogManager.getLogger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class StepDefinitions {

    private static final Logger LOGGER = getLogger(StepDefinitions.class);

    @Autowired
    DatabasePreloadService databasePreloadService;

    @LocalServerPort
    private int port;

    private String uri;

    @PostConstruct
    public void init() {
        uri = "http://localhost:" + port;
    }

    private Response response;

    @After
    public void cleanup() {
        LOGGER.info("Cleaning up database");
        databasePreloadService.clear();
    }

    @Given("I have {int} customers in database")
    public void iHaveCustomersInTheDatabase(int numberOfCustomers) {
        databasePreloadService.preloadDatabase(numberOfCustomers);
    }

    @When("I send a {string} request to {string} with query {string}")
    public void iSendAMethodRequestToEndpointWithQuery(String method, String endpoint, String query) {
        switch (method.toUpperCase()) {
            case "GET":
                response = given().when().get(uri + endpoint + query);
                break;
            // Add other HTTP methods as needed
            default:
                throw new IllegalArgumentException("Invalid method: " + method);
        }
    }

    @Then("I should get {int} status code")
    public void the_response_status_code_should_be(int statusCode) {
        response.then().statusCode(statusCode);
    }


    @Then("the customers returned should be {int}")
    public void customer_count_should_be(int count) {
        Page<Customer> pages = response.getBody().as(new TypeRef<CustomPageImpl<Customer>>(){});
        List<Customer> customers = pages.getContent();
        if (customers.size() != count) {
            throw new AssertionError("Expected " + count + " customers, but got " + customers.size());
        }
    }

}
