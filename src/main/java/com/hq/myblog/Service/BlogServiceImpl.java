package com.hq.myblog.Service;

import com.hq.myblog.Dao.BlogRepository;
import com.hq.myblog.Exception.CustomException;
import com.hq.myblog.Exception.CustomExceptionType;
import com.hq.myblog.Po.Blog;

import com.hq.myblog.Po.Type;
import com.hq.myblog.Utils.MarkDownUtils;
import com.hq.myblog.Utils.MyBeanUtils;
import com.hq.myblog.Vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Override
    public Page<Blog> findPublishedBlogs(Pageable pageable) {
        return blogRepository.findPublishedBlogs(pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, String query) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Override
    public Long BlogCount() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public int updateLikeCount(Long id, Integer likeCount) {
        return blogRepository.updateLikeCount(id, likeCount);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, BlogQuery blogQuery, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (blogQuery.isPublished()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("published"), blogQuery.isPublished()));
                    criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                    Join join = root.join("tags");
                    predicates.add(criteriaBuilder.equal(join.get("id"), tagId));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (blog == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        b.setContent(MarkDownUtils.MarkDownToHtmlExtensions(content));
        blogRepository.updateViewCount(id);
        return b;
    }

    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                if (blog.isPublished()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("published"), blog.isPublished()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setLikeCount(0);
            blog.setViewCount(0);
        } else {
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }

    @Override
    public Blog findByTitle(String title) {
        return blogRepository.findByTitle(title);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public List<Blog> listRecentBlogTop(Integer size) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, Sort.by(order));
        return blogRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if (b == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该条博客记录不存在");
        }
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyName(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
