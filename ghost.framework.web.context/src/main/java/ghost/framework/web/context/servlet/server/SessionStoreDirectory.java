/*
 * Copyright 2012-2019 the original author or authors.
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

package ghost.framework.web.context.servlet.server;

import java.io.File;

import ghost.framework.context.base.ApplicationHome;
import ghost.framework.context.base.SystemTemp;
import ghost.framework.util.Assert;

/**
 * Manages a session store directory.
 *
 * @author Phillip Webb
 * @see AbstractServletWebServerFactory
 */
class SessionStoreDirectory {
	/**
	 * 会话目录
	 */
	private File directory;

	/**
	 * 获取会话目录
	 *
	 * @return 会话目录
	 */
	File getDirectory() {
		return this.directory;
	}

	/**
	 * 设置会话目录
	 *
	 * @param directory 会话目录
	 */
	void setDirectory(File directory) {
		this.directory = directory;
	}

	/**
	 * 获取验证会话目录
	 *
	 * @param mkdirs 是否创建会话目录
	 * @return
	 */
	File getValidDirectory(boolean mkdirs) {
		File dir = getDirectory();
		if (dir == null) {
			return new SystemTemp().getDir("servlet-sessions");
		}
		if (!dir.isAbsolute()) {
			dir = new File(new ApplicationHome(this.getClass()).getDir(), dir.getPath());
		}
		if (!dir.exists() && mkdirs) {
			dir.mkdirs();
		}
		assertDirectory(mkdirs, dir);
		return dir;
	}

	/**
	 * 断言会话目录
	 * @param mkdirs 是否创建会话目录
	 * @param dir 会话目录
	 */
	private void assertDirectory(boolean mkdirs, File dir) {
		Assert.state(!mkdirs || dir.exists(), () -> "Session dir " + dir + " does not exist");
		Assert.state(!dir.isFile(), () -> "Session dir " + dir + " points to a file");
	}
}