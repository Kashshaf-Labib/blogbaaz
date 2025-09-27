package com.blogbaaz.SearchService.services;

import com.blogbaaz.SearchService.dtos.SearchRequest;
import com.blogbaaz.SearchService.dtos.SearchResult;

public interface SearchService {
    
    SearchResult searchPosts(SearchRequest request);
    
    SearchResult searchByCategory(String categoryId, SearchRequest request);
    
    SearchResult searchByAuthor(String authorId, SearchRequest request);
    
    SearchResult searchByTags(String[] tags, SearchRequest request);
    
    SearchResult getFeaturedPosts(SearchRequest request);
    
    SearchResult getPublishedPosts(SearchRequest request);
}
