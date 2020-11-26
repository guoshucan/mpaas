package ghost.framework.data.mongodb.core.aggregation;

/**
 * package: ghost.framework.data.mongodb.core.aggregation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/29:22:20
 */
import ghost.framework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Base class for {@link Output} builders that result in a {@link BucketOperationSupport} providing the built
 * {@link Output}.
 *
 * @author Mark Paluch
 */
public abstract class OutputBuilder<B extends OutputBuilder<B, T>, T extends BucketOperationSupport<T, B>> {

    protected final Object value;
    protected final T operation;

    /**
     * Creates a new {@link OutputBuilder} for the given value and {@link BucketOperationSupport}.
     *
     * @param value must not be {@literal null}.
     * @param operation must not be {@literal null}.
     */
    protected OutputBuilder(Object value, T operation) {

        Assert.notNull(value, "Value must not be null or empty!");
        Assert.notNull(operation, "ProjectionOperation must not be null!");

        this.value = value;
        this.operation = operation;
    }

    /**
     * Generates a builder for a {@code $sum}-expression. <br />
     * Count expressions are emulated via {@code $sum: 1}.
     *
     * @return never {@literal null}.
     */
    public B count() {
        return sum(1);
    }

    /**
     * Generates a builder for a {@code $sum}-expression for the current value.
     *
     * @return never {@literal null}.
     */
    public B sum() {
        return apply(Accumulators.SUM);
    }

    /**
     * Generates a builder for a {@code $sum}-expression for the given {@literal value}.
     *
     * @param value must not be {@literal null}.
     * @return never {@literal null}.
     */
    public B sum(Number value) {
        return apply(new OperationOutput(Accumulators.SUM.getMongoOperator(), Collections.singleton(value)));
    }

    /**
     * Generates a builder for an {@code $last}-expression for the current value..
     *
     * @return never {@literal null}.
     */
    public B last() {
        return apply(Accumulators.LAST);
    }

    /**
     * Generates a builder for a {@code $first}-expression the current value.
     *
     * @return never {@literal null}.
     */
    public B first() {
        return apply(Accumulators.FIRST);
    }

    /**
     * Generates a builder for an {@code $avg}-expression for the current value.
     *
     * @return never {@literal null}.
     */
    public B avg() {
        return apply(Accumulators.AVG);
    }

    /**
     * Generates a builder for an {@code $min}-expression for the current value.
     *
     * @return never {@literal null}.
     */
    public B min() {
        return apply(Accumulators.MIN);
    }

    /**
     * Generates a builder for an {@code $max}-expression for the current value.
     *
     * @return never {@literal null}.
     */
    public B max() {
        return apply(Accumulators.MAX);
    }

    /**
     * Generates a builder for an {@code $push}-expression for the current value.
     *
     * @return never {@literal null}.
     */
    public B push() {
        return apply(Accumulators.PUSH);
    }

    /**
     * Generates a builder for an {@code $addToSet}-expression for the current value.
     *
     * @return never {@literal null}.
     */
    public B addToSet() {
        return apply(Accumulators.ADDTOSET);
    }

    /**
     * Apply an operator to the current value.
     *
     * @param operation the operation name, must not be {@literal null} or empty.
     * @param values must not be {@literal null}.
     * @return never {@literal null}.
     */
    public B apply(String operation, Object... values) {

        Assert.hasText(operation, "Operation must not be empty or null!");
        Assert.notNull(value, "Values must not be null!");

        List<Object> objects = new ArrayList<Object>(values.length + 1);
        objects.add(value);
        objects.addAll(Arrays.asList(values));
        return apply(new OperationOutput(operation, objects));
    }

    /**
     * Apply an {@link OperationOutput} to this output.
     *
     * @param operationOutput must not be {@literal null}.
     * @return never {@literal null}.
     */
    protected abstract B apply(OperationOutput operationOutput);

    private B apply(Accumulators operation) {
        return this.apply(operation.getMongoOperator());
    }

    /**
     * Returns the finally to be applied {@link BucketOperation} with the given alias.
     *
     * @param alias will never be {@literal null} or empty.
     * @return never {@literal null}.
     */
    public T as(String alias) {

        if (value instanceof OperationOutput) {
            return this.operation.andOutput(((OperationOutput) this.value).withAlias(alias));
        }

        if (value instanceof Field) {
            throw new IllegalStateException("Cannot add a field as top-level output. Use accumulator expressions.");
        }
        return this.operation
                .andOutput(new AggregationExpressionOutput(Fields.field(alias), (AggregationExpression) value));
    }
}