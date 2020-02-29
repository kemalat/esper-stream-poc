package de.oriontec.esper.app.event;

import com.google.common.base.Charsets;
import de.oriontec.esper.app.Main;
import de.oriontec.esper.app.config.AppConfig;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EventListener {

  private static ServerBootstrap bootstrap;
  private static Channel channel = null;
  private static Logger logger = LoggerFactory.getLogger(EventListener.class);

  private static EventListener instance;
  private final AppConfig appConfig;

  private EventListener() {
    appConfig = Main.getApplicationContext().getBean(AppConfig.class);
    
    ChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
        Executors.newCachedThreadPool());

    bootstrap = new ServerBootstrap(factory);
    bootstrap.setPipelineFactory(() -> {
      ChannelPipeline pipeline = Channels.pipeline();
      String endOfEventDelimiter = "~";
      ChannelBuffer channelBuffer = ChannelBuffers.copiedBuffer(endOfEventDelimiter, Charsets.UTF_8);
      pipeline.addLast("framer", new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, channelBuffer));
      pipeline.addLast("decoder", new StringDecoder(Charsets.UTF_8));
      pipeline.addLast("encoder", new StringEncoder(Charsets.UTF_8));
      pipeline.addLast("handler", new EventHandler());
      return pipeline;
    });
    bootstrap.setOption("child.tcpNoDelay", true);
    bootstrap.setOption("child.keepAlive", true);
  }

  public static EventListener getInstance() {
    if (instance == null) {
      instance = new EventListener();
    }
    return instance;
  }

  public void startServer() {
    try {
      channel = bootstrap.bind(new InetSocketAddress(InetAddress.getByName(appConfig.getEventListenerIp()),
          appConfig.getEventListenerPort()));
      logger.info("EVENT listener server started listening from 5432");
    } catch (UnknownHostException e) {
      logger.info(e.getMessage(), e);
    }
  }


  public void stopServerGracefully() {
    if (channel != null && channel.isOpen()) {
      channel.close().awaitUninterruptibly();
    }
  }

}
