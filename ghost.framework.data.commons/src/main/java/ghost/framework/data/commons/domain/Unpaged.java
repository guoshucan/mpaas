/*
 * Copyright 2017-2020 the original author or authors.
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
package ghost.framework.data.commons.domain;

/**
 * {@link Pageable} implementation to represent the absence of pagination information.
 *
 * @author Oliver Gierke
 */
enum Unpaged implements Pageable {

	INSTANCE;

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.commons.domain.Pageable#isPaged()
	 */
	@Override
	public boolean isPaged() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.commons.domain.Pageable#previousOrFirst()
	 */
	@Override
	public Pageable previousOrFirst() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.commons.domain.Pageable#next()
	 */
	@Override
	public Pageable next() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.commons.domain.Pageable#hasPrevious()
	 */
	@Override
	public boolean hasPrevious() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.commons.domain.Pageable#getSort()
	 */
	@Override
	public Sort getSort() {
		return Sort.unsorted();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.commons.domain.Pageable#getPageSize()
	 */
	@Override
	public int getPageSize() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.commons.domain.Pageable#getPageNumber()
	 */
	@Override
	public int getPageNumber() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.commons.domain.Pageable#getOffset()
	 */
	@Override
	public long getOffset() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.commons.domain.Pageable#first()
	 */
	@Override
	public Pageable first() {
		return this;
	}
}
