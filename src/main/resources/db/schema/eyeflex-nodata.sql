-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: localhost    Database: eyeflex
-- ------------------------------------------------------
-- Server version	5.7.24-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `CORTEX_ACTION`
--
use
eyeflex;
DROP TABLE IF EXISTS ` CORTEX_ACTION `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_ACTION ` (
  `ACTION_NAME` varchar(
  255
) NOT NULL,
  ` ACTION_DESC ` varchar (
  255
) DEFAULT NULL,
  ` ACTION_TEMPLATE_ACTION_TEMPLATE_NAME ` varchar (
  255
) DEFAULT NULL,
  PRIMARY KEY (
  `ACTION_NAME`
),
  KEY ` FKvi09uksd8hk3obns5loqgw2n ` (
  `ACTION_TEMPLATE_ACTION_TEMPLATE_NAME`
),
  CONSTRAINT ` FKvi09uksd8hk3obns5loqgw2n ` FOREIGN KEY (
  `ACTION_TEMPLATE_ACTION_TEMPLATE_NAME`
) REFERENCES ` CORTEX_ACTION_TEMPLATE ` (
  `ACTION_TEMPLATE_NAME`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_ACTION_LOG`
--

DROP TABLE IF EXISTS ` CORTEX_ACTION_LOG `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_ACTION_LOG ` (
  `ID` int(
  11
) NOT NULL,
  ` ACTION_NAME ` varchar (
  255
) DEFAULT NULL,
  ` ACTION_TEMPLATE_NAME ` varchar (
  255
) DEFAULT NULL,
  ` CURR_STATE ` varchar (
  255
) DEFAULT NULL,
  ` EVENT ` varchar (
  255
) DEFAULT NULL,
  ` INSERT_TIME ` datetime DEFAULT NULL,
  ` LOG ` text,
  ` PREV_STATE ` varchar (
  255
) DEFAULT NULL,
  ` SCENARIO_KEY ` bigint (
  20
) DEFAULT NULL,
  ` SCENARIO_NAME ` varchar (
  255
) DEFAULT NULL,
  ` TAG ` varchar (
  255
) DEFAULT NULL,
  PRIMARY KEY (
  `ID`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_ACTION_PARAM`
--

DROP TABLE IF EXISTS ` CORTEX_ACTION_PARAM `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_ACTION_PARAM ` (
  `ACTION_NAME` varchar(
  255
) NOT NULL,
  ` PARAM_NAME ` varchar (
  255
) NOT NULL,
  ` PARAM_VALUE ` varchar (
  255
) DEFAULT NULL,
  PRIMARY KEY (
  `
  ACTION_NAME
  `,
  `
  PARAM_NAME
  `
),
  CONSTRAINT ` FK7kqqjdhpm7wim5qevhbmdso22 ` FOREIGN KEY (
  `ACTION_NAME`
) REFERENCES ` CORTEX_ACTION ` (
  `ACTION_NAME`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_ACTION_TEMPLATE`
--

DROP TABLE IF EXISTS ` CORTEX_ACTION_TEMPLATE `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_ACTION_TEMPLATE ` (
  `ACTION_TEMPLATE_NAME` varchar(
  255
) NOT NULL,
  ` ACTION_TEMPLATE_DESC ` varchar (
  255
) DEFAULT NULL,
  ` ACTION_TEMPLATE_FILENAME ` varchar (
  255
) DEFAULT NULL,
  PRIMARY KEY (
  `ACTION_TEMPLATE_NAME`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_ENGINE_USER`
--

DROP TABLE IF EXISTS ` CORTEX_ENGINE_USER `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_ENGINE_USER ` (
  `USERNAME` varchar(
  255
) NOT NULL,
  ` ACTIVATED ` bit (
  1
) NOT NULL,
  ` ACTIVATION_KEY ` varchar (
  255
) DEFAULT NULL,
  ` EMAIL ` varchar (
  255
) DEFAULT NULL,
  ` FULLNAME ` varchar (
  255
) DEFAULT NULL,
  ` PASSWORD ` varchar (
  255
) DEFAULT NULL,
  ` PHONE ` varchar (
  255
) DEFAULT NULL,
  ` RESET_PASSWORD_KEY ` varchar (
  255
) DEFAULT NULL,
  ` TYPE ` varchar (
  255
) DEFAULT NULL,
  PRIMARY KEY (
  `USERNAME`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_EVENT`
--

DROP TABLE IF EXISTS ` CORTEX_EVENT `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_EVENT ` (
  `EVENT_NAME` varchar(
  255
) NOT NULL,
  ` SCENARIO_NAME ` varchar (
  255
) NOT NULL,
  ` EVENT_DESC ` varchar (
  255
) DEFAULT NULL,
  PRIMARY KEY (
  `
  EVENT_NAME
  `,
  `
  SCENARIO_NAME
  `
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_EVENT_PARAM`
--

