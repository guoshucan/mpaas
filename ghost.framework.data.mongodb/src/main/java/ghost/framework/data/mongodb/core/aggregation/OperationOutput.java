package ghost.framework.data.mongodb.core.aggregation;

/**
 * package: ghost.framework.data.mongodb.core.aggregation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/29:22:23
 */

import ghost.framework.util.Assert;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Output field that uses a Mongo operation (expression object) to generate an output field value. <br />
 * {@link OperationOutput} is used either with a regular field name or an operation keyword (e.g.
 * {@literal $sum, $count}).
 *
 * @author Mark Paluch
 */
public class OperationOutput extends Output {

    private final String operation;
    private final List<Object> values;

    /**
     * Creates a new {@link Output} for the given field.
     *
     * @param operation the actual operation key, must not be {@literal null} or empty.
     * @param values the values to pass into the operation, must not be {@literal null}.
     */
    public OperationOutput(String operation, Collection<? extends Object> values) {

        super(Fields.field(operation));

        Assert.hasText(operation, "Operation must not be null or empty!");
        Assert.notNull(values, "Values must not be null!");

        this.operation = operation;
        this.values = new ArrayList<Object>(values);
    }

    private OperationOutput(Field field, OperationOutput operationOutput) {

        super(field);

        this.operation = operationOutput.operation;
        this.values = operationOutput.values;
    }

    /*
     * (non-Javadoc)
     * @see ghost.framework.data.mongodb.core.aggregation.ProjectionOperation.Projection#toDocument(ghost.framework.data.mongodb.core.aggregation.AggregationOperationContext)
     */
    @Override
    public Document toDocument(AggregationOperationContext context) {

        List<Object> operationArguments = getOperationArguments(context);
        return new Document(operation, operationArguments.size() == 1 ? operationArguments.get(0) : operationArguments);
    }

    protected List<Object> getOperationArguments(AggregationOperationContext context) {

        List<Object> result = new ArrayList<Object>(values != null ? values.size() : 1);

        for (Object element : values) {

            if (element instanceof Field) {
                result.add(context.getReference((Field) element).toString());
            } else if (element instanceof Fields) {
                for (Field field : (Fields) element) {
                    result.add(context.getReference(field).toString());
                }
            } else if (element instanceof AggregationExpression) {
                result.add(((AggregationExpression) element).toDocument(context));
            } else {
                result.add(element);
            }
        }

        return result;
    }

    /**
     * Returns the field that holds the {@link ProjectionOperation.ProjectionOperationBuilder.OperationProjection}.
     *
     * @return never {@literal null}.
     */
    protected Field getField() {
        return getExposedField();
    }

    /**
     * Creates a new instance of this {@link OperationOutput} with the given alias.
     *
     * @param alias the alias to set
     * @return new instance of {@link OperationOutput}.
     */
    public OperationOutput withAlias(String alias) {

        final Field aliasedField = Fields.field(alias);
        return new OperationOutput(aliasedField, this) {

            @Override
            protected Field getField() {
                return aliasedField;
            }

            @Override
            protected List<Object> getOperationArguments(AggregationOperationContext context) {

                // We have to make sure that we use the arguments from the "previous" OperationOutput that we replace
                // with this new instance.
                return OperationOutput.this.getOperationArguments(context);
            }
        };
    }
}
