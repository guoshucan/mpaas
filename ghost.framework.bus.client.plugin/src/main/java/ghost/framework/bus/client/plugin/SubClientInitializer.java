package ghost.framework.bus.client.plugin;

import ghost.framework.bus.SubConstant;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.TimeUnit;

/**
 * package: ghost.framework.bus.client.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/13:2:53
 */
public class SubClientInitializer extends ChannelInitializer<SocketChannel> {
    private Log log = LogFactory.getLog(SubClientInitializer.class);
    private BusClient busClient;

    public SubClientInitializer(BusClient busClient) {
        this.busClient = busClient;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 自定义空闲时间
        if (this.busClient.getEnv() == null) {
            //默认空闲时间设置
            socketChannel.pipeline().addLast(new IdleStateHandler(
                    SubConstant.Nio.DEFAULT_READER_IDLE_TIME,
                    SubConstant.Nio.DEFAULT_WRITER_IDLE_TIME,
                    SubConstant.Nio.DEFAULT_ALL_IDLE_TIME,
                    TimeUnit.SECONDS));
        } else {
            if (this.busClient.getEnv().containsKey(SubConstant.Environment.NIO_READER_IDLE_TIME) &&
                    this.busClient.getEnv().containsKey(SubConstant.Environment.NIO_WRITER_IDLE_TIME) &&
                    this.busClient.getEnv().containsKey(SubConstant.Environment.NIO_ALL_IDLE_TIME)) {
                //指定空闲时间设置
                socketChannel.pipeline().addLast(new IdleStateHandler(
                        this.busClient.getEnv().getInt(SubConstant.Environment.NIO_READER_IDLE_TIME),
                        this.busClient.getEnv().getInt(SubConstant.Environment.NIO_WRITER_IDLE_TIME),
                        this.busClient.getEnv().getInt(SubConstant.Environment.NIO_ALL_IDLE_TIME),
                        TimeUnit.SECONDS));
            } else {
                //默认空闲时间设置
                socketChannel.pipeline().addLast(new IdleStateHandler(
                        SubConstant.Nio.DEFAULT_READER_IDLE_TIME,
                        SubConstant.Nio.DEFAULT_WRITER_IDLE_TIME,
                        SubConstant.Nio.DEFAULT_ALL_IDLE_TIME,
                        TimeUnit.SECONDS));
            }
        }
        //初始化编码器
//        socketChannel.pipeline().addLast("StringDecoder", new StringDecoder());
//        socketChannel.pipeline().addLast("StringEncoder", new StringEncoder());
//        socketChannel.pipeline().addLast("base64Decoder", new Base64Decoder());
//        socketChannel.pipeline().addLast("base64Encoder", new Base64Encoder());
        //判断是否有ssl容器
        if (this.busClient.sslEngine != null) {
            //有ssl容器做ssl处理
            socketChannel.pipeline().addLast("ssl", new SslHandler(this.busClient.sslEngine));
        }
        socketChannel.pipeline().addLast("ObjectDecoder", new ObjectDecoder(ClassResolvers.cacheDisabled(
                this.getClass().getClassLoader()
        )));
        socketChannel.pipeline().addLast("ObjectEncoder", new ObjectEncoder());
//        socketChannel.pipeline().addLast("MessageEventEncoder", new BusClientToMessageEncoder(this.busClient));
//        socketChannel.pipeline().addLast("MessageEventDecoder", new BusClientToMessageDecoder(this.busClient));
        //找到他的管道 增加他的handler
        socketChannel.pipeline().addLast(new SubClientHandler());
    }
}