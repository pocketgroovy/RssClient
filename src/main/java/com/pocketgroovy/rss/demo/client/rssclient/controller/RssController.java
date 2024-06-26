package com.pocketgroovy.rss.demo.client.rssclient.controller;

import com.pocketgroovy.rss.demo.client.rssclient.config.Properties;
import com.pocketgroovy.rss.demo.client.rssclient.dto.FeedEntryDTO;
import com.pocketgroovy.rss.demo.client.rssclient.service.RssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class RssController {
    @Autowired
    private RssService rssService;

    @Autowired
    private final Properties properties;

    public RssController(Properties properties) {
        this.properties = properties;
    }

    @GetMapping("/about")
    public String about(Model model) {
              return "about";
    }

    @GetMapping("/get_entry")
    public Mono<String> getEntry(Model model) {
        return rssService.getFeedEntryById("1").log("Debug").doOnNext(data ->
                model.addAttribute("entryData", data)).then(Mono.just("index"));
    }

    @GetMapping("/")
    public Mono<String> getEntries(Model model) {
        Map<String, String> publisherNames = properties.getPublishers();
        return Flux.fromIterable(publisherNames.entrySet())
                .flatMap(pub -> getMostRecentFeedIdByPubId(String.valueOf(pub.getKey())) // using the most recent feed id to get the entries
                        .flatMap(this::getEntriesForFeedId)
                )
                .collectList()
                .doOnNext(allEntriesList -> {
                    allEntriesList.forEach(entryListData -> {
                        log.info("{}: entryListData", entryListData.toString());
                        String pubId = entryListData.getFirst().getPubId();
                        log.info("{}: pubId", pubId);
                        String pubName = publisherNames.get(pubId);
                        model.addAttribute("pubName" + pubId, pubName);
                        model.addAttribute("entryListData" + pubId, entryListData);
                    });
                })
                .then(Mono.just("index"));
    }

    public Mono<String> getMostRecentFeedIdByPubId(String pubId) {
        return rssService.getMostRecentFeedByPubId(pubId).map(data -> data.getId().toString());
    }

    // getting the entries with the feed id then just change the format of the dates
    public Mono<List<FeedEntryDTO>> getEntriesForFeedId(String feedId) {
        return rssService.getAllFeedEntriesForFeedId(feedId).log("Debug").collectList().map(
                data -> {
                    formatPubDate(data);
                    return data;
                }
        );
    }

    private void formatPubDate(List<FeedEntryDTO> feedEntryDTOList) {
        for (FeedEntryDTO entry : feedEntryDTOList) {
            String pubDate = entry.getPubDate();
            String pubDateFormatted = pubDate.replace("+0000", "");
            entry.setPubDate(pubDateFormatted);
        }
    }
}
