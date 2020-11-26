package ghost.framework.web.context.utils;

/**
 * package: ghost.framework.web.module.util
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:8:52
 */

import ghost.framework.util.MultiValueMap;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

/**
 * Builder-style methods to prepare and expand a URI template with variables.
 *
 * <p>Effectively a generalization of {@link UriComponentsBuilder} but with
 * shortcuts to expand directly into {@link URI} rather than
 * {@link UriComponents} and also leaving common concerns such as encoding
 * preferences, a base URI, and others as implementation concerns.
 *
 * <p>Typically obtained via {@link UriBuilderFactory} which serves as a central
 * component configured once and used to create many URLs.
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 * @see UriBuilderFactory
 * @see UriComponentsBuilder
 */
public interface UriBuilder {

    /**
     * Set the URI scheme which may contain URI template variables,
     * and may also be {@code null} to clear the scheme of this builder.
     * @param scheme the URI scheme
     */
    UriBuilder scheme( String scheme);

    /**
     * Set the URI user info which may contain URI template variables, and
     * may also be {@code null} to clear the user info of this builder.
     * @param userInfo the URI user info
     */
    UriBuilder userInfo( String userInfo);

    /**
     * Set the URI host which may contain URI template variables, and may also
     * be {@code null} to clear the host of this builder.
     * @param host the URI host
     */
    UriBuilder host( String host);

    /**
     * Set the URI port. Passing {@code -1} will clear the port of this builder.
     * @param port the URI port
     */
    UriBuilder port(int port);

    /**
     * Set the URI port . Use this method only when the port needs to be
     * parameterized with a URI variable. Otherwise use {@link #port(int)}.
     * Passing {@code null} will clear the port of this builder.
     * @param port the URI port
     */
    UriBuilder port( String port);

    /**
     * Append the given path to the existing path of this builder.
     * The given path may contain URI template variables.
     * @param path the URI path
     */
    UriBuilder path(String path);

    /**
     * Set the path of this builder overriding the existing path values.
     * @param path the URI path, or {@code null} for an empty path
     */
    UriBuilder replacePath( String path);

    /**
     * Append path segments to the existing path. Each path segment may contain
     * URI template variables and should not contain any slashes.
     * Use {@code path("/")} subsequently to ensure a trailing slash.
     * @param pathSegments the URI path segments
     */
    UriBuilder pathSegment(String... pathSegments) throws IllegalArgumentException;

    /**
     * Append the given query to the existing query of this builder.
     * The given query may contain URI template variables.
     * <p><strong>Note:</strong> The presence of reserved characters can prevent
     * correct parsing of the URI string. For example if a query parameter
     * contains {@code '='} or {@code '&'} characters, the query string cannot
     * be parsed unambiguously. Such values should be substituted for URI
     * variables to enable correct parsing:
     * <pre class="code">
     * builder.query(&quot;filter={value}&quot;).uriString(&quot;hot&amp;cold&quot;);
     * </pre>
     * @param query the query string
     */
    UriBuilder query(String query);

    /**
     * Set the query of this builder overriding all existing query parameters.
     * @param query the query string, or {@code null} to remove all query params
     */
    UriBuilder replaceQuery( String query);

    /**
     * Append the given query parameter to the existing query parameters. The
     * given value or any of the values may contain URI template variables. If no
     * values are given, the resulting URI will contain the query parameter value
     * only (i.e. {@code ?foo} instead of {@code ?foo=bar}.
     * @param name the query parameter value
     * @param values the query parameter values
     * @see #queryParam(String, Collection)
     */
    UriBuilder queryParam(String name, Object... values);

    /**
     * Append the given query parameter to the existing query parameters. The
     * given value or any of the values may contain URI template variables. If no
     * values are given, the resulting URI will contain the query parameter value
     * only (i.e. {@code ?foo} instead of {@code ?foo=bar}.
     * @param name the query parameter value
     * @param values the query parameter values
     * @since 5.2
     * @see #queryParam(String, Object...)
     */
    UriBuilder queryParam(String name,  Collection<?> values);

    /**
     * Add the given query parameters.
     * @param params the params
     */
    UriBuilder queryParams(MultiValueMap<String, String> params);

    /**
     * Set the query parameter values overriding all existing query values for
     * the same parameter. If no values are given, the query parameter is removed.
     * @param name the query parameter value
     * @param values the query parameter values
     * @see #replaceQueryParam(String, Collection)
     */
    UriBuilder replaceQueryParam(String name, Object... values);

    /**
     * Set the query parameter values overriding all existing query values for
     * the same parameter. If no values are given, the query parameter is removed.
     * @param name the query parameter value
     * @param values the query parameter values
     * @since 5.2
     * @see #replaceQueryParam(String, Object...)
     */
    UriBuilder replaceQueryParam(String name,  Collection<?> values);

    /**
     * Set the query parameter values overriding all existing query values.
     * @param params the query parameter value
     */
    UriBuilder replaceQueryParams(MultiValueMap<String, String> params);

    /**
     * Set the URI fragment. The given fragment may contain URI template variables,
     * and may also be {@code null} to clear the fragment of this builder.
     * @param fragment the URI fragment
     */
    UriBuilder fragment( String fragment);

    /**
     * Build a {@link URI} instance and replaces URI template variables
     * with the values from an array.
     * @param uriVariables the map of URI variables
     * @return the URI
     */
    URI build(Object... uriVariables);

    /**
     * Build a {@link URI} instance and replaces URI template variables
     * with the values from a map.
     * @param uriVariables the map of URI variables
     * @return the URI
     */
    URI build(Map<String, ?> uriVariables);

}
