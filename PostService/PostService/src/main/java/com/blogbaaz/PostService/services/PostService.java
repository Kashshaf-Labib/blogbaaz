package com.blogbaaz.PostService.services;

import com.blogbaaz.PostService.dtos.CreatePostRequest;
import com.blogbaaz.PostService.dtos.PostDto;
import com.blogbaaz.PostService.dtos.UpdatePostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PostService {

    PostDto createPost(CreatePostRequest request);

    PostDto getPostById(String postId);

    PostDto getPostBySlug(String slug);

    // Paginated methods
    Page<PostDto> getAllPosts(Pageable pageable);

    Page<PostDto> getPostsByAuthor(String authorId, Pageable pageable);

    Page<PostDto> getPublishedPosts(Pageable pageable);

    Page<PostDto> getPostsByCategory(String category, Pageable pageable);

    // List methods (for SearchService)
    List<PostDto> searchPosts(String keyword, Sort sort);

    List<PostDto> getPostsByAuthor(String authorId, Sort sort);

    List<PostDto> getPostsByCategory(String category, Sort sort);

    List<PostDto> getPostsByTags(String[] tags, Sort sort);

    List<PostDto> getPublishedPosts(Sort sort);

    PostDto updatePost(String postId, UpdatePostRequest request);

    PostDto publishPost(String postId);

    PostDto archivePost(String postId);

    void deletePost(String postId);

    void incrementViewCount(String postId);

    void incrementLikeCount(String postId);

    void incrementCommentCount(String postId);
}
