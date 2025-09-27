package com.blogbaaz.SearchService.controllers;

import com.blogbaaz.SearchService.dtos.SearchRequest;
import com.blogbaaz.SearchService.dtos.SearchResult;
import com.blogbaaz.SearchService.services.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {
    
    private final SearchService searchService;
    
    @PostMapping
    public ResponseEntity<SearchResult> searchPosts(@Valid @RequestBody SearchRequest request) {
        log.info("Search request received: {}", request);
        SearchResult result = searchService.searchPosts(request);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping
    public ResponseEntity<SearchResult> searchPostsGet(
            @RequestParam String query,
            @RequestParam(required = false) String authorId,
            @RequestParam(required = false) String[] tags,
            @RequestParam(defaultValue = "20") int maxResults,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "true") boolean publishedOnly,
            @RequestParam(defaultValue = "false") boolean featuredOnly) {
        
        SearchRequest request = SearchRequest.builder()
                .query(query)
                .authorId(authorId)
                .tags(tags)
                .maxResults(maxResults)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .publishedOnly(publishedOnly)
                .featuredOnly(featuredOnly)
                .build();
        
        log.info("Search request received: {}", request);
        SearchResult result = searchService.searchPosts(request);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<SearchResult> searchByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "20") int maxResults,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        SearchRequest request = SearchRequest.builder()
                .query("")
                .maxResults(maxResults)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .publishedOnly(true)
                .build();
        
        log.info("Category search request received for category: {}", category);
        SearchResult result = searchService.searchByCategory(category, request);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/author/{authorId}")
    public ResponseEntity<SearchResult> searchByAuthor(
            @PathVariable String authorId,
            @RequestParam(defaultValue = "20") int maxResults,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        SearchRequest request = SearchRequest.builder()
                .query("")
                .maxResults(maxResults)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .publishedOnly(true)
                .build();
        
        log.info("Author search request received for author: {}", authorId);
        SearchResult result = searchService.searchByAuthor(authorId, request);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/tags")
    public ResponseEntity<SearchResult> searchByTags(
            @RequestParam String[] tags,
            @RequestParam(defaultValue = "20") int maxResults,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        SearchRequest request = SearchRequest.builder()
                .query("")
                .maxResults(maxResults)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .publishedOnly(true)
                .build();
        
        log.info("Tag search request received for tags: {}", String.join(", ", tags));
        SearchResult result = searchService.searchByTags(tags, request);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/featured")
    public ResponseEntity<SearchResult> getFeaturedPosts(
            @RequestParam(defaultValue = "20") int maxResults) {
        
        SearchRequest request = SearchRequest.builder()
                .query("")
                .maxResults(maxResults)
                .featuredOnly(true)
                .build();
        
        log.info("Featured posts request received");
        SearchResult result = searchService.getFeaturedPosts(request);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/published")
    public ResponseEntity<SearchResult> getPublishedPosts(
            @RequestParam(defaultValue = "20") int maxResults,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        SearchRequest request = SearchRequest.builder()
                .query("")
                .maxResults(maxResults)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .publishedOnly(true)
                .build();
        
        log.info("Published posts request received");
        SearchResult result = searchService.getPublishedPosts(request);
        return ResponseEntity.ok(result);
    }
}

