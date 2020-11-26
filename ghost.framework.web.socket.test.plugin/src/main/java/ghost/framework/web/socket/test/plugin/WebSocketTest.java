package ghost.framework.web.socket.test.plugin;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.module.IModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * package: ghost.framework.web.socket.test.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/2:15:46
 */
@ServerEndpoint(
        value = "/websocketDemo/websocket",
        decoders = DecoderTest.class,
        encoders = EncoderTest.class)
public class WebSocketTest {
    @Autowired
    private IModule module;
    private static final Log log = LogFactory.getLog(WebSocketTest.class);

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    // concurrent包的线程安全Map，用来存放每个客户端对应的MyWebSocket对象。
    private static ConcurrentHashMap<String, WebSocketTest> webSocketMap = new ConcurrentHashMap<String, WebSocketTest>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String ip; // 客户端ip
    public static final String ACTION_PRINT_ORDER = "printOrder";
    public static final String ACTION_SHOW_PRINT_EXPORT = "showPrintExport";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) throws IOException, InterruptedException {
        this.session = session;
        webSocketMap.put(session.getId(), this);
        addOnlineCount(); // 在线数加1
//		System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        log.info("有新连接加入," + session.getId() + "！当前在线人数为:" + getOnlineCount());
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String s = "";
                        for (int i = 0; i < 300; i++) {
                            s += UUID.randomUUID().toString();
                        }
                        System.out.println(s.length());
                        session.getBasicRemote().sendText("ServerEndpoint [websocket] 连接成功:" + s + "==");

//                            this.send(("[websocket] 连接成功:" + i).getBytes("UTF-8"));
                        Thread.sleep(100);

                    } catch (Exception e) {

                    }
                }
            }
        }).start();*/
//        ExportService es = BeanUtils.getBean(ExportService.class);
//        List<String> list = es.listExportCodesByPrintIp(ip);
//        ResponseData<String> rd = new ResponseData<String>();
//        rd.setAction(MyWebSocket.ACTION_SHOW_PRINT_EXPORT);
//        rd.setList(list);
//        sendObject(rd);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session see) {
        webSocketMap.remove(session.getId()); // 从set中删除
        // Map<String, String> map = session.getPathParameters();
        // webSocketMap.remove(map.get("ip"));
        subOnlineCount(); // 在线数减1
        // System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        log.info("websocket关闭，" + session.getId() + "：{},当前在线人数为:" + getOnlineCount());
    }
    @OnMessage
    public void onMessage(ByteBuffer message, Session session) {
//		System.out.println("来自客户端的消息:" + message);
        log.debug("ByteBuffer websocket来自客户端的消息:" + new String(message.array()));
        System.out.println("ByteBuffer websocket来自客户端的消息:" + new String(message.array()));

//        OrderService os = BeanUtils.getBean(OrderService.class);
//        OrderVo ov = os.getOrderDetailByOrderNo(message);
////		System.out.println(ov);
//        ResponseData<OrderVo> rd = new ResponseData<OrderVo>();
//        ArrayList<OrderVo> list = new ArrayList<OrderVo>();
//        list.add(ov);
//        rd.setAction(MyWebSocket.ACTION_PRINT_ORDER);
//        rd.setList(list);
//        sendObject(rd);
//		log.info("推送打印信息完成，单号：{}", ov.getOrderNo());
    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException{
//		System.out.println("来自客户端的消息:" + message);
        log.debug("String websocket来自客户端的消息:" + message);
        System.out.println("String websocket来自客户端的消息:" + message);
        this.session.getBasicRemote().sendText(message);
//        OrderService os = BeanUtils.getBean(OrderService.class);
//        OrderVo ov = os.getOrderDetailByOrderNo(message);
////		System.out.println(ov);
//        ResponseData<OrderVo> rd = new ResponseData<OrderVo>();
//        ArrayList<OrderVo> list = new ArrayList<OrderVo>();
//        list.add(ov);
//        rd.setAction(MyWebSocket.ACTION_PRINT_ORDER);
//        rd.setList(list);
//        sendObject(rd);
//		log.info("推送打印信息完成，单号：{}", ov.getOrderNo());
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
//		System.out.println("发生错误");
        log.error("webSocket发生错误！：" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 像当前客户端发送消息
     *
     * @param message 字符串消息
     * @throws IOException
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
            // this.session.getAsyncRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("发送数据错误，" + session.getId() + ",msg：" + message);
        }
    }

//    /**
//     * 向当前客户端发送对象
//     *
//     * @param obj
//     *            所发送对象
//     * @throws IOException
//     */
//    public void sendObject(Object obj) {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(Include.NON_NULL);
//        String s = null;
//        try {
//            s = mapper.writeValueAsString(obj);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            log.error("转json错误！{}", obj);
//        }
//        this.sendMessage(s);
//    }

//    /**
//     * 群发自定义消息
//     */
//    public static void sendInfo(String message) {
//        for (Entry<String, MyWebSocket> entry : webSocketMap.entrySet()) {
//            MyWebSocket value = entry.getValue();
//            value.sendMessage(message);
//        }
//    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketTest.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketTest.onlineCount--;
    }

    public static ConcurrentHashMap<String, WebSocketTest> getWebSocketMap() {
        return webSocketMap;
    }
}
