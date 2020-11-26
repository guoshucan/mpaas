package ghost.framework.bus.client.plugin;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.bus.BusStatus;
import ghost.framework.bus.IBusClient;
import ghost.framework.bus.SubConstant;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.utils.AssemblyUtil;
import ghost.framework.util.StopWatch;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.SSLEngine;
import java.io.*;

/**
 * package: ghost.framework.bus.client.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:总线客户端
 * {@link AutoCloseable#close()}
 * {@link IBusClient#getStatus()}
 * {@link IBusClient#close()}
 * @Date: 2020/6/12:20:15
 */
@Component
public class BusClient implements IBusClient {
    public static void main(String[] args) {
        BusClient client = new BusClient();
        client.start();
    }
    /**
     * 初始化总线客户端
     * 由Bean构建入口
     */
    @Constructor
    public BusClient() {
    }
    /**
     * 初始化总线客户端，指定是否使用基础ssl证书
     *
     * @param baseSsl 是否使用ssl证书
     */
    public BusClient(boolean baseSsl) {
        this.baseSsl = baseSsl;
    }
    /**
     * 是否使用ssl证书
     * 使用基础证书将由包内部自带的证书文件作为ssl通信签名证书
     */
    private boolean baseSsl;
    /**
     * 日志
     */
    private Log log = LogFactory.getLog(BusClient.class);
    /**
     * 注入应用env接口
     */
    @Autowired
    private IEnvironment env;

    @Override
    public IEnvironment getEnv() {
        return env;
    }

    // 首先，netty通过ServerBootstrap启动服务端
    private Bootstrap client;
    //第1步 定义线程组，处理读写和链接事件，没有了accept事件
    private EventLoopGroup group;
    private ChannelFuture future;
    /**
     * 同步锁对象
     */
    private final Object root = new Object();
    private BusStatus status = BusStatus.close;
    /**
     * ssl容器
     */
    protected static SSLEngine sslEngine;
    /**
     * 获取总线服务状态
     *
     * @return
     */
    @Override
    public BusStatus getStatus() {
        return status;
    }
    /**
     * 初始化基础证书
     */
    private void initBaseSsl() {
        try {
            //获取默认证书数据
            this.initBytesSsl(AssemblyUtil.getResourceBytes(this.getClass(), "client.jks").getBytes());
        } catch (Exception e) {
            if (this.log.isDebugEnabled()) {
                e.printStackTrace();
                this.log.debug(e.getMessage(), e);
            } else {
                this.log.error(e.getMessage(), e);
            }
        }
    }

