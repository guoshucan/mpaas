package ghost.framework.bus.client.plugin;

import ghost.framework.bus.ConnectionCompleteMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * package: ghost.framework.bus.client.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/13:3:05
 */
public class SubClientHandler  extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof IdleStateEvent){
            //心跳
        }
        if (msg instanceof ConnectionCompleteMessage) {
            ConnectionCompleteMessage msg1 = (ConnectionCompleteMessage) msg;
            System.out.println("服务器端返回的数据:" + msg1.getSource().toString());
        }
//        AttributeKey<String> key = AttributeKey.valueOf("ServerData");
//        ctx.channel().attr(key).set("客户端处理完毕");
        //把客户端的通道关闭
//        ctx.channel().close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println(evt.toString());
        super.userEventTriggered(ctx, evt);
    }
}
