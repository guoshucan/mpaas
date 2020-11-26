//package ghost.framework.web.socket.plugin.server;
//
//import ghost.framework.web.socket.plugin.Transformation;
//import ghost.framework.web.socket.plugin.WsSession;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import javax.servlet.http.WebConnection;
//import javax.websocket.RemoteEndpoint;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.util.concurrent.atomic.AtomicBoolean;
//
///**
// * package: ghost.framework.web.socket.plugin.server
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * RFC6455标准
// * RFC7692标准对permessage-deflate扩展，permessage-deflate在微软浏览器无用
// * @Date: 2020/9/2:16:31
// */
//public class WsRemoteEndpointServer implements RemoteEndpoint, AutoCloseable {
//    private final Log log = LogFactory.getLog(WsRemoteEndpointServer.class);
//    /**
//     * 写入超时
//     */
//    private final WsWriteTimeout writeTimeout;
//    private final WebConnection connection;
//    public WsRemoteEndpointServer(WebConnection connection, WsWriteTimeout writeTimeout){
//        this.connection = connection;
//        this.writeTimeout = writeTimeout;
//    }
//    /**
//     * 批处理原子布尔值
//     */
//    private final AtomicBoolean batchingAllowed = new AtomicBoolean(false);
//
//    @Override
//    public void setBatchingAllowed(boolean allowed) throws IOException {
//        boolean oldValue = this.batchingAllowed.getAndSet(allowed);
//        if (oldValue && !allowed) {
//            flushBatch();
//        }
//    }
//
//    @Override
//    public boolean getBatchingAllowed() {
//        return batchingAllowed.get();
//    }
//
//    @Override
//    public void flushBatch() throws IOException {
//
//    }
//
//    @Override
//    public void sendPing(ByteBuffer applicationData) throws IOException, IllegalArgumentException {
//
//    }
//
//    @Override
//    public void sendPong(ByteBuffer applicationData) throws IOException, IllegalArgumentException {
//
//    }
//
//    public void setSession(WsSession session) {
//        this.session = session;
//    }
//    private WsSession session;
//
//    public void setTransformation(Transformation transformation) {
//        this.transformation = transformation;
//    }
//    private Transformation transformation;
//    public long getSendTimeout() {
//        return sendTimeout;
//    }
//    public void setSendTimeout(long timeout) {
//        this.sendTimeout = timeout;
//    }
//    private long sendTimeout;
//
//    @Override
//    public void close() throws Exception {
//
//    }
//}
