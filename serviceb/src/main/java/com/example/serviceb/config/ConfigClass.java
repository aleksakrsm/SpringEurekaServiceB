package com.example.serviceb.config;

import com.example.serviceb.components.loadBalancers.LeastConnectionsLoadBalancer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;


//pitanja:
// kako imati vise razlicitih load balancera za razlicite service ili pozive servise
// kako dinamicki odrediti koji load balancer treba da se uzme?
// sama implementacija leastConnections load balancera mi ima vrlo malo smisla
// koja je poenta ovog load balancinga, ako se konekcije brzo zatvaraju ako sve radi, a ako ne radi, otvara se
// circuit breaker  ili se radi retry? jedino je mozda logican ovaj geografski ili ovo nasumicni i round robin.
// dobar je i weighted, ali to treba ispisati

// dorada:
// Spring Retry, Spring circuit breaker (resilience4j, hystrix)

@Configuration
@LoadBalancerClient(name = "servicea", configuration = LoadBalancerClientConfiguration.class)
public class ConfigClass {

//    @LoadBalanced
//    @Bean
//    public RestClient restClientBuilder() {
//        return RestClient.create();
//    }

    //round-robin default load balancer
    @LoadBalanced
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    // Random Load Balancer, radi
//    @Bean
//    public RandomLoadBalancer randomLoadBalancer(LoadBalancerClientFactory clientFactory) {
//        String serviceId = "servicea";
//        if (serviceId == null) {
//            throw new IllegalStateException("Service ID cannot be null for LoadBalancer");
//        }
//        return new RandomLoadBalancer(clientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class), serviceId);
//    }

    // least connections Load Balancer, radi
//    @Bean
//    public LeastConnectionsLoadBalancer leastConnectionsLoadBalancer(LoadBalancerClientFactory clientFactory) {
//        String serviceId = "servicea";
//        if (serviceId == null) {
//            throw new IllegalStateException("Service ID cannot be null for LoadBalancer");
//        }
//        return new LeastConnectionsLoadBalancer(clientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class), serviceId);
//    }


//    ne radi
//    @Bean
//    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
//        ConfigurableApplicationContext context) {
//        return new WeightedServiceInstanceListSupplier();
//    }

//    ne radi, ne znam zasto??????
//    @Bean
//    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
//        ConfigurableApplicationContext context) {
//
//        return ServiceInstanceListSupplier.builder()
//            .withDiscoveryClient()
//            .withWeighted()
//            .withHealthChecks()
//            .withCaching()
//            .build(context);
//    }

}
