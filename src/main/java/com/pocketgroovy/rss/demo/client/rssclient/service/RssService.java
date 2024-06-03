package com.pocketgroovy.rss.demo.client.rssclient.service;

import com.pocketgroovy.rss.demo.client.rssclient.dto.FeedDTO;
import com.pocketgroovy.rss.demo.client.rssclient.dto.FeedEntryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;

@Slf4j
@Service
public class RssService {

    private final WebClient webClient;

    public RssService(WebClient.Builder webClientBuilder) throws SSLException {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/api/v1/rss").build();
    }

    public Mono<FeedDTO> getFeedById(String id) {
        return webClient.get()
                .uri("/get_feed/{id}", id)
                .retrieve()
                .bodyToMono(FeedDTO.class)
                .doOnError(e -> log.error("Error occurred on getFeedById", e));
    }

    public Mono<FeedDTO> getMostRecentFeedByPubId(String pubId) {
        return webClient.get()
                .uri("/get_most_recent_feed_by_pubId/{id}", pubId)
                .retrieve()
                .bodyToMono(FeedDTO.class)
                .doOnError(e -> log.error("Error occurred on getFeedById", e));
    }


    public Flux<FeedDTO> getAllFeedsByPubId(String pubId) {
        return webClient.get()
                .uri("/get_all_feeds_by_pubId/{id}", pubId)
                .retrieve()
                .bodyToFlux(FeedDTO.class)
                .doOnError(e -> log.error("Error occurred on getFeedById", e));
    }

    public Mono<FeedEntryDTO> getFeedEntryById(String id) {
        return webClient.get()
                .uri("/get_message/{id}", id)
                .retrieve()
                .bodyToMono(FeedEntryDTO.class)
                .doOnError(e -> log.error("Error occurred on getFeedEntryById", e));
    }

    public Flux<FeedEntryDTO> getAllFeedEntriesForFeedId(String id) {
        return webClient.get()
                .uri("/get_all_messages_by_feedId/{id}", id)
                .retrieve()
                .bodyToFlux(FeedEntryDTO.class)
                .doOnError(e -> log.error("Error occurred on getAllFeedEntriesForFeedId", e));
    }
}
