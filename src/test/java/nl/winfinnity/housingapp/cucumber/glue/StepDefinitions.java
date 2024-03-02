package nl.winfinnity.housingapp.cucumber.glue;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.annotation.PostConstruct;
import nl.winfinnity.housingapp.models.CustomPageImpl;
import nl.winfinnity.housingapp.models.Customer;
import nl.winfinnity.housingapp.services.CustomerService;
import nl.winfinnity.housingapp.services.DatabasePreloadService;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.logging.log4j.LogManager.getLogger;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class StepDefinitions {

    private static final Logger LOGGER = getLogger(StepDefinitions.class);

    private final CustomerService customerService;

    private final DatabasePreloadService databasePreloadService;

    @LocalServerPort
    private int port;

    private String uri;

    private long customerId = 0L;
    private Response response;

    public StepDefinitions(CustomerService customerService, DatabasePreloadService databasePreloadService) {
        this.customerService = customerService;
        this.databasePreloadService = databasePreloadService;
    }

    @PostConstruct
    public void init() {
        uri = "http://localhost:" + port;
    }


    @After
    public void cleanup() {
        LOGGER.info("Cleaning up database");
        databasePreloadService.clear();
    }

    @Given("I have {int} customers in database")
    public void iHaveCustomersInTheDatabase(int numberOfCustomers) {
        databasePreloadService.preloadDatabase(numberOfCustomers);
    }


    @When("I send a {string} request to {string}")
    public void iSendAMethodRequestToEndpoint(String method, String endpoint) {
        sendRequest(method, endpoint, null);
    }

    @When("I send a {string} request to {string} with query {string}")
    public void iSendAMethodRequestToEndpointWithQuery(String method, String endpoint, String query) {
        sendRequest(method,endpoint+query,null);
    }

    @When("I send a {string} request to {string} with body {string}")
    public void iSendAMethodRequestToEndpointWithBody(String method, String endpoint, String filename) throws IOException {
        var path = Paths.get("src/test/resources/json/" + filename);
        LOGGER.info("Reading file {}", path);
        var body = Files.readString(path);
        sendRequest(method,endpoint,body);
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

    private void sendRequest(String method, String endpoint, String body) {
        switch (method.toUpperCase()) {
            case "GET":
                response = given().when().get(uri + endpoint);
                break;
            case "PUT":
                response = given().when()
                        .contentType(ContentType.JSON).body(body).put(uri + endpoint);
                break;
            case "POST":
                response = given().when()
                        .contentType(ContentType.JSON).body(body).post(uri + endpoint);
                break;
            case "DELETE":
                response = given().when().delete(uri + endpoint);
                break;
            default:
                throw new IllegalArgumentException("Invalid method: " + method);
        }
    }
    
    @Given("I have John Doe and Jane Doe in the database")
    public void iHaveJohnDoeJaneDoeInTheDatabase() {
        var john = createCustomer("John","Doe","John.doe@example.com",18);
        var jane = createCustomer("Jane","Doe","jane.doe@example.com",18);
        customerId = john.getId();
    }

    @When("I update John Doe with body {string}")
    public void iUpdateJohnDoeWithBodyFile(String filename) throws IOException {
        iSendAMethodRequestToEndpointWithBody("PUT", "/api/customers/" + customerId, filename);
    }

    @And("I should get error message {string}")
    public void validateErrorMessage(String message) {
        if (!message.isEmpty()){
        response.then().body("errorMessage", equalTo(message));
        response.then().body("traceId", not(equalTo("No traceId available")));
        }
    }

    private Customer createCustomer(String firstname, String lastname, String email, int age) {
        var customer = new Customer();
        customer.setFirstname(firstname);
        customer.setLastname(lastname);
        customer.setEmail(email);
        customer.setAge(age);
        return customerService.saveCustomer(customer);
    }
}
