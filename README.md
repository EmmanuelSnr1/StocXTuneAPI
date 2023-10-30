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

# TODO 
- General 
  - Clean up portfolio Service Implementation service.
- Debug holdings
  - Fix holdings method when creating a transaction. 
  - Test holdings controller to see if holdings are properly fetched from the database. 
  - Fix convertTransactionDTOsToHoldings method. 
- Debug transaction Service 
  - Make sure additional fields added in the front end are included in the transaction service. 
  - 