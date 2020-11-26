package ghost.framework.context.resolver;

import ghost.framework.beans.annotation.constraints.Nullable;

/**
 * 字符串值解析器接口
 */
@FunctionalInterface
public interface StringValueResolver extends IResolver{
	/**
	 * 字符串内容解析
	 * @param strVal 原文
	 * @return 返回解析内容
	 */
	@Nullable
	String resolveStringValue(String strVal);
}