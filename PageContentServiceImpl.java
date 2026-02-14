package com.bookstore.service.impl;

import com.bookstore.entity.PageContent;
import com.bookstore.repository.PageContentRepository;
import com.bookstore.service.PageContentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class PageContentServiceImpl implements PageContentService {

    private static final List<String> MANAGED_KEYS = List.of("home", "about", "contact");

    private final PageContentRepository pageContentRepository;

    public PageContentServiceImpl(PageContentRepository pageContentRepository) {
        this.pageContentRepository = pageContentRepository;
    }

    @Override
    @Transactional
    public PageContent getPageContent(String pageKey) {
        String normalizedKey = normalizeKey(pageKey);
        return pageContentRepository.findByPageKey(normalizedKey)
                .orElseGet(() -> pageContentRepository.save(defaultFor(normalizedKey)));
    }

    @Override
    @Transactional
    public List<PageContent> getManagedPages() {
        return MANAGED_KEYS.stream().map(this::getPageContent).toList();
    }

    @Override
    @Transactional
    public void savePageContent(String pageKey, PageContent updatedContent) {
        PageContent existing = getPageContent(pageKey);
        existing.setTitle(updatedContent.getTitle());
        existing.setSubtitle(updatedContent.getSubtitle());
        existing.setContent(updatedContent.getContent());
        existing.setExtraContent(updatedContent.getExtraContent());
        existing.setCtaText(updatedContent.getCtaText());
        existing.setCtaUrl(updatedContent.getCtaUrl());
        pageContentRepository.save(existing);
    }

    private String normalizeKey(String key) {
        if (key == null || key.isBlank()) {
            return "home";
        }
        String normalized = key.trim().toLowerCase(Locale.ENGLISH);
        return MANAGED_KEYS.contains(normalized) ? normalized : "home";
    }

    private PageContent defaultFor(String pageKey) {
        return switch (pageKey) {
            case "about" -> PageContent.builder()
                    .pageKey("about")
                    .title("Stories Build Better Worlds")
                    .subtitle("We are a modern bookstore helping readers discover books they will actually finish.")
                    .content("Alariis started with one goal: make discovering the next great book feel effortless. We curate titles across fiction, sci-fi, fantasy, business, and more, then deliver quickly and reliably.")
                    .extraContent("Trusted by thousands of readers, with curated selections, secure checkout, and transparent order tracking.")
                    .ctaText("Browse Fiction")
                    .ctaUrl("/books?category=fiction")
                    .build();
            case "contact" -> PageContent.builder()
                    .pageKey("contact")
                    .title("Let Us Help You Find the Right Book")
                    .subtitle("Questions, bulk orders, or support requests. We reply fast.")
                    .content("Email: support@alariis.com\nPhone: +94 11 234 5678\nAddress: 221B Reader Lane, Colombo")
                    .extraContent("Support hours: Monday to Friday, 9:00 AM to 6:00 PM")
                    .ctaText("Browse Fiction")
                    .ctaUrl("/books?category=fiction")
                    .build();
            default -> PageContent.builder()
                    .pageKey("home")
                    .title("Discover Your Next Favorite Book")
                    .subtitle("Explore thousands of books across all genres. From bestsellers to hidden gems.")
                    .content("Free Shipping on Orders Over LKR 50|Flash Sale: 30% Off Best Sellers|New Arrivals Every Week|Join Our Loyalty Program")
                    .extraContent("")
                    .ctaText("Shop Fiction")
                    .ctaUrl("/books?category=fiction")
                    .build();
        };
    }
}