package com.hq.myblog.Controller.Admin;

import com.hq.myblog.Po.Tag;
import com.hq.myblog.Po.Type;
import com.hq.myblog.Service.TagService;
import com.hq.myblog.Service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminTagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String goTags(@PageableDefault(size = 6, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("tags", tagService.listTag(pageable));
        return "admin_tags";
    }

    @GetMapping("/tags/goInput")
    public String goInput(Model model) {
        model.addAttribute("tag", new Tag());
        return "tag_input";
    }

    @DeleteMapping("/tags/{tagId}")
    public String deleteTag(@PathVariable Long tagId, RedirectAttributes redirectAttributes) {
        Tag tag = tagService.getTag(tagId);
        tagService.deleteTag(tagId);
        redirectAttributes.addFlashAttribute("message", "删除标签成功:" + tag.getName());
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags")
    public String addTag(@Valid Tag tag, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (tagService.findByName(tag.getName()) != null) {
            bindingResult.rejectValue("name", "tagExist", "标签已存在:" + tag.getName());
        }
        if (bindingResult.hasErrors()) {
            return "tag_input";
        }
        Tag t = tagService.saveTag(tag);
        if (t == null) {
            redirectAttributes.addFlashAttribute("message", "添加失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "添加标签成功:" + tag.getName());
        }
        return "redirect:/admin/tags";
    }

}