    /**
     *
     * @param bytes
     */
    private void initBytesSsl(byte[] bytes) throws IOException{
        //创建服务器端证书流
        try (InputStream pkStream = new ByteArrayInputStream(bytes)) {
            //创建ca证书流
            try (InputStream caStream = new ByteArrayInputStream(bytes)) {
                this.initStreamSsl(pkStream, caStream);
            }
        }
    }
    /**
     *
     * @param pkStream
     * @param caStream
     */
    private void initStreamSsl(InputStream pkStream, InputStream caStream) {
        //
        if (this.env == null) {
            this.sslEngine = SubClientSslContextFactory.getClientContext(pkStream, caStream, SubConstant.Ssl.DEFAULT_SSL_PASSWORD, SubConstant.Ssl.DEFAULT_SSL_PROTOCOL).createSSLEngine();
        } else {
            if (this.env.containsKey(SubConstant.Ssl.SSL_PROTOCOL)) {
                this.sslEngine = SubClientSslContextFactory.getClientContext(pkStream, caStream, SubConstant.Ssl.DEFAULT_SSL_PASSWORD, this.env.getString(SubConstant.Ssl.SSL_PROTOCOL)).createSSLEngine();
            } else {
                this.sslEngine = SubClientSslContextFactory.getClientContext(pkStream, caStream, SubConstant.Ssl.DEFAULT_SSL_PASSWORD, SubConstant.Ssl.DEFAULT_SSL_PROTOCOL).createSSLEngine();
            }
        }
        //设置不为客户端模式
        this.sslEngine.setUseClientMode(true);
    }
    /**
     * 初始化ssl证书
     */
    private void initSsl() throws IOException {
        if (this.env == null) {
            if (this.baseSsl) {
                //有指定使用默认ssl证书
                this.initBaseSsl();
            }
        } else {
            //判断是否由有在evn指定ssl证书
            if (!this.env.containsKey(SubConstant.Ssl.SSL_CLIENT_DATA) && !this.env.containsKey(SubConstant.Ssl.SSL_CLIENT_STREAM) && !this.env.containsKey(SubConstant.Ssl.SSL_CLIENT_FILE)) {
                //没有在evn指定ssl证书
                if (this.baseSsl) {
                    //有指定使用默认ssl证书
                    this.initBaseSsl();
                }
            } else {
                //判断是否使用二进制证书
                if (this.env.containsKey(SubConstant.Ssl.SSL_CLIENT_DATA)) {
                    this.initBytesSsl(this.env.get(SubConstant.Ssl.SSL_CLIENT_DATA));
                    return;
                }
                //判断是否使用流证书
                if (this.env.containsKey(SubConstant.Ssl.SSL_CLIENT_STREAM)) {
                    InputStream stream = this.env.get(SubConstant.Ssl.SSL_CLIENT_STREAM);
                    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        IOUtils.copy(stream, out);
                        try (InputStream pkStream = new ByteArrayInputStream(out.toByteArray())) {
                            try (InputStream caStream = new ByteArrayInputStream(out.toByteArray())) {
                                this.initStreamSsl(pkStream, caStream);
                                return;
                            }
                        }
                    }
                }
                //判断是否使用文件证书
                if (this.env.containsKey(SubConstant.Ssl.SSL_SERVER_FILE)) {
                    File file = this.env.get(SubConstant.Ssl.SSL_SERVER_FILE);
                    try (InputStream pkStream = new FileInputStream(file)) {
                        try (InputStream caStream = new FileInputStream(file)) {
                            this.initStreamSsl(pkStream, caStream);
                            return;
                        }
                    }
                }
            }
        }
    }
    /**
     * 加载启动
     */
    @Override
    @Loader
    public void start() {
        synchronized (root) {
            //判断启动状态
            if (this.status == BusStatus.start) {
                return;
            } else {
                this.status = BusStatus.start;
            }
            final StopWatch watch = new StopWatch();
            watch.start("start");
            this.log.info("启动 Bus Client");
            try {
                //初始化ssl证书
                this.initSsl();
                // 首先，netty通过ServerBootstrap启动服务端
                this.client = new Bootstrap();
                //第1步 定义线程组，处理读写和链接事件，没有了accept事件
                this.group = new NioEventLoopGroup();
                this.client.group(this.group);
                if(this.env == null) {
                    this.client.option(ChannelOption.SO_KEEPALIVE, SubConstant.Nio.DEFAULT_KEEPALIVE);
                    this.client.option(ChannelOption.SO_SNDBUF, SubConstant.Nio.DEFAULT_SNDBUF);
                    this.client.option(ChannelOption.SO_RCVBUF, SubConstant.Nio.DEFAULT_RCVBUF);
                }else{
                    if (this.env.containsKey(SubConstant.Environment.NIO_SNDBUF)) {
                        this.client.option(ChannelOption.SO_SNDBUF, this.env.getInt(SubConstant.Environment.NIO_SNDBUF));
                    }
                    if (this.env.containsKey(SubConstant.Environment.NIO_RCVBUF)) {
                        this.client.option(ChannelOption.SO_RCVBUF, this.env.getInt(SubConstant.Environment.NIO_RCVBUF));
                    }
                    if (this.env.containsKey(SubConstant.Environment.NIO_KEEPALIVE)) {
                        this.client.option(ChannelOption.SO_KEEPALIVE, this.env.getBoolean(SubConstant.Environment.NIO_KEEPALIVE));
                    }
                }
                //第2步 绑定客户端通道
                this.client.channel(NioSocketChannel.class);
                //第3步 给NIoSocketChannel初始化handler， 处理读写事件
                this.client.handler(new SubClientInitializer(this));
                //获取服务器ip端口
                String ip;
                int port;
                if (this.env == null) {
                    ip = SubConstant.Nio.DEFAULT_IP_ADDRESS;
                    port = SubConstant.Nio.DEFAULT_PORT;
                } else {
                    ip = this.env.containsKey(SubConstant.Environment.IP_ADDRESS) ? this.env.getString(SubConstant.Environment.IP_ADDRESS) : SubConstant.Nio.DEFAULT_IP_ADDRESS;
                    port = this.env.containsKey(SubConstant.Environment.PORT) ? this.env.getInt(SubConstant.Environment.PORT) : SubConstant.Nio.DEFAULT_PORT;
                }
                this.log.info("启动连接服务器 > ip=" + ip + ":" + port);
                //连接服务器
                this.future = client.connect(ip, port).sync();
                this.log.info("启动 Bus Client 完成 > ip=" + ip + ":" + port);
                //计数器结束
                watch.stop();
                this.log.info(watch.prettyPrint());
            } catch (Exception e) {
                if (this.future != null) {
                    this.future.channel().close();
                }
                synchronized (this.root) {
                    this.status = BusStatus.close;
                }
                this.log.error("启动 Bus Client 错误！");
                if (this.log.isDebugEnabled()) {
                    e.printStackTrace();
                    this.log.debug(e.getMessage(), e);
                } else {
                    this.log.error(e.getMessage(), e);
                }
            }
        }
    }
    /**
     * 关闭释放资源
     * @throws Exception
     */
//    @Unloader
    @Override
    public void close() throws Exception {
        synchronized (root) {
            if (status == BusStatus.close) {
                return;
            }
            //当通道关闭了，就继续往下走
//            future.channel().closeFuture().sync();
            this.future.channel().close();
        }
    }
}