/*
 * Copyright 2017-2020 the original author or authors.
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
package ghost.framework.data.jdbc.jpa.plugin.repository.query;

//import ghost.framework.data.jpa.repository.query.JpaParameters.JpaParameter;
//import ghost.framework.data.jpa.repository.query.ParameterMetadataProvider.ParameterMetadata;
//import ghost.framework.data.jpa.repository.query.QueryParameterSetter.NamedOrIndexedQueryParameterSetter;
//import ghost.framework.data.jpa.repository.query.StringQuery.ParameterBinding;
//import ghost.framework.data.repository.query.Parameter;
//import ghost.framework.data.repository.query.Parameters;
//import ghost.framework.data.repository.query.QueryMethodEvaluationContextProvider;
//import ghost.framework.data.spel.EvaluationContextProvider;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.commons.repository.query.Parameter;
import ghost.framework.data.commons.repository.query.Parameters;
import ghost.framework.data.commons.spel.EvaluationContextProvider;
import ghost.framework.data.commons.repository.QueryMethodEvaluationContextProvider;
import ghost.framework.expression.EvaluationContext;
import ghost.framework.expression.Expression;
import ghost.framework.expression.spel.standard.SpelExpressionParser;
import ghost.framework.util.Assert;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.List;
import java.util.function.Function;

/**
 * Encapsulates different strategies for the creation of a {@link QueryParameterSetter} from a {@link Query} and a
 * {@link StringQuery.ParameterBinding}
 *
 * @author Jens Schauder
 * @author Oliver Gierke
 * @author Mark Paluch
 * @since 2.0
 */
abstract class QueryParameterSetterFactory {

	@Nullable
	abstract QueryParameterSetter create(StringQuery.ParameterBinding binding, DeclaredQuery declaredQuery);

	/**
	 * Creates a new {@link QueryParameterSetterFactory} for the given {@link JpaParameters}.
	 *
	 * @param parameters must not be {@literal null}.
	 * @return a basic {@link QueryParameterSetterFactory} that can handle named and index parameters.
	 */
	static QueryParameterSetterFactory basic(JpaParameters parameters) {

		Assert.notNull(parameters, "JpaParameters must not be null!");

		return new BasicQueryParameterSetterFactory(parameters);
	}

	/**
	 * Creates a new {@link QueryParameterSetterFactory} using the given {@link JpaParameters} and
	 * {@link ParameterMetadataProvider.ParameterMetadata}.
	 *
	 * @param parameters must not be {@literal null}.
	 * @param metadata must not be {@literal null}.
	 * @return a {@link QueryParameterSetterFactory} for criteria Queries.
	 */
	static QueryParameterSetterFactory forCriteriaQuery(JpaParameters parameters, List<ParameterMetadataProvider.ParameterMetadata<?>> metadata) {

		Assert.notNull(parameters, "JpaParameters must not be null!");
		Assert.notNull(metadata, "ParameterMetadata must not be null!");

		return new CriteriaQueryParameterSetterFactory(parameters, metadata);
	}

	/**
	 * Creates a new {@link QueryParameterSetterFactory} for the given {@link SpelExpressionParser},
	 * {@link EvaluationContextProvider} and {@link Parameters}.
	 *
	 * @param parser must not be {@literal null}.
	 * @param evaluationContextProvider must not be {@literal null}.
	 * @param parameters must not be {@literal null}.
	 * @return a {@link QueryParameterSetterFactory} that can handle
	 *         {@link ghost.framework.expression.spel.standard.SpelExpression}s.
	 */
	static QueryParameterSetterFactory parsing(SpelExpressionParser parser,
											   QueryMethodEvaluationContextProvider evaluationContextProvider, Parameters<?, ?> parameters) {
		Assert.notNull(parser, "SpelExpressionParser must not be null!");
		Assert.notNull(evaluationContextProvider, "EvaluationContextProvider must not be null!");
		Assert.notNull(parameters, "Parameters must not be null!");

		return new ExpressionBasedQueryParameterSetterFactory(parser, evaluationContextProvider, parameters);
	}

	/**
	 * Creates a {@link QueryParameterSetter} from a {@link JpaParameters.JpaParameter}. Handles named and indexed parameters,
	 * TemporalType annotations and might ignore certain exception when requested to do so.
	 *
	 * @param valueExtractor extracts the relevant value from an array of method parameter values.
	 * @param binding the binding of the query parameter to be set.
	 * @param parameter the method parameter to bind.
	 */
	private static QueryParameterSetter createSetter(Function<JpaParametersParameterAccessor, Object> valueExtractor,
													 StringQuery.ParameterBinding binding, @Nullable JpaParameters.JpaParameter parameter) {

		TemporalType temporalType = parameter != null && parameter.isTemporalParameter() //
				? parameter.getRequiredTemporalType() //
				: null;

		return new QueryParameterSetter.NamedOrIndexedQueryParameterSetter(valueExtractor.andThen(binding::prepare),
				ParameterImpl.of(parameter, binding), temporalType);
	}

