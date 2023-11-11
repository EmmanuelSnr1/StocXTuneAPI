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
  - Clean up portfolio Service Implementation service - Done.
  - Thinking of moving functions with similar purposes into a single file then called whenever.
  - Clean up of portfolio controller to clear up unwanted methods. 
  - Data visualization for portfolio 
  - Analytics
- Debug holdings
  - Fix holdings method when creating a transaction. - Done 
  - Test holdings controller to see if holdings are properly fetched from the database. - Done 
  - Fix convertTransactionDTOsToHoldings method. - Done
  - Modify calculate holdings to include cash holdings as well. 
  - Make holdings to be computed on the fly from now onwards
  - Add a property that can also be computed on the fly. 
    - Portfolio value 
    - Cash Value
    - Net profit
    - Net Loss
  - Figure out how to compute details corresponding to time (I.e., portfolio performance over time)

- Nice to have
  - High performance logic. i.e., calculation of portfolio Meta values on the fly could use Maps instead of lists. 
  - User Details Management 
  - Media consolidation
  - Data exports
    - Pdfs - Presentation
    - CSVs 
    - Spreadsheets
  - 

- Debug transaction Service 
  - Make sure additional fields added in the front end are included in the transaction service. 


# To Learn 
- Integration Unit testing. 
- Unit mock testing 
- Java 8 Streams API
- Test Coverages
- How to create data science plugins to a Springboot app. 
- List and map data structures.
- Editable PDF with useful java cheat sheet. 
- 