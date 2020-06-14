package com.hq.myblog.Controller;

import com.hq.myblog.Config.AvatarConfig;
import com.hq.myblog.Po.Blog;
import com.hq.myblog.Po.Comment;
import com.hq.myblog.Po.User;
import com.hq.myblog.Service.BlogService;
import com.hq.myblog.Service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.Random;

@Slf4j
@Controller
public class CommentController {
    @Autowired
    BlogService blogService;
    @Autowired
    CommentService commentService;
    @Autowired
    AvatarConfig avatarConfig;
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String fetchComments(@PathVariable Long blogId, Model model) {
        for (Comment comment : commentService.listCommentByBlogId(blogId)) {
            log.info("comment:{}", comment);

        }
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String postComment(Comment comment, HttpSession session) {
        Blog blog = blogService.getBlog(comment.getBlog().getId());
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAdminComment(true);
            comment.setAvatar(user.getAvatar());
            comment.setNickName(user.getUserName());
        } else {
            comment.setAvatar(avatarConfig.getAvatar().get(new Random().nextInt(avatarConfig.getAvatar().size())));
        }
        comment.setBlog(blog);
        commentService.saveComment(comment);
        return "redirect:/comments/" + comment.getBlog().getId();
    }
}
