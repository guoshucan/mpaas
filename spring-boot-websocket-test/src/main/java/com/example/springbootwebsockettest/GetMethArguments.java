package com.example.springbootwebsockettest;

/**
 * package: com.example.springbootwebsockettest
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/16:12:26
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ghost.framework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;

/** * descrption: 通过spring的LocalVariableTableParameterNameDiscoverer 获取方法的参数，spring也是通过使用ASM通过字节码获取方法中参数的具体的名称 * authohr: wangji * date: 2017-08-15 10:20 */

public class GetMethArguments {
    private static Log log = LogFactory.getLog(GetMethArguments.class);
    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public void testArguments(String test, Integer myInteger, boolean booleanTest) {
    }

    public void test() {

    }

    public static void main(String[] args) {
        Method[] methods = VLInterfaceTest.class.getMethods();
        for (Method method : methods) {
            String[] paraNames = parameterNameDiscoverer.getParameterNames(method);

            log.info("methodName:" + method.getName());
            if (paraNames != null) {
                StringBuffer buffer = new StringBuffer();
                for (String string : paraNames) {
                    buffer.append(string).append("\t");
                }
                log.info("parameArguments:" + buffer.toString());
            } else {
                log.info("无参数");
            }
        }
    }
}