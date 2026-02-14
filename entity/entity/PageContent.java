package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "page_contents", uniqueConstraints = @UniqueConstraint(columnNames = "page_key"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageContent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_key", nullable = false, length = 50)
    private String pageKey;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 500)
    private String subtitle;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "extra_content", columnDefinition = "TEXT")
    private String extraContent;

    @Column(name = "cta_text", length = 120)
    private String ctaText;

    @Column(name = "cta_url", length = 255)
    private String ctaUrl;
}