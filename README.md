

Tech Stack
------------

- Java 1.8
- Maven 3.5
- Lombok
- JPA
- Hibernate Validator
- H2 in-memory Database
- RestAssured
- Swagger
- JUNIT 5

Testing Application
---------------
1. Open application through Intelij Idea
2. Let download necessary dependencies to be run the application
3. Right click on the Project and select 'Run All Tests' option.
4. All tests should be executed.


Swagger Documentation
--------------

URL : http://localhost:8080/swagger-ui/index.html

Application Functionalities
--------------

1. Creating a new Guild Bank for players.
2. Creating a new account for an existing bank
3. Adding members to Guild Bank ( Then and only they can deposit and withdraw)
4. A member makes a Deposit to bank account.
5. A member makes a withdrawal from bank account.
6. Closing the Bank
7. Closing the Account
8. Listning all created and opened banks
9. Listing members associated with the bank

Application Improvements
--------------

1. Implementing role based authorization with oAuth2 + Spring Security + JWT
2. Adding business validations
3. Migrating Data layer to like MySQL, PostgreSql or AWS Aurora
4. Integrating application with a distributed caching system.
5. Dockerizing the application