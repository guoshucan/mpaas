package ghost.framework.data.mongodb.core.aggregation;

/**
 * package: ghost.framework.data.mongodb.core.aggregation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/29:22:21
 */
public enum Accumulators {

    SUM("$sum"), AVG("$avg"), FIRST("$first"), LAST("$last"), MAX("$max"), MIN("$min"), PUSH("$push"), ADDTOSET(
            "$addToSet");

    private String mongoOperator;

    Accumulators(String mongoOperator) {
        this.mongoOperator = mongoOperator;
    }

    public String getMongoOperator() {
        return mongoOperator;
    }
}