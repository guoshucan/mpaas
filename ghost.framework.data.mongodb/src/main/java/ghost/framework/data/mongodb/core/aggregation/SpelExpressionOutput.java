package ghost.framework.data.mongodb.core.aggregation;

/**
 * package: ghost.framework.data.mongodb.core.aggregation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/29:22:19
 */

import ghost.framework.util.Assert;
import org.bson.Document;

/**
 * A {@link Output} based on a SpEL expression.
 */
public class SpelExpressionOutput extends Output {

    private static final SpelExpressionTransformer TRANSFORMER = new SpelExpressionTransformer();

    private final String expression;
    private final Object[] params;

    /**
     * Creates a new {@link SpelExpressionOutput} for the given field, SpEL expression and parameters.
     *
     * @param expression must not be {@literal null} or empty.
     * @param parameters must not be {@literal null}.
     */
    public SpelExpressionOutput(String expression, Object[] parameters) {

        super(Fields.field(expression));

        Assert.hasText(expression, "Expression must not be null!");
        Assert.notNull(parameters, "Parameters must not be null!");

        this.expression = expression;
        this.params = parameters.clone();
    }

    /* (non-Javadoc)
     * @see ghost.framework.data.mongodb.core.aggregation.BucketOperationSupport.Output#toDocument(ghost.framework.data.mongodb.core.aggregation.AggregationOperationContext)
     */
    @Override
    public Document toDocument(AggregationOperationContext context) {
        return (Document) TRANSFORMER.transform(expression, context, params);
    }
}