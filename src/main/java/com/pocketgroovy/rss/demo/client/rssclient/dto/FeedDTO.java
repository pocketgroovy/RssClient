package com.pocketgroovy.rss.demo.client.rssclient.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedDTO {
    private Long id;

    private String title;

    private String link;

    private String description;

    private String language;

    private String copyright;

    private String pubDate;
}