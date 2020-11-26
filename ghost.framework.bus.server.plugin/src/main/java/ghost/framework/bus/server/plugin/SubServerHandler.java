package ghost.framework.bus.server.plugin;

import ghost.framework.bus.ConnectionCompleteMessage;
import ghost.framework.bus.IBusServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
/**
 * package: ghost.framework.bus
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理接收数据
 * @Date: 2020/6/12:20:34
 */
final class SubServerHandler extends ChannelInboundHandlerAdapter {
    private IBusServer busServer;

    public SubServerHandler(IBusServer busServer) {
        this.busServer = busServer;
    }

    private Log log = LogFactory.getLog(SubServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            super.channelRead(ctx, msg);
//            ByteBuf in = (ByteBuf) msg;
//            int readableBytes = in.readableBytes();
//            byte[] bytes = new byte[readableBytes];
//            in.readBytes(bytes);
//            System.out.println(new String(bytes));
            //System.out.print(in.toString(CharsetUtil.UTF_8));
            log.error("服务端接受的消息 : " + msg.toString());
        } catch (Exception e) {
            // 抛弃收到的数据
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println(evt.toString());
        super.userEventTriggered(ctx, evt);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
        log.debug("连接的客户端地址:" + ctx.channel().remoteAddress());
        log.debug("连接的客户端ID:" + ctx.channel().id());
        ctx.writeAndFlush(new ConnectionCompleteMessage("client" + InetAddress.getLocalHost().getHostName() + "success connected！ \n"));
//        System.out.println("connection");
        //StaticVar.ctxList.add(ctx);
        //StaticVar.chc = ctx;
        super.channelActive(ctx);
    }
}