	/**
	 * Handles bindings that are SpEL expressions by evaluating the expression to obtain a value.
	 *
	 * @author Jens Schauder
	 * @author Oliver Gierke
	 * @since 2.0
	 */
	private static class ExpressionBasedQueryParameterSetterFactory extends QueryParameterSetterFactory {
		private final SpelExpressionParser parser;
		private final QueryMethodEvaluationContextProvider evaluationContextProvider;
		private final Parameters<?, ?> parameters;
		/**
		 * @param parser must not be {@literal null}.
		 * @param evaluationContextProvider must not be {@literal null}.
		 * @param parameters must not be {@literal null}.
		 */
		ExpressionBasedQueryParameterSetterFactory(SpelExpressionParser parser,
				QueryMethodEvaluationContextProvider evaluationContextProvider, Parameters<?, ?> parameters) {
			Assert.notNull(evaluationContextProvider, "EvaluationContextProvider must not be null!");
			Assert.notNull(parser, "SpelExpressionParser must not be null!");
			Assert.notNull(parameters, "Parameters must not be null!");

			this.evaluationContextProvider = evaluationContextProvider;
			this.parser = parser;
			this.parameters = parameters;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.jpa.repository.query.QueryParameterSetterFactory#create(ghost.framework.data.jpa.repository.query.StringQuery.ParameterBinding, java.lang.String)
		 */
		@Nullable
		@Override
		public QueryParameterSetter create(StringQuery.ParameterBinding binding, DeclaredQuery declaredQuery) {

			if (!binding.isExpression()) {
				return null;
			}

			Expression expression = parser.parseExpression(binding.getExpression());

			return createSetter(values -> evaluateExpression(expression, values), binding, null);
		}

		/**
		 * Evaluates the given {@link Expression} against the given values.
		 *
		 * @param expression must not be {@literal null}.
		 * @param accessor must not be {@literal null}.
		 * @return the result of the evaluation.
		 */
		@Nullable
		private Object evaluateExpression(Expression expression, JpaParametersParameterAccessor accessor) {

			EvaluationContext context = evaluationContextProvider.getEvaluationContext(parameters, accessor.getValues());

			return expression.getValue(context, Object.class);
		}
	}

	/**
	 * Extracts values for parameter bindings from method parameters. It handles named as well as indexed parameters.
	 *
	 * @author Jens Schauder
	 * @author Oliver Gierke
	 * @since 2.0
	 */
	private static class BasicQueryParameterSetterFactory extends QueryParameterSetterFactory {

		private final JpaParameters parameters;

		/**
		 * @param parameters must not be {@literal null}.
		 */
		BasicQueryParameterSetterFactory(JpaParameters parameters) {

			Assert.notNull(parameters, "JpaParameters must not be null!");

			this.parameters = parameters;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.jpa.repository.query.QueryParameterSetterFactory#create(ghost.framework.data.jpa.repository.query.StringQuery.ParameterBinding, java.lang.String)
		 */
		@Override
		public QueryParameterSetter create(StringQuery.ParameterBinding binding, DeclaredQuery declaredQuery) {

			Assert.notNull(binding, "Binding must not be null.");

			JpaParameters.JpaParameter parameter;

			if (declaredQuery.hasNamedParameter()) {
				parameter = findParameterForBinding(binding);
			} else {

				int parameterIndex = binding.getRequiredPosition() - 1;
				JpaParameters bindableParameters = parameters.getBindableParameters();

				Assert.isTrue( //
						parameterIndex < bindableParameters.getNumberOfParameters(), //
						() -> String.format( //
								"At least %s parameter(s) provided but only %s parameter(s) present in query.", //
								binding.getRequiredPosition(), //
								bindableParameters.getNumberOfParameters() //
						) //
				);

				parameter = bindableParameters.getParameter(binding.getRequiredPosition() - 1);
			}

			return parameter == null //
					? QueryParameterSetter.NOOP //
					: createSetter(values -> getValue(values, parameter), binding, parameter);
		}

		@Nullable
		private JpaParameters.JpaParameter findParameterForBinding(StringQuery.ParameterBinding binding) {
			JpaParameters bindableParameters = parameters.getBindableParameters();

			for (JpaParameters.JpaParameter bindableParameter : bindableParameters) {
				if (binding.getRequiredName().equals(getName(bindableParameter))) {
					return bindableParameter;
				}
			}

			return null;
		}

		private Object getValue(JpaParametersParameterAccessor accessor, Parameter parameter) {
			return accessor.getValue(parameter);
		}

		private static String getName(JpaParameters.JpaParameter p) {
			return p.getName().orElseThrow(() -> new IllegalStateException(ParameterBinder.PARAMETER_NEEDS_TO_BE_NAMED));
		}
	}

