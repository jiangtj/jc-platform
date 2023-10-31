package com.jiangtj.cloud.auth;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CoreInstanceService {

    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private ObjectProvider<AuthServer> authServers;

    public String createToken() {
        return Objects.requireNonNull(authServers.getIfUnique())
            .createServerToken("core-server");
    }

    public Optional<ServiceInstance> getInstance() {
        List<ServiceInstance> instances = discoveryClient.getInstances("core-server");
        if (CollectionUtils.isEmpty(instances)) {
            return Optional.empty();
        }
        return Optional.of(instances.get(0));
    }

    public Optional<URI> getUri() {
        return getInstance()
            .map(ServiceInstance::getUri);
    }

}
