/*
 * Copyright 2014-2020 the original author or authors.
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
package ghost.framework.data.keyvalue.repository.query;

import ghost.framework.data.dao.InvalidDataAccessApiUsageException;
import ghost.framework.data.domain.Sort;
import ghost.framework.data.keyvalue.core.query.KeyValueQuery;
import ghost.framework.data.repository.query.ParameterAccessor;
import ghost.framework.data.repository.query.parser.AbstractQueryCreator;
import ghost.framework.data.repository.query.parser.Part;
import ghost.framework.data.repository.query.parser.Part.IgnoreCaseType;
import ghost.framework.data.repository.query.parser.Part.Type;
import ghost.framework.data.repository.query.parser.PartTree;
import ghost.framework.data.repository.query.parser.PartTree.OrPart;
import ghost.framework.expression.spel.standard.SpelExpression;
import ghost.framework.expression.spel.standard.SpelExpressionParser;
import ghost.framework.util.StringUtils;

import java.util.Iterator;

/**
 * {@link AbstractQueryCreator} to create {@link SpelExpression} based {@link KeyValueQuery}s.
 *
 * @author Christoph Strobl
 * @author Oliver Gierke
 * @author Mark Paluch
 */
public class SpelQueryCreator extends AbstractQueryCreator<KeyValueQuery<SpelExpression>, String> {

	private static final SpelExpressionParser PARSER = new SpelExpressionParser();

	private final SpelExpression expression;

	/**
	 * Creates a new {@link SpelQueryCreator} for the given {@link PartTree} and {@link ParameterAccessor}.
	 *
	 * @param tree must not be {@literal null}.
	 * @param parameters must not be {@literal null}.
	 */
	public SpelQueryCreator(PartTree tree, ParameterAccessor parameters) {

		super(tree, parameters);

		this.expression = toPredicateExpression(tree);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.query.parser.AbstractQueryCreator#create(ghost.framework.data.repository.query.parser.Part, java.util.Iterator)
	 */
	@Override
	protected String create(Part part, Iterator<Object> iterator) {
		return "";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.query.parser.AbstractQueryCreator#and(ghost.framework.data.repository.query.parser.Part, java.lang.Object, java.util.Iterator)
	 */
	@Override
	protected String and(Part part, String base, Iterator<Object> iterator) {
		return "";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.query.parser.AbstractQueryCreator#or(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected String or(String base, String criteria) {
		return "";
	}

	@Override
	protected KeyValueQuery<SpelExpression> complete(String criteria, Sort sort) {

		KeyValueQuery<SpelExpression> query = new KeyValueQuery<>(this.expression);

		if (sort.isSorted()) {
			query.orderBy(sort);
		}

		return query;
	}

	protected SpelExpression toPredicateExpression(PartTree tree) {

		int parameterIndex = 0;
		StringBuilder sb = new StringBuilder();

		for (Iterator<OrPart> orPartIter = tree.iterator(); orPartIter.hasNext();) {

			int partCnt = 0;
			StringBuilder partBuilder = new StringBuilder();
			OrPart orPart = orPartIter.next();

			for (Iterator<Part> partIter = orPart.iterator(); partIter.hasNext();) {

				Part part = partIter.next();

				if (!requiresInverseLookup(part)) {

					partBuilder.append("#it?.");
					partBuilder.append(part.getProperty().toDotPath().replace(".", "?."));
				}

				// TODO: check if we can have caseinsensitive search
				if (!part.shouldIgnoreCase().equals(IgnoreCaseType.NEVER)) {
					throw new InvalidDataAccessApiUsageException("Ignore case not supported!");
				}

				switch (part.getType()) {
					case TRUE:
						partBuilder.append("?.equals(true)");
						break;
					case FALSE:
						partBuilder.append("?.equals(false)");
						break;
					case SIMPLE_PROPERTY:
						partBuilder.append("?.equals(").append("[").append(parameterIndex++).append("])");
						break;
					case IS_NULL:
						partBuilder.append(" == null");
						break;
					case IS_NOT_NULL:
						partBuilder.append(" != null");
						break;
					case LIKE:
						partBuilder.append("?.contains(").append("[").append(parameterIndex++).append("])");
						break;
					case STARTING_WITH:
						partBuilder.append("?.startsWith(").append("[").append(parameterIndex++).append("])");
						break;
					case AFTER:
					case GREATER_THAN:
						partBuilder.append(">").append("[").append(parameterIndex++).append("]");
						break;
					case GREATER_THAN_EQUAL:
						partBuilder.append(">=").append("[").append(parameterIndex++).append("]");
						break;
					case BEFORE:
					case LESS_THAN:
						partBuilder.append("<").append("[").append(parameterIndex++).append("]");
						break;
					case LESS_THAN_EQUAL:
						partBuilder.append("<=").append("[").append(parameterIndex++).append("]");
						break;
					case ENDING_WITH:
						partBuilder.append("?.endsWith(").append("[").append(parameterIndex++).append("])");
						break;
					case BETWEEN:

						int index = partBuilder.lastIndexOf("#it?.");

						partBuilder.insert(index, "(");
						partBuilder.append(">").append("[").append(parameterIndex++).append("]");
						partBuilder.append("&&");
						partBuilder.append("#it?.");
						partBuilder.append(part.getProperty().toDotPath().replace(".", "?."));
						partBuilder.append("<").append("[").append(parameterIndex++).append("]");
						partBuilder.append(")");

						break;

					case REGEX:

						partBuilder.append(" matches ").append("[").append(parameterIndex++).append("]");
						break;
					case IN:

						partBuilder.append("[").append(parameterIndex++).append("].contains(");
						partBuilder.append("#it?.");
						partBuilder.append(part.getProperty().toDotPath().replace(".", "?."));
						partBuilder.append(")");
						break;

					case CONTAINING:
					case NOT_CONTAINING:
					case NEGATING_SIMPLE_PROPERTY:
					case EXISTS:
					default:
						throw new InvalidDataAccessApiUsageException(String.format("Found invalid part '%s' in query",
								part.getType()));
				}

				if (partIter.hasNext()) {
					partBuilder.append("&&");
				}

				partCnt++;
			}

			if (partCnt > 1) {
				sb.append("(").append(partBuilder).append(")");
			} else {
				sb.append(partBuilder);
			}

			if (orPartIter.hasNext()) {
				sb.append("||");
			}
		}

		return StringUtils.hasText(sb) ? PARSER.parseRaw(sb.toString()) : PARSER.parseRaw("true");
	}

	private static boolean requiresInverseLookup(Part part) {
		return part.getType() == Type.IN;
	}
}
