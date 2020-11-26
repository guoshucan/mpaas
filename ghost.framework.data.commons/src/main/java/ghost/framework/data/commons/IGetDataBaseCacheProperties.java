package ghost.framework.data.commons;

/**
 * Created by guo-pc on 2017/9/16.
 */
public interface IGetDataBaseCacheProperties {
    String getProviderClass();

    boolean isUseQueryCache();

    boolean isUseSecondLevelCache();

    String getFactoryClass();
}
