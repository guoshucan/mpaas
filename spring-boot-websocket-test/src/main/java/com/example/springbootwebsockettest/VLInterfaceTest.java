package com.example.springbootwebsockettest;

import com.example.springbootwebsockettest.jd.deserializer.classfile.MethodExpand;

import javax.validation.constraints.NotNull;

/**
 * package: com.example.springbootwebsockettest
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/16:10:33
 */
public interface VLInterfaceTest {
     @Deprecated
      void test(String param11,
                @NotNull(groups = {MethodExpand.class}, message = "void test(String param11, @NotNull(groups = {MethodExpand.class}, message = \"\") int param22);") int param22,
                Integer p4,
                boolean b1,
                Boolean b2,
                byte by1,
                Byte by2,
                short s1,
                Short s2,
                long l1,
                Long l2,
                float f1,
                Float f2,
                double d1,
                Double d2,
                char c1);
     String test() throws Exception;
    int test(long i) throws Exception;
    Exception test(Exception i) throws Exception;
}
