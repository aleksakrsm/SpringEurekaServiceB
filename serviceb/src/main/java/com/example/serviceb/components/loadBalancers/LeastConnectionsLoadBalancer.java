package com.example.serviceb.components.loadBalancers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.function.SingletonSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class LeastConnectionsLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private static final Log log = LogFactory.getLog(LeastConnectionsLoadBalancer.class);
    private final String serviceId;
    private final SingletonSupplier<ServiceInstanceListSupplier> serviceInstanceListSingletonSupplier;
    private final Map<String, AtomicInteger> connectionCounts = new ConcurrentHashMap<>();

    public LeastConnectionsLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
//        System.out.println("-------------------------------------------");
        this.serviceId = serviceId;
        this.serviceInstanceListSingletonSupplier = SingletonSupplier.of(() ->
            serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new));
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = this.serviceInstanceListSingletonSupplier.obtain();
        return supplier.get(request).next().map(this::processInstanceResponse);
    }

    private Response<ServiceInstance> processInstanceResponse(List<ServiceInstance> serviceInstances) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances);
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + this.serviceId);
            }
            return new EmptyResponse();
        }

        ServiceInstance leastConnectedInstance = instances.stream()
            .min((i1, i2) -> getConnectionCount(i1).get() - getConnectionCount(i2).get())
            .orElse(instances.get(0));

//        System.out.println("-------------------");
//        System.out.println(getConnectionCount(leastConnectedInstance));


        getConnectionCount(leastConnectedInstance).incrementAndGet();
        return new DefaultResponse(leastConnectedInstance);
    }

    private AtomicInteger getConnectionCount(ServiceInstance instance) {
        return connectionCounts.computeIfAbsent(instance.getInstanceId(), id -> new AtomicInteger(0));
    }
}