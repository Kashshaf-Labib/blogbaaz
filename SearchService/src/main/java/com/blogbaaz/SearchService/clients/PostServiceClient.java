package com.blogbaaz.SearchService.clients;

import com.blogbaaz.SearchService.dtos.PostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "POST-SERVICE", url = "${post-service.url}")
public interface PostServiceClient {
    
    @GetMapping("/api/posts/search")
    List<PostDto> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );
    
    @GetMapping("/api/posts/category/{category}")
    List<PostDto> getPostsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );
    
    @GetMapping("/api/posts/author/{authorId}")
    List<PostDto> getPostsByAuthor(
            @PathVariable String authorId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );
    
    @GetMapping("/api/posts/tags")
    List<PostDto> getPostsByTags(
            @RequestParam String[] tags,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );
    
    @GetMapping("/api/posts/published")
    List<PostDto> getPublishedPosts(
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );
}
