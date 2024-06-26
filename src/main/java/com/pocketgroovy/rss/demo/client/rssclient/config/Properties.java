package com.pocketgroovy.rss.demo.client.rssclient.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties("app")
public class Properties {
    private Map<String, String> publishers = new HashMap<>();
    @Value("${app.subscriber.server.reader.url}")
    private String subscriberServerReaderUrl;
}