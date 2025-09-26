package com.blogbaaz.SearchService.clients;

import com.blogbaaz.SearchService.dtos.SearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "POST-SERVICE", url = "${post-service.url}")
public interface PostServiceClient {
    
    @GetMapping("/api/posts/search")
    List<SearchResponse> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );
    
    @GetMapping("/api/posts/category/{categoryId}")
    List<SearchResponse> getPostsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );
    
    @GetMapping("/api/posts/author/{authorId}")
    List<SearchResponse> getPostsByAuthor(
            @PathVariable String authorId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );
    
    @GetMapping("/api/posts/tags")
    List<SearchResponse> getPostsByTags(
            @RequestParam String[] tags,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );
    
    @GetMapping("/api/posts/published")
    List<SearchResponse> getPublishedPosts(
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );
    
    @GetMapping("/api/posts/featured")
    List<SearchResponse> getFeaturedPosts();
}
