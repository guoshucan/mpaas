/*
 * Copyright 2019-2020 the original author or authors.
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
package ghost.framework.data.jdbc.jpa.plugin.repository.support;

//import lombok.RequiredArgsConstructor;
//import ghost.framework.data.jpa.repository.query.EscapeCharacter;
//import ghost.framework.data.spel.spi.EvaluationContextExtension;

import ghost.framework.data.commons.spel.spi.EvaluationContextExtension;
import ghost.framework.data.jdbc.jpa.plugin.repository.query.EscapeCharacter;

/**
 * {@link EvaluationContextExtension} to register {@link EscapeCharacter} as root object to essentially expose an
 * {@code expose(…)} function to SpEL.
 *
 * @author Oliver Drotbohm
 */
public class JpaEvaluationContextExtension implements EvaluationContextExtension {

	private JpaRootObject root;

	/**
	 * Creates a new {@link JpaEvaluationContextExtension} for the given escape character.
	 *
	 * @param escapeCharacter the character to be used to escape parameters for LIKE expression.
	 */
	public JpaEvaluationContextExtension(char escapeCharacter) {
//		this.root = new JpaRootObject(new EscapeCharacter(escapeCharacter));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.spel.spi.EvaluationContextExtension#getExtensionId()
	 */
	@Override
	public String getExtensionId() {
		return "jpa";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.spel.spi.EvaluationContextExtension#getRootObject()
	 */
	@Override
	public Object getRootObject() {
		return root;
	}

//	@RequiredArgsConstructor(staticName = "of")
	public static class JpaRootObject {
		private EscapeCharacter character;
		/**
		 * Escapes the given source {@link String} for LIKE expressions.
		 *
		 * @param source can be {@literal null}.
		 * @return
		 * @see EscapeCharacter#escape(String)
		 */
		public String escape(String source) {
			return character.escape(source);
		}

		/**
		 * Returns the escape character being used to escape special characters for LIKE expressions.
		 *
		 * @return
		 */
		public char escapeCharacter() {
			return character.getEscapeCharacter();
		}
	}
}
