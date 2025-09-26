package com.blogbaaz.PostService.repositories;

import com.blogbaaz.PostService.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

    Optional<Post> findBySlug(String slug);

    Page<Post> findByAuthorId(String authorId, Pageable pageable);

    Page<Post> findByStatus(Post.PostStatus status, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.category.categoryId = :categoryId")
    Page<Post> findByCategoryId(@Param("categoryId") String categoryId, Pageable pageable);

    Page<Post> findByTagsContaining(String tag, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isPublished = true AND p.status = 'PUBLISHED' ORDER BY p.publishedAt DESC")
    Page<Post> findPublishedPosts(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isFeatured = true AND p.isPublished = true AND p.status = 'PUBLISHED'")
    List<Post> findFeaturedPosts();

    @Query("SELECT p FROM Post p WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.excerpt) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);

    boolean existsBySlug(String slug);
}
