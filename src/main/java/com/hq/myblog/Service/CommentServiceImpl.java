package com.hq.myblog.Service;

import com.hq.myblog.Dao.CommentRepository;
import com.hq.myblog.Po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;
    private List<Comment> tempReplys = new ArrayList<>();

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "createTime");
        List<Comment> commentList = commentRepository.findByBlogIdAndParentCommentIsNull(blogId, Sort.by(order));
        return eachComment(commentList);
    }

    /**
     * 生成新的list避免影响数据库
     * 循环每个顶级的评论节点
     *
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        return combineComment(commentsView);
    }

    /**
     * root根节点,blog不为空的对象集合
     *
     * @param comments
     * @return
     */
    private List<Comment> combineComment(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replys = comment.getReplyComment();
            for (Comment reply : replys) {
                recursive(reply);
            }
            comment.setReplyComment(tempReplys);
            tempReplys = new ArrayList<>();
        }
        return comments;
    }

    /**
     * 递归每个评论,先加入自己再递归自己的子节点
     * @param comment
     */
    private void recursive(Comment comment) {
        tempReplys.add(comment);
        if (comment.getReplyComment().size() > 0) {
            for (Comment reply : comment.getReplyComment()) {
                tempReplys.add(reply);
                if (reply.getReplyComment().size() > 0) {
                    recursive(reply);
                }
            }
        }
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentID = comment.getParentComment().getId();
        if (parentID != -1) {
            Comment parent = commentRepository.getOne(parentID);
            comment.setParentComment(parent);
            parent.getReplyComment().add(comment);
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }
}
