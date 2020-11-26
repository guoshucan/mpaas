package ghost.framework.data.mongodb.core.aggregation;

/**
 * package: ghost.framework.data.mongodb.core.aggregation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/29:22:17
 */
/**
 * {@link OutputBuilder} implementation for {@link BucketAutoOperation}.
 */
public class BucketAutoOperationOutputBuilder extends OutputBuilder<BucketAutoOperationOutputBuilder, BucketAutoOperation> {

    /**
     * Creates a new {@link BucketAutoOperationOutputBuilder} fot the given value and {@link BucketAutoOperation}.
     *
     * @param value must not be {@literal null}.
     * @param operation must not be {@literal null}.
     */
    protected BucketAutoOperationOutputBuilder(Object value, BucketAutoOperation operation) {
        super(value, operation);
    }

    /* (non-Javadoc)
     * @see ghost.framework.data.mongodb.core.aggregation.BucketOperationSupport.OutputBuilder#apply(ghost.framework.data.mongodb.core.aggregation.BucketOperationSupport.OperationOutput)
     */
    @Override
    protected BucketAutoOperationOutputBuilder apply(OperationOutput operationOutput) {
        return new BucketAutoOperationOutputBuilder(operationOutput, this.operation);
    }
}