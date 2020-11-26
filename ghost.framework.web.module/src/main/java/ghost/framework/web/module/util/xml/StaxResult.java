/*
 * Copyright 2002-2017 the original author or authors.
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

package ghost.framework.web.module.util.xml;

import ghost.framework.beans.annotation.constraints.Nullable;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.sax.SAXResult;

/**
 * Implementation of the {@code Result} tagging interface for StAX writers. Can be constructed with
 * an {@code XMLEventConsumer} or an {@code XMLStreamWriter}.
 *
 * <p>This class is necessary because there is no implementation of {@code Source} for StaxReaders
 * in JAXP 1.3. There is a {@code StAXResult} in JAXP 1.4 (JDK 1.6), but this class is kept around
 * for backwards compatibility reasons.
 *
 * <p>Even though {@code StaxResult} extends from {@code SAXResult}, calling the methods of
 * {@code SAXResult} is <strong>not supported</strong>. In general, the only supported operation
 * on this class is to use the {@code ContentHandler} obtained via {@link #getHandler()} to parse an
 * input source using an {@code XMLReader}. Calling {@link #setHandler(ContentHandler)}
 * or {@link #setLexicalHandler(LexicalHandler)} will result in
 * {@code UnsupportedOperationException}s.
 *
 * @author Arjen Poutsma
 * @since 3.0
 * @see XMLEventWriter
 * @see XMLStreamWriter
 * @see javax.xml.transform.Transformer
 */
class StaxResult extends SAXResult {

	@Nullable
	private XMLEventWriter eventWriter;

	@Nullable
	private XMLStreamWriter streamWriter;


	/**
	 * Construct a new instance of the {@code StaxResult} with the specified {@code XMLEventWriter}.
	 * @param eventWriter the {@code XMLEventWriter} to write to
	 */
	public StaxResult(XMLEventWriter eventWriter) {
		StaxEventHandler handler = new StaxEventHandler(eventWriter);
		super.setHandler(handler);
		super.setLexicalHandler(handler);
		this.eventWriter = eventWriter;
	}

	/**
	 * Construct a new instance of the {@code StaxResult} with the specified {@code XMLStreamWriter}.
	 * @param streamWriter the {@code XMLStreamWriter} to write to
	 */
	public StaxResult(XMLStreamWriter streamWriter) {
		StaxStreamHandler handler = new StaxStreamHandler(streamWriter);
		super.setHandler(handler);
		super.setLexicalHandler(handler);
		this.streamWriter = streamWriter;
	}


	/**
	 * Return the {@code XMLEventWriter} used by this {@code StaxResult}.
	 * <p>If this {@code StaxResult} was created with an {@code XMLStreamWriter},
	 * the result will be {@code null}.
	 * @return the StAX event writer used by this result
	 * @see #StaxResult(XMLEventWriter)
	 */
	@Nullable
	public XMLEventWriter getXMLEventWriter() {
		return this.eventWriter;
	}

	/**
	 * Return the {@code XMLStreamWriter} used by this {@code StaxResult}.
	 * <p>If this {@code StaxResult} was created with an {@code XMLEventConsumer},
	 * the result will be {@code null}.
	 * @return the StAX stream writer used by this result
	 * @see #StaxResult(XMLStreamWriter)
	 */
	@Nullable
	public XMLStreamWriter getXMLStreamWriter() {
		return this.streamWriter;
	}


	/**
	 * Throws an {@code UnsupportedOperationException}.
	 * @throws UnsupportedOperationException always
	 */
	@Override
	public void setHandler(ContentHandler handler) {
		throw new UnsupportedOperationException("setHandler is not supported");
	}

	/**
	 * Throws an {@code UnsupportedOperationException}.
	 * @throws UnsupportedOperationException always
	 */
	@Override
	public void setLexicalHandler(LexicalHandler handler) {
		throw new UnsupportedOperationException("setLexicalHandler is not supported");
	}

}