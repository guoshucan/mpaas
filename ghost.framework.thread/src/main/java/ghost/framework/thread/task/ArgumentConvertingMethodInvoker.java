///*
// * Copyright 2002-2017 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package ghost.framework.thread.task;
//
//import java.beans.PropertyEditor;
//import java.lang.reflect.Method;
//
///**
// * Subclass of {@link MethodInvoker} that tries to convert the given
// * arguments for the actual target method via a {@link TypeDescriptorConverter}.
// *
// * <p>Supports flexible argument conversions, in particular for
// * invoking a specific overloaded method.
// *
// * @author Juergen Hoeller
// * @since 1.1
// * @see ghost.framework.beans.BeanWrapperImpl#convertIfNecessary
// */
//public class ArgumentConvertingMethodInvoker extends MethodInvoker {
//
//	@Nullable
//	private TypeDescriptorConverter typeConverter;
//
//	private boolean useDefaultConverter = true;
//
//
//	/**
//	 * Set a TypeDescriptorConverter to use for argument type conversion.
//	 * <p>Default is a {@link ghost.framework.beans.SimpleTypeConverter}.
//	 * Can be overridden with any TypeDescriptorConverter implementation, typically
//	 * a pre-configured SimpleTypeConverter or a BeanWrapperImpl instance.
//	 * @see ghost.framework.beans.SimpleTypeConverter
//	 * @see ghost.framework.beans.BeanWrapperImpl
//	 */
//	public void setTypeConverter(@Nullable TypeDescriptorConverter typeConverter) {
//		this.typeConverter = typeConverter;
//		this.useDefaultConverter = (typeConverter == null);
//	}
//
//	/**
//	 * Return the TypeDescriptorConverter used for argument type conversion.
//	 * <p>Can be cast to {@link ghost.framework.beans.PropertyEditorRegistry}
//	 * if direct access to the underlying PropertyEditors is desired
//	 * (provided that the present TypeDescriptorConverter actually implements the
//	 * PropertyEditorRegistry interface).
//	 */
//	@Nullable
//	public TypeDescriptorConverter getTypeConverter() {
//		if (this.typeConverter == null && this.useDefaultConverter) {
//			this.typeConverter = getDefaultTypeConverter();
//		}
//		return this.typeConverter;
//	}
//
//	/**
//	 * Obtain the default TypeDescriptorConverter for this method invoker.
//	 * <p>Called if no explicit TypeDescriptorConverter has been specified.
//	 * The default implementation builds a
//	 * {@link ghost.framework.beans.SimpleTypeConverter}.
//	 * Can be overridden in subclasses.
//	 */
//	protected TypeDescriptorConverter getDefaultTypeConverter() {
//		return new SimpleTypeConverter();
//	}
//
//	/**
//	 * Register the given custom property editor for all properties of the given type.
//	 * <p>Typically used in conjunction with the default
//	 * {@link ghost.framework.beans.SimpleTypeConverter}; will work with any
//	 * TypeDescriptorConverter that implements the PropertyEditorRegistry interface as well.
//	 * @param requiredType type of the property
//	 * @param propertyEditor editor to register
//	 * @see #setTypeConverter
//	 * @see ghost.framework.beans.PropertyEditorRegistry#registerCustomEditor
//	 */
//	public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
//		TypeDescriptorConverter converter = getTypeConverter();
//		if (!(converter instanceof PropertyEditorRegistry)) {
//			throw new IllegalStateException(
//					"TypeDescriptorConverter does not implement PropertyEditorRegistry interface: " + converter);
//		}
//		((PropertyEditorRegistry) converter).registerCustomEditor(requiredType, propertyEditor);
//	}
//
//
//	/**
//	 * This implementation looks for a method with matching parameter types.
//	 * @see #doFindMatchingMethod
//	 */
//	@Override
//	protected Method findMatchingMethod() {
//		Method matchingMethod = super.findMatchingMethod();
//		// Second pass: look for method where arguments can be converted to parameter types.
//		if (matchingMethod == null) {
//			// Interpret argument array as individual method arguments.
//			matchingMethod = doFindMatchingMethod(getArguments());
//		}
//		if (matchingMethod == null) {
//			// Interpret argument array as single method argument of array type.
//			matchingMethod = doFindMatchingMethod(new Object[] {getArguments()});
//		}
//		return matchingMethod;
//	}
//
//	/**
//	 * Actually find a method with matching parameter type, i.e. where each
//	 * argument value is assignable to the corresponding parameter type.
//	 * @param arguments the argument values to match against method parameters
//	 * @return a matching method, or {@code null} if none
//	 */
//	@Nullable
//	protected Method doFindMatchingMethod(Object[] arguments) {
//		TypeDescriptorConverter converter = getTypeConverter();
//		if (converter != null) {
//			String targetMethod = getTargetMethod();
//			Method matchingMethod = null;
//			int argCount = arguments.length;
//			Class<?> targetClass = getTargetClass();
//			Assert.state(targetClass != null, "No target class set");
//			Method[] candidates = ReflectionUtils.getAllDeclaredMethods(targetClass);
//			int minTypeDiffWeight = Integer.MAX_VALUE;
//			Object[] argumentsToUse = null;
//			for (Method candidate : candidates) {
//				if (candidate.getName().equals(targetMethod)) {
//					// Check if the inspected method has the correct number of parameters.
//					Class<?>[] paramTypes = candidate.getParameterTypes();
//					if (paramTypes.length == argCount) {
//						Object[] convertedArguments = new Object[argCount];
//						boolean match = true;
//						for (int j = 0; j < argCount && match; j++) {
//							// Verify that the supplied argument is assignable to the method parameter.
//							try {
//								convertedArguments[j] = converter.convertIfNecessary(arguments[j], paramTypes[j]);
//							}
//							catch (TypeMismatchException ex) {
//								// Ignore -> simply doesn't match.
//								match = false;
//							}
//						}
//						if (match) {
//							int typeDiffWeight = getTypeDifferenceWeight(paramTypes, convertedArguments);
//							if (typeDiffWeight < minTypeDiffWeight) {
//								minTypeDiffWeight = typeDiffWeight;
//								matchingMethod = candidate;
//								argumentsToUse = convertedArguments;
//							}
//						}
//					}
//				}
//			}
//			if (matchingMethod != null) {
//				setArguments(argumentsToUse);
//				return matchingMethod;
//			}
//		}
//		return null;
//	}
//
//}
