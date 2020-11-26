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

package ghost.framework.web.module.http.converter;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.io.IResource;
import ghost.framework.web.context.http.HttpInputMessage;
import ghost.framework.web.context.http.HttpOutputMessage;
import ghost.framework.web.context.http.MediaType;
import ghost.framework.web.context.http.MediaTypeFactory;

import java.io.IOException;

//import ghost.framework.context.io.ByteArrayResource;
//import ghost.framework.context.io.InputStreamResource;

/**
 * Implementation of {@link HttpMessageConverter} that can read/write {@link IResource Resources}
 * and supports byte range requests.
 *
 * <p>By default, this converter can read all media types. The {@link MediaTypeFactory} is used
 * to determine the {@code Content-Type} of written resources.
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @author Kazuki Shimizu
 * @since 3.0.2
 */
public class ResourceHttpMessageConverter extends AbstractHttpMessageConverter<IResource> {

	private final boolean supportsReadStreaming;


	/**
	 * Create a new instance of the {@code ResourceHttpMessageConverter}
	 * that supports read streaming, i.e. can convert an
	 * {@code HttpInputMessage} to {@code InputStreamResource}.
	 */
	public ResourceHttpMessageConverter() {
		super(MediaType.ALL);
		this.supportsReadStreaming = true;
	}

	/**
	 * Create a new instance of the {@code ResourceHttpMessageConverter}.
	 * @param supportsReadStreaming whether the converter should support
	 * read streaming, i.e. convert to {@code InputStreamResource}
	 * @since 5.0
	 */
	public ResourceHttpMessageConverter(boolean supportsReadStreaming) {
		super(MediaType.ALL);
		this.supportsReadStreaming = supportsReadStreaming;
	}


	@Override
	protected boolean supports(Class<?> clazz) {
		return IResource.class.isAssignableFrom(clazz);
	}

	@Override
	protected IResource readInternal(Class<? extends IResource> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

//		if (this.supportsReadStreaming && InputStreamResource.class == clazz) {
//			return new InputStreamResource(inputMessage.getBody()) {
//				@Override
//				public String getFilename() {
//					return inputMessage.getHeaders().getContentDisposition().getFilename();
//				}
//			};
//		}
//		else if (IResource.class == clazz || ByteArrayResource.class.isAssignableFrom(clazz)) {
//			byte[] body = StreamUtils.copyToByteArray(inputMessage.getBody());
//			return new ByteArrayResource(body) {
//				@Override
//				@Nullable
//				public String getFilename() {
//					return inputMessage.getHeaders().getContentDisposition().getFilename();
//				}
//			};
//		}
//		else {
//			throw new HttpMessageNotReadableException("Unsupported resource class: " + clazz, inputMessage);
//		}
		return null;
	}

	@Override
	protected MediaType getDefaultContentType(IResource resource) {
		return MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
	}

	@Override
	protected Long getContentLength(IResource resource, @Nullable MediaType contentType) throws IOException {
		// Don't try to determine contentLength on InputStreamResource - cannot be read afterwards...
		// Note: custom InputStreamResource subclasses could provide a pre-calculated content length!
//		if (InputStreamResource.class == resource.getClass()) {
//			return null;
//		}
		long contentLength = resource.contentLength();
		return (contentLength < 0 ? null : contentLength);
	}

	@Override
	protected void writeInternal(IResource resource, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		writeContent(resource, outputMessage);
	}

	protected void writeContent(IResource resource, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
//		try {
//			InputStream in = resource.getInputStream();
//			try {
//				StreamUtils.copy(in, outputMessage.getBody());
//			}
//			catch (NullPointerException ex) {
//				// ignore, see SPR-13620
//			}
//			finally {
//				try {
//					in.close();
//				}
//				catch (Throwable ex) {
//					// ignore, see SPR-12999
//				}
//			}
//		}
//		catch (FileNotFoundException ex) {
//			// ignore, see SPR-12999
//		}
	}

}
