package com.hq.myblog.Dao;

import com.hq.myblog.Po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByBlogIdAndParentCommentIsNull(Long blog_id, Sort sort);
}
