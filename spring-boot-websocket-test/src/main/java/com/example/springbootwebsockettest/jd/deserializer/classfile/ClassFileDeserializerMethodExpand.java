/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package com.example.springbootwebsockettest.jd.deserializer.classfile;

import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.model.classfile.ClassFile;
import org.jd.core.v1.model.classfile.Method;
import org.jd.core.v1.service.deserializer.classfile.ClassFileDeserializer;

import java.util.ArrayList;
import java.util.List;

/**
 * {@see org.jd.core.v1}
 * 函数扩展
 */
public class ClassFileDeserializerMethodExpand extends ClassFileDeserializer {
    public ClassFileDeserializerMethodExpand() {
        super();
    }

    /**
     * 获取全部函数
     * @param loader
     * @param internalTypeName
     * @return
     * @throws Exception
     */
    public List<MethodExpand> getMethods(Loader loader, String internalTypeName) throws Exception {
        List<MethodExpand> methodExpandList = new ArrayList<>();
        ClassFile classFile = this.innerLoadClassFile(loader, internalTypeName);
        if (classFile.getMethods() != null) {
            for (Method method : classFile.getMethods()) {
                methodExpandList.add(new MethodExpand(method));
            }
        }
        return methodExpandList;
    }

    /**
     * 获取指定名称函数
     * @param loader
     * @param internalTypeName
     * @param methodName
     * @return
     * @throws Exception
     */
    public List<MethodExpand> getMethods(Loader loader, String internalTypeName, String methodName) throws Exception {
        List<MethodExpand> methodExpandList = new ArrayList<>();
        ClassFile classFile = this.innerLoadClassFile(loader, internalTypeName);
        if (classFile.getMethods() != null) {
            for (Method method : classFile.getMethods()) {
                if (method.getName().equals(methodName)) {
                    methodExpandList.add(new MethodExpand(method));
                }
            }
        }
        return methodExpandList;
    }
}