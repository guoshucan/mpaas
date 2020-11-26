package ghost.framework.web.socket.plugin.server;

import ghost.framework.web.socket.plugin.WsSession;
import ghost.framework.web.socket.plugin.net.SocketBufferHandler;
import ghost.framework.web.socket.plugin.net.SocketWrapperBase;
import ghost.framework.web.socket.plugin.net.WriteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.WebConnection;
import javax.websocket.Endpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.CompletionHandler;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * package: ghost.framework.web.socket.plugin.server
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:ws包装流操作类
 * Servlet 3.1
 * @Date: 2020/5/3:12:40
 */
public final class WsSocketWrapper implements WebConnection, WriteListener {
    private static Logger log = LoggerFactory.getLogger(WsSocketWrapper.class);
    private WebConnection connection;
    private WsServerContainer serverContainer;
    /**
     * 创建ws终结点实例对象
     */
    private Endpoint endpoint;
    public Endpoint getEndpoint() {
        return endpoint;
    }

    /**
     * 数据包头长度
     */
//    private volatile int headerLength = 0;
    /*
     * Used if block/non-blocking is set at the socket level. The client is
     * responsible for the thread-safe use of this field via the locks provided.
     */
    private volatile boolean blockingStatus = true;
    public boolean getBlockingStatus() { return blockingStatus; }
    public void setBlockingStatus(boolean blockingStatus) {
        this.blockingStatus = blockingStatus;
    }
    private final Lock blockingStatusReadLock;
    private final ReentrantReadWriteLock.WriteLock blockingStatusWriteLock;
    private final WritableByteChannel byteChannel;

    public WsSocketWrapper(WsServerContainer serverContainer, WebConnection connection, Endpoint endpoint) throws IOException {

        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        this.blockingStatusReadLock = lock.readLock();
        this.blockingStatusWriteLock = lock.writeLock();

        this.serverContainer = serverContainer;
        this.connection = connection;
        this.endpoint = endpoint;
        //注册本身为流读取监听对象
//        this.connection.getInputStream().setReadListener(this);
        this.connection.getOutputStream().setWriteListener(this);
        //初始化写入接口
        this.byteChannel = Channels.newChannel(this.connection.getOutputStream());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return this.connection.getInputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this.connection.getOutputStream();
    }

    @Override
    public void close() throws Exception {
        this.connection = null;
    }

    /**
     * 写入事件
     * @throws IOException
     */
    @Override
    public void onWritePossible() throws IOException {
        System.out.println("onWritePossible");
    }
    /**
     * 读取流错误回调
     * @param t
     */
    @Override
    public void onError(Throwable t) {
        //引发错误处理
        this.endpoint.onError(this.session, t);
    }

    private WsSession session;

    public void setSession(WsSession session) {
        this.session = session;
    }

    /**
     * 验证是否异步IO操作
     * @return
     */
    public boolean hasAsyncIO() {
        return false;
    }

