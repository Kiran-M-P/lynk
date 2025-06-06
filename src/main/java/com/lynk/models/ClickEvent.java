package com.lynk.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "click_event")
public class ClickEvent
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "click_date", nullable = false)
    private LocalDateTime clickDate;

    @ManyToOne
    @JoinColumn(name = "url_mapping_id", nullable = false)
    private UrlMapping urlMapping;
}
