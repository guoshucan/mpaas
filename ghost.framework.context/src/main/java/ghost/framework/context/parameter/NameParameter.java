package ghost.framework.context.parameter;

import ghost.framework.util.Assert;
import ghost.framework.context.asm.ParameterUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * package: ghost.framework.context.parameter
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:扩展名称参数，主要解决原本参数没有获取不到名称问题
 * 使用 {@link ParameterUtils} 参数工具类通过实现asm的字节码来获取参数对象的名称
 * 获取参数名称后对此名称参数类进行拓展
 * @Date: 2020/3/4:13:59
 */
public final class NameParameter implements AnnotatedElement {
    private Parameter parameter;

    public Parameter getParameter() {
        return parameter;
    }

    /**
     * Return the {@code Executable} which declares this parameter.
     *
     * @return The {@code Executable} declaring this parameter.
     */
    public Executable getDeclaringExecutable() {
        return parameter.getDeclaringExecutable();
    }

    public Class<?> getType() {
        return parameter.getType();
    }
    /**
     * Get the modifier flags for this the parameter represented by
     * this {@code Parameter} object.
     *
     * @return The modifier flags for this parameter.
     */
    public int getModifiers() {
        return parameter.getModifiers();
    }
    /**
     * Returns {@code true} if this parameter is implicitly declared
     * in source code; returns {@code false} otherwise.
     *
     * @return true if and only if this parameter is implicitly
     * declared as defined by <cite>The Java&trade; Language
     * Specification</cite>.
     */
    public boolean isImplicit() {
        return parameter.isImplicit();
    }

    /**
     * Returns {@code true} if this parameter is neither implicitly
     * nor explicitly declared in source code; returns {@code false}
     * otherwise.
     *
     * @jls 13.1 The Form of a Binary
     * @return true if and only if this parameter is a synthetic
     * construct as defined by
     * <cite>The Java&trade; Language Specification</cite>.
     */
    public boolean isSynthetic() {
        return parameter.isSynthetic();
    }

    /**
     * Returns {@code true} if this parameter represents a variable
     * argument list; returns {@code false} otherwise.
     *
     * @return {@code true} if an only if this parameter represents a
     * variable argument list.
     */
    public boolean isVarArgs() {
        return parameter.isVarArgs();
    }
    /**
     * Returns true if the parameter has a name according to the class
     * file; returns false otherwise. Whether a parameter has a name
     * is determined by the {@literal MethodParameters} attribute of
     * the method which declares the parameter.
     *
     * @return true if and only if the parameter has a name according
     * to the class file.
     */
    public boolean isNamePresent() {
        return parameter.isNamePresent();
    }
    /**
     * Returns a {@code Type} object that identifies the parameterized
     * type for the parameter represented by this {@code Parameter}
     * object.
     *
     * @return a {@code Type} object identifying the parameterized
     * type of the parameter represented by this object
     */
    public Type getParameterizedType() {
        return parameter.getParameterizedType();
    }

    /**
     * 初始化扩展名称参数
     * @param name 参数名称
     * @param parameter 参数对象
     */
    public NameParameter(String name, Parameter parameter) {
        Assert.notNullOrEmpty(name, "NameParameter is name null error");
        Assert.notNull(parameter, "NameParameter is parameter null error");
        this.name = name;
        this.parameter = parameter;
    }

    private String name;

    /**
     * 获取参数名称
     * @return
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "NameParameter{" +
                "parameter=" + parameter.toString() +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameParameter that = (NameParameter) o;
        return parameter.equals(that.parameter) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameter, name);
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return parameter.isAnnotationPresent(annotationClass);
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return parameter.getAnnotation(annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return parameter.getAnnotations();
    }

    @Override
    public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
        return parameter.getAnnotationsByType(annotationClass);
    }

    @Override
    public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
        return parameter.getDeclaredAnnotation(annotationClass);
    }

    @Override
    public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
        return parameter.getDeclaredAnnotationsByType(annotationClass);
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return parameter.getDeclaredAnnotations();
    }
}