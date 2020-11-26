/*
 * Copyright 2018-2020 the original author or authors.
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
package ghost.framework.data.lettuce;

import ghost.framework.context.converter.Converter;
import ghost.framework.data.redis.connection.RedisStreamCommands;
import ghost.framework.data.redis.connection.convert.ListConverter;
import ghost.framework.data.redis.connection.stream.*;
import ghost.framework.data.redis.util.ByteUtils;
import ghost.framework.util.NumberUtils;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XClaimArgs;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.models.stream.PendingParser;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Converters for Redis Stream-specific types.
 * <p/>
 * Converters typically convert between value objects/argument objects retaining the actual types of values (i.e. no
 * serialization/deserialization happens here).
 *
 * @author Mark Paluch
 * @author Christoph Strobl
 * @since 2.2
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
class StreamConverters {

	private static final Converter<List<StreamMessage<byte[], byte[]>>, List<RecordId>> MESSAGEs_TO_IDs = new ListConverter<>(
			messageToIdConverter());

	private static final BiFunction<List<Object>, String, PendingMessages> PENDING_MESSAGES_CONVERTER = (
			source, groupName) -> {

		List<Object> target = source.stream().map(StreamConverters::preConvertNativeValues).collect(Collectors.toList());
		List<PendingMessage> pendingMessages = PendingParser.parseRange(target);

		List<PendingMessage> messages = pendingMessages.stream()
				.map(it -> {

					RecordId id = RecordId.of(it.getId());
					Consumer consumer = Consumer.from(groupName, it.getConsumer());

					return new PendingMessage(id, consumer,
							Duration.ofMillis(it.getMsSinceLastDelivery()), it.getRedeliveryCount());

				}).collect(Collectors.toList());

		return new PendingMessages(groupName, messages);

	};

	private static final BiFunction<List<Object>, String, PendingMessagesSummary> PENDING_MESSAGES_SUMMARY_CONVERTER = (
			source, groupName) -> {

		List<Object> target = source.stream().map(StreamConverters::preConvertNativeValues).collect(Collectors.toList());

		PendingMessages pendingMessages = PendingParser.parse(target);
		ghost.framework.data.commons.domain.Range<String> range = ghost.framework.data.commons.domain.Range.open(
				pendingMessages.getMessageIds().getLower().getValue(), pendingMessages.getMessageIds().getUpper().getValue());

		return new PendingMessagesSummary(groupName, pendingMessages.getCount(), range,
				pendingMessages.getConsumerMessageCount());
	};

	/**
	 * Convert {@link StreamReadOptions} to Lettuce's {@link XReadArgs}.
	 *
	 * @param readOptions must not be {@literal null}.
	 * @return the converted {@link XReadArgs}.
	 */
	static XReadArgs toReadArgs(StreamReadOptions readOptions) {
		return StreamReadOptionsToXReadArgsConverter.INSTANCE.convert(readOptions);
	}
	/**
	 * Convert {@link RedisStreamCommands.XClaimOptions} to Lettuce's {@link XClaimArgs}.
	 *
	 * @param options must not be {@literal null}.
	 * @return the converted {@link XClaimArgs}.
	 * @since 2.3
	 */
	static XClaimArgs toXClaimArgs(RedisStreamCommands.XClaimOptions options) {
		return XClaimOptionsToXClaimArgsConverter.INSTANCE.convert(options);
	}

	static Converter<StreamMessage<byte[], byte[]>, ByteRecord> byteRecordConverter() {
		return (it) -> StreamRecords.newRecord().in(it.getStream()).withId(it.getId()).ofBytes(it.getBody());
	}

	static Converter<List<StreamMessage<byte[], byte[]>>, List<ByteRecord>> byteRecordListConverter() {
		return new ListConverter<>(byteRecordConverter());
	}

	static Converter<StreamMessage<byte[], byte[]>, RecordId> messageToIdConverter() {
		return (it) -> RecordId.of(it.getId());
	}

	static Converter<List<StreamMessage<byte[], byte[]>>, List<RecordId>> messagesToIds() {
		return MESSAGEs_TO_IDs;
	}

	/**
	 * Convert the raw Lettuce xpending result to {@link PendingMessages}.
	 *
	 * @param groupName the group name
	 * @param range     the range of messages requested
	 * @param source    the raw lettuce response.
	 * @return
	 * @since 2.3
	 */
	static ghost.framework.data.redis.connection.stream.PendingMessages toPendingMessages(String groupName,
																						  ghost.framework.data.domain.Range<?> range, List<Object> source) {
		return PENDING_MESSAGES_CONVERTER.apply(source, groupName).withinRange(range);
	}

	/**
	 * Convert the raw Lettuce xpending result to {@link PendingMessagesSummary}.
	 *
	 * @param groupName
	 * @param source    the raw lettuce response.
	 * @return
	 * @since 2.3
	 */
	static PendingMessagesSummary toPendingMessagesInfo(String groupName, List<Object> source) {
		return PENDING_MESSAGES_SUMMARY_CONVERTER.apply(source, groupName);
	}

	/**
	 * We need to convert values into the correct target type since lettuce will give us {@link ByteBuffer} or arrays but
	 * the parser requires us to have them as {@link String} or numeric values. Oh and {@literal null} values aren't real
	 * good citizens as well, so we make them empty strings instead - see it works - somehow ;P
	 *
	 * @param value dont't get me started om this.
	 * @return preconverted values that Lettuce parsers are able to understand \ö/.
	 */
	private static Object preConvertNativeValues(Object value) {

		if (value instanceof ByteBuffer || value instanceof byte[]) {

			byte[] targetArray = value instanceof ByteBuffer ? ByteUtils.getBytes((ByteBuffer) value) : (byte[]) value;
			String tmp = LettuceConverters.toString(targetArray);

			try {
				return NumberUtils.parseNumber(tmp, Long.class);
			} catch (NumberFormatException e) {
				return tmp;
			}
		}
		if (value instanceof List) {
			List<Object> targetList = new ArrayList<>();
			for (Object it : (List) value) {
				targetList.add(preConvertNativeValues(it));
			}
			return targetList;
		}

		return value != null ? value : "";
	}

	/**
	 * {@link Converter} to convert {@link StreamReadOptions} to Lettuce's {@link XReadArgs}.
	 */
	enum StreamReadOptionsToXReadArgsConverter implements Converter<StreamReadOptions, XReadArgs> {

		INSTANCE;

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.Converter#convert(java.lang.Object)
		 */
		@Override
		public XReadArgs convert(StreamReadOptions source) {

			XReadArgs args = new XReadArgs();

			if (source.isNoack()) {
				args.noack(true);
			}

			if (source.getBlock() != null) {
				args.block(source.getBlock());
			}

			if (source.getCount() != null) {
				args.count(source.getCount());
			}
			return args;
		}
	}

	/**
	 * {@link Converter} to convert {@link RedisStreamCommands.XClaimOptions} to Lettuce's {@link XClaimArgs}.
	 *
	 * @since 2.3
	 */
	enum XClaimOptionsToXClaimArgsConverter implements Converter<RedisStreamCommands.XClaimOptions, XClaimArgs> {
		INSTANCE;

		@Override
		public XClaimArgs convert(RedisStreamCommands.XClaimOptions source) {

			XClaimArgs args = XClaimArgs.Builder.minIdleTime(source.getMinIdleTime());
			args.minIdleTime(source.getMinIdleTime());
			args.force(source.isForce());

			if (source.getIdleTime() != null) {
				args.idle(source.getIdleTime());
			}
			if (source.getRetryCount() != null) {
				args.retryCount(source.getRetryCount());
			}
			if (source.getUnixTime() != null) {
				args.time(source.getUnixTime());
			}

			return args;
		}
	}
}