package de.oriontec.esper.app;

import com.espertech.esper.client.EPStatement;
import de.oriontec.esper.app.cep.EsperInitializer;
import de.oriontec.esper.app.config.AppConfig;
import de.oriontec.esper.app.event.EventListener;
import de.oriontec.esper.app.listener.ListenerImpl;
import de.oriontec.esper.app.util.GlobalExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//import com.voida.eyeflex.persistence.config.EngineConfig;


@SpringBootApplication
@EntityScan(basePackageClasses = {Main.class, Jsr310JpaConverters.class})
@EnableJpaRepositories(basePackageClasses = {Main.class})
@EnableConfigurationProperties(AppConfig.class)
public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private static ConfigurableApplicationContext applicationContext;

  public static ConfigurableApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public static void main(String[] args) {
    applicationContext = SpringApplication.run(Main.class, args);

    Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
    logger.info("Booting CEP Engine");

    final EsperInitializer cep = EsperInitializer.getInstance();

    EventListener.getInstance().startServer();
    logger.info("Socket start to listen!!!");
    Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    cep.initModule();

    List<EPStatement> statements = new ArrayList<>();

    logger.debug("adding event types.");
    HashMap<String, Object> uowdatapf = new HashMap<>();
    uowdatapf.put("UOWACC", String.class);
    uowdatapf.put("UOWAMA", String.class);
    uowdatapf.put("UOWAMACCY", String.class);
    uowdatapf.put("UOWID", String.class);
    uowdatapf.put("UOWP1", String.class);

    cep.getEngine().getEPAdministrator().getConfiguration().addEventType("EVENT_UOWDATAPF", uowdatapf);

    HashMap<String, Object> internal = new HashMap<>();
    internal.put("UOWACC", String.class);
    internal.put("MESSAGE", String.class);
    internal.put("ACTION_NAME", String.class);

    cep.getEngine().getEPAdministrator().getConfiguration().addEventType("EVENT_INTERNAL_EVENT", internal);


    logger.debug("adding variables.");
    cep.getEngine().getEPAdministrator().getConfiguration().addVariable("VAR_MESSAGE_TXT", String.class.getName(), "Congrats...");
    cep.getEngine().getEPAdministrator().getConfiguration().addVariable("VAR_TARGET", Integer.class.getName(), 100);


    String tableCreateEpl = "@Resilient @Audit CREATE TABLE TBL_TRX_MAIN"
        + "(ID String PRIMARY KEY, CURR_STATE String, PREV_STATE String, CREATED_DATE  long, "
        + "UPDATED_DATE  long, TRX_COUNT Long, LAST_TOTAL Double, PROMO_COUNT Long, REMAINDER Double, "
        + "SERVED_MSISDN String, TOPUP_COUNT Long, TOTAL_TOPUP Double, TR_REFERENCE String)";

     EPStatement epStatement1 = cep.getEngine().getEPAdministrator().createEPL(tableCreateEpl, "TBL_TRX_MAIN");
     statements.add(epStatement1);

    String mainQueryEpl = "@Resilient @Audit ON EVENT_UOWDATAPF E " + " MERGE TBL_TRX_MAIN T " + " WHERE T.ID = E.UOWACC "

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
          + " SELECT E.UOWACC AS UOWACC, VAR_MESSAGE_TXT AS MESSAGE, 'SMS_COUNT_' || CAST(TRX_COUNT+1, STRING) AS ACTION_NAME";

    EPStatement epStatement2 = cep.getEngine().getEPAdministrator().createEPL(mainQueryEpl, "QUERY_MAIN");
    statements.add(epStatement2);

    String eventSelectQuery = "@Resilient @Audit SELECT UOWACC, MESSAGE, ACTION_NAME FROM EVENT_INTERNAL_EVENT";
    EPStatement epStatement3 = cep.getEngine().getEPAdministrator().createEPL(eventSelectQuery, "QUERY_ACTION");
    statements.add(epStatement3);
    epStatement3.addListener(new ListenerImpl());

    statements.forEach(epStatement -> epStatement.start());


  }
}
