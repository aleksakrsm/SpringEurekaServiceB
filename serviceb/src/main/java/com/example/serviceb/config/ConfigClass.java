package com.example.serviceb.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
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

//    @Bean
//    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
//        ConfigurableApplicationContext context) {
//        return ServiceInstanceListSupplier.builder()
//            .withDiscoveryClient()
//            .withWeighted()
//            .withCaching()
//            .build(context);
//    }

}
