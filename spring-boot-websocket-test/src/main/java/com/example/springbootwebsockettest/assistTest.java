package com.example.springbootwebsockettest;

/**
 * package: com.example.springbootwebsockettest
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/13:5:46
 */
public class assistTest {
    public static void main(String[] args) {
        Proxy<IStu> proxy = new Proxy<>(IStu.class);
        IStu proxyOject = proxy.getProxyOject();
        System.out.println("proxy Object name:"+proxyOject.getClass().getName());
        proxyOject.handupTask();
    }
}
