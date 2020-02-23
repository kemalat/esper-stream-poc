# esper-stream-poc

In this proof of concept, I wanted to provide full working stream analytics application which implements sample business logic.  It is the Java Spring Boot application using Esper SDK, Kafka Client Consumer API, Spring Rest Template and Spring Security API. Here are links for Esper [datasheet](http://static.espertech.com/EsperTech%20technical%20datasheet.pdf) and [reference](http://www.espertech.com/esper/esper-documentation/) documents

### Prerequisities 

* Install Confluent if you do not have Kafka on your local environment.
* Project needs MySQL database to authenticate API user using Basic Auth to access Rest endpoint.
* Good knowledge and understanding about SQL
* Knowledge about Zookeeper and Kafka to start and manage basic operational level
* Basic knowledge about using Kafka command line apps(Kafka Topics, Kafka Console Producer/Consumer)

### Kafka Message Queue
In this demonstration I used, Confluent package which comes with required Kafka components with ready use start/stop scripts. We need to start zookeeper and kafka server at least to make the message queue ready.  Zookeeper acts as a centralized service and is used to keep track of status of the Kafka cluster nodes, topics and partitions etc. In short we can say that High availability of Kafka messaging queue in distributed architecture managed and achieved by Zookeeper. Kafka as distributed streaming platform performs publishing and subscribing to streams of records, similar to a message queue or enterprise messaging systems. It stores streams of records in a fault-tolerant durable way and processes streams of records as they occur. 

### Start up commands for zookeeper and kafka
Please follow the instructions on this [link](https://docs.confluent.io/current/quickstart/ce-quickstart.html)
You can start required processses with their default configuration without changing the property files.

```
cd etc/confluent-5.3.2/bin/
./bin/zookeeper-server-start ./etc/kafka/zookeeper.properties
./bin/kafka-server-start ./etc/kafka/server.properties
```
#### Create topic and produce events

After creating the topic with the name which is matching with `esper.kafka.topics` property in `application.properties`, you are good to go for pushing event the thi topic as shown below. 

```
./kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic tweet
echo "EVENT_UOWDATAPF,UOWACC,100,UOWAMA,200" | ./kafka-console-producer --broker-list localhost:9092 --topic tweet > /dev/null
```
### Esper Engine Initialization
Esper initialization is done by `initializeEngine()` function of `EsperInitializer` class. 
Excerpt from `application.properties` related to Esper Engine Initialization

```
esper.name=esper-core
esper.sdk.name=ESPER
esper.sdk.version=1.0.0

#Server Socket port to wait connection and events
event.listener.port=5432
event.listener.ip=0.0.0.0

#Enable/Disable logging for EPL statements. 
statement.audit.enabled=true

#JDBC connectivity to Esper Engine
esper.jdbc.enabled=true
esper.jdbc.port=8450
esper.jdbc.processorCount=1
esper.jdbc.sessionIdleTimeout=600


#Configuring Esper Kafka Consumer to find out Kafka Bootstrap and relevant topic for consuming event types
esper.kafka.enabled=true
esper.kafka.bootstrap.servers=localhost:9092
esper.kafka.consumer.group.id=consumerGroup1
esper.kafka.consumer.client.id=eyeflex
esper.kafka.consumer.offset=earliest
esper.kafka.topics=tweet

# Esper engine places inbound events in a queue for processing  by number of created threads
# engine-managed threads other than the delivering application threads
esper.worker.inboundThreadSize=2

# Esper engine places outbound events in a queue for delivery by number of created threads
# engine-managed threads other than the processing thread originating the result.
esper.worker.outboundThreadSize=2
```

### About POC application
Java application provided has standard Spring Boot App structure. To make conceptual development/testing easier and faster , full stream analysis model creation done in Main function of app. Application supports listening events from event sources via either plain socket connection(EventListener) or EsperIOKafkaInputProcessor as plain text of character array. App also exposes Rest service to execute on demand queries entered as Rest service body and return response as Json DTO.


### 1. Adding Event Types
Events are defining the data structure format as Hashmap with the event name. Once the events are sent to Esper run-time , they become available for filtering by EPL statements that were already registered and started before sending events. EPL statement can also produce another events by inserting event parameters to event as SQL Insert statement to make it possible event driven communication with other EPL statements.

```
HashMap<String, Object> eventParams = new HashMap<>();
eventParams.put("ACC_ID", String.class);
eventParams.put("TRX_AMNT", Double.class);
cep.getEngine().getEPAdministrator().getConfiguration().addEventType("EVENT_TRX", eventParams);
```

### 2. Adding Variables
Variables are SQL like EPL query conditions which makes it the data stream analytics concept powerfull. Variable name, data type and initial value is submitted to EP runtime before starting the EPL statements. After starting EPL statements Esper allows to update the variable value, value changes become effective immediately. For example while query selecting transaction amounts more than 10$ (variable = min_payment_amount), once variable value is updated it will filter with new varibale value for the upcoming transactions as event types.

```
cep.getEngine().getEPAdministrator().getConfiguration().addVariable("VAR_TARGET_AMNT", Integer.class.getName(), 100);
```

### 3. EP Runtime table creation
All parameters or subset of eligible event type parameters can be stored in SQL like tables in Esper memory area for further processing with next upcoming events. In order to perform accumulation or aggregation per customer or per event id to catch trend or pattern, it is must to store eligible event parameters. This table can support primary keys, unique keys and common data types.

```
String tableCreateEpl = "@Resilient @Audit CREATE TABLE TBL_TRX_MAIN"
    + "(ID String PRIMARY, TRX_COUNT Long, LAST_TOTAL Double, TR_REFERENCE String)";

cep.getEngine().getEPAdministrator().createEPL(tableCreateEpl, "TBL_TRX_MAIN");

```

### 4. Merge Query
Merge query as it name suggests merges floating event type stream with the EP runtime you created before, here you define filter conditions using SQL like query then either you insert event params after calculations to EP runtime table and another internal event or only to EP runtime table for further accumulation or processing. Inside EPL queries it is possible to call Java static utility functions. This type of queries are self explantory and very similar to Oracle DB merger queries. 


```
@Resilient @Audit ON EVENT_UOWDATAPF E " + " MERGE TBL_TRX_MAIN T " + " WHERE T.ID = E.UOWACC "

        + " WHEN NOT MATCHED AND E.UOWACC IS NOT NULL AND E.UOWAMA IS NOT NULL AND CAST(E.UOWAMA, LONG) > VAR_TARGET"

          + "   THEN INSERT SELECT UOWACC AS ID, '1_TRANSACTIONS' AS CURR_STATE,1 AS TRX_COUNT,System.currentTimeMillis()"
          + " AS CREATED_DATE,  System.currentTimeMillis()"
          + " AS UPDATED_DATE"

          + "   THEN INSERT INTO EVENT_INTERNAL_EVENT"
          + " SELECT E.UOWACC AS UOWACC,  VAR_MESSAGE_TXT AS MESSAGE, 'SMS_COUNT_1' AS ACTION_NAME"

          + " WHEN MATCHED AND T.TRX_COUNT < 2 AND E.UOWACC IS NOT NULL AND E.UOWAMA IS NOT NULL AND CAST(E.UOWAMA, LONG) > VAR_TARGET"

        + "   THEN UPDATE SET PREV_STATE = CURR_STATE, CURR_STATE = (CAST(TRX_COUNT + 1, STRING) || '_TRANSACTIONS'), TRX_COUNT  = "
          + "TRX_COUNT  + 1, UPDATED_DATE = System.currentTimeMillis()"

        + "   THEN INSERT INTO EVENT_INTERNAL_EVENT"
        + " SELECT E.UOWACC AS UOWACC, VAR_MESSAGE_TXT AS MESSAGE, 'SMS_COUNT_' || CAST(TRX_COUNT+1, STRING) AS ACTION_NAME"

        + " WHEN MATCHED AND T.TRX_COUNT + 1 = 3 AND E.UOWACC IS NOT NULL AND E.UOWAMA IS NOT NULL AND CAST(E.UOWAMA, LONG) > VAR_TARGET"

          + "   THEN UPDATE SET PREV_STATE = CURR_STATE, CURR_STATE = 'END', TRX_COUNT  = TRX_COUNT  + 1, UPDATED_DATE = System"
          + ".currentTimeMillis()"

          + "   THEN INSERT INTO EVENT_INTERNAL_EVENT"
          + " SELECT E.UOWACC AS UOWACC, VAR_MESSAGE_TXT AS MESSAGE, 'SMS_COUNT_' || CAST(TRX_COUNT+1, STRING) AS ACTION_NAME
```

### 5. Select Query
It is also possible to use Select query to filter out events and trigger Java listener class functions. Once Select returns the result with given conditions, result become available to use by Listener class function as function parameter via callback by Esper. You do it by adding listener to your statements. Your listener class should implement `com.espertech.esper.client.UpdateListener`

It might be good idea to start stream analysis with Select query and capture only valid events then do enrichment if needed inside Listener update functions and send back to stream as internal events to make it available for other EPL statements. 

```
String eventSelectQuery = "@Resilient @Audit SELECT UOWACC, MESSAGE, ACTION_NAME FROM EVENT_INTERNAL_EVENT";
EPStatement epStatement3 = cep.getEngine().getEPAdministrator().createEPL(eventSelectQuery, "QUERY_ACTION");
statements.add(epStatement3);
epStatement3.addListener(new ListenerImpl());
```

### 6. Listener 
You can register Listener or in other words callback function to your EPL Select statements at final stage or at the begining of analysis to do data enrichment. By means of registered callback function, you can call another API or REST web service execute to final phase business logic such as sending alarm notification, triggering cashback or promotion system etc.

```
  @Override
  public void update(EventBean[] eventBeans, EventBean[] eventBeans1) {

  }
```

### 7. On Demand Query 
Esper Runtime supports on-demand queries by means of its `epRuntime.prepareQuery(query)` function. Using prepareQuery function it is possible to write SQL queries to do data manipulation on EP Runtime tables. This query functionality can be used for real time reporting/statistics or high availability purposes to take the snapshoot of EP Runtime tables.

