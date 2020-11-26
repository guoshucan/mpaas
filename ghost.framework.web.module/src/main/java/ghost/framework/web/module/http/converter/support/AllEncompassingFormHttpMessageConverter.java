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

package ghost.framework.web.module.http.converter.support;


import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.module.IModule;
import ghost.framework.web.module.http.converter.FormHttpMessageConverter;
import ghost.framework.web.module.http.converter.json.GsonHttpMessageConverter;
import ghost.framework.web.module.http.converter.json.JsonbHttpMessageConverter;
import ghost.framework.web.module.http.converter.json.MappingJackson2HttpMessageConverter;
import ghost.framework.web.module.http.converter.smile.MappingJackson2SmileHttpMessageConverter;
import ghost.framework.web.module.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import ghost.framework.web.module.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import ghost.framework.web.module.http.converter.xml.SourceHttpMessageConverter;

/**
 * Extension of {@link ghost.framework.http.converter.FormHttpMessageConverter},
 * adding support for XML and JSON-based parts.
 *
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @since 3.2
 */
public class AllEncompassingFormHttpMessageConverter extends FormHttpMessageConverter {
	@Autowired
	private IModule module;
	private static boolean jaxb2Present;

	private static boolean jackson2Present;

	private static boolean jackson2XmlPresent;

	private static boolean jackson2SmilePresent;

	private static boolean gsonPresent;

	private static boolean jsonbPresent;




	public AllEncompassingFormHttpMessageConverter() {
		jaxb2Present = this.module.isPresent("javax.xml.bind.Binder");
		jackson2Present = this.module.isPresent("com.fasterxml.jackson.databind.ObjectMapper") &&
				this.module.isPresent("com.fasterxml.jackson.core.JsonGenerator");
		jackson2XmlPresent = this.module.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper");
		jackson2SmilePresent = this.module.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory");
		gsonPresent = this.module.isPresent("com.google.gson.Gson");
		jsonbPresent = this.module.isPresent("javax.json.bind.Jsonb");
		try {
			addPartConverter(new SourceHttpMessageConverter<>());
		}
		catch (Error err) {
			// Ignore when no TransformerFactory implementation is available
		}

		if (jaxb2Present && !jackson2XmlPresent) {
			addPartConverter(new Jaxb2RootElementHttpMessageConverter());
		}

		if (jackson2Present) {
			addPartConverter(new MappingJackson2HttpMessageConverter());
		}
		else if (gsonPresent) {
			addPartConverter(new GsonHttpMessageConverter());
		}
		else if (jsonbPresent) {
			addPartConverter(new JsonbHttpMessageConverter());
		}

		if (jackson2XmlPresent) {
			addPartConverter(new MappingJackson2XmlHttpMessageConverter());
		}

		if (jackson2SmilePresent) {
			addPartConverter(new MappingJackson2SmileHttpMessageConverter());
		}
	}

}
