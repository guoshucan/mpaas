package ghost.framework.data.mongodb.core.aggregation;

/**
 * package: ghost.framework.data.mongodb.core.aggregation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/29:22:18
 */
/**
 * {@link ExpressionBucketOperationBuilderSupport} implementation for {@link BucketAutoOperation} using SpEL
 * expression based {@link Output}.
 *
 * @author Mark Paluch
 */
public class ExpressionBucketAutoOperationBuilder
        extends ExpressionBucketOperationBuilderSupport<BucketAutoOperationOutputBuilder, BucketAutoOperation> {
    /**
     * Creates a new {@link ExpressionBucketAutoOperationBuilder} for the given value, {@link BucketAutoOperation} and
     * parameters.
     *
     * @param expression must not be {@literal null}.
     * @param operation must not be {@literal null}.
     * @param parameters must not be {@literal null}.
     */
    protected ExpressionBucketAutoOperationBuilder(String expression, BucketAutoOperation operation,
                                                   Object[] parameters) {
        super(expression, operation, parameters);
    }

    /* (non-Javadoc)
     * @see ghost.framework.data.mongodb.core.aggregation.BucketOperationSupport.OutputBuilder#apply(ghost.framework.data.mongodb.core.aggregation.BucketOperationSupport.OperationOutput)
     */
    @Override
    protected BucketAutoOperationOutputBuilder apply(OperationOutput operationOutput) {
        return new BucketAutoOperationOutputBuilder(operationOutput, this.operation);
    }
}
