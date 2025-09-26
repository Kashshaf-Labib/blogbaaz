package com.blogbaaz.SearchService.services.impl;

import com.blogbaaz.SearchService.clients.PostServiceClient;
import com.blogbaaz.SearchService.dtos.SearchRequest;
import com.blogbaaz.SearchService.dtos.SearchResult;
import com.blogbaaz.SearchService.dtos.SearchResponse;
import com.blogbaaz.SearchService.services.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {
    
    private final PostServiceClient postServiceClient;
    
    @Override
    public SearchResult searchPosts(SearchRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            List<SearchResponse> posts;
            
            if (request.getAuthorId() != null && !request.getAuthorId().isEmpty()) {
                posts = postServiceClient.getPostsByAuthor(
                    request.getAuthorId(),
                    request.getSortBy(),
                    request.getSortDirection()
                );
            } else if (request.getTags() != null && request.getTags().length > 0) {
                posts = postServiceClient.getPostsByTags(
                    request.getTags(),
                    request.getSortBy(),
                    request.getSortDirection()
                );
            } else if (request.isFeaturedOnly()) {
                posts = postServiceClient.getFeaturedPosts();
            } else if (request.isPublishedOnly()) {
                posts = postServiceClient.getPublishedPosts(
                    request.getSortBy(),
                    request.getSortDirection()
                );
            } else {
                posts = postServiceClient.searchPosts(
                    request.getQuery(),
                    request.getSortBy(),
                    request.getSortDirection()
                );
            }
            
            // Apply max results limit
            if (posts.size() > request.getMaxResults()) {
                posts = posts.stream()
                    .limit(request.getMaxResults())
                    .collect(Collectors.toList());
            }
            
            return createSearchResult(posts, request, startTime);
            
        } catch (Exception e) {
            log.error("Error searching posts: {}", e.getMessage(), e);
            throw new RuntimeException("Search failed: " + e.getMessage());
        }
    }
    
    @Override
    public SearchResult searchByCategory(String categoryId, SearchRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            List<SearchResponse> posts = postServiceClient.getPostsByCategory(
                categoryId,
                request.getSortBy(),
                request.getSortDirection()
            );
            
            // Apply max results limit
            if (posts.size() > request.getMaxResults()) {
                posts = posts.stream()
                    .limit(request.getMaxResults())
                    .collect(Collectors.toList());
            }
            
            return createSearchResult(posts, request, startTime);
            
        } catch (Exception e) {
            log.error("Error searching posts by category: {}", e.getMessage(), e);
            throw new RuntimeException("Category search failed: " + e.getMessage());
        }
    }
    
    @Override
    public SearchResult searchByAuthor(String authorId, SearchRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            List<SearchResponse> posts = postServiceClient.getPostsByAuthor(
                authorId,
                request.getSortBy(),
                request.getSortDirection()
            );
            
            // Apply max results limit
            if (posts.size() > request.getMaxResults()) {
                posts = posts.stream()
                    .limit(request.getMaxResults())
                    .collect(Collectors.toList());
            }
            
            return createSearchResult(posts, request, startTime);
            
        } catch (Exception e) {
            log.error("Error searching posts by author: {}", e.getMessage(), e);
            throw new RuntimeException("Author search failed: " + e.getMessage());
        }
    }
    
    @Override
    public SearchResult searchByTags(String[] tags, SearchRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            List<SearchResponse> posts = postServiceClient.getPostsByTags(
                tags,
                request.getSortBy(),
                request.getSortDirection()
            );
            
            // Apply max results limit
            if (posts.size() > request.getMaxResults()) {
                posts = posts.stream()
                    .limit(request.getMaxResults())
                    .collect(Collectors.toList());
            }
            
            return createSearchResult(posts, request, startTime);
            
        } catch (Exception e) {
            log.error("Error searching posts by tags: {}", e.getMessage(), e);
            throw new RuntimeException("Tag search failed: " + e.getMessage());
        }
    }
    
    @Override
    public SearchResult getFeaturedPosts(SearchRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            List<SearchResponse> featuredPosts = postServiceClient.getFeaturedPosts();
            
            // Apply max results limit
            if (featuredPosts.size() > request.getMaxResults()) {
                featuredPosts = featuredPosts.stream()
                    .limit(request.getMaxResults())
                    .collect(Collectors.toList());
            }
            
            return createSearchResult(featuredPosts, request, startTime);
            
        } catch (Exception e) {
            log.error("Error getting featured posts: {}", e.getMessage(), e);
            throw new RuntimeException("Featured posts retrieval failed: " + e.getMessage());
        }
    }
    
    @Override
    public SearchResult getPublishedPosts(SearchRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            List<SearchResponse> posts = postServiceClient.getPublishedPosts(
                request.getSortBy(),
                request.getSortDirection()
            );
            
            // Apply max results limit
            if (posts.size() > request.getMaxResults()) {
                posts = posts.stream()
                    .limit(request.getMaxResults())
                    .collect(Collectors.toList());
            }
            
            return createSearchResult(posts, request, startTime);
            
        } catch (Exception e) {
            log.error("Error getting published posts: {}", e.getMessage(), e);
            throw new RuntimeException("Published posts retrieval failed: " + e.getMessage());
        }
    }
    
    private SearchResult createSearchResult(List<SearchResponse> posts, SearchRequest request, long startTime) {
        long searchTime = System.currentTimeMillis() - startTime;
        
        return SearchResult.builder()
                .posts(posts)
                .totalResults(posts.size())
                .searchQuery(request.getQuery())
                .searchTimeMs(searchTime)
                .sortBy(request.getSortBy())
                .sortDirection(request.getSortDirection())
                .build();
    }
}
