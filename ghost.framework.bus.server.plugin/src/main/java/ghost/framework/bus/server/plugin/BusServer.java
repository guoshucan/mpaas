package ghost.framework.bus.server.plugin;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.bus.BusStatus;
import ghost.framework.bus.IBusServer;
import ghost.framework.bus.SubConstant;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.io.ResourceBytes;
import ghost.framework.context.utils.AssemblyUtil;
import ghost.framework.thread.OneRunnable;
import ghost.framework.util.StopWatch;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.SSLEngine;
import java.io.*;
import java.net.InetAddress;

/**
 * package: ghost.framework.bus
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:总线服务
 * {@link AutoCloseable#close()}
 * {@link IBusServer#getStatus()}
 * {@link IBusServer#close()}
 * @Date: 2020/6/12:20:10
 */
@Component
public class BusServer implements IBusServer {
    /**
     * 初始化总线服务
     * 由Bean构建入口
     */
    @Constructor
    public BusServer() {
    }

    /**
     * 初始化总线服务，指定是否使用基础ssl证书
     *
     * @param baseSsl 是否使用ssl证书
     */
    public BusServer(boolean baseSsl) {
        this.baseSsl = baseSsl;
    }
    /**
     * 初始化总线服务，指定是否使用基础ssl证书
     *
     * @param baseSsl 是否使用ssl证书
     * @param needClientAuth  是否开启客户端鉴权
     */
    public BusServer(boolean baseSsl, boolean needClientAuth) {
        this.baseSsl = baseSsl;
        this.needClientAuth = needClientAuth;
    }
    /**
     * 是否开启客户端鉴权
     */
    private boolean needClientAuth = true;
    /**
     * 是否使用ssl证书
     * 使用基础证书将由包内部自带的证书文件作为ssl通信签名证书
     */
    private boolean baseSsl;
    /**
     * ssl容器
     */
    protected SSLEngine sslEngine;
    /**
     * 日志
     */
    private Log log = LogFactory.getLog(BusServer.class);
    /**
     * 注入应用env接口
     */
    @Autowired
    protected IEnvironment env;

    @Override
    public IEnvironment getEnv() {
        return env;
    }

