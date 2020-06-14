package com.hq.myblog.Controller.Admin;

import com.hq.myblog.Po.Type;
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
public class AdminTypeController {
    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String goTypes(@PageableDefault(size = 6, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("types", typeService.listType(pageable));
        return "admin_types";
    }

    @GetMapping("/types/goInput")
    public String goInput(Model model) {
        model.addAttribute("type", new Type());
        return "type_input";
    }

    @DeleteMapping("/types/{typeId}")
    public String deleteType(@PathVariable Long typeId, RedirectAttributes redirectAttributes) {
        Type type = typeService.getType(typeId);
        typeService.deleteType(typeId);
        redirectAttributes.addFlashAttribute("message", "删除分类成功:" + type.getName());
        return "redirect:/admin/types";
    }

    @PostMapping("/types")
    public String addType(@Valid Type type, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (typeService.findByName(type.getName()) != null) {
            bindingResult.rejectValue("name", "typeExist", "分类已存在:" + type.getName());
        }
        if (bindingResult.hasErrors()) {
            return "type_input";
        }
        Type t = typeService.saveType(type);
        if (t == null) {
            redirectAttributes.addFlashAttribute("message", "添加失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "添加分类成功:" + type.getName());
        }
        return "redirect:/admin/types";
    }

}
