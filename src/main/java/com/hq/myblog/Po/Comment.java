package com.hq.myblog.Po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_comment")
public class Comment {
    @Id
    @GeneratedValue
    private long id;
    private String nickName;
    private String email;
    private String content;
    private String avatar;
    private boolean adminComment;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @ToString.Exclude
    @ManyToOne
    private Blog blog;
    @OneToMany()
    private List<Comment> replyComment = new ArrayList<>();
    @ToString.Exclude
    @ManyToOne
    private Comment parentComment;




}
