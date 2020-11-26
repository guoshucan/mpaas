//package ghost.framework.bus.client.plugin;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.MessageToMessageDecoder;
//
//import java.util.List;
//
///**
// * package: ghost.framework.bus.client.plugin
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:消息事件解码器
// * @Date: 2020/6/13:9:32
// */
//final class BusClientToMessageDecoder extends MessageToMessageDecoder<ByteBuf> {
//    private BusClient busClient;
//
//    BusClientToMessageDecoder(BusClient busClient) {
//        this.busClient = busClient;
//    }
//
//    @Override
//    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf messageEvent, List<Object> list) throws Exception {
//
//    }
//}