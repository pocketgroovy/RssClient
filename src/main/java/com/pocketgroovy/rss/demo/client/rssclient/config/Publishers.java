package com.pocketgroovy.rss.demo.client.rssclient.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "publishers")
public class Publishers {
    private Map<String, String> name  = new TreeMap<>();
}