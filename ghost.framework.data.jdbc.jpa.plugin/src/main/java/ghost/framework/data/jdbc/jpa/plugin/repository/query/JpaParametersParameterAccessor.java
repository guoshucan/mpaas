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
//import ghost.framework.data.repository.query.Parameter;
//import ghost.framework.data.repository.query.Parameters;
//import ghost.framework.data.repository.query.ParametersParameterAccessor;

import ghost.framework.data.commons.repository.query.Parameter;
import ghost.framework.data.commons.repository.query.Parameters;

/**
 * {@link ghost.framework.data.repository.query.ParameterAccessor} based on an {@link Parameters} instance. It also
 * offers access to all the values, not just the bindable ones based on a {@link JpaParameters.JpaParameter} instance.
 *
 * @author Jens Schauder
 * @author Mark Paluch
 */
public class JpaParametersParameterAccessor extends ParametersParameterAccessor {

	/**
	 * Creates a new {@link ParametersParameterAccessor}.
	 *
	 * @param parameters must not be {@literal null}.
	 * @param values must not be {@literal null}.
	 */
	JpaParametersParameterAccessor(Parameters<?, ?> parameters, Object[] values) {
		super(parameters, values);
	}

	public <T> T getValue(Parameter parameter) {
		return super.getValue(parameter.getIndex());
	}

	@Override
	public Object[] getValues() {
		return super.getValues();
	}
}
