package ghost.framework.data.transaction.support;

import ghost.framework.transaction.TransactionCallback;
import ghost.framework.transaction.TransactionException;
import ghost.framework.transaction.TransactionOperations;
import ghost.framework.transaction.TransactionStatus;

/**
 * package: ghost.framework.data.transaction.support
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/26:20:48
 */
public final class EmptyTransactionTemplate implements TransactionOperations {
    private final TransactionStatus status = new TransactionStatus() {
        @Override
        public boolean isNewTransaction() {
            return false;
        }

        @Override
        public void setRollbackOnly() {

        }

        @Override
        public boolean isRollbackOnly() {
            return false;
        }

        @Override
        public boolean isCompleted() {
            return false;
        }

        @Override
        public Object createSavepoint() throws TransactionException {
            return null;
        }

        @Override
        public void rollbackToSavepoint(Object savepoint) throws TransactionException {

        }

        @Override
        public void releaseSavepoint(Object savepoint) throws TransactionException {

        }

        @Override
        public boolean hasSavepoint() {
            return false;
        }

        @Override
        public void flush() {

        }
    };

    @Override
    public <T> T execute(TransactionCallback<T> action) throws TransactionException {
        return action.doInTransaction(status);
    }
}