    public final <A> SocketWrapperBase.CompletionState write(SocketWrapperBase.BlockingMode block, long timeout,
                                                             TimeUnit unit, A attachment, SocketWrapperBase.CompletionCheck check,
                                                             CompletionHandler<Long, ? super A> handler, ByteBuffer... srcs) {
        if (srcs == null) {
            throw new IllegalArgumentException();
        }
        return null;
    }
    /**
     * Writes the provided data to the socket write buffer. If the socket write
     * buffer fills during the write, the content of the socket write buffer is
     * written to the network and this method starts to fill the socket write
     * buffer again. Depending on the size of the data to write, there may be
     * multiple writes to the network.
     * <p>
     * Non-blocking writes must return immediately and the ByteBuffer holding
     * the data to be written must be immediately available for re-use. It may
     * not be possible to write sufficient data to the network to allow this to
     * happen. In this case data that cannot be written to the network and
     * cannot be held by the socket buffer is stored in the non-blocking write
     * buffer.
     * <p>
     * Note: There is an implementation assumption that, before switching from
     *       non-blocking writes to blocking writes, any data remaining in the
     *       non-blocking write buffer will have been written to the network.
     *
     * @param block  <code>true</code> if a blocking write should be used,
     *               otherwise a non-blocking write will be used
     * @param from   The ByteBuffer containing the data to be written
     *
     * @throws IOException If an IO error occurs during the write
     */
    public final void write(boolean block, ByteBuffer from) throws IOException {
        if (from == null || from.remaining() == 0) {
            return;
        }
        /*
         * While the implementations for blocking and non-blocking writes are
         * very similar they have been split into separate methods:
         * - To allow sub-classes to override them individually. NIO2, for
         *   example, overrides the non-blocking write but not the blocking
         *   write.
         * - To enable a marginally more efficient implemented for blocking
         *   writes which do not require the additional checks related to the
         *   use of the non-blocking write buffer
         */
//        if (from.position() == 2) {
//            from.flip();
//            return;
//        }
//        from.flip();
//        System.out.println("write:" + this.byteChannel.write(from));
        if (block) {
            writeBlocking(from);
        } else {
            writeNonBlocking(from);
        }
    }
    /**
     * Writes the provided data to the socket write buffer. If the socket write
     * buffer fills during the write, the content of the socket write buffer is
     * written to the network using a blocking write. Once that blocking write
     * is complete, this method starts to fill the socket write buffer again.
     * Depending on the size of the data to write, there may be multiple writes
     * to the network. On completion of this method there will always be space
     * remaining in the socket write buffer.
     *
     * @param from The ByteBuffer containing the data to be written
     *
     * @throws IOException If an IO error occurs during the write
     */
    protected void writeBlocking(ByteBuffer from) throws IOException {
        if (from.hasRemaining()) {
            socketBufferHandler.configureWriteBufferForWrite();
            transfer(from, socketBufferHandler.getWriteBuffer());
            if(this.writeBlockingCount != 2) {
                this.writeBlockingCount++;
            }
            if(this.writeBlockingCount == 2){
                doWrite(true);
                this.writeBlockingCount = 0;
            }
            while (from.hasRemaining()) {
                doWrite(true);
                socketBufferHandler.configureWriteBufferForWrite();
                transfer(from, socketBufferHandler.getWriteBuffer());
            }
        }
    }
    private volatile int writeBlockingCount = 0;
    /**
     * Write the contents of the socketWriteBuffer to the socket. For blocking
     * writes either then entire contents of the buffer will be written or an
     * IOException will be thrown. Partial blocking writes will not occur.
     *
     * @param block Should the write be blocking or not?
     *
     * @throws IOException If an I/O error such as a timeout occurs during the
     *                     write
     */
    protected void doWrite(boolean block) throws IOException {
        socketBufferHandler.configureWriteBufferForRead();
        doWrite(block, socketBufferHandler.getWriteBuffer());
    }


    protected void doWrite(boolean block, ByteBuffer from) throws IOException {
        blockingStatusReadLock.lock();
        try {
            if (blockingStatus == block) {
//                if (block) {
//                    Socket.timeoutSet(getSocket().longValue(), getWriteTimeout() * 1000);
//                }
                doWriteInternal(from);
                return;
            }
        } finally {
            blockingStatusReadLock.unlock();
        }
        blockingStatusWriteLock.lock();
        try {
            // Set the current settings for this socket
            setBlockingStatus(block);
//            if (block) {
//                Socket.timeoutSet(getSocket().longValue(), getWriteTimeout() * 1000);
//            } else {
//                Socket.timeoutSet(getSocket().longValue(), 0);
//            }
            // Downgrade the lock
            blockingStatusReadLock.lock();
            try {
                blockingStatusWriteLock.unlock();
                doWriteInternal(from);
            } finally {
                blockingStatusReadLock.unlock();
            }
        } finally {
            // Should have been released above but may not have been on some
            // exception paths
            if (blockingStatusWriteLock.isHeldByCurrentThread()) {
                blockingStatusWriteLock.unlock();
            }
        }
    }

    /**
     * 发送数据
     * @param from
     * @throws IOException
     */
    private void doWriteInternal(ByteBuffer from) throws IOException {
        byte[] bytes = new byte[from.limit()];
        from.position(0);
        from.get(bytes);
        this.connection.getOutputStream().write(bytes);
    }
    // --------------------------------------------------------- Utility methods
    protected static int transfer(byte[] from, int offset, int length, ByteBuffer to) {
        int max = Math.min(length, to.remaining());
        if (max > 0) {
            to.put(from, offset, max);
        }
        return max;
    }

