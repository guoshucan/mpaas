package ghost.framework.data.mongodb.core.aggregation;

/**
 * package: ghost.framework.data.mongodb.core.aggregation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/29:22:22
 */

import ghost.framework.util.Assert;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Encapsulates {@link Output}s.
 *
 * @author Mark Paluch
 */
public class Outputs implements AggregationExpression {

    protected static final Outputs EMPTY = new Outputs();

    private List<Output> outputs;

    /**
     * Creates a new, empty {@link Outputs}.
     */
    private Outputs() {
        this.outputs = new ArrayList<Output>();
    }

    /**
     * Creates new {@link Outputs} containing all given {@link Output}s.
     *
     * @param current
     * @param output
     */
    private Outputs(Collection<Output> current, Output output) {

        this.outputs = new ArrayList<Output>(current.size() + 1);
        this.outputs.addAll(current);
        this.outputs.add(output);
    }

    /**
     * @return the {@link ExposedFields} derived from {@link Output}.
     */
    protected ExposedFields asExposedFields() {

        // The count field is included by default when the output is not specified.
        if (isEmpty()) {
            return ExposedFields.from(new ExposedFields.ExposedField("count", true));
        }

        ExposedFields fields = ExposedFields.from();

        for (Output output : outputs) {
            fields = fields.and(output.getExposedField());
        }

        return fields;
    }

    /**
     * Create a new {@link Outputs} that contains the new {@link Output}.
     *
     * @param output must not be {@literal null}.
     * @return the new {@link Outputs} that contains the new {@link Output}
     */
    public Outputs and(Output output) {

        Assert.notNull(output, "BucketOutput must not be null!");
        return new Outputs(this.outputs, output);
    }

    /**
     * @return {@literal true} if {@link Outputs} contains no {@link Output}.
     */
    protected boolean isEmpty() {
        return outputs.isEmpty();
    }

    /* (non-Javadoc)
     * @see ghost.framework.data.mongodb.core.aggregation.AggregationExpression#toDocument(ghost.framework.data.mongodb.core.aggregation.AggregationOperationContext)
     */
    @Override
    public Document toDocument(AggregationOperationContext context) {

        Document document = new Document();

        for (Output output : outputs) {
            document.put(output.getExposedField().getName(), output.toDocument(context));
        }

        return document;
    }

}
