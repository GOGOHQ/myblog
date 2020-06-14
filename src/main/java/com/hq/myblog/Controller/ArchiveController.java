package com.hq.myblog.Controller;

import com.hq.myblog.Service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/archives")
public class ArchiveController {
    @Autowired
    BlogService blogService;
    @GetMapping()
    public String goArchive(Model model){
        model.addAttribute("data",blogService.archiveBlog());
        model.addAttribute("blogCount",blogService.BlogCount());
        return "archives";
    }

}
