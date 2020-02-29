package de.oriontec.esper.app;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SocketEventRequest {

  private static ClientBootstrap bootstrap;
  private static final Logger logger = LoggerFactory.getLogger(SocketEventRequest.class);

  public static void main(String[] args) {

    bootstrap = new ClientBootstrap(
        new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

    // Set up the pipeline factory.
    bootstrap.setPipelineFactory(() -> {
      ChannelPipeline pipeline = Channels.pipeline();

      // Decoders
      int maxFrameLength = 1024;
      pipeline.addLast("framer", new DelimiterBasedFrameDecoder(maxFrameLength, Delimiters.lineDelimiter()));
      pipeline.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
      pipeline.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));

      return pipeline;
    });

//    RateLimiter limiter = RateLimiter.create(2000);
//
//    Random random = new Random();

    // while (true) {
    //   limiter.acquire();
    try {
      // Start the connection attempt.
      ChannelFuture future = bootstrap.connect(new InetSocketAddress(InetAddress.getByName("localhost"), 5433));
      // Wait until the connection is closed or the connection attempt fails.
      Channel channel = future.awaitUninterruptibly().getChannel();

      // String message1 = "a,IOM_KEY,com.voida.scenario.MyCardScenario1and2,PRODTABLE,ID,5124411141003,RBS,30145124410130270,
      // PACCPRODUCTCODE,Альфа чек,SUMMAZ,1";

      // String message2 = "a,IOM_KEY,com.voida.scenario.MyCardScenario1and2,PRODTABLE,ID,5124411141003,RBS,30145124410130270,
      // PACCPRODUCTCODE,Моя карта,SUMMAZ,1";

      // String message3 = "a,IOM_KEY,ALL,UOWDATAPF,UOWTR,CH DEBIT,UOWAMA,10,UOWACC,30145124410130270,SUMMAZ,1";

      String message = "c,IOM_KEY,ALL,ClockEvent,MINUTE,0,HOUR,12,DAY,22,MONTH,12,YEAR,2016,DAY_OF_WEEK,2~";
      String message2 = "a,IOM_KEY,ALL,UOWDATAPF,UOWTR,SOA DEBIT,UOWAMA,10,UOWACC,30144460790140270,UOWDTL,ALFA~";

      // This is where the test write is <<------
      // channel.write(message1 + "\n");
      // channel.write(message2 + "\n");
      // channel.write(message3 + "\n");

//      String mess = "a,IOM_KEY,ALL,CDR,msisdn,999,in_balance,1,lcal_lcal_id,130~";

//      while (true) {
//        limiter.acquire();
//        channel.write(message);
//      }

      channel.write(message2);

      // System.out.println("Sent.");
      // if (!future.isSuccess()) {
      //   System.err.println(future.getCause());
      //   bootstrap.releaseExternalResources();
      // }
    } catch (UnknownHostException e) {
      logger.error(e.getMessage(), e);
    }
    // }

  }


}
