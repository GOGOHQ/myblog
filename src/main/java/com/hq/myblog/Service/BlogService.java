package com.hq.myblog.Service;

import com.hq.myblog.Po.Tag;
import com.hq.myblog.Vo.BlogQuery;
import com.hq.myblog.Po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);

    Page<Blog> findPublishedBlogs(Pageable pageable);

    Long BlogCount();

    int updateLikeCount(Long id, Integer likeCount);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Pageable pageable, String query);

    Page<Blog> listBlog(Long tagId, BlogQuery blogQuery, Pageable pageable);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);

    List<Blog> listRecentBlogTop(Integer size);

    Map<String, List<Blog>> archiveBlog();

    Blog findByTitle(String title);

    Blog getAndConvert(Long id);
}
