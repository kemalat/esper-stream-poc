package de.oriontec.esper.app.cep;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
public class Helper {

  private static final Logger logger = LoggerFactory.getLogger(EsperInitializer.class);

  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
  private static DecimalFormat df2 = new DecimalFormat("#.##");

  static long convertDateToLong(String s) {
    try {
      return sdf.parse(s).getTime();
    } catch (ParseException e) {
      return -1;
    }
  }

  public static String getKey(String walletid) {

    long key = System.currentTimeMillis() + Long.parseLong(walletid);
    return String.valueOf(key);
  }

  private static int i = 0;
  public static int increment(int modulo) {
    System.out.println("incrementing :"+i);
    return (i++ % modulo);
  }

  public static String formatDecimal(double input) {
    return df2.format(input);
  }
  
}
