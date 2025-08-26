package com.blogbaaz.PostService.services.impl;


import com.blogbaaz.PostService.dtos.CreatePostRequest;
import com.blogbaaz.PostService.dtos.PostDto;
import com.blogbaaz.PostService.dtos.UpdatePostRequest;
import com.blogbaaz.PostService.entities.Category;
import com.blogbaaz.PostService.entities.Post;
import com.blogbaaz.PostService.exceptions.PostNotFoundException;
import com.blogbaaz.PostService.repositories.CategoryRepository;
import com.blogbaaz.PostService.repositories.PostRepository;
import com.blogbaaz.PostService.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public PostDto createPost(CreatePostRequest request) {
        // Generate slug from title
        String slug = generateSlug(request.getTitle());

        // Get category if provided
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElse(null);
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .excerpt(request.getExcerpt())
                .authorId(request.getAuthorId())
                .authorName(request.getAuthorName())
                .tags(request.getTags())
                .category(category)
                .slug(slug)
                .featuredImage(request.getFeaturedImage())
                .isFeatured(request.isFeatured())
                .status(Post.PostStatus.DRAFT)
                .build();

        Post savedPost = postRepository.save(post);
        return entityToDto(savedPost);
    }

    @Override
    public PostDto getPostById(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        return entityToDto(post);
    }

    @Override
    public PostDto getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new PostNotFoundException("Post not found with slug: " + slug));
        return entityToDto(post);
    }

    @Override
    public Page<PostDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(this::entityToDto);
    }

    @Override
    public Page<PostDto> getPostsByAuthor(String authorId, Pageable pageable) {
        return postRepository.findByAuthorId(authorId, pageable)
                .map(this::entityToDto);
    }

    @Override
    public Page<PostDto> getPublishedPosts(Pageable pageable) {
        return postRepository.findPublishedPosts(pageable)
                .map(this::entityToDto);
    }

    @Override
    public Page<PostDto> getPostsByCategory(String categoryId, Pageable pageable) {
        return postRepository.findByCategoryId(categoryId, pageable)
                .map(this::entityToDto);
    }

    @Override
    public Page<PostDto> searchPosts(String keyword, Pageable pageable) {
        return postRepository.searchPosts(keyword, pageable)
                .map(this::entityToDto);
    }

    @Override
    public List<PostDto> getFeaturedPosts() {
        return postRepository.findFeaturedPosts()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto updatePost(String postId, UpdatePostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        if (request.getTitle() != null) {
            post.setTitle(request.getTitle());
            post.setSlug(generateSlug(request.getTitle()));
        }

        if (request.getContent() != null) {
            post.setContent(request.getContent());
        }

        if (request.getExcerpt() != null) {
            post.setExcerpt(request.getExcerpt());
        }

        if (request.getTags() != null) {
            post.setTags(request.getTags());
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElse(null);
            post.setCategory(category);
        }

        if (request.getFeaturedImage() != null) {
            post.setFeaturedImage(request.getFeaturedImage());
        }

        post.setFeatured(request.isFeatured());

        if (request.getStatus() != null) {
            post.setStatus(Post.PostStatus.valueOf(request.getStatus()));
        }

        Post updatedPost = postRepository.save(post);
        return entityToDto(updatedPost);
    }

    @Override
    public PostDto publishPost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        post.setStatus(Post.PostStatus.PUBLISHED);
        post.setPublished(true);
        post.setPublishedAt(LocalDateTime.now());

        Post publishedPost = postRepository.save(post);
        return entityToDto(publishedPost);
    }

    @Override
    public PostDto archivePost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        post.setStatus(Post.PostStatus.ARCHIVED);
        post.setPublished(false);

        Post archivedPost = postRepository.save(post);
        return entityToDto(archivedPost);
    }

    @Override
    public void deletePost(String postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post not found with id: " + postId);
        }
        postRepository.deleteById(postId);
    }

    @Override
    public void incrementViewCount(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
    }

    @Override
    public void incrementLikeCount(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }

    @Override
    public void incrementCommentCount(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);
    }

    private String generateSlug(String title) {
        String slug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .trim();

        // Ensure uniqueness
        String baseSlug = slug;
        int counter = 1;
        while (postRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter++;
        }

        return slug;
    }

    private PostDto entityToDto(Post post) {
        return PostDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .excerpt(post.getExcerpt())
                .authorId(post.getAuthorId())
                .authorName(post.getAuthorName())
                .status(post.getStatus().name())
                .tags(post.getTags())
                .categoryId(post.getCategory() != null ? post.getCategory().getCategoryId() : null)
                .categoryName(post.getCategory() != null ? post.getCategory().getName() : null)
                .slug(post.getSlug())
                .featuredImage(post.getFeaturedImage())
                .isPublished(post.isPublished())
                .isFeatured(post.isFeatured())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .publishedAt(post.getPublishedAt())
                .build();
    }
}
