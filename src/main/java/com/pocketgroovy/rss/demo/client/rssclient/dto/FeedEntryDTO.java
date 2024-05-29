package com.pocketgroovy.rss.demo.client.rssclient.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedEntryDTO {
    private Long id;

    private String title;

    private String description;

    private String link;

    private String author;

    private String guid;
}
