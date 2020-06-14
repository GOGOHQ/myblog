package com.hq.myblog.Controller;

import com.hq.myblog.Po.Blog;
import com.hq.myblog.Po.Type;
import com.hq.myblog.Service.BlogService;
import com.hq.myblog.Service.TypeService;
import com.hq.myblog.Vo.BlogQuery;
import lombok.extern.slf4j.Slf4j;
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

import java.util.List;

@Controller
@RequestMapping("/types")
@Slf4j
public class TypeController {
    @Autowired
    TypeService typeService;
    @Autowired
    BlogService blogService;

    @GetMapping("/{id}")
    public String goTypes(Model model, @PathVariable Long id, @PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        List<Type> types = typeService.listTypeTop(10000);
        model.addAttribute("types", types);
        Long curId;
        if (id == -1) {
            curId = types.get(0).getId();
        } else {
            curId = id;
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(curId);
        blogQuery.setPublished(true);
        Page<Blog> blogs = blogService.listBlog(pageable, blogQuery);
        model.addAttribute("blogs", blogs);
        model.addAttribute("curId", curId);
        return "types";
    }
}
