//package ghost.framework.bus.client.plugin;
//
//import ghost.framework.bus.MessageEvent;
//import ghost.framework.serialization.SerializeUtils;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.MessageToMessageEncoder;
//
//import java.util.List;
//
///**
// * package: ghost.framework.bus.client.plugin
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/6/13:9:27
// */
//final class BusClientToMessageEncoder extends MessageToMessageEncoder<MessageEvent> {
//    private BusClient busClient;
//
//    BusClientToMessageEncoder(BusClient busClient) {
//        this.busClient = busClient;
//    }
//
//    @Override
//    protected void encode(ChannelHandlerContext channelHandlerContext, MessageEvent messageEvent, List<Object> list) throws Exception {
//        list.add(SerializeUtils.serialize(messageEvent));
//    }
//}