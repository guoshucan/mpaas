/*
 * Copyright 2008-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghost.framework.data.commons.util;

//import ghost.framework.data.repository.Repository;
//import ghost.framework.lang.Nullable;
//import ghost.framework.util.Assert;
//import ghost.framework.util.StringUtils;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Utility class to work with classes.
 *
 * @author Oliver Gierke
 * @author Mark Paluch
 */
public abstract class ClassUtils {

	/**
	 * Returns whether the given class contains a property with the given name.
	 *
	 * @param type
	 * @param property
	 * @return
	 */
	public static boolean hasProperty(Class<?> type, String property) {
		if (null != ghost.framework.util.ReflectionUtils.findMethod(type, "get" + property)) {
			return true;
		}
		return null != ghost.framework.util.ReflectionUtils.findField(type, StringUtils.uncapitalize(property));
	}

	/**
	 * Determine whether the {@link Class} identified by the supplied {@code className} is present * and can be loaded and
	 * call the {@link Consumer action} if the {@link Class} could be loaded.
	 *
	 * @param className   the name of the class to check.
	 * @param classLoader the class loader to use.
	 * @param action      the action callback to notify. (may be {@code null} which indicates the default class loader)
	 * @throws IllegalStateException if the corresponding class is resolvable but there was a readability mismatch in the
	 *                               inheritance hierarchy of the class (typically a missing dependency declaration in a Jigsaw module
	 *                               definition for a superclass or interface implemented by the class to be checked here)
	 */
	public static void ifPresent(String className, @Nullable ClassLoader classLoader, Consumer<Class<?>> action) {

		try {
			Class<?> theClass = ghost.framework.util.ClassUtils.forName(className, classLoader);
			action.accept(theClass);
		} catch (IllegalAccessError err) {
			throw new IllegalStateException(
					"Readability mismatch in inheritance hierarchy of class [" + className + "]: " + err.getMessage(), err);
		} catch (Throwable ex) {
			// Typically ClassNotFoundException or NoClassDefFoundError...
		}
	}


	/**
	 * Returns the number of occurences of the given type in the given {@link Method}s parameters.
	 *
	 * @param method
	 * @param type
	 * @return
	 */
	public static int getNumberOfOccurences(Method method, Class<?> type) {

		int result = 0;
		for (Class<?> clazz : method.getParameterTypes()) {
			if (type.equals(clazz)) {
				result++;
			}
		}

		return result;
	}



	/**
	 * Returns whether the given object is of one of the given types. Will return {@literal false} for {@literal null}.
	 *
	 * @param object
	 * @param types
	 * @return
	 */
	public static boolean isOfType(@Nullable Object object, Collection<Class<?>> types) {

		if (object == null) {
			return false;
		}

		return types.stream().anyMatch(it -> it.isAssignableFrom(object.getClass()));
	}

	/**
	 * Returns whether the given {@link Method} has a parameter of the given type.
	 *
	 * @param method
	 * @param type
	 * @return
	 */
	public static boolean hasParameterOfType(Method method, Class<?> type) {
		return Arrays.asList(method.getParameterTypes()).contains(type);
	}

	/**
	 * Helper method to extract the original exception that can possibly occur during a reflection call.
	 *
	 * @param ex
	 * @throws Throwable
	 */
	public static void unwrapReflectionException(Exception ex) throws Throwable {

		if (ex instanceof InvocationTargetException) {
			throw ((InvocationTargetException) ex).getTargetException();
		}

		throw ex;
	}
}