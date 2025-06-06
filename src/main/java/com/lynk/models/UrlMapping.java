package com.lynk.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "url_mapping")
public class UrlMapping
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "short_url", nullable = false)
    private String shortUrl;

    @Column(name = "click_count", nullable = false)
    private Integer clickCount = 0;

    @Column(name = "createdDate", nullable = false)
    private LocalDateTime createdDate; // TODO : convert it to ZonedDateTime

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @OneToMany(mappedBy = "urlMapping")
    private List<ClickEvent> clickEvents;
}
