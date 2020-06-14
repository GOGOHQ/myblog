package com.hq.myblog.Controller;

import com.hq.myblog.Exception.CustomException;
import com.hq.myblog.Exception.CustomExceptionType;
import com.hq.myblog.Po.Blog;
import com.hq.myblog.Service.BlogService;
import com.hq.myblog.Service.CommentService;
import com.hq.myblog.Service.TagService;
import com.hq.myblog.Service.TypeService;
import com.hq.myblog.Vo.BlogQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    BlogService blogService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;
    @Autowired
    CommentService commentService;

    @GetMapping()
    public String index() {
        return "welcome";
    }

    @GetMapping("index")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("blogs", blogService.findPublishedBlogs(pageable));
        log.info("blogs:{}", blogService.findPublishedBlogs(pageable));
        model.addAttribute("recommendBlogs", blogService.listRecentBlogTop(8));
        return "index";
    }

    @GetMapping("blogs")
    public String searchBlogs(@RequestParam(value = "query") String query, @PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("blogs", blogService.listBlog(pageable, "%" + query + "%"));
        model.addAttribute("query", query);
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecentBlogTop(8));
        return "search";
    }

    @GetMapping("footer/newblog")
    public String fetchRecentBlogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecentBlogTop(3));
        log.info("size:{}", blogService.listRecentBlogTop(3).size());
        return "about :: newsList";
    }

    @GetMapping("like")
    public String likePro(@RequestParam("id") String id, @RequestParam("fav") String fav, Model model) {
        log.info("fav:{}", fav);
        Long Id = Long.valueOf(id);
        String msg = "取消点赞";

        model.addAttribute("blog", blogService.getBlog(Id));
        Integer likeCount = Integer.valueOf(fav);
        if (likeCount == 1) {
            msg = "点赞成功";
        }
        model.addAttribute("msg", msg);
        log.info("likeCount:{}", likeCount);
        blogService.updateLikeCount(Id, likeCount);
        return "blog :: infos";
    }

    @GetMapping("blogs/{id}")
    public String Godetail(@PathVariable Long id, Model model) {
        Blog blog = blogService.getAndConvert(id);
        model.addAttribute("blog", blog);
        model.addAttribute("comments", commentService.listCommentByBlogId(id));
        return "blog";
    }

}
