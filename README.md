# esper-stream-poc

In this proof of concept, I wanted to provide full working stream analytics application which implements sample business logic.  It is the Java Spring Boot application using Esper SDK, Kafka Client Consumer API, Spring Rest Template and Spring Security API. 

### Prerequisities 

* Install Confluent if you do not have Kafka on your local environment.
* Project needs MySQL database to authenticate API user using Basic Auth to access Rest endpoint.
* Knowledge about Zookeeper and Kafka to start and manage basic operational level
* Basic knowledge about using Kafka command line apps(Kafka Topics, Kafka Console Producer/Consumer)

### 1. Adding Event Types
Events are defining the data structure format as Hashmap with the event name. Once the events are sent to Esper run-time , they become available for filtering by EPL statements that were already registered and started before sending events. EPL statement can also produce another events by inserting event parameters to event as SQL Insert statement to make it possible event driven communication with other EPL statements.

### 2. Adding Variables
Variables are SQL like EPL query conditions which makes it the data stream analytics concept powerfull. Variable name, data type and initial value is submitted to EP runtime before starting the EPL statements. After starting EPL statements Esper allows to update the variable value, value changes become effective immediately. For example while query selecting transaction amounts more than 10$ (variable = min_payment_amount), once variable value is updated it will filter with new varibale value for the upcoming transactions as event types.

### 3. EP Runtime table creation
All parameters or subset of eligible event type parameters can be stored in SQL like tables in Esper memory area for further processing with next upcoming events. In order to perform accumulation or aggregation per customer or per event id to catch trend or pattern, it is must to store eligible event parameters. This table can support primary keys, unique keys and common data types.

### 4. Merge Query
