package com.hq.myblog.Po;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;
    private String firstPicture;
    private String flag;
    private Integer viewCount;
    private Integer likeCount;
    private String etc;
    private String description;
    private boolean appreciation;//是否开启赞赏
    private boolean shareStatement;//是否开启转载声明
    private boolean commentabled;//是否开启评论
    private boolean published;//是否发布
    private boolean recommend;//是否被推荐
    @Transient
    private String tagIds;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @ManyToOne
    @ToString.Exclude
    private Type type;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<Tag> tags = new ArrayList<>();
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "blog")
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    public void init() {
        this.tagIds = initTagIds(tags);
    }

    public String initTagIds(List<Tag> list) {
        if (!list.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            boolean flag = false;
            for (Tag tag : list) {
                if (flag) {
                    sb.append(",");
                } else {
                    flag = true;
                }
                sb.append(tag.getId());
            }
            return sb.toString();
        } else {
            return tagIds;
        }
    }
}