    /**
     * NioEventLoopGroup是用来处理IO操作的多线程事件循环器
     */
    private EventLoopGroup bossGroup;
    /**
     * NioEventLoopGroup是用来处理IO操作的多线程事件循环器
     */
    private EventLoopGroup workerGroup;
    private ServerBootstrap server;
    private ChannelFuture future;
    private BusStatus status = BusStatus.close;

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
     * 同步锁对象
     */
    private final Object root = new Object();
    /**
     *
     * @param pkStream
     * @param caStream
     */
    private void initStreamSsl(InputStream pkStream, InputStream caStream) {
        //
        if (this.env == null) {
            this.sslEngine = SubServerSslContextFactory.getServerContext(pkStream, caStream, SubConstant.Ssl.DEFAULT_SSL_PASSWORD, SubConstant.Ssl.DEFAULT_SSL_PROTOCOL).createSSLEngine();
            //设置为客户端鉴权模式
            this.sslEngine.setNeedClientAuth(this.needClientAuth);
        } else {
            if (this.env.containsKey(SubConstant.Ssl.SSL_PROTOCOL)) {
                this.sslEngine = SubServerSslContextFactory.getServerContext(pkStream, caStream, SubConstant.Ssl.DEFAULT_SSL_PASSWORD, this.env.getString(SubConstant.Ssl.SSL_PROTOCOL)).createSSLEngine();
            } else {
                this.sslEngine = SubServerSslContextFactory.getServerContext(pkStream, caStream, SubConstant.Ssl.DEFAULT_SSL_PASSWORD, SubConstant.Ssl.DEFAULT_SSL_PROTOCOL).createSSLEngine();
            }
            //设置为客户端鉴权模式
            if (this.env.containsKey(SubConstant.Ssl.SSL_NEED_CLIENT_AUTH)) {
                this.sslEngine.setNeedClientAuth(this.env.getBoolean(SubConstant.Ssl.SSL_NEED_CLIENT_AUTH));
            } else {
                this.sslEngine.setNeedClientAuth(this.needClientAuth);
            }
        }
        //设置不为客户端模式
        this.sslEngine.setUseClientMode(false);
    }
    /**
     *
     * @param bytes
     */
    private void initBytesSsl(byte[] bytes) throws IOException {
        //创建服务器端证书流
        try (InputStream pkStream = new ByteArrayInputStream(bytes)) {
            //创建ca证书流
            try (InputStream caStream = new ByteArrayInputStream(bytes)) {
                this.initStreamSsl(pkStream, caStream);
            }
        }
    }
    /**
     * 初始化基础证书
     */
    private void initBaseSsl() {
        try {
            //获取默认证书数据
            ResourceBytes resourceBytes = AssemblyUtil.getResourceBytes(this.getClass(), "server.jks");
            //创建服务器端证书流
            try (InputStream pkStream = new ByteArrayInputStream(resourceBytes.getBytes())) {
                //创建ca证书流
                try (InputStream caStream = new ByteArrayInputStream(resourceBytes.getBytes())) {
                    this.initStreamSsl(pkStream, caStream);
                }
            }
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
            if (!this.env.containsKey(SubConstant.Ssl.SSL_SERVER_DATA) && !this.env.containsKey(SubConstant.Ssl.SSL_SERVER_STREAM) && !this.env.containsKey(SubConstant.Ssl.SSL_SERVER_FILE)) {
                //没有在evn指定ssl证书
                if (this.baseSsl) {
                    //有指定使用默认ssl证书
                    this.initBaseSsl();
                }
            } else {
                //判断是否使用二进制证书
                if (this.env.containsKey(SubConstant.Ssl.SSL_SERVER_DATA)) {
                    this.initBytesSsl(this.env.get(SubConstant.Ssl.SSL_SERVER_DATA));
                    return;
                }
                //判断是否使用流证书
                if (this.env.containsKey(SubConstant.Ssl.SSL_SERVER_STREAM)) {
                    InputStream stream = this.env.get(SubConstant.Ssl.SSL_SERVER_STREAM);
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
        synchronized (this.root) {
            //判断启动状态
            if (this.status == BusStatus.start) {
                return;
            } else {
                this.status = BusStatus.start;
            }
            final StopWatch watch = new StopWatch();
            watch.start("start");
            this.log.info("启动 Bus Server");
            try {
                //初始化ssl证书
                this.initSsl();
                new Thread(new OneRunnable<BusServer>(this) {
                    @Override
                    public void run() {
                        try {
                            //
                            this.getA().bossGroup = new NioEventLoopGroup();  // 用来接收进来的连接
                            this.getA().workerGroup = new NioEventLoopGroup();// 用来处理已经被接收的连接
                            this.getA().server = new ServerBootstrap();//是一个启动NIO服务的辅助启动类
                            this.getA().server.group(this.getA().bossGroup, this.getA().workerGroup)
                                    .channel(NioServerSocketChannel.class)  // 这里告诉Channel如何接收新的连接
                                    .childHandler(new SubServerInitializer(this.getA()));
                            this.getA().server.childOption(ChannelOption.TCP_NODELAY, SubConstant.Nio.DEFAULT_TCP_NODELAY);
                            //option主要是针对boss线程组
                            this.getA().server.option(ChannelOption.SO_BACKLOG, SubConstant.Nio.DEFAULT_BACKLOG);
//                            this.getA().server.option(ChannelOption.SO_KEEPALIVE, SubConstant.Nio.DEFAULT_KEEPALIVE);
//                            this.getA().server.option(ChannelOption.SO_SNDBUF, SubConstant.Nio.DEFAULT_SNDBUF);
                            this.getA().server.option(ChannelOption.SO_RCVBUF, SubConstant.Nio.DEFAULT_RCVBUF);
                            //child主要是针对worker线程组
//                            this.getA().server.childOption(ChannelOption.SO_BACKLOG, SubConstant.Nio.DEFAULT_BACKLOG);
                            this.getA().server.childOption(ChannelOption.SO_KEEPALIVE, SubConstant.Nio.DEFAULT_KEEPALIVE);
                            this.getA().server.childOption(ChannelOption.SO_SNDBUF, SubConstant.Nio.DEFAULT_SNDBUF);
                            this.getA().server.childOption(ChannelOption.SO_RCVBUF, SubConstant.Nio.DEFAULT_RCVBUF);
                            this.getA().server.childOption(ChannelOption.ALLOW_HALF_CLOSURE, SubConstant.Nio.DEFAULT_ALLOW_HALF_CLOSURE);
                            if (this.getA().env == null) {
                                this.getA().future = this.getA().server.bind(SubConstant.Nio.DEFAULT_PORT).sync();// 绑定端口，开始接收进来的连接
                            } else {
                                if (this.getA().env.containsKey(SubConstant.Environment.NIO_ALLOW_HALF_CLOSURE)) {
                                    this.getA().server.childOption(ChannelOption.SO_SNDBUF, this.getA().env.getInt(SubConstant.Environment.NIO_ALLOW_HALF_CLOSURE));
                                }
                                if (this.getA().env.containsKey(SubConstant.Environment.NIO_SNDBUF)) {
                                    this.getA().server.option(ChannelOption.SO_SNDBUF, this.getA().env.getInt(SubConstant.Environment.NIO_SNDBUF));
                                    this.getA().server.childOption(ChannelOption.SO_SNDBUF, this.getA().env.getInt(SubConstant.Environment.NIO_SNDBUF));
                                }
                                if (this.getA().env.containsKey(SubConstant.Environment.NIO_RCVBUF)) {
                                    this.getA().server.option(ChannelOption.SO_RCVBUF, this.getA().env.getInt(SubConstant.Environment.NIO_RCVBUF));
                                    this.getA().server.childOption(ChannelOption.SO_RCVBUF, this.getA().env.getInt(SubConstant.Environment.NIO_RCVBUF));
                                }
                                if (this.getA().env.containsKey(SubConstant.Environment.NIO_BACKLOG)) {
                                    this.getA().server.option(ChannelOption.SO_BACKLOG, this.getA().env.getInt(SubConstant.Environment.NIO_BACKLOG));
                                    this.getA().server.childOption(ChannelOption.SO_BACKLOG, this.getA().env.getInt(SubConstant.Environment.NIO_BACKLOG));
                                }
                                if (this.getA().env.containsKey(SubConstant.Environment.NIO_KEEPALIVE)) {
                                    this.getA().server.option(ChannelOption.SO_KEEPALIVE, this.getA().env.getBoolean(SubConstant.Environment.NIO_KEEPALIVE));
                                    this.getA().server.childOption(ChannelOption.SO_KEEPALIVE, this.getA().env.getBoolean(SubConstant.Environment.NIO_KEEPALIVE));
                                }
                                //判断是否指定网卡
                                if (this.getA().env.containsKey(SubConstant.Environment.IP_ADDRESS)) {
                                    //指定网卡
                                    this.getA().future = server.bind(
                                            InetAddress.getByName(this.getA().env.getString(SubConstant.Environment.IP_ADDRESS)),
                                            this.getA().env.getInt(SubConstant.Environment.PORT)).sync();// 绑定端口，开始接收进来的连接
                                } else {
                                    //不指定网卡
                                    this.getA().future = server.bind(this.getA().env.getInt(SubConstant.Environment.PORT)).sync();// 绑定端口，开始接收进来的连接
                                }
                            }
                            this.getA().log.info("启动 Bus Server 完成");
                            //计数器结束
                            watch.stop();
                            this.getA().log.info(watch.prettyPrint());
                            // 监听服务器关闭监听
                            this.getA().future.channel().closeFuture().sync();
                        } catch (InterruptedException e) {
                            if (this.getA().log.isDebugEnabled()) {
                                e.printStackTrace();
                                this.getA().log.debug(e.getMessage(), e);
                            } else {
                                this.getA().log.error(e.getMessage(), e);
                            }
                        } catch (Exception e) {
                            this.getA().log.error("启动 Bus Server 错误！");
                            if (this.getA().log.isDebugEnabled()) {
                                e.printStackTrace();
                                this.getA().log.debug(e.getMessage(), e);
                            } else {
                                this.getA().log.error(e.getMessage(), e);
                            }
                        } finally {
                            this.getA().bossGroup.shutdownGracefully(); ////关闭EventLoopGroup，释放掉所有资源包括创建的线程
                            this.getA().workerGroup.shutdownGracefully();
                            //设置状态
                            synchronized (this.getA().root) {
                                this.getA().status = BusStatus.close;
                            }
                        }
                    }
                }).start();
            } catch (IOException e) {
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
     * 释放资源
     *
     * @throws Exception
     */
//    @Unloader
    @Override
    public void close() throws Exception {
        synchronized (this.root) {
            if (this.status == BusStatus.close) {
                return;
            }
        }
    }
}