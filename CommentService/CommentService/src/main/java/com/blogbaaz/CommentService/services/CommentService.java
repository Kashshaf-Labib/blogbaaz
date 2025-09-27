package com.blogbaaz.CommentService.services;

import com.blogbaaz.CommentService.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    CommentDto createComment(CreateCommentRequest request, String authorId, String authorName);
    
    CommentDto getCommentById(String commentId);
    
    CommentDto updateComment(String commentId, UpdateCommentRequest request, String authorId);
    
    void deleteComment(String commentId, String authorId);
    
    List<CommentTreeResponse> getCommentsByPostId(String postId);
    
    List<CommentTreeResponse> getRootCommentsByPostId(String postId);
    
    List<CommentDto> getRepliesByCommentId(String commentId);
    
    List<CommentDto> getCommentsByAuthor(String authorId);
    
    Page<CommentDto> getCommentsByPostIdPaginated(String postId, Pageable pageable);
    
    Page<CommentDto> getRootCommentsByPostIdPaginated(String postId, Pageable pageable);
    
    void likeComment(String commentId);
    
    void unlikeComment(String commentId);
    
    long getCommentCountByPostId(String postId);
    
    long getReplyCountByCommentId(String commentId);
    
    List<CommentDto> searchComments(String keyword);
    
    boolean isCommentOwner(String commentId, String authorId);
}

