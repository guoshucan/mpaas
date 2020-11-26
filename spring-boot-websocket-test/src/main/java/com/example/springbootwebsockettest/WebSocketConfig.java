package com.example.springbootwebsockettest;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ghost.framework.context.annotation.Bean;
import ghost.framework.stereotype.Component;
import ghost.framework.web.socket.server.standard.ServerEndpointExporter;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * package: com.example.springbootwebsockettest
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/2:21:35
 */
@Component
public class WebSocketConfig {
    private Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
    /**
     * ServerEndpointExporter 作用
     *
     * 这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
//    @Bean
//    public Thread newThread(){
//        Thread r = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://127.0.0.1:8081/websocketDemo/websocket"), new Draft_6455()) {
//                            @Override
//                            public void onOpen(ServerHandshake handshakedata) {
//                                log.info("[websocket] 连接成功");
//                                send("run [websocket] 连接成功:" + UUID.randomUUID().toString());
//                                this.close();
//                            }
//
//                            @Override
//                            public void onMessage(ByteBuffer bytes) {
//                                super.onMessage(bytes);
//                            }
//
//                            @Override
//                            public void onMessage(String message) {
//                                log.info("[websocket] 收到消息={}", message);
//
//                            }
//
//                            @Override
//                            public void onClose(int code, String reason, boolean remote) {
//                                log.info("[websocket] 退出连接");
//                            }
//
//                            @Override
//                            public void onError(Exception ex) {
//                                log.info("[websocket] 连接错误={}", ex.getMessage());
//                            }
//                        };
//                        webSocketClient.setTcpNoDelay(true);
//                        webSocketClient.setConnectionLostTimeout(0);
//                        webSocketClient.connect();
//                        Thread.sleep(500);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        r.start();
//        return r;
//    }
    @Bean
    public WebSocketClient webSocketClient() {
        try {
            WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://127.0.0.1:8081/websocketDemo/websocket"), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info("[websocket] 连接成功");
                    new Thread(new Runnable() {
                        //                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    String s = UUID.randomUUID().toString();
//                                    for (int i = 0; i < 200; i++) {
//                                        s += UUID.randomUUID().toString();
//                                    }
                                    System.out.println(s.length());
                                    send("a [websocket] 连接成功:" + UUID.randomUUID().toString() + "==");

                                    this.send(("[websocket] 连接成功:" + i).getBytes("UTF-8"));
                                    Thread.sleep(1000);
//                                    sendPing();
                                } catch (Exception e) {

                                }
                            }
                        }
                    }).start();
                }

                @Override
                public void onMessage(ByteBuffer bytes) {
                    super.onMessage(bytes);
                }

                @Override
                public void onMessage(String message) {
                    log.info("[websocket] 收到消息={}",message);

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("[websocket] 退出连接");
                }

                @Override
                public void onError(Exception ex) {
                    log.info("[websocket] 连接错误={}",ex.getMessage());
                }
            };
            webSocketClient.setTcpNoDelay(true);
            webSocketClient.setConnectionLostTimeout(0);
            webSocketClient.connect();
            return webSocketClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    @Bean
//    public WebSocketClient webSocketClient1() {
//        try {
//            WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://127.0.0.1:8081/websocketDemo/websocket"), new Draft_6455()) {
//                @Override
//                public void onOpen(ServerHandshake handshakedata) {
//                    log.info("[websocket] 连接成功");
////
//                }
//
//                @Override
//                public void onMessage(ByteBuffer bytes) {
//                    super.onMessage(bytes);
//                }
//
//                @Override
//                public void onMessage(String message) {
//                    log.info("[websocket] 收到消息={}",message);
//
//                }
//
//                @Override
//                public void onClose(int code, String reason, boolean remote) {
//                    log.info("[websocket] 退出连接");
//                }
//
//                @Override
//                public void onError(Exception ex) {
//                    log.info("[websocket] 连接错误={}",ex.getMessage());
//                }
//            };
//            webSocketClient.setConnectionLostTimeout(0);
//            webSocketClient.setTcpNoDelay(true);
//            webSocketClient.connect();
//            return webSocketClient;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
