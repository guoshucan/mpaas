package ghost.framework.web.angular1x.ssh.plugin.ws;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.converter.json.JsonConverterContainer;
import ghost.framework.context.converter.json.JsonToObjectConverter;
import ghost.framework.context.module.IModule;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.web.angular1x.ssh.plugin.entity.SshServerEntity;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * package: ghost.framework.web.angular1x.ssh.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/30:19:02
 */
@ServerEndpoint(
        value = "/ws/ssh/plugin",
        decoders = SshSocketDecoder.class,
        encoders = SshSocketEncoder.class)
public class SshSocket {
    @Application
    @Autowired
    private JsonConverterContainer converterContainer;
    private static JsonToObjectConverter toObjectConverter;
    /**
     * 注入会话工厂
     */
    @Autowired
    @Application
    protected ISessionFactory sessionFactory;
    @Autowired
    private IModule module;
    private static final Logger log = LoggerFactory.getLogger(SshSocket.class);
    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    /**
     * 发送指令：连接
     */
    private static final String OPERATE_CONNECT = "connect";
    /**
     * 发送指令：命令
     */
    private static final String OPERATE_COMMAND = "command";
    //线程池
    private ExecutorService executorService = Executors.newCachedThreadPool();
    //存放ssh连接信息
    private SshClientConnectionInfo connectionInfo;
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session see) {
        if (toObjectConverter == null) {
            toObjectConverter = this.converterContainer.getConverter(JsonToObjectConverter.class);
        }
        see.setMaxIdleTimeout(Integer.MAX_VALUE);
        this.connectionInfo = new SshClientConnectionInfo(this, see);
        onlineCount++; // 在线数加1
        if (log.isDebugEnabled()) {
            log.debug("有新连接加入:" + see.getId() + "->当前在线人数为:" + onlineCount);
        }
        if (log.isInfoEnabled()) {
            log.info("有新连接加入:" + see.getId() + "->当前在线人数为:" + onlineCount);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session see) {
//        sshMap.remove(see.getId()); // 从set中删除
        onlineCount--; // 在线数减1
    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session see) throws IOException, SQLException {
        System.out.println(message);
        SshClientData clientData = toObjectConverter.toObject(message, SshClientData.class);
        //连接
        if (clientData.getOperate().equals(OPERATE_CONNECT)) {
            //获取连接信息
            DetachedCriteria criteria = DetachedCriteria.forClass(SshServerEntity.class);
            criteria.createAlias("typeEntity", "t");
            criteria.createAlias("accountEntity", "a");
            criteria.add(Restrictions.eq("id", clientData.getId()));
            Map<String, String> map = this.sessionFactory.map(criteria,
                    new String[]{"id", "name",
                            "a.name as accountName", "a.password as accountPassword",
                            "t.name as typeName", "t.version as version",
                            "hostName", "port", "status", "timeout", "channelTimeout", "remoteDirectory"});
            clientData.setHost(map.get("hostName"));
            clientData.setPort(Integer.parseInt(String.valueOf(map.get("port"))));
            clientData.setPassword(map.get("accountPassword"));
            clientData.setUsername(map.get("accountName"));
            clientData.setTimeout(Integer.parseInt(String.valueOf(map.get("timeout"))));
            clientData.setChannelTimeout(Integer.parseInt(String.valueOf(map.get("channelTimeout"))));
            clientData.setType(map.get("typeName"));
            clientData.setVersion(map.get("version"));
            //
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        connectToSSH(connectionInfo, clientData);
                    } catch (JSchException | IOException e) {
                        log.error("webssh连接异常");
                        log.error("异常信息:{}", e.getMessage());
                        onClose(see);
                    }
                }
            });
        } else if (clientData.getOperate().equals(OPERATE_COMMAND)) {
            String command = clientData.getCommand();
            if (connectionInfo != null) {
                try {
                    transToSSH(connectionInfo.getChannel(), command);
                } catch (IOException e) {
                    log.error("webssh连接异常");
                    log.error("异常信息:{}", e.getMessage());
                    onClose(see);
                }
            }
        } else {
            log.error("不支持的操作");
            onClose(see);
        }
        if (log.isDebugEnabled()) {
            log.debug("接收消息:" + see.getId() + "->内容:" + onlineCount);
        }
    }

    private void connectToSSH(SshClientConnectionInfo connectionInfo, SshClientData clientData) throws JSchException, IOException {
        com.jcraft.jsch.Session jschSession;
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        //获取jsch的会话
        connectionInfo.setjSch(new JSch());
        jschSession = connectionInfo.getjSch().getSession(clientData.getUsername(), clientData.getHost(), clientData.getPort());
        jschSession.setConfig(config);
        //设置密码
        jschSession.setPassword(clientData.getPassword());
        //连接超时时间30s
        jschSession.connect(clientData.getTimeout());
        //开启shell通道
        connectionInfo.setChannel(jschSession.openChannel("shell"));
        //通道连接 超时时间3s
        connectionInfo.getChannel().connect(clientData.getChannelTimeout());
        //转发消息
//        connectionInfo.getSession().getBasicRemote().sendText("\r" + clientData.getType() + ":" + clientData.getVersion() + "\r");
//        transToSSH(connectionInfo.getChannel(), "\r");
        //读取终端返回的信息流
        InputStream inputStream = connectionInfo.getChannel().getInputStream();
        try {
            //循环读取
            byte[] buffer = new byte[1024];
            int i;
            //如果没有数据来，线程会一直阻塞在这个地方等待数据。
            while ((i = inputStream.read(buffer)) != -1) {
                sendMessage(connectionInfo.getSession(), Arrays.copyOfRange(buffer, 0, i));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            //断开连接后关闭会话
            jschSession.disconnect();
            connectionInfo.getChannel().disconnect();
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * @Description: 将消息转发到终端
     * @Param: [channel, data]
     * @return: void
     * @Author: NoCortY
     * @Date: 2020/3/7
     */
    private void transToSSH(Channel channel, String command) throws IOException {
        if (channel != null) {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session sess, Throwable error) {
        log.error("发生错误！：" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 给当前客户端发送消息
     *
     * @param message 字符串消息
     * @throws IOException
     */
    private void sendMessage(Session sess, byte[] message) {
        try {
            sess.getBasicRemote().sendText(new String(message, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("发送数据错误，" + sess.getId() + ",msg：" + message);
        }
    }
}
