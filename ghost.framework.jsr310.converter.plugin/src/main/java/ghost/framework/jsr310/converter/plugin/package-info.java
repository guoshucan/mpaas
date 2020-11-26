/**
 * package: ghost.framework.jsr310.convert.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/26:1:17
 */
@PluginPackage(loadClass = {
        JSR310BytesToDurationConverter.class,
        JSR310BytesToInstantConverter.class,
        JSR310BytesToLocalDateConverter.class,
        JSR310BytesToLocalDateTimeConverter.class,
        JSR310BytesToLocalTimeConverter.class,
        JSR310BytesToPeriodConverter.class,
        JSR310BytesToZonedDateTimeConverter.class,
        JSR310BytesToZoneIdConverter.class,
        JSR310DurationToBytesConverter.class,
        JSR310InstantToBytesConverter.class,
        JSR310LocalDateTimeToBytesConverter.class,
        JSR310LocalDateToBytesConverter.class,
        JSR310LocalTimeToBytesConverter.class,
        JSR310PeriodToBytesConverter.class,
        JSR310ZonedDateTimeToBytesConverter.class,
        JSR310ZoneIdToBytesConverter.class
})
package ghost.framework.jsr310.converter.plugin;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;