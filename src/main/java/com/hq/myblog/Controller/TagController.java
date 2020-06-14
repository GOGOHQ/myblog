package com.hq.myblog.Controller;

import com.hq.myblog.Dao.TagRepository;
import com.hq.myblog.Po.Blog;
import com.hq.myblog.Po.Tag;
import com.hq.myblog.Po.Type;
import com.hq.myblog.Service.BlogService;
import com.hq.myblog.Service.TagService;
import com.hq.myblog.Service.TypeService;
import com.hq.myblog.Vo.BlogQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tags")
@Slf4j
public class TagController {
    @Autowired
    TagService tagService;
    @Autowired
    BlogService blogService;
    @Autowired
    TagRepository tagRepository;

    @GetMapping("/{id}")
    public String goTypes(Model model, @PathVariable Long id, @PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        List<Tag> tags = tagService.listTagTop(10000);
        model.addAttribute("tags", tags);
        Long curId;
        if (id == -1) {
            curId = tags.get(0).getId();
        } else {
            curId = id;
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setPublished(true);
        Page<Blog> blogs = blogService.listBlog(curId, blogQuery, pageable);
        model.addAttribute("blogs", blogs);
        model.addAttribute("curId", curId);
        return "tags";
    }
}
