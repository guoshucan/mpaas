/*
 * Copyright 2018-2020 the original author or authors.
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

//import ghost.framework.data.repository.query.SpelQueryContext.SpelExtractor;
//import ghost.framework.expression.EvaluationContext;
//import ghost.framework.expression.spel.standard.SpelExpressionParser;
//import ghost.framework.lang.Nullable;
//import ghost.framework.util.Assert;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.commons.repository.query.Parameters;
import ghost.framework.data.commons.repository.QueryMethodEvaluationContextProvider;
import ghost.framework.expression.EvaluationContext;
import ghost.framework.expression.spel.standard.SpelExpressionParser;
import ghost.framework.util.Assert;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Evaluates SpEL expressions as extracted by the {@link SpelExtractor} based on parameter information from a method and
 * parameter values from a method call.
 *
 * @author Jens Schauder
 * @author Gerrit Meier
 * @author Oliver Gierke
 * @since 2.1
 * @see SpelQueryContext#parse(String, Parameters)
 */
public class SpelEvaluator {

	private final static SpelExpressionParser PARSER = new SpelExpressionParser();

	private final QueryMethodEvaluationContextProvider evaluationContextProvider;
	private final Parameters<?, ?> parameters;
	private final SpelQueryContext.SpelExtractor extractor;

	public SpelEvaluator(QueryMethodEvaluationContextProvider evaluationContextProvider, Parameters<?, ?> parameters,
                         SpelQueryContext.SpelExtractor extractor) {
		this.evaluationContextProvider = evaluationContextProvider;
		this.parameters = parameters;
		this.extractor = extractor;
	}

	/**
	 * Evaluate all the SpEL expressions in {@link #parameterNameToSpelMap} based on values provided as an argument.
	 *
	 * @param values Parameter values. Must not be {@literal null}.
	 * @return a map from parameter name to evaluated value. Guaranteed to be not {@literal null}.
	 */
	public Map<String, Object> evaluate(Object[] values) {

		Assert.notNull(values, "Values must not be null.");

		EvaluationContext evaluationContext = evaluationContextProvider.getEvaluationContext(parameters, values);

		return extractor.getParameters().collect(Collectors.toMap(//
				it -> it.getKey(), //
				it -> getSpElValue(evaluationContext, it.getValue()) //
		));
	}

	/**
	 * Returns the query string produced by the intermediate SpEL expression collection step.
	 *
	 * @return
	 */
	public String getQueryString() {
		return extractor.getQueryString();
	}

	@Nullable
	private static Object getSpElValue(EvaluationContext evaluationContext, String expression) {
		return PARSER.parseExpression(expression).getValue(evaluationContext);
	}
}
