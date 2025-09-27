package com.blogbaaz.SearchService.services.impl;

import com.blogbaaz.SearchService.clients.PostServiceClient;
import com.blogbaaz.SearchService.dtos.SearchRequest;
import com.blogbaaz.SearchService.dtos.SearchResult;
import com.blogbaaz.SearchService.dtos.SearchResponse;
import com.blogbaaz.SearchService.dtos.PostDto;
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
            List<PostDto> posts;
            
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
            
            // Convert PostDto to SearchResponse
            List<SearchResponse> searchResponses = posts.stream()
                .map(SearchResponse::fromPostDto)
                .collect(Collectors.toList());
            
            // Apply max results limit
            if (searchResponses.size() > request.getMaxResults()) {
                searchResponses = searchResponses.stream()
                    .limit(request.getMaxResults())
                    .collect(Collectors.toList());
            }
            
            return createSearchResult(searchResponses, request, startTime);
            
        } catch (Exception e) {
            log.error("Error searching posts: {}", e.getMessage(), e);
            throw new RuntimeException("Search failed: " + e.getMessage());
        }
    }
    
    @Override
    public SearchResult searchByCategory(String category, SearchRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            List<PostDto> posts = postServiceClient.getPostsByCategory(
                category,
                request.getSortBy(),
                request.getSortDirection()
            );
            
            // Convert PostDto to SearchResponse
            List<SearchResponse> searchResponses = posts.stream()
                .map(SearchResponse::fromPostDto)
                .collect(Collectors.toList());
            
            // Apply max results limit
            if (searchResponses.size() > request.getMaxResults()) {
                searchResponses = searchResponses.stream()
                    .limit(request.getMaxResults())
                    .collect(Collectors.toList());
            }
            
            return createSearchResult(searchResponses, request, startTime);
            
        } catch (Exception e) {
            log.error("Error searching posts by category: {}", e.getMessage(), e);
            throw new RuntimeException("Category search failed: " + e.getMessage());
        }
    }
    
    @Override
    public SearchResult searchByAuthor(String authorId, SearchRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            List<PostDto> posts = postServiceClient.getPostsByAuthor(
                authorId,
                request.getSortBy(),
                request.getSortDirection()
            );
            
            // Convert PostDto to SearchResponse
            List<SearchResponse> searchResponses = posts.stream()
                .map(SearchResponse::fromPostDto)
                .collect(Collectors.toList());
            
            // Apply max results limit
            if (searchResponses.size() > request.getMaxResults()) {
                searchResponses = searchResponses.stream()
                    .limit(request.getMaxResults())
                    .collect(Collectors.toList());
            }
            
            return createSearchResult(searchResponses, request, startTime);
            
        } catch (Exception e) {
            log.error("Error searching posts by author: {}", e.getMessage(), e);
            throw new RuntimeException("Author search failed: " + e.getMessage());
        }
    }
    
    @Override
    public SearchResult searchByTags(String[] tags, SearchRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            List<PostDto> posts = postServiceClient.getPostsByTags(
                tags,
                request.getSortBy(),
                request.getSortDirection()
            );
            
            // Convert PostDto to SearchResponse
            List<SearchResponse> searchResponses = posts.stream()
                .map(SearchResponse::fromPostDto)
                .collect(Collectors.toList());
            
            // Apply max results limit
            if (searchResponses.size() > request.getMaxResults()) {
                searchResponses = searchResponses.stream()
                    .limit(request.getMaxResults())
                    .collect(Collectors.toList());
            }
            
            return createSearchResult(searchResponses, request, startTime);
            
        } catch (Exception e) {
            log.error("Error searching posts by tags: {}", e.getMessage(), e);
            throw new RuntimeException("Tag search failed: " + e.getMessage());
        }
    }
    
    @Override
    public SearchResult getFeaturedPosts(SearchRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Since PostService doesn't have featured posts endpoint, 
            // we'll get published posts and filter by some criteria
            List<PostDto> posts = postServiceClient.getPublishedPosts(
                request.getSortBy(),
                request.getSortDirection()
            );
            
            // Convert PostDto to SearchResponse
            List<SearchResponse> searchResponses = posts.stream()
                .map(SearchResponse::fromPostDto)
                .collect(Collectors.toList());
            
            // Apply max results limit
            if (searchResponses.size() > request.getMaxResults()) {
                searchResponses = searchResponses.stream()
                    .limit(request.getMaxResults())
                    .collect(Collectors.toList());
            }
            
            return createSearchResult(searchResponses, request, startTime);
            
        } catch (Exception e) {
            log.error("Error getting featured posts: {}", e.getMessage(), e);
            throw new RuntimeException("Featured posts retrieval failed: " + e.getMessage());
        }
    }
    
    @Override
    public SearchResult getPublishedPosts(SearchRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            List<PostDto> posts = postServiceClient.getPublishedPosts(
                request.getSortBy(),
                request.getSortDirection()
            );
            
            // Convert PostDto to SearchResponse
            List<SearchResponse> searchResponses = posts.stream()
                .map(SearchResponse::fromPostDto)
                .collect(Collectors.toList());
            
            // Apply max results limit
            if (searchResponses.size() > request.getMaxResults()) {
                searchResponses = searchResponses.stream()
                    .limit(request.getMaxResults())
                    .collect(Collectors.toList());
            }
            
            return createSearchResult(searchResponses, request, startTime);
            
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
