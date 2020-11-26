package ghost.framework.data.commons.repository.utils;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.commons.repository.Repository;
import ghost.framework.data.commons.util.ClassTypeInformation;
import ghost.framework.data.commons.util.TypeInformation;
import ghost.framework.util.Assert;

import java.lang.reflect.Method;
import java.util.Arrays;

//import ghost.framework.data.jdbc.jpa.plugin.annotation.Repository;
//import ghost.framework.data.commons.repository.utils.QueryExecutionConverters;

/**
 * package: ghost.framework.data.jdbc.jpa.plugin.util
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/3:20:59
 */
public final class JapClassUtils {
    /**
     * Returns wthere the given type is the {@link Repository} interface.
     *
     * @param interfaze
     * @return
     */
    public static boolean isGenericRepositoryInterface(Class<?> interfaze) {

        return Repository.class.equals(interfaze);
    }

    /**
     * Returns whether the given type name is a repository interface name.
     *
     * @param interfaceName
     * @return
     */
    public static boolean isGenericRepositoryInterface(@Nullable String interfaceName) {
        return Repository.class.getName().equals(interfaceName);
    }
    private static TypeInformation<?> getEffectivelyReturnedTypeFrom(Method method) {
        TypeInformation<?> returnType = ClassTypeInformation.fromReturnTypeOf(method);
        return QueryExecutionConverters.supports(returnType.getType()) ? returnType.getRequiredComponentType() : returnType;
    }
    /**
     * Asserts the given {@link Method}'s return type to be one of the given types. Will unwrap known wrapper types before
     * the assignment check (see {@link QueryExecutionConverters}).
     *
     * @param method must not be {@literal null}.
     * @param types  must not be {@literal null} or empty.
     */
    public static void assertReturnTypeAssignable(Method method, Class<?>... types) {

        Assert.notNull(method, "Method must not be null!");
        Assert.notEmpty(types, "Types must not be null or empty!");

        TypeInformation<?> returnType = getEffectivelyReturnedTypeFrom(method);

        Arrays.stream(types)//
                .filter(it -> it.isAssignableFrom(returnType.getType()))//
                .findAny().orElseThrow(() -> new IllegalStateException(
                "Method has to have one of the following return types! " + Arrays.toString(types)));
    }
}
