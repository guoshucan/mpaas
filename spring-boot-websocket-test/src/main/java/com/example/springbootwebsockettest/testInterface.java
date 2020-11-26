package com.example.springbootwebsockettest;

import com.example.springbootwebsockettest.jd.deserializer.classfile.ClassFileDeserializerMethodExpand;
import com.example.springbootwebsockettest.jd.deserializer.classfile.MethodExpand;
import com.example.springbootwebsockettest.jd.deserializer.classfile.MethodExpandParameter;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * package: ghost.framework.context.test
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/17:3:20
 */
public interface testInterface {
    void test(String name1);

     void test5(int name15) ;

    static void main(String[] args) throws Exception {
        Loader loader = new Loader() {
            @Override
            public byte[] load(String internalName) throws LoaderException {
                InputStream is = this.getClass().getResourceAsStream("/" + internalName + ".class");

                if (is == null) {
                    return null;
                } else {
                    try (InputStream in = is; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        byte[] buffer = new byte[1024];
                        int read = in.read(buffer);

                        while (read > 0) {
                            out.write(buffer, 0, read);
                            read = in.read(buffer);
                        }

                        return out.toByteArray();
                    } catch (IOException e) {
                        throw new LoaderException(e);
                    }
                }
            }

            @Override
            public boolean canLoad(String internalName) {
                return this.getClass().getResource("/" + internalName + ".class") != null;
            }
        };
        //获取指定函数信息
        ClassFileDeserializerMethodExpand methodExpand = new ClassFileDeserializerMethodExpand();
        List<MethodExpand> methodExpandList = methodExpand.getMethods(loader, "com/example/springbootwebsockettest/testInterface");
        for (MethodExpand m : methodExpandList) {
            System.out.println(m.getName());
            for (MethodExpandParameter parameter : m.getParameters()) {
                System.out.println(parameter.getName());
            }
        }
    }
}