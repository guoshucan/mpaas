package ghost.framework.web.context.cors;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.web.context.http.HttpMethod;

import java.time.Duration;
import java.util.List;

/**
 * package: ghost.framework.web.module.cors
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/15:13:49
 */
public interface ICorsConfiguration {
    /**
     * Set the origins to allow, e.g. {@code "https://domain1.com"}.
     * <p>The special value {@code "*"} allows all domains.
     * <p>By default this is not set.
     */
    void setAllowedOrigins(@Nullable List<String> allowedOrigins);

    /**
     * Return the configured origins to allow, or {@code null} if none.
     * @see #addAllowedOrigin(String)
     * @see #setAllowedOrigins(List)
     */
    @Nullable
    List<String> getAllowedOrigins();
    /**
     * Add an origin to allow.
     */
    void addAllowedOrigin(String origin);
    /**
     * Set the HTTP methods to allow, e.g. {@code "GET"}, {@code "POST"},
     * {@code "PUT"}, etc.
     * <p>The special value {@code "*"} allows all methods.
     * <p>If not set, only {@code "GET"} and {@code "HEAD"} are allowed.
     * <p>By default this is not set.
     * <p><strong>Note:</strong> CORS checks use values from "Forwarded"
     * (<a href="https://tools.ietf.org/html/rfc7239">RFC 7239</a>),
     * "X-Forwarded-Host", "X-Forwarded-Port", and "X-Forwarded-Proto" headers,
     * if present, in order to reflect the client-originated address.
     * Consider using the {@code ForwardedHeaderFilter} in order to choose from a
     * central place whether to extract and use, or to discard such headers.
     * See the Spring Framework reference for more on this filter.
     */
    void setAllowedMethods(@Nullable List<String> allowedMethods);
    /**
     * Return the allowed HTTP methods, or {@code null} in which case
     * only {@code "GET"} and {@code "HEAD"} allowed.
     * @see #addAllowedMethod(HttpMethod)
     * @see #addAllowedMethod(String)
     * @see #setAllowedMethods(List)
     */
    @Nullable
    List<String> getAllowedMethods();
    /**
     * Add an HTTP method to allow.
     */
    void addAllowedMethod(HttpMethod method);
    /**
     * Add an HTTP method to allow.
     */
    void addAllowedMethod(String method);
    /**
     * Set the list of headers that a pre-flight request can list as allowed
     * for use during an actual request.
     * <p>The special value {@code "*"} allows actual requests to send any
     * header.
     * <p>A header name is not required to be listed if it is one of:
     * {@code Cache-Control}, {@code Content-Language}, {@code Expires},
     * {@code Last-Modified}, or {@code Pragma}.
     * <p>By default this is not set.
     */
    void setAllowedHeaders(@Nullable List<String> allowedHeaders);
    /**
     * Return the allowed actual request headers, or {@code null} if none.
     * @see #addAllowedHeader(String)
     * @see #setAllowedHeaders(List)
     */
    @Nullable
    List<String> getAllowedHeaders();
    /**
     * Add an actual request header to allow.
     */
    void addAllowedHeader(String allowedHeader);
    /**
     * Set the list of response headers other than simple headers (i.e.
     * {@code Cache-Control}, {@code Content-Language}, {@code Content-Type},
     * {@code Expires}, {@code Last-Modified}, or {@code Pragma}) that an
     * actual response might have and can be exposed.
     * <p>Note that {@code "*"} is not a valid exposed header value.
     * <p>By default this is not set.
     */
    void setExposedHeaders(@Nullable List<String> exposedHeaders);
    /**
     * Return the configured response headers to expose, or {@code null} if none.
     * @see #addExposedHeader(String)
     * @see #setExposedHeaders(List)
     */
    @Nullable
    List<String> getExposedHeaders();
    /**
     * Add a response header to expose.
     * <p>Note that {@code "*"} is not a valid exposed header value.
     */
    void addExposedHeader(String exposedHeader);
    /**
     * Whether user credentials are supported.
     * <p>By default this is not set (i.e. user credentials are not supported).
     */
    void setAllowCredentials(@Nullable Boolean allowCredentials);
    /**
     * Return the configured {@code allowCredentials} flag, or {@code null} if none.
     * @see #setAllowCredentials(Boolean)
     */
    @Nullable
    Boolean getAllowCredentials();
    /**
     * Configure how long, as a duration, the response from a pre-flight request
     * can be cached by clients.
     * @since 5.2
     * @see #setMaxAge(Long)
     */
    void setMaxAge(Duration maxAge);
    /**
     * Configure how long, in seconds, the response from a pre-flight request
     * can be cached by clients.
     * <p>By default this is not set.
     */
    void setMaxAge(@Nullable Long maxAge);
    /**
     * Return the configured {@code maxAge} value, or {@code null} if none.
     * @see #setMaxAge(Long)
     */
    @Nullable
    Long getMaxAge();
    /**
     * By default a newly created {@code CorsConfiguration} does not permit any
     * cross-origin requests and must be configured explicitly to indicate what
     * should be allowed.
     * <p>Use this method to flip the initialization entity to start with open
     * defaults that permit all cross-origin requests for GET, HEAD, and POST
     * requests. Note however that this method will not override any existing
     * values already set.
     * <p>The following defaults are applied if not already set:
     * <ul>
     * <li>Allow all origins.</li>
     * <li>Allow "simple" methods {@code GET}, {@code HEAD} and {@code POST}.</li>
     * <li>Allow all headers.</li>
     * <li>Set max age to 1800 seconds (30 minutes).</li>
     * </ul>
     */
    ICorsConfiguration applyPermitDefaultValues();
    /**
     * Combine the non-null properties of the supplied
     * {@code CorsConfiguration} with this one.
     * <p>When combining single values like {@code allowCredentials} or
     * {@code maxAge}, {@code this} properties are overridden by non-null
     * {@code other} properties if any.
     * <p>Combining lists like {@code allowedOrigins}, {@code allowedMethods},
     * {@code allowedHeaders} or {@code exposedHeaders} is done in an additive
     * way. For example, combining {@code ["GET", "POST"]} with
     * {@code ["PATCH"]} results in {@code ["GET", "POST", "PATCH"]}, but keep
     * in mind that combining {@code ["GET", "POST"]} with {@code ["*"]}
     * results in {@code ["*"]}.
     * <p>Notice that default permit values set by
     * {@link CorsConfiguration#applyPermitDefaultValues()} are overridden by
     * any value explicitly defined.
     * @return the combined {@code CorsConfiguration}, or {@code this}
     * configuration if the supplied configuration is {@code null}
     */
    @Nullable
    ICorsConfiguration combine(@Nullable ICorsConfiguration other);
    /**
     * Check the origin of the request against the configured allowed origins.
     * @param requestOrigin the origin to check
     * @return the origin to use for the response, or {@code null} which
     * means the request origin is not allowed
     */
    @Nullable
     String checkOrigin(@Nullable String requestOrigin);
    /**
     * Check the HTTP request method (or the method from the
     * {@code Access-Control-Request-Method} header on a pre-flight request)
     * against the configured allowed methods.
     * @param requestMethod the HTTP request method to check
     * @return the list of HTTP methods to list in the response of a pre-flight
     * request, or {@code null} if the supplied {@code requestMethod} is not allowed
     */
    @Nullable
     List<HttpMethod> checkHttpMethod(@Nullable HttpMethod requestMethod);
    /**
     * Check the supplied request headers (or the headers listed in the
     * {@code Access-Control-Request-Headers} of a pre-flight request) against
     * the configured allowed headers.
     * @param requestHeaders the request headers to check
     * @return the list of allowed headers to list in the response of a pre-flight
     * request, or {@code null} if none of the supplied request headers is allowed
     */
    @Nullable
    public List<String> checkHeaders(@Nullable List<String> requestHeaders);
}