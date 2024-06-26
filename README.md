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
- Geeks for Geeks 

# TODO 
- General 
  - Clean up portfolio Service Implementation service - Done.
  - Thinking of moving functions with similar purposes into a single file then called whenever.
  - Clean up of portfolio controller to clear up unwanted methods. 
  - Data visualization for portfolio 
  - Analytics
  - - Update trello board with java tasks
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
    - Figure out a way to plot the portfolios performance on a graph
  - Figure out how to compute details corresponding to time (I.e., portfolio performance over time)
- Making Use of Nulab account for project management 

- Nice to have
  - High performance logic. i.e., calculation of portfolio Meta values on the fly could use Maps instead of lists. 
  - User Profile Management 
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
- Project Based learning with Java
- Find a course on Testing in Java. https://java-programming.mooc.fi/part-6/3-introduction-to-testing 


Notes on setting up mysql.
- Install Mysql https://dev.mysql.com/doc/refman/8.3/en/macos-installation-pkg.html
- after installation, setup a default user named "root"
- Use brew install on MAC /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
- run brew install mysql
- brew services start mysql
- mysql_secure_installation (optional)
- To start the shell, run mysql -u root -p
- create the database by running:
  CREATE DATABASE smpm;
  CREATE USER 'root'@'localhost' IDENTIFIED BY 'Gloryv31';
  GRANT ALL PRIVILEGES ON smpm.* TO 'root'@'localhost';
  FLUSH PRIVILEGES;


