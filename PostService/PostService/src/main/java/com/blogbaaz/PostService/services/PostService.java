package com.blogbaaz.PostService.services;

import com.blogbaaz.PostService.dtos.CreatePostRequest;
import com.blogbaaz.PostService.dtos.PostDto;
import com.blogbaaz.PostService.dtos.UpdatePostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostDto createPost(CreatePostRequest request);

    PostDto getPostById(String postId);

    PostDto getPostBySlug(String slug);

    Page<PostDto> getAllPosts(Pageable pageable);

    Page<PostDto> getPostsByAuthor(String authorId, Pageable pageable);

    Page<PostDto> getPublishedPosts(Pageable pageable);

    Page<PostDto> getPostsByCategory(String categoryId, Pageable pageable);

    Page<PostDto> searchPosts(String keyword, Pageable pageable);

    List<PostDto> getFeaturedPosts();

    PostDto updatePost(String postId, UpdatePostRequest request);

    PostDto publishPost(String postId);

    PostDto archivePost(String postId);

    void deletePost(String postId);

    void incrementViewCount(String postId);

    void incrementLikeCount(String postId);

    void incrementCommentCount(String postId);
}
