package io.jmnarloch.spring.boot.rxjava.async;

import org.glassfish.jersey.media.sse.SseFeature;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@Configuration
@EnableAutoConfiguration
public class BaseConfiguration {
    @Bean
    public TestRestTemplate testRestTemplate() {
        return new TestRestTemplate();
    }

    @Bean
    public Client client() {
        return ClientBuilder.newBuilder().register(SseFeature.class).build();
    }
}
