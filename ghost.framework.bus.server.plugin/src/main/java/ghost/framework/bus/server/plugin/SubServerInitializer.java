package ghost.framework.bus.server.plugin;

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
 * package: ghost.framework.bus
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:初始化nio通道
 * @Date: 2020/6/12:20:33
 */
final class SubServerInitializer extends ChannelInitializer<SocketChannel> {
    private Log log = LogFactory.getLog(SubServerInitializer.class);
    private BusServer busServer;

    public SubServerInitializer(BusServer busServer) {
        this.busServer = busServer;
    }

    /**
     * 初始化连接
     *
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        if (this.busServer.env == null) {
            //默认空闲时间设置
            socketChannel.pipeline().addLast(new IdleStateHandler(
                    SubConstant.Nio.DEFAULT_READER_IDLE_TIME,
                    SubConstant.Nio.DEFAULT_WRITER_IDLE_TIME,
                    SubConstant.Nio.DEFAULT_ALL_IDLE_TIME,
                    TimeUnit.SECONDS));
        } else {
            // 自定义空闲时间
            if (this.busServer.env.containsKey(SubConstant.Environment.NIO_READER_IDLE_TIME) &&
                    this.busServer.env.containsKey(SubConstant.Environment.NIO_WRITER_IDLE_TIME) &&
                    this.busServer.env.containsKey(SubConstant.Environment.NIO_ALL_IDLE_TIME)) {
                //指定空闲时间设置
                socketChannel.pipeline().addLast(new IdleStateHandler(
                        this.busServer.env.getInt(SubConstant.Environment.NIO_READER_IDLE_TIME),
                        this.busServer.env.getInt(SubConstant.Environment.NIO_WRITER_IDLE_TIME),
                        this.busServer.env.getInt(SubConstant.Environment.NIO_ALL_IDLE_TIME),
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
        if (this.busServer.sslEngine != null) {
            //有ssl容器做ssl处理
            socketChannel.pipeline().addLast("ssl", new SslHandler(this.busServer.sslEngine));
        }
        socketChannel.pipeline().addLast("ObjectDecoder", new ObjectDecoder(ClassResolvers.cacheDisabled(
                this.getClass().getClassLoader()
        )));
        socketChannel.pipeline().addLast("ObjectEncoder", new ObjectEncoder());
//        socketChannel.pipeline().addLast("MessageEventEncoder", new BusServerToMessageEncoder(this.busServer));
//        socketChannel.pipeline().addLast("MessageEventDecoder", new BusServerToMessageDecoder(this.busServer));
        // 自定义处理类
        socketChannel.pipeline().addLast(new SubServerHandler(this.busServer));
    }
}