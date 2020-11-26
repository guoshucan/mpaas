package ghost.framework.data.mongodb.core.aggregation;

/**
 * package: ghost.framework.data.mongodb.core.aggregation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/29:22:23
 */

import ghost.framework.expression.spel.ast.Projection;
import ghost.framework.util.Assert;

/**
 * Encapsulates an output field in a bucket aggregation stage. <br />
 * Output fields can be either top-level fields that define a valid field name or nested output fields using
 * operators.
 *
 * @author Mark Paluch
 */
public abstract class Output implements AggregationExpression {

    private final ExposedFields.ExposedField field;

    /**
     * Creates new {@link Projection} for the given {@link Field}.
     *
     * @param field must not be {@literal null}.
     */
    protected Output(Field field) {

        Assert.notNull(field, "Field must not be null!");
        this.field = new ExposedFields.ExposedField(field, true);
    }

    /**
     * Returns the field exposed by the {@link Output}.
     *
     * @return will never be {@literal null}.
     */
    protected ExposedFields.ExposedField getExposedField() {
        return field;
    }
}
