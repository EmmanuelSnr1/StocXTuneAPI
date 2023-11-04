# StocXTuneAPI
## Complete backend as a REST API for a stock-market portfolio management application, with proper authentication, login/ logout system, live stock prices, etc.

Technologies:

- Spring boot (with JDK 17)
    - Spring Web
    - Spring Security
    - Spring Data
    - Spring Open API documentation
- MySQL with JPA (Credentials needed to run API)
- JWT authentication

- Sourcing Data from RAPID API

# Sources of Information: 
- Java 8 Stream API https://www.w3schools.blog/java-8-stream-api-tutorial-with-examples

# TODO 
- General 
  - Clean up portfolio Service Implementation service.
- Debug holdings
  - Fix holdings method when creating a transaction. - Done 
  - Test holdings controller to see if holdings are properly fetched from the database. - Done 
  - Fix convertTransactionDTOsToHoldings method. - Done
  - Modify calculate holdings to include cash holdings as well. 
  - 
- Debug transaction Service 
  - Make sure additional fields added in the front end are included in the transaction service. 
  - 