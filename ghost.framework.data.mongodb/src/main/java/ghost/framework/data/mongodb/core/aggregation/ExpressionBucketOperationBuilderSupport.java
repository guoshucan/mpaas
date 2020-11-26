package ghost.framework.data.mongodb.core.aggregation;

/**
 * package: ghost.framework.data.mongodb.core.aggregation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/29:22:19
 */
/**
 * Builder for SpEL expression-based {@link Output}.
 *
 * @author Mark Paluch
 */
public abstract class ExpressionBucketOperationBuilderSupport<B extends OutputBuilder<B, T>, T extends BucketOperationSupport<T, B>>
        extends OutputBuilder<B, T> {

    /**
     * Creates a new {@link ExpressionBucketOperationBuilderSupport} for the given value, {@link BucketOperationSupport}
     * and parameters.
     *
     * @param expression must not be {@literal null}.
     * @param operation must not be {@literal null}.
     * @param parameters
     */
    protected ExpressionBucketOperationBuilderSupport(String expression, T operation, Object[] parameters) {
        super(new SpelExpressionOutput(expression, parameters), operation);
    }
}