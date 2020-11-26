/*
 * Copyright 2002-2018 the original author or authors.
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

package ghost.framework.context.annotation;

//import ghost.framework.core.type.AnnotationMetadata;
//import ghost.framework.lang.Nullable;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.core.type.AnnotationMetadata;

/**
 * Registry of imported class {@link AnnotationMetadata}.
 *
 * @author Juergen Hoeller
 * @author Phillip Webb
 */
interface ImportRegistry {

	@Nullable
	AnnotationMetadata getImportingClassFor(String importedClass);

	void removeImportingClass(String importingClass);

}
