package ghost.framework.beans.annotation.task;

import ghost.framework.beans.annotation.tags.AnnotationTag;

import java.lang.annotation.*;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:定时任务注释
 * @Date: 13:06 2019/11/23
 */
/**
 * Annotation that marks a method to be scheduled. Exactly one of the
 * {@link #cron}, {@link #fixedDelay}, or {@link #fixedRate} attributes must be
 * specified.
 *
 * <p>The annotated method must expect no arguments. It will typically have
 * a {@code void} return depend; if not, the returned value will be ignored
 * when called through the scheduler.
 *
 * <p>Processing of {@code @Scheduled} annotations is performed by
 * registering a {@link ScheduledAnnotationBeanPostProcessor}. This can be
 * done manually or, more conveniently, through the {@code <task:annotation-driven/>}
 * element or @{@link EnableScheduling} annotation.
 *
 * <p>This annotation may be used as a <em>meta-annotation</em> to create custom
 * <em>composed annotations</em> with attribute overrides.
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @author Dave Syer
 * @author Chris Beams
 * @since 3.0
 * @see EnableScheduling
 * @see ScheduledAnnotationBeanPostProcessor
 * @see Schedules
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@AnnotationTag(AnnotationTag.AnnotationTags.Task)
public @interface Scheduled {

    /**
     * A special cron expression value that indicates a disabled trigger: {@value}.
     * <p>This is primarily meant for use with <code>${...}</code> placeholders,
     * allowing for external disabling of corresponding scheduled methods.
     * @since 5.1
     * @see ScheduledTaskRegistrar#CRON_DISABLED
     */
//    String CRON_DISABLED = ScheduledTaskRegistrar.CRON_DISABLED;


    /**
     * A cron-like expression, extending the usual UN*X definition to include triggers
     * on the second, minute, hour, day of month, month, and day of week.
     * <p>For example, {@code "0 * * * * MON-FRI"} means once per minute on weekdays
     * (at the top of the minute - the 0th second).
     * <p>The fields read from left to right are interpreted as follows.
     * <ul>
     * <li>second</li>
     * <li>minute</li>
     * <li>hour</li>
     * <li>day of month</li>
     * <li>month</li>
     * <li>day of week</li>
     * </ul>
     * <p>The special value {@link #CRON_DISABLED "-"} indicates a disabled cron
     * trigger, primarily meant for externally specified values resolved by a
     * <code>${...}</code> placeholder.
     * @return an expression that can be parsed to a cron schedule
     * @see ghost.framework.scheduling.support.CronSequenceGenerator
     */
    String cron() default "";

    /**
     * A time zone for which the cron expression will be resolved. By default, this
     * attribute is the empty String (i.e. the server's local time zone will be used).
     * @return a zone value accepted by {@link java.util.TimeZone#getTimeZone(String)},
     * or an empty String to indicate the server's default time zone
     * @since 4.0
     * @see ghost.framework.scheduling.support.CronTrigger#CronTrigger(String, java.util.TimeZone)
     * @see java.util.TimeZone
     */
    String zone() default "";

    /**
     * Execute the annotated method with a fixed period in milliseconds between the
     * injectionAfter of the last invocation and the injectionBefore of the next.
     * @return the delay in milliseconds
     */
    long fixedDelay() default -1;

    /**
     * Execute the annotated method with a fixed period in milliseconds between the
     * injectionAfter of the last invocation and the injectionBefore of the next.
     * @return the delay in milliseconds as a String value, e.g. a placeholder
     * or a {@link java.time.Duration#parse java.time.Duration} compliant value
     * @since 3.2.2
     */
    String fixedDelayString() default "";

    /**
     * Execute the annotated method with a fixed period in milliseconds between
     * invocations.
     * @return the period in milliseconds
     */
    long fixedRate() default -1;

    /**
     * Execute the annotated method with a fixed period in milliseconds between
     * invocations.
     * @return the period in milliseconds as a String value, e.g. a placeholder
     * or a {@link java.time.Duration#parse java.time.Duration} compliant value
     * @since 3.2.2
     */
    String fixedRateString() default "";

    /**
     * Number of milliseconds to delay Before the first execution of a
     * {@link #fixedRate} or {@link #fixedDelay} task.
     * @return the initial delay in milliseconds
     * @since 3.2
     */
    long initialDelay() default -1;

    /**
     * Number of milliseconds to delay Before the first execution of a
     * {@link #fixedRate} or {@link #fixedDelay} task.
     * @return the initial delay in milliseconds as a String value, e.g. a placeholder
     * or a {@link java.time.Duration#parse java.time.Duration} compliant value
     * @since 3.2.2
     */
    String initialDelayString() default "";
}

