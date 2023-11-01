package com.jiangtj.cloud.core.pk;

import com.jiangtj.cloud.common.utils.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.cloud.client.discovery.event.ParentHeartbeatEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class InstanceDiscoveryListener {

	private final HeartbeatMonitor monitor = new HeartbeatMonitor();

	@Resource
	private DiscoveryClient discoveryClient;

	@EventListener
	public void onApplicationReady(ApplicationReadyEvent event) {
		discover();
	}

	@EventListener
	public void onInstanceRegistered(InstanceRegisteredEvent<?> event) {
		discover();
	}

	@EventListener
	public void onParentHeartbeat(ParentHeartbeatEvent event) {
		discoverIfNeeded(event.getValue());
	}

	@EventListener
	public void onApplicationEvent(HeartbeatEvent event) {
		discoverIfNeeded(event.getValue());
	}

	private void discoverIfNeeded(Object value) {
		if (this.monitor.update(value)) {
			discover();
		}
	}

	protected void discover() {
		log.info("discovering client change.");
		Flux.fromIterable(discoveryClient.getServices())
			.flatMapIterable(s -> discoveryClient.getInstances(s))
			.subscribe(instance -> {
				String instanceId = instance.getInstanceId();
				log.error(instanceId);
				log.error(instance.getUri().toString());
				log.error(JsonUtils.toJson(instance.getMetadata()));
			});
	}

}
