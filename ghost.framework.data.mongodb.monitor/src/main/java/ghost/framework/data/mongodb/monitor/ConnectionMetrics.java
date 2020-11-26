/*
 * Copyright 2002-2020 the original author or authors.
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
package ghost.framework.data.mongodb.monitor;

import com.mongodb.client.MongoClient;
import org.bson.Document;
import ghost.framework.jmx.export.annotation.ManagedMetric;
import ghost.framework.jmx.export.annotation.ManagedResource;
import ghost.framework.jmx.support.MetricType;

/**
 * JMX Metrics for Connections
 *
 * @author Mark Pollack
 */
@ManagedResource(description = "Connection metrics")
public class ConnectionMetrics extends AbstractMonitor {

	/**
	 * @param mongoClient must not be {@literal null}.
	 * @since 2.2
	 */
	public ConnectionMetrics(MongoClient mongoClient) {
		super(mongoClient);
	}

	@ManagedMetric(metricType = MetricType.GAUGE, displayName = "Current Connections")
	public int getCurrent() {
		return getConnectionData("current", Integer.class);
	}

	@ManagedMetric(metricType = MetricType.GAUGE, displayName = "Available Connections")
	public int getAvailable() {
		return getConnectionData("available", Integer.class);
	}

	@SuppressWarnings("unchecked")
	private <T> T getConnectionData(String key, Class<T> targetClass) {
		Document mem = (Document) getServerStatus().get("connections");
		// Class c = mem.get(key).getClass();
		return (T) mem.get(key);
	}

}
