package com.hq.myblog.Controller.Admin;

import com.hq.myblog.Po.User;
import com.hq.myblog.Service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
@Slf4j
@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    UserServiceImpl userService;

    @GetMapping
    public String goLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String userName, @RequestParam String passWord, HttpSession session, RedirectAttributes attributes, Model model) {
        User user = userService.checkUser(userName, passWord);
        log.info("{}",user);
        if (user != null) {
            user.setPassWord(null);
            session.setAttribute("user", user);
            model.addAttribute("user",user);
            log.info("登陆成功:用户{}",user);
            return "admin_index";
        } else {
            attributes.addFlashAttribute("message", "用户名或密码错误..");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logOut")
    public String logOut(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }

}
