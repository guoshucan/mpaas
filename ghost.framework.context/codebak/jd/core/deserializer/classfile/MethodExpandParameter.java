package ghost.framework.context.asm.jd.core.deserializer.classfile;

import org.objectweb.asm.Type;

import java.util.Objects;

/**
 * package: com.example.springbootwebsockettest.jd.deserializer.classfile
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:函数参数
 * @Date: 2020/5/16:23:02
 */
public class MethodExpandParameter {

    private String name;
    private Type type;
//    private Annotation[] annotations;

    public MethodExpandParameter(String name, Type type) {
        this.name = name;
        this.type = type;
    }

//    public MethodExpandParameter(String name, Type type, Annotation[] annotations) {
//        this.name = name;
//        this.type = type;
//        this.annotations = annotations;
//    }


//    public Annotation[] getAnnotations() {
//        return annotations;
//    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodExpandParameter that = (MethodExpandParameter) o;
        return name.equals(that.name) &&
                type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "MethodParameter{" +
                "name='" + name + '\'' +
                ", type=" + (type == null ? "" : type.toString()) +
                '}';
    }
}