DROP TABLE IF EXISTS ` CORTEX_EVENT_PARAM `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_EVENT_PARAM ` (
  `EVENT_NAME` varchar(
  255
) NOT NULL,
  ` SCENARIO_NAME ` varchar (
  255
) NOT NULL,
  ` PARAM_NAME ` varchar (
  255
) NOT NULL,
  ` PARAM_DESC ` varchar (
  255
) DEFAULT NULL,
  ` PARAM_TYPE ` varchar (
  255
) DEFAULT NULL,
  PRIMARY KEY (
  `
  EVENT_NAME
  `,
  `
  SCENARIO_NAME
  `,
  `
  PARAM_NAME
  `
),
  CONSTRAINT ` FKebc2hnnbwhaqkrsfacw8la150 ` FOREIGN KEY (
  `
  EVENT_NAME
  `,
  `
  SCENARIO_NAME
  `
) REFERENCES ` CORTEX_EVENT ` (
  `
  EVENT_NAME
  `,
  `
  SCENARIO_NAME
  `
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_EXTERNAL_CONNECTION`
--

DROP TABLE IF EXISTS ` CORTEX_EXTERNAL_CONNECTION `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_EXTERNAL_CONNECTION ` (
  `NAME` varchar(
  255
) NOT NULL,
  ` AUTO_COMMIT ` char (
  1
) DEFAULT NULL,
  ` CONN_TEST_QUERY ` varchar (
  255
) DEFAULT NULL,
  ` CONN_TIMEOUT ` int (
  11
) NOT NULL,
  ` DB_DRIVER ` varchar (
  255
) DEFAULT NULL,
  ` DB_PASSWORD ` varchar (
  255
) DEFAULT NULL,
  ` DB_URL ` varchar (
  255
) DEFAULT NULL,
  ` DB_USER ` varchar (
  255
) DEFAULT NULL,
  ` MAX_POOL_SIZE ` int (
  11
) NOT NULL,
  ` MIN_IDLE ` int (
  11
) NOT NULL,
  ` READ_ONLY ` char (
  1
) DEFAULT NULL,
  PRIMARY KEY (
  `NAME`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_EXTERNAL_DATA`
--

DROP TABLE IF EXISTS ` CORTEX_EXTERNAL_DATA `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_EXTERNAL_DATA ` (
  `DATA_NAME` varchar(
  255
) NOT NULL,
  ` SCENARIO_NAME ` varchar (
  255
) NOT NULL,
  ` CACHE_KEY ` varchar (
  255
) DEFAULT NULL,
  ` CACHE_VALIDITY ` bigint (
  20
) NOT NULL,
  ` CACHEABLE ` bit (
  1
) NOT NULL,
  ` QUERY ` varchar (
  2000
) DEFAULT NULL,
  ` VAR_NAME ` varchar (
  255
) DEFAULT NULL,
  ` VAR_TYPE ` varchar (
  255
) DEFAULT NULL,
  ` EXTERNAL_CONNECTION_NAME ` varchar (
  255
) DEFAULT NULL,
  PRIMARY KEY (
  `
  DATA_NAME
  `,
  `
  SCENARIO_NAME
  `
),
  KEY ` FKth71bj6k0eff99ytt5v8fnj79 ` (
  `EXTERNAL_CONNECTION_NAME`
),
  CONSTRAINT ` FKth71bj6k0eff99ytt5v8fnj79 ` FOREIGN KEY (
  `EXTERNAL_CONNECTION_NAME`
) REFERENCES ` CORTEX_EXTERNAL_CONNECTION ` (
  `NAME`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_FUTURE_EVENT`
--

DROP TABLE IF EXISTS ` CORTEX_FUTURE_EVENT `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_FUTURE_EVENT ` (
  `ID` bigint(
  20
) NOT NULL AUTO_INCREMENT,
  ` CREATED_DATE ` datetime DEFAULT NULL,
  ` EVENT_NAME ` varchar (
  200
) DEFAULT NULL,
  ` EVENT_PARAMS ` varchar (
  5000
) DEFAULT NULL,
  ` EVENT_TIME ` datetime DEFAULT NULL,
  ` SCENARIO_KEY ` varchar (
  255
) DEFAULT NULL,
  ` SCENARIO_NAME ` varchar (
  255
) DEFAULT NULL,
  ` STATUS ` int (
  11
) DEFAULT NULL,
  PRIMARY KEY (
  `ID`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_HISTORY`
--

DROP TABLE IF EXISTS ` CORTEX_HISTORY `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_HISTORY ` (
  `ID` bigint(
  20
) NOT NULL AUTO_INCREMENT,
  ` ISSUE_DATE ` datetime DEFAULT NULL,
  ` ISSUE_TYPE ` varchar (
  255
) DEFAULT NULL,
  ` ISSUE_USER ` varchar (
  255
) DEFAULT NULL,
  ` MESSAGE ` varchar (
  255
) DEFAULT NULL,
  ` SCENARIO_NAME ` varchar (
  255
) DEFAULT NULL,
  PRIMARY KEY (
  `ID`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_MISSED_ACTION`
