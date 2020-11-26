/*
 * Copyright 2002-2019 the original author or authors.
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
package ghost.framework.context;

import ghost.framework.context.utils.AntPathMatcher;
import org.junit.jupiter.api.Test;

import java.util.*;
/**
 * Unit tests for {@link AntPathMatcher}.
 *
 * @author Alef Arendsen
 * @author Seth Ladd
 * @author Juergen Hoeller
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @author Sam Brannen
 */
class AntPathMatcherTests {

	private final AntPathMatcher pathMatcher = new AntPathMatcher();


	@Test
	void match() {
		// test exact matching
		System.out.println(pathMatcher.match("test", "test"));
		System.out.println(pathMatcher.match("/test", "/test"));
		// SPR-14141
		System.out.println(pathMatcher.match("https://example.org", "https://example.org"));
		System.out.println(pathMatcher.match("/test.jpg", "test.jpg"));
		System.out.println(pathMatcher.match("test", "/test"));
		System.out.println(pathMatcher.match("/test", "test"));

		// test matching with ?'s
		System.out.println(pathMatcher.match("t?st", "test"));
		System.out.println(pathMatcher.match("??st", "test"));
		System.out.println(pathMatcher.match("tes?", "test"));
		System.out.println(pathMatcher.match("te??", "test"));
		System.out.println(pathMatcher.match("?es?", "test"));
		System.out.println(pathMatcher.match("tes?", "tes"));
		System.out.println(pathMatcher.match("tes?", "testt"));
		System.out.println(pathMatcher.match("tes?", "tsst"));

		// test matching with *'s
		System.out.println(pathMatcher.match("*", "test"));
		System.out.println(pathMatcher.match("test*", "test"));
		System.out.println(pathMatcher.match("test*", "testTest"));
		System.out.println(pathMatcher.match("test/*", "test/Test"));
		System.out.println(pathMatcher.match("test/*", "test/t"));
		System.out.println(pathMatcher.match("test/*", "test/"));
		System.out.println(pathMatcher.match("*test*", "AnothertestTest"));
		System.out.println(pathMatcher.match("*test", "Anothertest"));
		System.out.println(pathMatcher.match("*.*", "test."));
		System.out.println(pathMatcher.match("*.*", "test.test"));
		System.out.println(pathMatcher.match("*.*", "test.test.test"));
		System.out.println(pathMatcher.match("test*aaa", "testblaaaa"));
		System.out.println(pathMatcher.match("test*", "tst"));
		System.out.println(pathMatcher.match("test*", "tsttest"));
		System.out.println(pathMatcher.match("test*", "test/"));
		System.out.println(pathMatcher.match("test*", "test/t"));
		System.out.println(pathMatcher.match("test/*", "test"));
		System.out.println(pathMatcher.match("*test*", "tsttst"));
		System.out.println(pathMatcher.match("*test", "tsttst"));
		System.out.println(pathMatcher.match("*.*", "tsttst"));
		System.out.println(pathMatcher.match("test*aaa", "test"));
		System.out.println(pathMatcher.match("test*aaa", "testblaaab"));

		// test matching with ?'s and /'s
		System.out.println(pathMatcher.match("/?", "/a"));
		System.out.println(pathMatcher.match("/?/a", "/a/a"));
		System.out.println(pathMatcher.match("/a/?", "/a/b"));
		System.out.println(pathMatcher.match("/??/a", "/aa/a"));
		System.out.println(pathMatcher.match("/a/??", "/a/bb"));
		System.out.println(pathMatcher.match("/?", "/a"));

		// test matching with **'s
		System.out.println(pathMatcher.match("/**", "/testing/testing"));
		System.out.println(pathMatcher.match("/*/**", "/testing/testing"));
		System.out.println(pathMatcher.match("/**/*", "/testing/testing"));
		System.out.println(pathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla"));
		System.out.println(pathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla/bla"));
		System.out.println(pathMatcher.match("/**/test", "/bla/bla/test"));
		System.out.println(pathMatcher.match("/bla/**/**/bla", "/bla/bla/bla/bla/bla/bla"));
		System.out.println(pathMatcher.match("/bla*bla/test", "/blaXXXbla/test"));
		System.out.println(pathMatcher.match("/*bla/test", "/XXXbla/test"));
		System.out.println(pathMatcher.match("/bla*bla/test", "/blaXXXbl/test"));
		System.out.println(pathMatcher.match("/*bla/test", "XXXblab/test"));
		System.out.println(pathMatcher.match("/*bla/test", "XXXbl/test"));

		System.out.println(pathMatcher.match("/????", "/bala/bla"));
		System.out.println(pathMatcher.match("/**/*bla", "/bla/bla/bla/bbb"));

		System.out.println(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing/"));
		System.out.println(pathMatcher.match("/*bla*/**/bla/*", "/XXXblaXXXX/testing/testing/bla/testing"));
		System.out.println(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing"));
		System.out.println(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing.jpg"));

		System.out.println(pathMatcher.match("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing/"));
		System.out.println(pathMatcher.match("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing"));
		System.out.println(pathMatcher.match("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing"));
		System.out.println(pathMatcher.match("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing/testing"));

		System.out.println(pathMatcher.match("/x/x/**/bla", "/x/x/x/"));

		System.out.println(pathMatcher.match("/foo/bar/**", "/foo/bar"));

		System.out.println(pathMatcher.match("", ""));

		System.out.println(pathMatcher.match("/{bla}.*", "/testing.html"));
	}

	@Test
	void matchWithNullPath() {
		System.out.println(pathMatcher.match("/test", null));
		System.out.println(pathMatcher.match("/", null));
		System.out.println(pathMatcher.match(null, null));
	}

	// SPR-14247
	@Test
	void matchWithTrimTokensEnabled() throws Exception {
		pathMatcher.setTrimTokens(true);

		System.out.println(pathMatcher.match("/foo/bar", "/foo /bar"));
	}

	@Test
	void matchStart() {
		// test exact matching
		System.out.println(pathMatcher.matchStart("test", "test"));
		System.out.println(pathMatcher.matchStart("/test", "/test"));
		System.out.println(pathMatcher.matchStart("/test.jpg", "test.jpg"));
		System.out.println(pathMatcher.matchStart("test", "/test"));
		System.out.println(pathMatcher.matchStart("/test", "test"));

		// test matching with ?'s
		System.out.println(pathMatcher.matchStart("t?st", "test"));
		System.out.println(pathMatcher.matchStart("??st", "test"));
		System.out.println(pathMatcher.matchStart("tes?", "test"));
		System.out.println(pathMatcher.matchStart("te??", "test"));
		System.out.println(pathMatcher.matchStart("?es?", "test"));
		System.out.println(pathMatcher.matchStart("tes?", "tes"));
		System.out.println(pathMatcher.matchStart("tes?", "testt"));
		System.out.println(pathMatcher.matchStart("tes?", "tsst"));

		// test matching with *'s
		System.out.println(pathMatcher.matchStart("*", "test"));
		System.out.println(pathMatcher.matchStart("test*", "test"));
		System.out.println(pathMatcher.matchStart("test*", "testTest"));
		System.out.println(pathMatcher.matchStart("test/*", "test/Test"));
		System.out.println(pathMatcher.matchStart("test/*", "test/t"));
		System.out.println(pathMatcher.matchStart("test/*", "test/"));
		System.out.println(pathMatcher.matchStart("*test*", "AnothertestTest"));
		System.out.println(pathMatcher.matchStart("*test", "Anothertest"));
		System.out.println(pathMatcher.matchStart("*.*", "test."));
		System.out.println(pathMatcher.matchStart("*.*", "test.test"));
		System.out.println(pathMatcher.matchStart("*.*", "test.test.test"));
		System.out.println(pathMatcher.matchStart("test*aaa", "testblaaaa"));
		System.out.println(pathMatcher.matchStart("test*", "tst"));
		System.out.println(pathMatcher.matchStart("test*", "test/"));
		System.out.println(pathMatcher.matchStart("test*", "tsttest"));
		System.out.println(pathMatcher.matchStart("test*", "test/"));
		System.out.println(pathMatcher.matchStart("test*", "test/t"));
		System.out.println(pathMatcher.matchStart("test/*", "test"));
		System.out.println(pathMatcher.matchStart("test/t*.txt", "test"));
		System.out.println(pathMatcher.matchStart("*test*", "tsttst"));
		System.out.println(pathMatcher.matchStart("*test", "tsttst"));
		System.out.println(pathMatcher.matchStart("*.*", "tsttst"));
		System.out.println(pathMatcher.matchStart("test*aaa", "test"));
		System.out.println(pathMatcher.matchStart("test*aaa", "testblaaab"));

		// test matching with ?'s and /'s
		System.out.println(pathMatcher.matchStart("/?", "/a"));
		System.out.println(pathMatcher.matchStart("/?/a", "/a/a"));
		System.out.println(pathMatcher.matchStart("/a/?", "/a/b"));
		System.out.println(pathMatcher.matchStart("/??/a", "/aa/a"));
		System.out.println(pathMatcher.matchStart("/a/??", "/a/bb"));
		System.out.println(pathMatcher.matchStart("/?", "/a"));

		// test matching with **'s
		System.out.println(pathMatcher.matchStart("/**", "/testing/testing"));
		System.out.println(pathMatcher.matchStart("/*/**", "/testing/testing"));
		System.out.println(pathMatcher.matchStart("/**/*", "/testing/testing"));
		System.out.println(pathMatcher.matchStart("test*/**", "test/"));
		System.out.println(pathMatcher.matchStart("test*/**", "test/t"));
		System.out.println(pathMatcher.matchStart("/bla/**/bla", "/bla/testing/testing/bla"));
		System.out.println(pathMatcher.matchStart("/bla/**/bla", "/bla/testing/testing/bla/bla"));
		System.out.println(pathMatcher.matchStart("/**/test", "/bla/bla/test"));
		System.out.println(pathMatcher.matchStart("/bla/**/**/bla", "/bla/bla/bla/bla/bla/bla"));
		System.out.println(pathMatcher.matchStart("/bla*bla/test", "/blaXXXbla/test"));
		System.out.println(pathMatcher.matchStart("/*bla/test", "/XXXbla/test"));
		System.out.println(pathMatcher.matchStart("/bla*bla/test", "/blaXXXbl/test"));
		System.out.println(pathMatcher.matchStart("/*bla/test", "XXXblab/test"));
		System.out.println(pathMatcher.matchStart("/*bla/test", "XXXbl/test"));

		System.out.println(pathMatcher.matchStart("/????", "/bala/bla"));
		System.out.println(pathMatcher.matchStart("/**/*bla", "/bla/bla/bla/bbb"));

		System.out.println(pathMatcher.matchStart("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing/"));
		System.out.println(pathMatcher.matchStart("/*bla*/**/bla/*", "/XXXblaXXXX/testing/testing/bla/testing"));
		System.out.println(pathMatcher.matchStart("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing"));
		System.out.println(pathMatcher.matchStart("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing.jpg"));

		System.out.println(pathMatcher.matchStart("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing/"));
		System.out.println(pathMatcher.matchStart("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing"));
		System.out.println(pathMatcher.matchStart("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing"));
		System.out.println(pathMatcher.matchStart("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing/testing"));

		System.out.println(pathMatcher.matchStart("/x/x/**/bla", "/x/x/x/"));

		System.out.println(pathMatcher.matchStart("", ""));
	}

	@Test
	void uniqueDeliminator() {
		pathMatcher.setPathSeparator(".");

		// test exact matching
		System.out.println(pathMatcher.match("test", "test"));
		System.out.println(pathMatcher.match(".test", ".test"));
		System.out.println(pathMatcher.match(".test/jpg", "test/jpg"));
		System.out.println(pathMatcher.match("test", ".test"));
		System.out.println(pathMatcher.match(".test", "test"));

		// test matching with ?'s
		System.out.println(pathMatcher.match("t?st", "test"));
		System.out.println(pathMatcher.match("??st", "test"));
		System.out.println(pathMatcher.match("tes?", "test"));
		System.out.println(pathMatcher.match("te??", "test"));
		System.out.println(pathMatcher.match("?es?", "test"));
		System.out.println(pathMatcher.match("tes?", "tes"));
		System.out.println(pathMatcher.match("tes?", "testt"));
		System.out.println(pathMatcher.match("tes?", "tsst"));

		// test matching with *'s
		System.out.println(pathMatcher.match("*", "test"));
		System.out.println(pathMatcher.match("test*", "test"));
		System.out.println(pathMatcher.match("test*", "testTest"));
		System.out.println(pathMatcher.match("*test*", "AnothertestTest"));
		System.out.println(pathMatcher.match("*test", "Anothertest"));
		System.out.println(pathMatcher.match("*/*", "test/"));
		System.out.println(pathMatcher.match("*/*", "test/test"));
		System.out.println(pathMatcher.match("*/*", "test/test/test"));
		System.out.println(pathMatcher.match("test*aaa", "testblaaaa"));
		System.out.println(pathMatcher.match("test*", "tst"));
		System.out.println(pathMatcher.match("test*", "tsttest"));
		System.out.println(pathMatcher.match("*test*", "tsttst"));
		System.out.println(pathMatcher.match("*test", "tsttst"));
		System.out.println(pathMatcher.match("*/*", "tsttst"));
		System.out.println(pathMatcher.match("test*aaa", "test"));
		System.out.println(pathMatcher.match("test*aaa", "testblaaab"));

		// test matching with ?'s and .'s
		System.out.println(pathMatcher.match(".?", ".a"));
		System.out.println(pathMatcher.match(".?.a", ".a.a"));
		System.out.println(pathMatcher.match(".a.?", ".a.b"));
		System.out.println(pathMatcher.match(".??.a", ".aa.a"));
		System.out.println(pathMatcher.match(".a.??", ".a.bb"));
		System.out.println(pathMatcher.match(".?", ".a"));

		// test matching with **'s
		System.out.println(pathMatcher.match(".**", ".testing.testing"));
		System.out.println(pathMatcher.match(".*.**", ".testing.testing"));
		System.out.println(pathMatcher.match(".**.*", ".testing.testing"));
		System.out.println(pathMatcher.match(".bla.**.bla", ".bla.testing.testing.bla"));
		System.out.println(pathMatcher.match(".bla.**.bla", ".bla.testing.testing.bla.bla"));
		System.out.println(pathMatcher.match(".**.test", ".bla.bla.test"));
		System.out.println(pathMatcher.match(".bla.**.**.bla", ".bla.bla.bla.bla.bla.bla"));
		System.out.println(pathMatcher.match(".bla*bla.test", ".blaXXXbla.test"));
		System.out.println(pathMatcher.match(".*bla.test", ".XXXbla.test"));
		System.out.println(pathMatcher.match(".bla*bla.test", ".blaXXXbl.test"));
		System.out.println(pathMatcher.match(".*bla.test", "XXXblab.test"));
		System.out.println(pathMatcher.match(".*bla.test", "XXXbl.test"));
	}

	@Test
	void extractPathWithinPattern() throws Exception {
		System.out.println(pathMatcher.extractPathWithinPattern("/docs/commit.html", "/docs/commit.html").equals(""));

		System.out.println(pathMatcher.extractPathWithinPattern("/docs/*", "/docs/cvs/commit").equals("cvs/commit"));
		System.out.println(pathMatcher.extractPathWithinPattern("/docs/cvs/*.html", "/docs/cvs/commit.html").equals("commit.html"));
		System.out.println(pathMatcher.extractPathWithinPattern("/docs/**", "/docs/cvs/commit").equals("cvs/commit"));
		System.out.println(pathMatcher.extractPathWithinPattern("/docs/**/*.html", "/docs/cvs/commit.html").equals("cvs/commit.html"));
		System.out.println(pathMatcher.extractPathWithinPattern("/docs/**/*.html", "/docs/commit.html").equals("commit.html"));
		System.out.println(pathMatcher.extractPathWithinPattern("/*.html", "/commit.html").equals("commit.html"));
		System.out.println(pathMatcher.extractPathWithinPattern("/*.html", "/docs/commit.html").equals("docs/commit.html"));
		System.out.println(pathMatcher.extractPathWithinPattern("*.html", "/commit.html").equals("/commit.html"));
		System.out.println(pathMatcher.extractPathWithinPattern("*.html", "/docs/commit.html").equals("/docs/commit.html"));
		System.out.println(pathMatcher.extractPathWithinPattern("**/*.*", "/docs/commit.html").equals("/docs/commit.html"));
		System.out.println(pathMatcher.extractPathWithinPattern("*", "/docs/commit.html").equals("/docs/commit.html"));
		// SPR-10515
		System.out.println(pathMatcher.extractPathWithinPattern("**/commit.html", "/docs/cvs/other/commit.html").equals("/docs/cvs/other/commit.html"));
		System.out.println(pathMatcher.extractPathWithinPattern("/docs/**/commit.html", "/docs/cvs/other/commit.html").equals("cvs/other/commit.html"));
		System.out.println(pathMatcher.extractPathWithinPattern("/docs/**/**/**/**", "/docs/cvs/other/commit.html").equals("cvs/other/commit.html"));

		System.out.println(pathMatcher.extractPathWithinPattern("/d?cs/*", "/docs/cvs/commit").equals("docs/cvs/commit"));
		System.out.println(pathMatcher.extractPathWithinPattern("/docs/c?s/*.html", "/docs/cvs/commit.html").equals("cvs/commit.html"));
		System.out.println(pathMatcher.extractPathWithinPattern("/d?cs/**", "/docs/cvs/commit").equals("docs/cvs/commit"));
		System.out.println(pathMatcher.extractPathWithinPattern("/d?cs/**/*.html", "/docs/cvs/commit.html").equals("docs/cvs/commit.html"));
	}

	@Test
	void extractUriTemplateVariables() throws Exception {
		Map<String, String> result = pathMatcher.extractUriTemplateVariables("/hotels/{hotel}", "/hotels/1");
		System.out.println(result.equals(Collections.singletonMap("hotel", "1")));

		result = pathMatcher.extractUriTemplateVariables("/h?tels/{hotel}", "/hotels/1");
		System.out.println(result.equals(Collections.singletonMap("hotel", "1")));

		result = pathMatcher.extractUriTemplateVariables("/hotels/{hotel}/bookings/{booking}", "/hotels/1/bookings/2");
		Map<String, String> expected = new LinkedHashMap<>();
		expected.put("hotel", "1");
		expected.put("booking", "2");
		System.out.println(result.equals(expected));

		result = pathMatcher.extractUriTemplateVariables("/**/hotels/**/{hotel}", "/foo/hotels/bar/1");
		System.out.println(result.equals(Collections.singletonMap("hotel", "1")));

		result = pathMatcher.extractUriTemplateVariables("/{page}.html", "/42.html");
		System.out.println(result.equals(Collections.singletonMap("page", "42")));

		result = pathMatcher.extractUriTemplateVariables("/{page}.*", "/42.html");
		System.out.println(result.equals(Collections.singletonMap("page", "42")));

		result = pathMatcher.extractUriTemplateVariables("/A-{B}-C", "/A-b-C");
		System.out.println(result.equals(Collections.singletonMap("B", "b")));

		result = pathMatcher.extractUriTemplateVariables("/{name}.{extension}", "/test.html");
		expected = new LinkedHashMap<>();
		expected.put("name", "test");
		expected.put("extension", "html");
		System.out.println(result.equals(expected));
	}

	@Test
	void extractUriTemplateVariablesRegex() {
		Map<String, String> result = pathMatcher
				.extractUriTemplateVariables("{symbolicName:[\\w\\.]+}-{version:[\\w\\.]+}.jar",
						"com.example-1.0.0.jar");
		System.out.println(result.get("symbolicName").equals("com.example"));
		System.out.println(result.get("version").equals("1.0.0"));

		result = pathMatcher.extractUriTemplateVariables("{symbolicName:[\\w\\.]+}-sources-{version:[\\w\\.]+}.jar",
				"com.example-sources-1.0.0.jar");
		System.out.println(result.get("symbolicName").equals("com.example"));
		System.out.println(result.get("version").equals("1.0.0"));
	}

	/**
	 * SPR-7787
	 */
	@Test
	void extractUriTemplateVarsRegexQualifiers() {
		Map<String, String> result = pathMatcher.extractUriTemplateVariables(
				"{symbolicName:[\\p{L}\\.]+}-sources-{version:[\\p{N}\\.]+}.jar",
				"com.example-sources-1.0.0.jar");
		System.out.println(result.get("symbolicName").equals("com.example"));
		System.out.println(result.get("version").equals("1.0.0"));

		result = pathMatcher.extractUriTemplateVariables(
				"{symbolicName:[\\w\\.]+}-sources-{version:[\\d\\.]+}-{year:\\d{4}}{month:\\d{2}}{day:\\d{2}}.jar",
				"com.example-sources-1.0.0-20100220.jar");
		System.out.println(result.get("symbolicName").equals("com.example"));
		System.out.println(result.get("version").equals("1.0.0"));
		System.out.println(result.get("year").equals("2010"));
		System.out.println(result.get("month").equals("02"));
		System.out.println(result.get("day").equals("20"));

		result = pathMatcher.extractUriTemplateVariables(
				"{symbolicName:[\\p{L}\\.]+}-sources-{version:[\\p{N}\\.\\{\\}]+}.jar",
				"com.example-sources-1.0.0.{12}.jar");
		System.out.println(result.get("symbolicName").equals("com.example"));
		System.out.println(result.get("version").equals("1.0.0.{12}"));
	}

	/**
	 * SPR-8455
	 */
	@Test
	void extractUriTemplateVarsRegexCapturingGroups() {
		System.out.println(pathMatcher.extractUriTemplateVariables("/web/{id:foo(bar)?}", "/web/foobar"));
	}

	@Test
	void combine() {
		System.out.println(pathMatcher.combine(null, null).equals(""));
		System.out.println(pathMatcher.combine("/hotels", null).equals("/hotels"));
		System.out.println(pathMatcher.combine(null, "/hotels").equals("/hotels"));
		System.out.println(pathMatcher.combine("/hotels/*", "booking").equals("/hotels/booking"));
		System.out.println(pathMatcher.combine("/hotels/*", "/booking").equals("/hotels/booking"));
		System.out.println(pathMatcher.combine("/hotels/**", "booking").equals("/hotels/**/booking"));
		System.out.println(pathMatcher.combine("/hotels/**", "/booking").equals("/hotels/**/booking"));
		System.out.println(pathMatcher.combine("/hotels", "/booking").equals("/hotels/booking"));
		System.out.println(pathMatcher.combine("/hotels", "booking").equals("/hotels/booking"));
		System.out.println(pathMatcher.combine("/hotels/", "booking").equals("/hotels/booking"));
		System.out.println(pathMatcher.combine("/hotels/*", "{hotel}").equals("/hotels/{hotel}"));
		System.out.println(pathMatcher.combine("/hotels/**", "{hotel}").equals("/hotels/**/{hotel}"));
		System.out.println(pathMatcher.combine("/hotels", "{hotel}").equals("/hotels/{hotel}"));
		System.out.println(pathMatcher.combine("/hotels", "{hotel}.*").equals("/hotels/{hotel}.*"));
		System.out.println(pathMatcher.combine("/hotels/*/booking", "{booking}").equals("/hotels/*/booking/{booking}"));
		System.out.println(pathMatcher.combine("/*.html", "/hotel.html").equals("/hotel.html"));
		System.out.println(pathMatcher.combine("/*.html", "/hotel").equals("/hotel.html"));
		System.out.println(pathMatcher.combine("/*.html", "/hotel.*").equals("/hotel.html"));
		System.out.println(pathMatcher.combine("/**", "/*.html").equals("/*.html"));
		System.out.println(pathMatcher.combine("/*", "/*.html").equals("/*.html"));
		System.out.println(pathMatcher.combine("/*.*", "/*.html").equals("/*.html"));
		// SPR-8858
		System.out.println(pathMatcher.combine("/{foo}", "/bar").equals("/{foo}/bar"));
		// SPR-7970
		System.out.println(pathMatcher.combine("/user", "/user").equals("/user/user"));
		// SPR-10062
		System.out.println(pathMatcher.combine("/{foo:.*[^0-9].*}", "/edit/").equals("/{foo:.*[^0-9].*}/edit/"));
		// SPR-10554
		System.out.println(pathMatcher.combine("/1.0", "/foo/test").equals("/1.0/foo/test"));
		// SPR-12975
		System.out.println(pathMatcher.combine("/", "/hotel").equals("/hotel"));
		// SPR-12975
		System.out.println(pathMatcher.combine("/hotel/", "/booking").equals("/hotel/booking"));
	}

	@Test
	void combineWithTwoFileExtensionPatterns() {
		System.out.println(	pathMatcher.combine("/*.html", "/*.txt"));
	}

	@Test
	void patternComparator() {
		Comparator<String> comparator = pathMatcher.getPatternComparator("/hotels/new");

		System.out.println(comparator.compare(null, null)==0);
		System.out.println(comparator.compare(null, "/hotels/new")==1);
		System.out.println(comparator.compare("/hotels/new", null)==-1);

		System.out.println(comparator.compare("/hotels/new", "/hotels/new")==0);

		System.out.println(comparator.compare("/hotels/new", "/hotels/*")==-1);
		System.out.println(comparator.compare("/hotels/*", "/hotels/new")==1);
		System.out.println(comparator.compare("/hotels/*", "/hotels/*")==0);

		System.out.println(comparator.compare("/hotels/new", "/hotels/{hotel}")==-1);
		System.out.println(comparator.compare("/hotels/{hotel}", "/hotels/new")==1);
		System.out.println(comparator.compare("/hotels/{hotel}", "/hotels/{hotel}")==0);
		System.out.println(comparator.compare("/hotels/{hotel}/booking", "/hotels/{hotel}/bookings/{booking}")==-1);
		System.out.println(comparator.compare("/hotels/{hotel}/bookings/{booking}", "/hotels/{hotel}/booking")==1);

		// SPR-10550
		System.out.println(comparator.compare("/hotels/{hotel}/bookings/{booking}/cutomers/{customer}", "/**")==-1);
		System.out.println(comparator.compare("/**", "/hotels/{hotel}/bookings/{booking}/cutomers/{customer}")==1);
		System.out.println(comparator.compare("/**", "/**")==0);

		System.out.println(comparator.compare("/hotels/{hotel}", "/hotels/*")==-1);
		System.out.println(comparator.compare("/hotels/*", "/hotels/{hotel}")==1);

		System.out.println(comparator.compare("/hotels/*", "/hotels/*/**")==-1);
		System.out.println(comparator.compare("/hotels/*/**", "/hotels/*")==1);

		System.out.println(comparator.compare("/hotels/new", "/hotels/new.*")==-1);
		System.out.println(comparator.compare("/hotels/{hotel}", "/hotels/{hotel}.*")==2);

		// SPR-6741
		System.out.println(comparator.compare("/hotels/{hotel}/bookings/{booking}/cutomers/{customer}", "/hotels/**")==-1);
		System.out.println(comparator.compare("/hotels/**", "/hotels/{hotel}/bookings/{booking}/cutomers/{customer}")==1);
		System.out.println(comparator.compare("/hotels/foo/bar/**", "/hotels/{hotel}")==1);
		System.out.println(comparator.compare("/hotels/{hotel}", "/hotels/foo/bar/**")==-1);

		// gh-23125
		System.out.println(comparator.compare("/hotels/*/bookings/**", "/hotels/**")==-11);

		// SPR-8683
		System.out.println(comparator.compare("/**", "/hotels/{hotel}")==1);

		// longer is better
		System.out.println(comparator.compare("/hotels", "/hotels2")==1);

		// SPR-13139
		System.out.println(comparator.compare("*", "*/**")==-1);
		System.out.println(comparator.compare("*/**", "*")==1);
	}

	@Test
	void patternComparatorSort() {
		Comparator<String> comparator = pathMatcher.getPatternComparator("/hotels/new");
		List<String> paths = new ArrayList<>(3);

		paths.add(null);
		paths.add("/hotels/new");
		Collections.sort(paths, comparator);
		System.out.println(paths.get(0).equals("/hotels/new"));
		System.out.println(paths.get(1));
		paths.clear();

		paths.add("/hotels/new");
		paths.add(null);
		Collections.sort(paths, comparator);
		System.out.println(paths.get(0).equals("/hotels/new"));
		System.out.println(paths.get(1));
		paths.clear();

		paths.add("/hotels/*");
		paths.add("/hotels/new");
		Collections.sort(paths, comparator);
		System.out.println(paths.get(0).equals("/hotels/new"));
		System.out.println(paths.get(1).equals("/hotels/*"));
		paths.clear();

		paths.add("/hotels/new");
		paths.add("/hotels/*");
		Collections.sort(paths, comparator);
		System.out.println(paths.get(0).equals("/hotels/new"));
		System.out.println(paths.get(1).equals("/hotels/*"));
		paths.clear();

		paths.add("/hotels/**");
		paths.add("/hotels/*");
		Collections.sort(paths, comparator);
		System.out.println(paths.get(0).equals("/hotels/*"));
		System.out.println(paths.get(1).equals("/hotels/**"));
		paths.clear();

		paths.add("/hotels/*");
		paths.add("/hotels/**");
		Collections.sort(paths, comparator);
		System.out.println(paths.get(0).equals("/hotels/*"));
		System.out.println(paths.get(1).equals("/hotels/**"));
		paths.clear();

		paths.add("/hotels/{hotel}");
		paths.add("/hotels/new");
		Collections.sort(paths, comparator);
		System.out.println(paths.get(0).equals("/hotels/new"));
		System.out.println(paths.get(1).equals("/hotels/{hotel}"));
		paths.clear();

		paths.add("/hotels/new");
		paths.add("/hotels/{hotel}");
		Collections.sort(paths, comparator);
		System.out.println(paths.get(0).equals("/hotels/new"));
		System.out.println(paths.get(1).equals("/hotels/{hotel}"));
		paths.clear();

		paths.add("/hotels/*");
		paths.add("/hotels/{hotel}");
		paths.add("/hotels/new");
		Collections.sort(paths, comparator);
		System.out.println(paths.get(0).equals("/hotels/new"));
		System.out.println(paths.get(1).equals("/hotels/{hotel}"));
		System.out.println(paths.get(2).equals("/hotels/*"));
		paths.clear();

		paths.add("/hotels/ne*");
		paths.add("/hotels/n*");
		Collections.shuffle(paths);
		Collections.sort(paths, comparator);
		System.out.println(paths.get(0).equals("/hotels/ne*"));
		System.out.println(paths.get(1).equals("/hotels/n*"));
		paths.clear();

		comparator = pathMatcher.getPatternComparator("/hotels/new.html");
		paths.add("/hotels/new.*");
		paths.add("/hotels/{hotel}");
		Collections.shuffle(paths);
		Collections.sort(paths, comparator);
		System.out.println(paths.get(0).equals("/hotels/new.*"));
		System.out.println(paths.get(1).equals("/hotels/{hotel}"));
		paths.clear();

		comparator = pathMatcher.getPatternComparator("/web/endUser/action/login.html");
		paths.add("/**/login.*");
		paths.add("/**/endUser/action/login.*");
		Collections.sort(paths, comparator);
		System.out.println(paths.get(0).equals("/**/endUser/action/login.*"));
		System.out.println(paths.get(1).equals("/**/login.*"));
		paths.clear();
	}

	@Test
        // SPR-8687
	void trimTokensOff() {
		pathMatcher.setTrimTokens(false);
		System.out.println(pathMatcher.match("/group/{groupName}/members", "/group/sales/members"));
		System.out.println(pathMatcher.match("/group/{groupName}/members", "/group/  sales/members"));
		System.out.println(pathMatcher.match("/group/{groupName}/members", "/Group/  Sales/Members"));
	}

	@Test
        // SPR-13286
	void caseInsensitive() {
		pathMatcher.setCaseSensitive(false);

		System.out.println(pathMatcher.match("/group/{groupName}/members", "/group/sales/members"));
		System.out.println(pathMatcher.match("/group/{groupName}/members", "/Group/Sales/Members"));
		System.out.println(pathMatcher.match("/Group/{groupName}/Members", "/group/Sales/members"));
	}

	@Test
	void defaultCacheSetting() {
		match();
		System.out.println(pathMatcher.stringMatcherCache.size() > 20);

		for (int i = 0; i < 65536; i++) {
			pathMatcher.match("test" + i, "test");
		}
		// Cache turned off because it went beyond the threshold
		System.out.println(pathMatcher.stringMatcherCache.isEmpty());
	}

	@Test
	void cachePatternsSetToTrue() {
		pathMatcher.setCachePatterns(true);
		match();
		System.out.println(pathMatcher.stringMatcherCache.size() > 20);

		for (int i = 0; i < 65536; i++) {
			pathMatcher.match("test" + i, "test" + i);
		}
		// Cache keeps being alive due to the explicit cache setting
		System.out.println(pathMatcher.stringMatcherCache.size() > 65536);
	}

	@Test
	void preventCreatingStringMatchersIfPathDoesNotStartsWithPatternPrefix() {
		pathMatcher.setCachePatterns(true);
		System.out.println(pathMatcher.stringMatcherCache.size()==0);

		pathMatcher.match("test?", "test");
		System.out.println(pathMatcher.stringMatcherCache.size()==1);

		pathMatcher.match("test?", "best");
		pathMatcher.match("test/*", "view/test.jpg");
		pathMatcher.match("test/**/test.jpg", "view/test.jpg");
		pathMatcher.match("test/{name}.jpg", "view/test.jpg");
		System.out.println(pathMatcher.stringMatcherCache.size()==1);
	}

	@Test
	void creatingStringMatchersIfPatternPrefixCannotDetermineIfPathMatch() {
		pathMatcher.setCachePatterns(true);
		System.out.println(pathMatcher.stringMatcherCache.size()==0);

		pathMatcher.match("test", "testian");
		pathMatcher.match("test?", "testFf");
		pathMatcher.match("test/*", "test/dir/name.jpg");
		pathMatcher.match("test/{name}.jpg", "test/lorem.jpg");
		pathMatcher.match("bla/**/test.jpg", "bla/test.jpg");
		pathMatcher.match("**/{name}.jpg", "test/lorem.jpg");
		pathMatcher.match("/**/{name}.jpg", "/test/lorem.jpg");
		pathMatcher.match("/*/dir/{name}.jpg", "/*/dir/lorem.jpg");

		System.out.println(pathMatcher.stringMatcherCache.size()==7);
	}

	@Test
	void cachePatternsSetToFalse() {
		pathMatcher.setCachePatterns(false);
		match();
		System.out.println(pathMatcher.stringMatcherCache.isEmpty());
	}

	@Test
	void extensionMappingWithDotPathSeparator() {
		pathMatcher.setPathSeparator(".");
		System.out.println(pathMatcher.combine("/*.html", "hotel.*"));//.as("Extension mapping should be disabled with \".\" as path separator").isEqualTo("/*.html.hotel.*");
	}

	@Test
        // gh-22959
	void isPattern() {
		System.out.println(pathMatcher.isPattern("/test/*"));
		System.out.println(pathMatcher.isPattern("/test/**/name"));
		System.out.println(pathMatcher.isPattern("/test?"));
		System.out.println(pathMatcher.isPattern("/test/{name}"));

		System.out.println(pathMatcher.isPattern("/test/name"));
		System.out.println(pathMatcher.isPattern("/test/foo{bar"));
	}

	@Test
        // gh-23297
	void isPatternWithNullPath() {
		System.out.println(pathMatcher.isPattern(null));
	}
}