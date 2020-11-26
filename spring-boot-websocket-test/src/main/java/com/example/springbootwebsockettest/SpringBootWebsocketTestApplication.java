package com.example.springbootwebsockettest;

import com.example.springbootwebsockettest.jd.deserializer.classfile.ClassFileDeserializerMethodExpand;
import com.example.springbootwebsockettest.jd.deserializer.classfile.MethodExpand;
import org.jd.core.v1.ClassFileToJavaSourceDecompiler;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;
import org.jd.core.v1.api.printer.Printer;
import ghost.framework.boot.SpringApplication;
import ghost.framework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class SpringBootWebsocketTestApplication {

    public static void main(String[] args) throws Exception {
        String s = "";
        for (int i = 0; i < 300; i++) {
            s += UUID.randomUUID().toString();
        }
        System.out.println(s.length());
//        ByteBuffer buff = ByteBuffer.allocate(1024);
//        String str = "helloWorld";
//        buff.put(str.getBytes());
//        byte[] ssss = buff.array();
//        System.out.println(new String(ssss));
//        System.out.println("position:" + buff.position() + "\t limit:"
//                + buff.limit() + "\t capacity:" + buff.capacity());
//        // 读取两个字节byte[] abytes = new byte[1];
//        byte[] abytes = new byte[1];
//        buff.get(abytes);
//        System.out.println("get one byte to string:" + new String(abytes));
//        // Reads the byte at this buffer's current position, and then increments
//        // the position.
//        buff.get();
//        System.out.println("获取两个字节（两次get()方法调用）后");
//        System.out.println("position:" + buff.position() + "\t limit:"
//                + buff.limit());
//        // Sets this buffer's mark at its position. like
//        // ByteBuffer.this.mark=position
//        buff.mark();
//        System.out.println("mark()...");
//        System.out.println("position:" + buff.position() + "\t limit:"
//                + buff.limit());
//
//        // 当读取到码流后，进行解码。首先对ByteBuffer进行flip操作，
//        // 它的作用是将缓冲区当前的limit设置为position,position设置为0
//        // flip方法将Buffer从写模式切换到读模式。调用flip()方法会将position设回0，并将limit设置成之前position的值。
//        buff.flip();
//        System.out.println("flip()...");
//        System.out.println("position:" + buff.position() + "\t limit:"
//                + buff.limit() + "\t capacity:" + buff.capacity());
//
//        byte[] tbyte = new byte[buff.limit()];
//        buff.get(tbyte);
//        System.out.println("get one byte to string:" + new String(tbyte));
//        System.out.println("position:" + buff.position() + "\t limit:"
//                + buff.limit());
//        if (buff.hasRemaining()) {
//            buff.compact();
//        } else {
//            buff.clear();
//        }
        Printer printer = new Printer() {
            protected static final String TAB = "  ";
            protected static final String NEWLINE = "\n";

            protected int indentationCount = 0;
            protected StringBuilder sb = new StringBuilder();

            @Override
            public String toString() {
                return sb.toString();
            }

            @Override
            public void start(int maxLineNumber, int majorVersion, int minorVersion) {
            }

            @Override
            public void end() {
            }

            @Override
            public void printText(String text) {
                sb.append(text);
            }

            @Override
            public void printNumericConstant(String constant) {
                sb.append(constant);
            }

            @Override
            public void printStringConstant(String constant, String ownerInternalName) {
                sb.append(constant);
            }

            @Override
            public void printKeyword(String keyword) {
                sb.append(keyword);
            }

            @Override
            public void printDeclaration(int flags, String internalTypeName, String name, String descriptor) {
                sb.append(name);
            }

            @Override
            public void printReference(int flags, String internalTypeName, String name, String descriptor, String ownerInternalName) {
                sb.append(name);
            }

            @Override
            public void indent() {
                this.indentationCount++;
            }

            @Override
            public void unindent() {
                if (this.indentationCount > 0) this.indentationCount--;
            }

            @Override
            public void startLine(int lineNumber) {
                for (int i = 0; i < indentationCount; i++) sb.append(TAB);
            }

            @Override
            public void endLine() {
                sb.append(NEWLINE);
            }

            @Override
            public void extraLine(int count) {
                while (count-- > 0) sb.append(NEWLINE);
            }

            @Override
            public void startMarker(int type) {
            }

            @Override
            public void endMarker(int type) {
            }
        };
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
        ClassFileToJavaSourceDecompiler decompiler = new ClassFileToJavaSourceDecompiler();

        decompiler.decompile(loader, printer, "com/example/springbootwebsockettest/VLInterfaceTest");

        String source = printer.toString();

        System.out.println(source);
        //获取指定函数信息
        ClassFileDeserializerMethodExpand methodExpand = new ClassFileDeserializerMethodExpand();
        List<MethodExpand> methodExpandList = methodExpand.getMethods(loader, "com/example/springbootwebsockettest/testInterface");

        System.out.println(methodExpandList.size());
        SpringApplication.run(SpringBootWebsocketTestApplication.class, args);
    }

}
