package ghost.framework.data.transaction.reactive.support;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.transaction.reactive.ReactiveTransaction;
import ghost.framework.data.transaction.reactive.ReactiveTransactionManager;
import ghost.framework.transaction.interceptor.TransactionAttribute;
import ghost.framework.util.Assert;

/**
 * package: ghost.framework.data.transaction.reactive.support
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/11:11:56
 */
public class ReactiveTransactionInfo {

    @Nullable
    private final ReactiveTransactionManager transactionManager;

    @Nullable
    public final TransactionAttribute transactionAttribute;

    private final String joinpointIdentification;

    @Nullable
    private ReactiveTransaction reactiveTransaction;

    public ReactiveTransactionInfo(@Nullable ReactiveTransactionManager transactionManager,
                                   @Nullable TransactionAttribute transactionAttribute, String joinpointIdentification) {

        this.transactionManager = transactionManager;
        this.transactionAttribute = transactionAttribute;
        this.joinpointIdentification = joinpointIdentification;
    }

    public ReactiveTransactionManager getTransactionManager() {
        Assert.state(this.transactionManager != null, "No ReactiveTransactionManager set");
        return this.transactionManager;
    }

    @Nullable
    public TransactionAttribute getTransactionAttribute() {
        return this.transactionAttribute;
    }

    /**
     * Return a String representation of this joinpoint (usually a Method call)
     * for use in logging.
     */
    public String getJoinpointIdentification() {
        return this.joinpointIdentification;
    }

    public void newReactiveTransaction(@Nullable ReactiveTransaction transaction) {
        this.reactiveTransaction = transaction;
    }

    @Nullable
    public ReactiveTransaction getReactiveTransaction() {
        return this.reactiveTransaction;
    }

    @Override
    public String toString() {
        return (this.transactionAttribute != null ? this.transactionAttribute.toString() : "No transaction");
    }
}
