package com.jiangtj.platform.auth.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.cloud.client.discovery.event.ParentHeartbeatEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

@Slf4j
public abstract class InstanceDiscoveryListener implements SmartApplicationListener {

    private final HeartbeatMonitor monitor = new HeartbeatMonitor();

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ApplicationReadyEvent.class.isAssignableFrom(eventType)
                || InstanceRegisteredEvent.class.isAssignableFrom(eventType)
                || HeartbeatEvent.class.isAssignableFrom(eventType)
                || ParentHeartbeatEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof HeartbeatEvent e){
            discoverIfNeeded(e.getValue());
            return;
        }
        if (event instanceof ParentHeartbeatEvent e){
            discoverIfNeeded(e.getValue());
            return;
        }
        discover();
    }

    private void discoverIfNeeded(Object value) {
        if (this.monitor.update(value)) {
            discover();
        }
    }

    public abstract void discover();
}
