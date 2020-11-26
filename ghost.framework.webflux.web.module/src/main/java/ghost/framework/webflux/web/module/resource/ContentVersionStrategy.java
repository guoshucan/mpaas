/*
 * Copyright 2002-2018 the original author or authors.
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

package ghost.framework.webflux.web.module.resource;

import ghost.framework.core.io.Resource;
import ghost.framework.core.io.buffer.DataBuffer;
import ghost.framework.core.io.buffer.DataBufferFactory;
import ghost.framework.core.io.buffer.DataBufferUtils;
import ghost.framework.core.io.buffer.DefaultDataBufferFactory;
import ghost.framework.util.DigestUtils;
import ghost.framework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A {@code VersionStrategy} that calculates an Hex MD5 hashes from the content
 * of the resource and appends it to the file name, e.g.
 * {@code "styles/main-e36d2e05253c6c7085a91522ce43a0b4.css"}.
 *
 * @author Rossen Stoyanchev
 * @author Brian Clozel
 * @since 5.0
 * @see VersionResourceResolver
 */
public class ContentVersionStrategy extends AbstractFileNameVersionStrategy {

	private static final DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();


	@Override
	public Mono<String> getResourceVersion(Resource resource) {
		Flux<DataBuffer> flux =
				DataBufferUtils.read(resource, dataBufferFactory, StreamUtils.BUFFER_SIZE);
		return DataBufferUtils.join(flux)
				.map(buffer -> {
					byte[] result = new byte[buffer.readableByteCount()];
					buffer.read(result);
					DataBufferUtils.release(buffer);
					return DigestUtils.md5DigestAsHex(result);
				});
	}

}
