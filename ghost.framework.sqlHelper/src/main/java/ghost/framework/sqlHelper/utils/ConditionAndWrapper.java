package ghost.framework.sqlHelper.utils;

import java.util.Arrays;
import java.util.Collection;

/**
 * 查询语句生成器 AND连接
 *
 */
public class ConditionAndWrapper extends ConditionWrapper {

	public ConditionAndWrapper() {
		andLink = true;
	}

	public ConditionAndWrapper and(ConditionWrapper conditionWrapper) {
		list.add(conditionWrapper);
		return this;
	}

	/**
	 * 等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionWrapper
	 */
	public ConditionAndWrapper eq(String column, Object params) {
		super.eq(column, params);
		return this;
	}

	/**
	 * 不等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper ne(String column, Object params) {
		super.ne(column, params);
		return this;
	}

	/**
	 * 小于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper lt(String column, Object params) {
		super.lt(column, params);
		return this;
	}

	/**
	 * 小于或等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper lte(String column, Object params) {
		super.lte(column, params);
		return this;
	}

	/**
	 * 大于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper gt(String column, Object params) {
		super.gt(column, params);
		return this;
	}

	/**
	 * 大于或等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper gte(String column, Object params) {
		super.gte(column, params);
		return this;
	}

	/**
	 * 相似于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper like(String column, String params) {
		super.like(column, params);
		return this;
	}

	/**
	 * 在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper in(String column, Collection<?> params) {
		super.in(column, params);
		return this;
	}

	/**
	 * 在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper in(String column, Object[] params) {
		super.in(column, Arrays.asList(params));
		return this;
	}

	/**
	 * 不在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper nin(String column, Collection<?> params) {
		super.nin(column, params);
		return this;
	}
	
	
	/**
	 * 不在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper nin(String column, Object[] params) {
		super.nin(column, Arrays.asList(params));
		return this;
	}

	/**
	 * 为空
	 * 
	 * @param <T>
	 * 
	 * @param column 字段
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper isNull(String column) {
		super.isNull(column);
		return this;
	}

	/**
	 * 不为空
	 * 
	 * @param <T>
	 * 
	 * @param column 字段
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper isNotNull(String column) {
		super.isNotNull(column);
		return this;
	}
}
