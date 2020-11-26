package ghost.framework.bus;

/**
 * package: ghost.framework.bus
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:总线常量
 * @Date: 2020/6/12:22:52
 */
public final class SubConstant {
    /**
     * nio常量
     */
    public static class Nio {
        /**
         * 默认读取空闲时间
         * 单位秒
         */
        public static final int DEFAULT_READER_IDLE_TIME = 10;
        /**
         * 默认写入空闲时间
         * 单位秒
         */
        public static final int DEFAULT_WRITER_IDLE_TIME = 10;
        /**
         * 默认全部空闲时间
         * 单位秒
         */
        public static final int DEFAULT_ALL_IDLE_TIME = 20;
        /**
         * 默认端口
         */
        public static final int DEFAULT_PORT = 8098;
        /**
         * 默认IP地址
         */
        public static final String DEFAULT_IP_ADDRESS = "localhost";
        /**
         * 默认等待列队长度
         * ChannelOption.SO_BACKLOG对应的是tcp/ip协议listen函数中的backlog参数，函数listen(int socketfd,int backlog)用来初始化服务端可连接队列，
         * 服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
         */
        public static final int DEFAULT_BACKLOG = 128;
        /**
         * 默认在线状态，默认为true
         * Channeloption.SO_KEEPALIVE参数对应于套接字选项中的SO_KEEPALIVE，该参数用于设置TCP连接，当设置该选项以后，连接会测试链接的状态，这个选项用于可能长时间没有数据交流的
         * 连接。当设置该选项以后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文。
         */
        public static final boolean DEFAULT_KEEPALIVE = true;
        /**
         * 默认发送缓冲大小
         * 单位字节
         */
        public static final int DEFAULT_SNDBUF = 32 * 1024;
        /**
         * 默认接收缓冲大小
         * 单位字节
         * ChannelOption.SO_SNDBUF参数对应于套接字选项中的SO_SNDBUF，ChannelOption.SO_RCVBUF参数对应于套接字选项中的SO_RCVBUF这两个参数用于操作接收缓冲区和发送缓冲区
         * 的大小，接收缓冲区用于保存网络协议站内收到的数据，直到应用程序读取成功，发送缓冲区用于保存发送数据，直到发送成功。
         */
        public static final int DEFAULT_RCVBUF = 32 * 1024;
        /**
         * Netty参数，一个连接的远端关闭时本地端是否关闭，默认值为False。值为False时，连接自动关闭；为True时，触发ChannelInboundHandler的userEventTriggered()方法，事件为ChannelInputShutdownEvent。
         */
        public static final boolean DEFAULT_ALLOW_HALF_CLOSURE = false;
        /**
         * 默认是否允许重复使用本地地址和端口
         */
        public static final boolean DEFAULT_REUSEADDR = false;
        /**
         * ChannelOption.TCP_NODELAY参数对应于套接字选项中的TCP_NODELAY,该参数的使用与Nagle算法有关
         * Nagle算法是将小的数据包组装为更大的帧然后进行发送，而不是输入一次发送一次,因此在数据包不足的时候会等待其他数据的到了，组装成大的数据包进行发送，虽然该方式有效提高网络的有效
         * 负载，但是却造成了延时，而该参数的作用就是禁止使用Nagle算法，使用于小数据即时传输，于TCP_NODELAY相对应的是TCP_CORK，该选项是需要等到发送的数据量最大的时候，一次性发送
         * 数据，适用于文件传输。
         */
        public static final boolean DEFAULT_TCP_NODELAY = true;
        /**
         * ChannelOption.SO_LINGER参数对应于套接字选项中的SO_LINGER,Linux内核默认的处理方式是当用户调用close（）方法的时候，函数返回，在可能的情况下，尽量发送数据，不一定保证
         * 会发生剩余的数据，造成了数据的不确定性，使用SO_LINGER可以阻塞close()的调用时间，直到数据完全发送
         */
        public static final boolean DEFAULT_LINGER = true;
    }

    /**
     * ssl常量
     */
    public static class Ssl {
        /**
         * 客户端鉴权
         */
        public static final String SSL_NEED_CLIENT_AUTH = "ghost.framework.bus.ssl.need.client.auth";
        /**
         * 默认ssl协议
         * 在未指定 {@link Ssl#SSL_PROTOCOL} 的使用使用此协议
         */
        public static final String DEFAULT_SSL_PROTOCOL = "TLS";
        /**
         * ssl协议
         */
        public static final String SSL_PROTOCOL = "ghost.framework.bus.ssl.protocol";
        /**
         * ssl默认证书密码
         */
        public static final String DEFAULT_SSL_PASSWORD = "a5f60909-047b-40ea-b58c-f6ac303aff8f";
        /**
         * 总线客户端ssl证书数据
         * {@link byte[]}
         */
        public static final String SSL_CLIENT_DATA = "ghost.framework.bus.ssl.client.data";
        /**
         * 总线客户端ssl证书数据
         * {@link java.io.File}
         */
        public static final String SSL_CLIENT_FILE = "ghost.framework.bus.ssl.client.file";
        /**
         * 总线客户端ssl证书数据
         * {@link java.io.InputStream}
         */
        public static final String SSL_CLIENT_STREAM = "ghost.framework.bus.ssl.client.stream";
        /**
         * 总线服务器端ssl证书数据
         * {@link byte[]}
         */
        public static final String SSL_SERVER_DATA = "ghost.framework.bus.ssl.server.data";
        /**
         * 总线服务器端ssl证书数据
         * {@link java.io.File}
         */
        public static final String SSL_SERVER_FILE = "ghost.framework.bus.ssl.server.file";
        /**
         * 总线服务器端ssl证书数据
         * {@link java.io.InputStream}
         */
        public static final String SSL_SERVER_STREAM = "ghost.framework.bus.ssl.server.stream";
    }

    /**
     * env常量
     */
    public static class Environment {
        /**
         * Netty参数，一个连接的远端关闭时本地端是否关闭，默认值为False。值为False时，连接自动关闭；为True时，触发ChannelInboundHandler的userEventTriggered()方法，事件为ChannelInputShutdownEvent。
         */
        public static final String NIO_ALLOW_HALF_CLOSURE = "ghost.framework.bus.nio.allow.half.closure";
        /**
         * 发送缓冲大小
         * 单位字节
         */
        public static final String NIO_SNDBUF = "ghost.framework.bus.nio.sndbuf";
        /**
         * 接收缓冲大小
         * 单位字节
         */
        public static final String NIO_RCVBUF = "ghost.framework.bus.nio.rcvbuf";
        /**
         * 默认读取空闲时间键
         * 单位秒
         */
        public static final String NIO_READER_IDLE_TIME = "ghost.framework.bus.nio.reader.idle.time";
        /**
         * 默认写入空闲时间键
         * 单位秒
         */
        public static final String NIO_WRITER_IDLE_TIME = "ghost.framework.bus.nio.writer.idle.time";
        /**
         * 默认全部空闲时间键
         * 单位秒
         */
        public static final String NIO_ALL_IDLE_TIME = "ghost.framework.bus.nio.all.idle.time";
        /**
         * 总线端口
         */
        public static final String PORT = "ghost.framework.bus.port";
        /**
         * 指定网卡
         * 如果有值侧指定某个网卡监听
         */
        public static final String IP_ADDRESS = "ghost.framework.bus.ip.address";
        /**
         * 总线等待列队长度
         */
        public static final String NIO_BACKLOG = "ghost.framework.bus.nio.backlog";
        /**
         * 保持连接
         */
        public static final String NIO_KEEPALIVE = "ghost.framework.bus.nio.keepalive";
    }
}