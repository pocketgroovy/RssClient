package com.pocketgroovy.rss.demo.client.rssclient.controller;

import com.pocketgroovy.rss.demo.client.rssclient.service.RssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class RssController {
    @Autowired
    private RssService rssService;

    @GetMapping("/get_feed")
    public Mono<String> getFeed(Model model) {
        return rssService.getFeedById("5").log("Debug").map(data -> {
            model.addAttribute("metaData", data);
            return "index";
        });
    }

    @GetMapping("/get_entry")
    public Mono<String> getEntry(Model model) {
        return rssService.getFeedEntryById("5").log("Debug").doOnNext(data->
                model.addAttribute("entryData", data)).then(Mono.just("index"));
    }

    @GetMapping("/")
    public Mono<String> getEntries(Model model) {
        return  rssService.getAllFeedEntriesForFeedId("5").log("Debug").collectList().doOnNext(data->
                model.addAttribute("entryListData", data)).then(Mono.just("index"));
    }
}
