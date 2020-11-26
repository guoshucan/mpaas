/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ghost.framework.web.session.core;

/**
 * A repository interface for managing {@link Session} instances.
 *
 * @param <S> the {@link Session} type
 * @author Rob Winch
 * @since 1.0
 */
public interface SessionRepository<S extends Session> {
	/**
	 * 会话常量
	 */
	class Constant {
		/**
		 * 默认表名称常量
		 */
		public static final String DEFAULT_TABLE_NAME = "GHOST_SESSION";
		/**
		 * 会话id字段常量
		 */
		public final static String DEFAULT_SESSION_ID = "SESSION_ID";
		/**
		 * 会话创建时间字段常量
		 */
		public final static String DEFAULT_CREATION_TIME = "CREATION_TIME";
		/**
		 * 会话最新动作时间常量
		 * 单位时间戳
		 */
		public final static String DEFAULT_LAST_ACCESS_TIME = "LAST_ACCESS_TIME";
		/**
		 * 会话最大过期时间常量
		 * 单位秒
		 */
		public final static String DEFAULT_MAX_INACTIVE_INTERVAL = "MAX_INACTIVE_INTERVAL";
		/**
		 * 会话过期时间常量
		 * 单位时间戳
		 */
		public final static String DEFAULT_EXPIRY_TIME = "EXPIRY_TIME";
		/**
		 * 会话字段名称常量
		 */
		public final static String DEFAULT_ATTRIBUTE_NAME = "ATTRIBUTE_NAME";
		/**
		 * 会话字段数据名称常量
		 */
		public final static String DEFAULT_ATTRIBUTE_BYTES = "ATTRIBUTE_BYTES";
	}

	/**
	 * Creates a new {@link Session} that is capable of being persisted by this
	 * {@link SessionRepository}.
	 *
	 * <p>
	 * This allows optimizations and customizations in how the {@link Session} is
	 * persisted. For example, the implementation returned might keep track of the changes
	 * ensuring that only the delta needs to be persisted on a save.
	 * </p>
	 *
	 * @return a new {@link Session} that is capable of being persisted by this
	 * {@link SessionRepository}
	 */
	S createSession();

	/**
	 * Ensures the {@link Session} created by
	 * {@link SessionRepository#createSession()} is saved.
	 *
	 * <p>
	 * Some implementations may choose to save as the {@link Session} is updated by
	 * returning a {@link Session} that immediately persists any changes. In this case,
	 * this method may not actually do anything.
	 * </p>
	 *
	 * @param session the {@link Session} to save
	 */
	void save(S session);

	/**
	 * Gets the {@link Session} by the {@link Session#getId()} or null if no
	 * {@link Session} is found.
	 *
	 * @param id the {@link Session#getId()} to lookup
	 * @return the {@link Session} by the {@link Session#getId()} or null if no
	 * {@link Session} is found.
	 */
	S findById(String id);

	/**
	 * 删除会话id
	 * @param id the {@link Session#getId()} to delete
	 */
	void deleteById(String id);

	/**
	 * 清理超时会话数据
	 */
	default void cleanUpExpiredSessions() {
	}

	/**
	 * 获取是否为默认会话操作仓库
	 *
	 * @return
	 */
	boolean isDefault();

	/**
	 * 设置是否为默认会话操作仓库
	 *
	 * @param def
	 */
	void setDefault(boolean def);
}