--

DROP TABLE IF EXISTS ` CORTEX_MISSED_ACTION `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_MISSED_ACTION ` (
  `ID` varchar(
  255
) NOT NULL,
  ` ACTION_NAME ` varchar (
  255
) DEFAULT NULL,
  ` INITIAL_EXECUTION_TIME ` datetime DEFAULT NULL,
  ` LAST_EXECUTION_DATE ` datetime DEFAULT NULL,
  ` MESSAGE ` varchar (
  255
) DEFAULT NULL,
  ` PARAMS ` varchar (
  255
) DEFAULT NULL,
  ` RESULT ` int (
  11
) DEFAULT NULL,
  ` SCENARIO_KEY ` varchar (
  255
) DEFAULT NULL,
  ` SCENARIO_NAME ` varchar (
  255
) DEFAULT NULL,
  PRIMARY KEY (
  `ID`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_SCENARIO`
--

DROP TABLE IF EXISTS ` CORTEX_SCENARIO `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_SCENARIO ` (
  `SCENARIO_NAME` varchar(
  255
) NOT NULL,
  ` DESCRIPTION ` varchar (
  255
) DEFAULT NULL,
  ` DETAILS ` varchar (
  255
) DEFAULT NULL,
  ` END_TIME ` datetime DEFAULT NULL,
  ` START_TIME ` datetime DEFAULT NULL,
  ` STATUS ` int (
  11
) DEFAULT NULL,
  PRIMARY KEY (
  `SCENARIO_NAME`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_SERVICE_USER`
--

DROP TABLE IF EXISTS ` CORTEX_SERVICE_USER `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_SERVICE_USER ` (
  `USERNAME` varchar(
  255
) NOT NULL,
  ` ACTIVATED ` bit (
  1
) NOT NULL,
  ` ACTIVATION_KEY ` varchar (
  255
) DEFAULT NULL,
  ` EMAIL ` varchar (
  255
) DEFAULT NULL,
  ` FULLNAME ` varchar (
  255
) DEFAULT NULL,
  ` PASSWORD ` varchar (
  255
) DEFAULT NULL,
  ` PHONE ` varchar (
  255
) DEFAULT NULL,
  ` RESET_PASSWORD_KEY ` varchar (
  255
) DEFAULT NULL,
  ` TYPE ` varchar (
  255
) DEFAULT NULL,
  PRIMARY KEY (
  `USERNAME`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_VALIDATION`
--

DROP TABLE IF EXISTS ` CORTEX_VALIDATION `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_VALIDATION ` (
  `SCENARIO_NAME` int(
  11
) NOT NULL,
  ` VALIDATION_PARAM ` varchar (
  255
) DEFAULT NULL,
  ` VALIDATION_TYPE ` int (
  11
) NOT NULL,
  ` VALIDATION_VALUE ` varchar (
  255
) DEFAULT NULL,
  PRIMARY KEY (
  `SCENARIO_NAME`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CORTEX_VARIABLE`
--

DROP TABLE IF EXISTS ` CORTEX_VARIABLE `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` CORTEX_VARIABLE ` (
  `SCENARIO_NAME` varchar(
  255
) NOT NULL,
  ` VARIABLE_NAME ` varchar (
  255
) NOT NULL,
  ` DESCRIPTION ` varchar (
  255
) DEFAULT NULL,
  ` VARIABLE_TYPE ` varchar (
  255
) DEFAULT NULL,
  ` VARIABLE_VALUE ` varchar (
  255
) DEFAULT NULL,
  PRIMARY KEY (
  `
  SCENARIO_NAME
  `,
  `
  VARIABLE_NAME
  `
),
  CONSTRAINT ` FKq9cula26x8152s9015tja4t6y ` FOREIGN KEY (
  `SCENARIO_NAME`
) REFERENCES ` CORTEX_SCENARIO ` (
  `SCENARIO_NAME`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oauth_access_token`
--

DROP TABLE IF EXISTS ` oauth_access_token `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` oauth_access_token ` (
  `token_id` varchar(
  256
) DEFAULT NULL,
  ` token ` blob,
  ` authentication_id ` varchar (
  256
) DEFAULT NULL,
  ` user_name ` varchar (
  256
) DEFAULT NULL,
  ` client_id ` varchar (
  256
) DEFAULT NULL,
  ` authentication ` blob,
  ` refresh_token ` varchar (
  256
) DEFAULT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oauth_refresh_token`
--

DROP TABLE IF EXISTS ` oauth_refresh_token `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` oauth_refresh_token ` (
  `token_id` varchar(
  256
) DEFAULT NULL,
  ` token ` blob,
  ` authentication ` blob
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS ` sessions `;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ` sessions ` (
  `session_id` varchar(
  255
) NOT NULL,
  ` expires ` int (
  11
) unsigned NOT NULL,
  ` data ` text,
  PRIMARY KEY (
  `session_id`
)
  ) ENGINE=InnoDB DEFAULT CHARSET =latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-13  5:20:30
