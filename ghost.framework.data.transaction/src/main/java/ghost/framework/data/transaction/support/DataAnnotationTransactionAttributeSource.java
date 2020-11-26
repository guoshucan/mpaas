package ghost.framework.data.transaction.support;

import ghost.framework.data.transaction.JtaTransactionAnnotationParser;
import ghost.framework.data.transaction.SpringTransactionAnnotationParser;
import ghost.framework.transaction.TransactionAnnotationParser;
import ghost.framework.transaction.support.AnnotationTransactionAttributeSource;
import ghost.framework.util.ClassUtils;

import java.util.Set;

/**
 * package: ghost.framework.data.transaction.support
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/11:12:50
 */
public class DataAnnotationTransactionAttributeSource extends AnnotationTransactionAttributeSource {
    private static final boolean jta12Present;

    private static final boolean ejb3Present;

    static {
        ClassLoader classLoader = AnnotationTransactionAttributeSource.class.getClassLoader();
        jta12Present = ClassUtils.isPresent("javax.transaction.Transactional", classLoader);
        ejb3Present = ClassUtils.isPresent("javax.ejb.TransactionAttribute", classLoader);
    }
    /**
     * Create a default AnnotationTransactionAttributeSource, supporting
     * public methods that carry the {@code Transactional} annotation
     * or the EJB3 {@link javax.ejb.TransactionAttribute} annotation.
     */
    public DataAnnotationTransactionAttributeSource() {
        super(true);
    }

    /**
     * Create a custom AnnotationTransactionAttributeSource, supporting
     * public methods that carry the {@code Transactional} annotation
     * or the EJB3 {@link javax.ejb.TransactionAttribute} annotation.
     * @param publicMethodsOnly whether to support public methods that carry
     * the {@code Transactional} annotation only (typically for use
     * with proxy-based AOP), or protected/private methods as well
     * (typically used with AspectJ class weaving)
     */
    public DataAnnotationTransactionAttributeSource(boolean publicMethodsOnly) {
        super(publicMethodsOnly);
		this.annotationParsers.add(new SpringTransactionAnnotationParser());
		if (jta12Present || ejb3Present) {
			if (jta12Present) {
				this.annotationParsers.add(new JtaTransactionAnnotationParser());
			}
			if (ejb3Present) {
//				this.annotationParsers.add(new Ejb3TransactionAnnotationParser());
			}
		}
    }

    /**
     * Create a custom AnnotationTransactionAttributeSource.
     * @param annotationParser the TransactionAnnotationParser to use
     */
    public DataAnnotationTransactionAttributeSource(TransactionAnnotationParser annotationParser) {
        super(annotationParser);
    }

    /**
     * Create a custom AnnotationTransactionAttributeSource.
     * @param annotationParsers the TransactionAnnotationParsers to use
     */
    public DataAnnotationTransactionAttributeSource(TransactionAnnotationParser... annotationParsers) {
        super(annotationParsers);
    }

    /**
     * Create a custom AnnotationTransactionAttributeSource.
     * @param annotationParsers the TransactionAnnotationParsers to use
     */
    public DataAnnotationTransactionAttributeSource(Set<TransactionAnnotationParser> annotationParsers) {
        super(annotationParsers);
    }
}
