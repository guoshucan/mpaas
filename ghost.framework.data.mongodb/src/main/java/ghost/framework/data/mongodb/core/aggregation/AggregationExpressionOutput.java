package ghost.framework.data.mongodb.core.aggregation;

/**
 * package: ghost.framework.data.mongodb.core.aggregation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/29:22:24
 */

import org.bson.Document;

/**
 * @author Mark Paluch
 */
public class AggregationExpressionOutput extends Output {

    private final AggregationExpression expression;

    /**
     * Creates a new {@link AggregationExpressionOutput}.
     *
     * @param field must not be {@literal null}.
     * @param expression must not be {@literal null}.
     */
    protected AggregationExpressionOutput(Field field, AggregationExpression expression) {

        super(field);

        this.expression = expression;
    }

    /* (non-Javadoc)
     * @see ghost.framework.data.mongodb.core.aggregation.BucketOperationSupport.Output#toDocument(ghost.framework.data.mongodb.core.aggregation.AggregationOperationContext)
     */
    @Override
    public Document toDocument(AggregationOperationContext context) {
        return expression.toDocument(context);
    }
}