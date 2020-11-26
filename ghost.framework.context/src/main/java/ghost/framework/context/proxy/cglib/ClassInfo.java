package ghost.framework.context.proxy.cglib;

import net.bytebuddy.jar.asm.Type;

/**
 * package: ghost.framework.context.proxy.cglib
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/27:10:08
 */
public abstract class ClassInfo {
    protected ClassInfo() {
    }

    public abstract Type getType();

    public abstract Type getSuperType();

    public abstract Type[] getInterfaces();

    public abstract int getModifiers();

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else {
            return !(o instanceof ClassInfo) ? false : this.getType().equals(((ClassInfo)o).getType());
        }
    }

    public int hashCode() {
        return this.getType().hashCode();
    }

    public String toString() {
        return this.getType().getClassName();
    }
}