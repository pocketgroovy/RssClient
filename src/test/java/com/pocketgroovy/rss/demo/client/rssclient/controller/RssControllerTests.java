package com.pocketgroovy.rss.demo.client.rssclient.controller;

import com.pocketgroovy.rss.demo.client.rssclient.config.Publishers;
import com.pocketgroovy.rss.demo.client.rssclient.dto.FeedDTO;
import com.pocketgroovy.rss.demo.client.rssclient.dto.FeedEntryDTO;
import com.pocketgroovy.rss.demo.client.rssclient.service.RssService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RssControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RssService rssService;

    @MockBean
    private Publishers publishers;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoadsFeedController() {
        assertThat(publishers).isNotNull();
    }
    @Test
    void requestGetAboutPage() throws Exception {
        String pageTitle = "<h1>About This Demo</h1>";
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(String.format("http://localhost:%d/about", port),
                String.class);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(response.getBody()).contains(pageTitle));
    }

    @Test
    void requestGetFeedEntryByIdThenIndexPage() throws Exception {
        String pageTitle = "<h1>RSS Feed Reader</h1>";
        FeedEntryDTO mockEntry = new FeedEntryDTO();
        mockEntry.setId(1L);
        mockEntry.setTitle("Test Title");

        when(rssService.getFeedEntryById("1")).thenReturn(Mono.just(mockEntry));

        webTestClient.get().uri("/get_entry")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String responseBody = new String(Objects.requireNonNull(response.getResponseBody()));
                    assert responseBody.contains(pageTitle);
                });
    }

    @Test
    void requestGetFeedEntriesThenIndexPage() throws Exception {
        String pageTitle = "<h1>RSS Feed Reader</h1>";

        Map<String, String> publisherNames = new HashMap<>();
        publisherNames.put("1", "Publisher One");
        publisherNames.put("2", "Publisher Two");

        FeedDTO mockEntry = new FeedDTO();
        mockEntry.setId(1L);
        mockEntry.setTitle("Test Title");
        mockEntry.setPubId("1");
        FeedDTO mockEntry2 = new FeedDTO();
        mockEntry2.setId(2L);
        mockEntry2.setTitle("Test Title2");
        mockEntry2.setPubId("2");

        FeedEntryDTO entry1 = new FeedEntryDTO();
        entry1.setId(1L);
        entry1.setTitle("Title 1");
        entry1.setPubDate("test date1");

        FeedEntryDTO entry2 = new FeedEntryDTO();
        entry2.setId(2L);
        entry2.setTitle("Title 2");
        entry2.setPubDate("test date2");


        List<FeedEntryDTO> mockEntries = Arrays.asList(entry1, entry2);
        when(publishers.getName()).thenReturn(publisherNames);
        when(rssService.getMostRecentFeedByPubId("1")).thenReturn(Mono.just(mockEntry));
        when(rssService.getMostRecentFeedByPubId("2")).thenReturn(Mono.just(mockEntry2));

        when(rssService.getAllFeedEntriesForFeedId("1")).thenReturn(Flux.fromIterable(mockEntries));
        when(rssService.getAllFeedEntriesForFeedId("2")).thenReturn(Flux.fromIterable(mockEntries));

        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String responseBody = new String(Objects.requireNonNull(response.getResponseBody()));
                    assert responseBody.contains(pageTitle);
                });
    }

}
