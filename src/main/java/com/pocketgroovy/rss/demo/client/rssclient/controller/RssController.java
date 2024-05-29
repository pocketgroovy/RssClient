package com.pocketgroovy.rss.demo.client.rssclient.controller;

import com.pocketgroovy.rss.demo.client.rssclient.config.WebClientConfig;
import com.pocketgroovy.rss.demo.client.rssclient.dto.FeedDTO;
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
    private WebClientConfig webClientConfig;

    @Autowired
    private RssService rssService;

    private FeedDTO res;

    @GetMapping("/get_feed")
    public Mono<String> getFeed(Model model) {
        Mono<FeedDTO> feedDTOMono = rssService.getFeedById("5");

        log.info(String.valueOf("accessed"));

        return feedDTOMono.map(data -> {
            model.addAttribute("dataValue", data);
            return "home/index";
        });
    }

}
