package com.bookstore.service;

import com.bookstore.entity.PageContent;

import java.util.List;

public interface PageContentService {
    PageContent getPageContent(String pageKey);

    List<PageContent> getManagedPages();

    void savePageContent(String pageKey, PageContent updatedContent);
}