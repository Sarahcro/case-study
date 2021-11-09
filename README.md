##Case Study - Sarah Croteau

###Running this service
- Start up the DB instance
  - `docker run --name cassandra -p 9042:9042 -d cassandra`
- Run ProductServiceApplication.java
- Note: DB doesn't have anything in it, so any GET calls will not have data until PUT is called
- GET request:
  - `curl --location --request GET 'localhost:8080/products/13264003'`
- PUT request:
  ```
    curl --location --request PUT 'localhost:8080/products/13264003' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "currentPrices": [
        {
           "value": 3.99,
           "currencyCode": "USD"
        },
        {
           "value": 4.99,
           "currencyCode": "CAD"
        }
      ]
    }'

###Some considerations
- Would want to mask the creds in the yml
- The prompt suggested that every product should have one price entry in the DB
  - I felt that if multiple currencies are being supported, we should allow for the DB to hold several currency codes for a single product
  - If this were a real product I would ask about use cases (are currency conversions being done elsewhere? If so then this service should store all in one standard currency)
- Using a library or have some validation for currencyCode to ensure user is providing a valid one
- Add circuitbreaker and retry configurations for the calls to the RedSky service, so as to be able to handle when that service is down or overwhelmed with calls
- DB configurations to be more mindful of a production environment
  - probably shouldn't have it build the keyspace on start-up or always have CREATE_IF_NOT_EXISTS set
  - Would want to research best practices on how schemas, tables should be structured and etc.
  - I did what I did to get it working with the limited time I had to learn this, so I recognize my setup was probably un-ideal
  
###Resources Used
(I had never worked with Cassandra before this, so required a bit of research to get that going)
- https://www.baeldung.com/spring-data-cassandra-tutorial
- https://www.baeldung.com/spring-data-cassandra-test-containers
- https://lankydan.dev/2017/10/12/getting-started-with-spring-data-cassandra
