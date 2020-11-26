/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package ghost.framework.web.socket.plugin.pojo;

import ghost.framework.web.socket.plugin.server.UpgradeUtil;
import ghost.framework.web.socket.plugin.util.ExceptionUtils;
import ghost.framework.web.socket.plugin.WsSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

/**
 * Base implementation (client and server have different concrete
 * implementations) of the wrapper that converts a POJO instance into a
 * WebSocket endpoint instance.
 */
public abstract class PojoEndpointBase extends Endpoint {
    //日志
    private final Log log = LogFactory.getLog(PojoEndpointBase.class); // must not be static
    /**
     * 终结点实例对象
     */
    private Object pojo;
    /**
     * url路径地址参数
     */
    private Map<String,String> pathParameters;
    /**
     * 终结点对象函数地图
     */
    private PojoMethodMapping methodMapping;

    /**
     * 打开连接
     * @param session ws会话
     * @param config ws会话配置
     */
    protected final void doOnOpen(Session session, EndpointConfig config) {
        PojoMethodMapping methodMapping = getMethodMapping();
        Object pojo = getPojo();
        Map<String,String> pathParameters = getPathParameters();

        // Add message handlers before calling onOpen since that may trigger a
        // message which in turn could trigger a response and/or close the
        // session
        for (MessageHandler mh : methodMapping.getMessageHandlers(pojo, pathParameters, session, config)) {
            session.addMessageHandler(mh);
        }
        //调用打开函数
        if (methodMapping.getOnOpen() != null) {
            try {
                methodMapping.getOnOpen().invoke(pojo,
                        methodMapping.getOnOpenArgs(
                                pathParameters, session, config));

            } catch (IllegalAccessException e) {
                // Reflection related problems
                log.error(UpgradeUtil.getLocalContainer().getString(
                        "pojoEndpointBase.onOpenFail",
                        pojo.getClass().getName()), e);
                handleOnOpenOrCloseError(session, e);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                handleOnOpenOrCloseError(session, cause);
            } catch (Throwable t) {
                handleOnOpenOrCloseError(session, t);
            }
        }
    }

    /**
     * 处理打开、关闭、错误操作
     * @param session
     * @param t
     */
    private void handleOnOpenOrCloseError(Session session, Throwable t) {
        // If really fatal - re-throw
        ExceptionUtils.handleThrowable(t);
        // Trigger the error handler and close the session
        onError(session, t);
        try {
            session.close();
        } catch (IOException ioe) {
            log.warn(UpgradeUtil.getLocalContainer().getString("pojoEndpointBase.closeSessionFail"), ioe);
        }
    }

    /**
     * 关闭ws连接
     * @param session
     * @param closeReason
     */
    @Override
    public void onClose(Session session, CloseReason closeReason) {
        //判断如果已经关闭了不再处理错误
        if (((WsSession)session).getState() == 2 || methodMapping == null) {
            return;
        }
        if (methodMapping.getOnClose() != null) {
            //调用关闭函数
            try {
                methodMapping.getOnClose().invoke(pojo, methodMapping.getOnCloseArgs(pathParameters, session, closeReason));
            } catch (Throwable t) {
                log.error(UpgradeUtil.getLocalContainer().getString("pojoEndpointBase.onCloseFail",
                        pojo.getClass().getName()), t);
                handleOnOpenOrCloseError(session, t);
            }
        }
        // Trigger the destroy method for any associated decoders
        Set<MessageHandler> messageHandlers = session.getMessageHandlers();
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler instanceof PojoMessageHandlerWholeBase<?>) {
                ((PojoMessageHandlerWholeBase<?>) messageHandler).onClose();
            }
        }
    }
    /**
     * 错误处理
     * @param session
     * @param throwable
     */
    @Override
    public void onError(Session session, Throwable throwable) {
        synchronized (session) {
            //判断是否已经清理
            if (methodMapping == null) {
                return;
            }
        }
        try {
            //判断是否有错误函数
            if (methodMapping.getOnError() == null) {
                log.error(UpgradeUtil.getLocalContainer().getString("pojoEndpointBase.onError",
                        pojo.getClass().getName()), throwable);
            } else {
                //调用错误函数
                try {
                    methodMapping.getOnError().invoke(
                            pojo,
                            methodMapping.getOnErrorArgs(pathParameters, session,
                                    throwable));
                } catch (Throwable t) {
                    ExceptionUtils.handleThrowable(t);
                    log.error(UpgradeUtil.getLocalContainer().getString("pojoEndpointBase.onErrorFail",
                            pojo.getClass().getName()), t);
                }
            }
        } finally {
            //强制执行关闭
            this.onClose(session,
                    new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY,
                            (throwable == null ? null : throwable.getMessage())));
        }
    }

    /**
     * 获取终结点实例对象
     * @return
     */
    protected Object getPojo() { return pojo; }

    /**
     * 设置终结点实例对象
     * @param pojo 终结点实例对象
     */
    protected void setPojo(Object pojo) { this.pojo = pojo; }

    /**
     * 获取url路径地址参数
     * @return
     */
    protected Map<String,String> getPathParameters() { return pathParameters; }

    /**
     * 设置url路径地址参数
     * @param pathParameters url路径地址参数
     */
    protected void setPathParameters(Map<String,String> pathParameters) {
        this.pathParameters = pathParameters;
    }

    /**
     * 获取终结点对象函数地图
     * @return
     */
    protected PojoMethodMapping getMethodMapping() { return methodMapping; }

    /**
     * 设置终结点对象函数地图
     * @param methodMapping 终结点对象函数地图
     */
    protected void setMethodMapping(PojoMethodMapping methodMapping) {
        this.methodMapping = methodMapping;
    }
}