package com.blogbaaz.PostService.controllers;

import com.blogbaaz.PostService.dtos.CreatePostRequest;
import com.blogbaaz.PostService.dtos.PostDto;
import com.blogbaaz.PostService.dtos.UpdatePostRequest;
import com.blogbaaz.PostService.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody CreatePostRequest request) {
        PostDto createdPost = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable String postId) {
        PostDto post = postService.getPostById(postId);
        postService.incrementViewCount(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<PostDto> getPostBySlug(@PathVariable String slug) {
        PostDto post = postService.getPostBySlug(slug);
        postService.incrementViewCount(post.getPostId());
        return ResponseEntity.ok(post);
    }

    // Paginated endpoints
    @GetMapping
    public ResponseEntity<Page<PostDto>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PostDto> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/paginated/author/{authorId}")
    public ResponseEntity<Page<PostDto>> getPostsByAuthorPaginated(
            @PathVariable String authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PostDto> posts = postService.getPostsByAuthor(authorId, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/paginated/category/{category}")
    public ResponseEntity<Page<PostDto>> getPostsByCategoryPaginated(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PostDto> posts = postService.getPostsByCategory(category, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/paginated/published")
    public ResponseEntity<Page<PostDto>> getPublishedPostsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        Page<PostDto> posts = postService.getPublishedPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    // List endpoints (for SearchService)
    @GetMapping("/search")
    public ResponseEntity<List<PostDto>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        List<PostDto> posts = postService.searchPosts(keyword, sort);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PostDto>> getPostsByAuthor(
            @PathVariable String authorId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        List<PostDto> posts = postService.getPostsByAuthor(authorId, sort);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<PostDto>> getPostsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        List<PostDto> posts = postService.getPostsByCategory(category, sort);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<PostDto>> getPostsByTags(
            @RequestParam String[] tags,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        List<PostDto> posts = postService.getPostsByTags(tags, sort);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/published")
    public ResponseEntity<List<PostDto>> getPublishedPosts(
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        List<PostDto> posts = postService.getPublishedPosts(sort);
        return ResponseEntity.ok(posts);
    }

    // Update operations
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable String postId,
            @Valid @RequestBody UpdatePostRequest request) {

        PostDto updatedPost = postService.updatePost(postId, request);
        return ResponseEntity.ok(updatedPost);
    }

    @PutMapping("/{postId}/publish")
    public ResponseEntity<PostDto> publishPost(@PathVariable String postId) {
        PostDto publishedPost = postService.publishPost(postId);
        return ResponseEntity.ok(publishedPost);
    }

    @PutMapping("/{postId}/archive")
    public ResponseEntity<PostDto> archivePost(@PathVariable String postId) {
        PostDto archivedPost = postService.archivePost(postId);
        return ResponseEntity.ok(archivedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable String postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable String postId) {
        postService.incrementLikeCount(postId);
        return ResponseEntity.ok().build();
    }
}
