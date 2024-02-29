Feature: Customers endpoints

  Scenario Outline: Get all customers with different pagesizes and pages
    Given I have <numberOfCustomers> customers in database
    When I send a 'GET' request to '/api/customers' with query '<query>'
    Then I should get <status> status code
    Then the customers returned should be <count>
    Examples:
      | query              | status | count | numberOfCustomers  |
      |                    | 200    | 10    | 20                 |
      | ?page=1            | 200    | 5     | 15                 |
      | ?page=2            | 200    | 0     | 15                 |
      | ?pageSize=2        | 200    | 2     | 10                 |
      | ?page=1&pageSize=5 | 200    | 2     | 7                  |
      |                    | 200    | 0     | 0                  |

  Scenario Outline: Get all customers with invalid page size
    Given I have 1 customers in database
    When I send a 'GET' request to '/api/customers' with query '<query>'
    Then I should get <status> status code
    Examples:
      | query                     | status |
      | ?page=23429043324343443423| 500    |
      | ?pageSize=afjadf&page=2   | 500    |
      | ?page=dfad                | 500    |

  Scenario Outline: Add new customer
    Given I have <numberOfCustomers> customers in database
    When I send a 'POST' request to '/api/customers' with body '<file>'
    Then I should get <status> status code
    When I send a 'GET' request to '/api/customers'
    Then I should get <status2> status code
    Then the customers returned should be <count>
    Examples:
      | status | status2  | count | numberOfCustomers | file                    |
      | 201    | 200      | 1     | 0                 | johndoe.json           |
      | 201    | 200      | 1     | 0                 | johndoe2Addresses.json |
      | 201    | 200      | 1     | 0                 | johndoeNoMail.json     |

  Scenario Outline: Add new customer error cases
    Given I have <numberOfCustomers> customers in database
    When I send a 'POST' request to '/api/customers' with body '<file>'
    Then I should get <status> status code
    When I send a 'GET' request to '/api/customers'
    Then I should get <status2> status code
    Then the customers returned should be <count>
    Examples:
      | status | status2  | count | numberOfCustomers | file                               |
      | 400    | 200      | 0     | 0                 | johndoeWithoutEmailOrAddress.json |
      | 400    | 200      | 0     | 0                 | johndoeUnderAge.json              |
      | 400    | 200      | 0     | 0                 | johndoeNoAge.json                 |
      | 400    | 200      | 0     | 0                 | johnNoLastName.json               |
      | 400    | 200      | 0     | 0                 | doeNoFirstName.json               |
      | 400    | 200      | 0     | 0                 | invalid.json                      |
      | 400    | 200      | 0     | 0                 | johndoeInvalidMail.json           |


  Scenario Outline: Add new customer twice
    Given I have <numberOfCustomers> customers in database
    When I send a 'POST' request to '/api/customers' with body '<file>'
    Then I should get <status> status code
    When I send a 'POST' request to '/api/customers' with body '<file2>'
    Then I should get <status2> status code
    Examples:
      | status | status2  | numberOfCustomers | file                    | file2                   |
      | 201    | 400      | 0                 | johndoe.json           | johndoe.json           |
      | 201    | 400      | 0                 | johndoeCAPS.json       | johndoe.json           |

  Scenario Outline: Update customer with id
    Given I have John Doe and Jane Doe in the database
    When I update John Doe with body '<file>'
    Then I should get <status> status code
    Examples:
      | status | file          |
      | 200    | johndoe.json |
      | 200    | johndoe2Addresses.json |
      | 200    | johndoeNoMail.json     |

  Scenario Outline: Update customer with id invalid input
    Given I have John Doe and Jane Doe in the database
    When I update John Doe with body '<file>'
    Then I should get <status> status code
    And I should get error message '<errorMessage>'
    Examples:
      | status | file                  | errorMessage                             |
      | 400    | janedoe.json         | Email already in use by another customer |
      | 400    | johndoeUnderAge.json | Age must be 18 or older                  |
      | 400    | johndoeNoAge.json    | Age must be 18 or older                  |
      | 400    | johnNoLastName.json  | Last name cannot be empty                |
      | 400    | doeNoFirstName.json  | First name cannot be empty               |
      | 400    | invalid.json         |                                  |

  Scenario Outline: Search customers
    Given I have John Doe and Jane Doe in the database
    When I send a 'GET' request to '/api/customers/search' with query '<query>'
    Then I should get <status> status code
    Then the customers returned should be <count>
    Examples:
      | query                          | status | count |
      | ?firstname=John                 | 200    | 1     |
      | ?lastname=Doe                  | 200    | 2     |
      | ?firstname=jane&lastname=doe    | 200    | 1     |
      |                                | 200    | 2     |
      | ?firstname=donotexist           | 200    | 0     |



