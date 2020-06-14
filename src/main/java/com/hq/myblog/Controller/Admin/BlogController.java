package com.hq.myblog.Controller.Admin;

import com.hq.myblog.Po.Blog;
import com.hq.myblog.Po.User;
import com.hq.myblog.Service.TagService;
import com.hq.myblog.Vo.BlogQuery;
import com.hq.myblog.Service.BlogService;
import com.hq.myblog.Service.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/admin")
public class BlogController {
    @Autowired
    BlogService blogService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;

    @GetMapping("/blogPage")
    public String goBlogs(Model model,
                          @PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                          BlogQuery blog) {
        model.addAttribute("pages", blogService.listBlog(pageable, blog));
        model.addAttribute("types", typeService.listType());
        log.info("blog:{}", blog);
        return "blogs";
    }

    @DeleteMapping("/blogs/{id}")
    public String deleteBlog(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Blog blog = blogService.getBlog(id);
        blogService.deleteBlog(id);
        redirectAttributes.addFlashAttribute("message", "删除博客成功:" + blog.getTitle());
        return "redirect:/admin/blogPage";

    }

    @GetMapping("/blogs/{id}")
    public String goEdit(Model model, @PathVariable Long id) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        return "blog_input";
    }


    @PostMapping("/blogList")
    public String fetchBlogs(Model model,
                             @PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                             BlogQuery blog) {
        model.addAttribute("pages", blogService.listBlog(pageable, blog));
        return "blogs :: blogList";
    }

    @GetMapping("/blogInput")
    public String goInputBlog(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("blog", new Blog());
        return "blog_input";
    }


    @PostMapping("/blogs")
    public String addBlog(Blog blog, RedirectAttributes redirectAttributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        blog.setFlag("".equals(blog.getFlag()) ? "原创" : blog.getFlag());
        log.info("blog:{}", blog);
        log.info("falg:{}", blog.getFlag());
        //编辑
        if (blog.getId() != null) {
            Blog blog1 = blogService.updateBlog(blog.getId(), blog);
            if (blog1 == null) {
                redirectAttributes.addFlashAttribute("error", "编辑失败:" + blog1.getTitle());
            } else {
                redirectAttributes.addFlashAttribute("message", "编辑成功:" + blog1.getTitle());
            }
            return "redirect:/admin/blogPage";
        }
        //新增
        else {
            Blog b = blogService.saveBlog(blog);
            if (b == null) {
                redirectAttributes.addFlashAttribute("error", "新增失败:" + blog.getTitle());
            } else {
                redirectAttributes.addFlashAttribute("message", "新增成功:" + blog.getTitle());
            }
            return "redirect:/admin/blogPage";
        }
    }


}
