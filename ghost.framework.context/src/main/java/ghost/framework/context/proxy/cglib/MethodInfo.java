package ghost.framework.context.proxy.cglib;

import net.bytebuddy.jar.asm.Type;
import net.sf.cglib.core.Signature;

/**
 * package: ghost.framework.context.proxy.cglib
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/27:10:10
 */
public abstract class MethodInfo {
    protected MethodInfo() {
    }

    public abstract ClassInfo getClassInfo();

    public abstract int getModifiers();

    public abstract Signature getSignature();

    public abstract Type[] getExceptionTypes();

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else {
            return !(o instanceof MethodInfo) ? false : this.getSignature().equals(((MethodInfo)o).getSignature());
        }
    }

    public int hashCode() {
        return this.getSignature().hashCode();
    }

    public String toString() {
        return this.getSignature().toString();
    }
}
