package com.blogbaaz.CommentService.repositories;

import com.blogbaaz.CommentService.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    // Find comments by post ID
    List<Comment> findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(String postId);
    
    // Find root comments (no parent) for a post
    List<Comment> findByPostIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtAsc(String postId);
    
    // Find replies to a specific comment
    List<Comment> findByParentCommentIdAndIsDeletedFalseOrderByCreatedAtAsc(String parentCommentId);
    
    // Find comments by author
    List<Comment> findByAuthorIdAndIsDeletedFalseOrderByCreatedAtDesc(String authorId);
    
    // Find comments by post with pagination
    Page<Comment> findByPostIdAndIsDeletedFalseOrderByCreatedAtDesc(String postId, Pageable pageable);
    
    // Find root comments with pagination
    Page<Comment> findByPostIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc(String postId, Pageable pageable);
    
    // Count comments by post
    long countByPostIdAndIsDeletedFalse(String postId);
    
    // Count replies to a comment
    long countByParentCommentIdAndIsDeletedFalse(String parentCommentId);
    
    // Find comment by ID
    Optional<Comment> findByCommentIdAndIsDeletedFalse(String commentId);
    
    // Find comments by path (for hierarchical queries)
    @Query("SELECT c FROM Comment c WHERE c.path LIKE :pathPattern AND c.isDeleted = false ORDER BY c.createdAt ASC")
    List<Comment> findByPathStartingWith(@Param("pathPattern") String pathPattern);
    
    // Find all comments in a tree (for a specific post)
    @Query("SELECT c FROM Comment c WHERE c.postId = :postId AND c.isDeleted = false ORDER BY c.path ASC, c.createdAt ASC")
    List<Comment> findCommentTreeByPostId(@Param("postId") String postId);
    
    // Search comments by content
    @Query("SELECT c FROM Comment c WHERE c.content LIKE %:keyword% AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<Comment> searchCommentsByContent(@Param("keyword") String keyword);
}
