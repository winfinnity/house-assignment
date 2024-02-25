Feature: Customers endpoints

  Scenario Outline: Get all customers with different pagesizes and pages
    Given I have <numberOfCustomers> customers in database
    When I send a '<method>' request to '<endpoint>' with query '<query>'
    Then I should get <status> status code
    Then the customers returned should be <count>
    Examples:
      | method | endpoint        | query              | status | count | numberOfCustomers  |
      | GET    | /api/customers  |                    | 200    | 10    | 20                 |
      | GET    | /api/customers  | ?page=1            | 200    | 5     | 15                 |
      | GET    | /api/customers  | ?page=2            | 200    | 0     | 15                 |
      | GET    | /api/customers  | ?pageSize=2        | 200    | 2     | 10                 |
      | GET    | /api/customers  | ?page=1&pageSize=5 | 200    | 2     | 7                  |
      | GET    | /api/customers  |                    | 200    | 0     | 0                  |

  Scenario Outline: Get all customers with invalid page size
    Given I have 1 customers in database
    When I send a '<method>' request to '<endpoint>' with query '<query>'
    Then I should get <status> status code
    Examples:
      | method | endpoint        | query                     | status |
      | GET    | /api/customers  | ?page=23429043324343443423| 500    |
      | GET    | /api/customers  | ?pageSize=afjadf&page=2   | 500    |
      | GET    | /api/customers  | ?page=dfad                | 500    |