    protected static int transfer(ByteBuffer from, ByteBuffer to) {
        int max = Math.min(from.remaining(), to.remaining());
        if (max > 0) {
            int fromLimit = from.limit();
            from.limit(from.position() + max);
            to.put(from);
            from.limit(fromLimit);
        }
        return max;
    }
    /**
     * The buffers used for communicating with the socket.
     */
    protected volatile SocketBufferHandler socketBufferHandler = new SocketBufferHandler(6 * 1500, 6 * 1500, true);
    /**
     * Transfers the data to the socket write buffer (writing that data to the
     * socket if the buffer fills up using a non-blocking write) until either
     * all the data has been transferred and space remains in the socket write
     * buffer or a non-blocking write leaves data in the socket write buffer.
     * After an incomplete write, any data remaining to be transferred to the
     * socket write buffer will be copied to the socket write buffer. If the
     * remaining data is too big for the socket write buffer, the socket write
     * buffer will be filled and the additional data written to the non-blocking
     * write buffer.
     *
     * @param buf   The byte array containing the data to be written
     * @param off   The offset within the byte array of the data to be written
     * @param len   The length of the data to be written
     *
     * @throws IOException If an IO error occurs during the write
     */
    protected void writeNonBlocking(byte[] buf, int off, int len) throws IOException {
        if (len > 0 && nonBlockingWriteBuffer.isEmpty()
                && socketBufferHandler.isWriteBufferWritable()) {
            socketBufferHandler.configureWriteBufferForWrite();
            int thisTime = transfer(buf, off, len, socketBufferHandler.getWriteBuffer());
            len -= thisTime;
            while (len > 0) {
                off = off + thisTime;
                doWrite(false);
                if (len > 0 && socketBufferHandler.isWriteBufferWritable()) {
                    socketBufferHandler.configureWriteBufferForWrite();
                    thisTime = transfer(buf, off, len, socketBufferHandler.getWriteBuffer());
                } else {
                    // Didn't write any data in the last non-blocking write.
                    // Therefore the write buffer will still be full. Nothing
                    // else to do here. Exit the loop.
                    break;
                }
                len -= thisTime;
            }
        }

        if (len > 0) {
            // Remaining data must be buffered
            nonBlockingWriteBuffer.add(buf, off, len);
        }
    }
    /**
     * Transfers the data to the socket write buffer (writing that data to the
     * socket if the buffer fills up using a non-blocking write) until either
     * all the data has been transferred and space remains in the socket write
     * buffer or a non-blocking write leaves data in the socket write buffer.
     * After an incomplete write, any data remaining to be transferred to the
     * socket write buffer will be copied to the socket write buffer. If the
     * remaining data is too big for the socket write buffer, the socket write
     * buffer will be filled and the additional data written to the non-blocking
     * write buffer.
     *
     * @param from The ByteBuffer containing the data to be written
     *
     * @throws IOException If an IO error occurs during the write
     */
    protected void writeNonBlocking(ByteBuffer from)
            throws IOException {

        if (from.hasRemaining() && nonBlockingWriteBuffer.isEmpty()
                && socketBufferHandler.isWriteBufferWritable()) {
            writeNonBlockingInternal(from);
        }

        if (from.hasRemaining()) {
            // Remaining data must be buffered
            nonBlockingWriteBuffer.add(from);
        }
    }
    /**
     * Separate method so it can be re-used by the socket write buffer to write
     * data to the network
     *
     * @param from The ByteBuffer containing the data to be written
     *
     * @throws IOException If an IO error occurs during the write
     */
    protected void writeNonBlockingInternal(ByteBuffer from) throws IOException {
        socketBufferHandler.configureWriteBufferForWrite();
        transfer(from, socketBufferHandler.getWriteBuffer());
        while (from.hasRemaining()) {
            doWrite(false);
            if (socketBufferHandler.isWriteBufferWritable()) {
                socketBufferHandler.configureWriteBufferForWrite();
                transfer(from, socketBufferHandler.getWriteBuffer());
            } else {
                break;
            }
        }
    }
    /**
     * The max size of the individual buffered write buffers
     */
    protected int bufferedWriteSize = 64 * 1024; // 64k default write buffer

    /**
     * Additional buffer used for non-blocking writes. Non-blocking writes need
     * to return immediately even if the data cannot be written immediately but
     * the socket buffer may not be big enough to hold all of the unwritten
     * data. This structure provides an additional buffer to hold the data until
     * it can be written.
     * Not that while the Servlet API only allows one non-blocking write at a
     * time, due to buffering and the possible need to write HTTP headers, this
     * layer may see multiple writes.
     */
    protected final WriteBuffer nonBlockingWriteBuffer = new WriteBuffer(bufferedWriteSize);
    public void setWriteTimeout(long timeout) {

    }

    public void flush(boolean f) {
    }
    public boolean canWrite() {
        if (socketBufferHandler == null) {
            throw new IllegalStateException(UpgradeUtil.getLocalContainer().getString("socket.closed"));
        }
        return socketBufferHandler.isWriteBufferWritable() && nonBlockingWriteBuffer.isEmpty();
    }
    public boolean isReadyForWrite() {
        boolean result = canWrite();
        if (!result) {
            registerWriteInterest();
        }
        return result;
    }
    public  void registerWriteInterest(){

    }

    public void execute(Runnable r) {
    }
}