	/**
	 * @author Jens Schauder
	 * @author Oliver Gierke
	 * @see QueryParameterSetterFactory
	 */
	private static class CriteriaQueryParameterSetterFactory extends QueryParameterSetterFactory {

		private final JpaParameters parameters;
		private final List<ParameterMetadataProvider.ParameterMetadata<?>> expressions;

		/**
		 * Creates a new {@link QueryParameterSetterFactory} from the given {@link JpaParameters} and
		 * {@link ParameterMetadataProvider.ParameterMetadata}.
		 *
		 * @param parameters must not be {@literal null}.
		 * @param metadata must not be {@literal null}.
		 */
		CriteriaQueryParameterSetterFactory(JpaParameters parameters, List<ParameterMetadataProvider.ParameterMetadata<?>> metadata) {

			Assert.notNull(parameters, "JpaParameters must not be null!");
			Assert.notNull(metadata, "Expressions must not be null!");

			this.parameters = parameters;
			this.expressions = metadata;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.jpa.repository.query.QueryParameterSetterFactory#create(ghost.framework.data.jpa.repository.query.StringQuery.ParameterBinding, java.lang.String)
		 */
		@Override
		public QueryParameterSetter create(StringQuery.ParameterBinding binding, DeclaredQuery declaredQuery) {

			int parameterIndex = binding.getRequiredPosition() - 1;

			Assert.isTrue( //
					parameterIndex < expressions.size(), //
					() -> String.format( //
							"At least %s parameter(s) provided but only %s parameter(s) present in query.", //
							binding.getRequiredPosition(), //
							expressions.size() //
					) //
			);

			ParameterMetadataProvider.ParameterMetadata<?> metadata = expressions.get(parameterIndex);

			if (metadata.isIsNullParameter()) {
				return QueryParameterSetter.NOOP;
			}
			JpaParameters.JpaParameter parameter = parameters.getBindableParameter(parameterIndex);
			TemporalType temporalType = parameter.isTemporalParameter() ? parameter.getRequiredTemporalType() : null;

			return new QueryParameterSetter.NamedOrIndexedQueryParameterSetter(values -> {
				return getAndPrepare(parameter, metadata, values);
			}, metadata.getExpression(), temporalType);
		}

		@Nullable
		private Object getAndPrepare(JpaParameters.JpaParameter parameter, ParameterMetadataProvider.ParameterMetadata<?> metadata,
									 JpaParametersParameterAccessor accessor) {
			return metadata.prepare(accessor.getValue(parameter));
		}
	}

	private static class ParameterImpl<T> implements javax.persistence.Parameter<T> {

		private final Class<T> parameterType;
		private final @Nullable String name;
		private final @Nullable Integer position;

		/**
		 * Creates a new {@link ParameterImpl} for the given {@link JpaParameters.JpaParameter} and {@link StringQuery.ParameterBinding}.
		 *
		 * @param parameter can be {@literal null}.
		 * @param binding must not be {@literal null}.
		 * @return a {@link javax.persistence.Parameter} object based on the information from the arguments.
		 */
		static javax.persistence.Parameter<?> of(@Nullable JpaParameters.JpaParameter parameter, StringQuery.ParameterBinding binding) {

			Class<?> type = parameter == null ? Object.class : parameter.getType();

			return new ParameterImpl<>(type, getName(parameter, binding), binding.getPosition());
		}

		/**
		 * Creates a new {@link ParameterImpl} for the given name, position and parameter type.
		 *
		 * @param parameterType must not be {@literal null}.
		 * @param name can be {@literal null}.
		 * @param position can be {@literal null}.
		 */
		private ParameterImpl(Class<T> parameterType, @Nullable String name, @Nullable Integer position) {

			this.name = name;
			this.position = position;
			this.parameterType = parameterType;
		}

		/*
		 * (non-Javadoc)
		 * @see javax.persistence.Parameter#getName()
		 */
		@Nullable
		@Override
		public String getName() {
			return name;
		}

		/*
		 * (non-Javadoc)
		 * @see javax.persistence.Parameter#getPosition()
		 */
		@Nullable
		@Override
		public Integer getPosition() {
			return position;
		}

		/*
		 * (non-Javadoc)
		 * @see javax.persistence.Parameter#getParameterType()
		 */
		@Override
		public Class<T> getParameterType() {
			return parameterType;
		}

		@Nullable
		private static String getName(@Nullable JpaParameters.JpaParameter parameter, StringQuery.ParameterBinding binding) {

			if (parameter == null) {
				return binding.getName();
			}

			return parameter.isNamedParameter() //
					? parameter.getName().orElseThrow(() -> new IllegalArgumentException("o_O parameter needs to have a name!")) //
					: null;
		}
	}
}
