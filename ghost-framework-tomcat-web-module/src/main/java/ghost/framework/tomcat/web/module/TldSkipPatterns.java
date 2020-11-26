/*
 * Copyright 2012-2019 the original author or authors.
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

package ghost.framework.tomcat.web.module;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * TLD Skip Patterns used by Tomcat.
 *
 * @author Phillip Webb
 */
final class TldSkipPatterns {
	public static final Set<String> TOMCAT;
	static {
		// Same as Tomcat
		Set<String> patterns = new LinkedHashSet<>();
		patterns.add("ant-*.jar");
		patterns.add("aspectj*.jar");
		patterns.add("commons-beanutils*.jar");
		patterns.add("commons-codec*.jar");
		patterns.add("commons-collections*.jar");
		patterns.add("commons-dbcp*.jar");
		patterns.add("commons-digester*.jar");
		patterns.add("commons-fileupload*.jar");
		patterns.add("commons-httpclient*.jar");
		patterns.add("commons-io*.jar");
		patterns.add("commons-lang*.jar");
		patterns.add("commons-logging*.jar");
		patterns.add("commons-math*.jar");
		patterns.add("commons-pool*.jar");
		patterns.add("geronimo-spec-jaxrpc*.jar");
		patterns.add("h2*.jar");
		patterns.add("hamcrest*.jar");
		patterns.add("hibernate*.jar");
		patterns.add("jaxb-runtime-*.jar");
		patterns.add("jmx*.jar");
		patterns.add("jmx-tools-*.jar");
		patterns.add("jta*.jar");
		patterns.add("junit-*.jar");
		patterns.add("httpclient*.jar");
		patterns.add("log4j-*.jar");
		patterns.add("mail*.jar");
		patterns.add("org.hamcrest*.jar");
		patterns.add("slf4j*.jar");
		patterns.add("tomcat-embed-core-*.jar");
		patterns.add("tomcat-embed-logging-*.jar");
		patterns.add("tomcat-jdbc-*.jar");
		patterns.add("tomcat-juli-*.jar");
		patterns.add("tools.jar");
		patterns.add("wsdl4j*.jar");
		patterns.add("xercesImpl-*.jar");
		patterns.add("xmlParserAPIs-*.jar");
		patterns.add("xml-apis-*.jar");
		TOMCAT = Collections.unmodifiableSet(patterns);
	}
}
