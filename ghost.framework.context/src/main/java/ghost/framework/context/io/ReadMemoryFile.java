package ghost.framework.context.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:读取内存文件
 * @Date: 15:15 2019-01-26
 */
public final class ReadMemoryFile implements AutoCloseable {
    private static Log logger = LogFactory.getLog(ReadMemoryFile.class);
    /**
     * 读取内存文件初始化。
     *
     * @param path 文件路径。
     * @throws IOException
     */
    public ReadMemoryFile(String path) throws IOException {
        this.accessFile = new RandomAccessFile(path, "r");
        this.channel = this.accessFile.getChannel();
        this.buffer = ByteBuffer.allocate((int) this.channel.size());
        this.channel.read(this.buffer);
        //this.buffer = this.channel.map(FileChannel.MapMode.READ_ONLY, 0, this.channel.size());
        if (logger.isDebugEnabled()) {
            logger.debug("size:" + String.valueOf(this.channel.size()));
        }
    }

    private RandomAccessFile accessFile;
    private FileChannel channel;

    public FileChannel getChannel() {
        return channel;
    }

    public RandomAccessFile getAccessFile() {
        return accessFile;
    }

    private ByteBuffer buffer;

    //    private MappedByteBuffer buffer;
//    public MappedByteBuffer getBuffer() {
//        return buffer;
//    }

    /**
     * 获取文件数据
     * @return
     */
    public byte[] getBuffer() {
        return this.buffer.array();
    }

    /**
     * 关闭资源。
     *
     * @throws Exception
     */
    @Override
    public synchronized void close() throws Exception {
        //清除数据。
        if (this.buffer != null) {
            this.buffer.clear();
        }
        //关闭资源。
        if (this.channel != null) {
            this.channel.close();
        }
        //关闭资源。
        if (this.accessFile != null) {
            this.accessFile.close();
        }
    }
}