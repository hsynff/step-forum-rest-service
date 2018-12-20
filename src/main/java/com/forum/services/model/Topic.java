package com.forum.services.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
public class Topic {
    private Integer id;
    private String title;
    private String desc;
    private LocalDateTime shareDate;
    private Integer viewCount;
    private User user;
    private List<Comment> commentList;
    private Integer commentCount;
    private Integer status;

    public Topic(){
        commentList = new ArrayList<>();
    }

    public void addComment(Comment comment){
        commentList.add(comment);
    }
}
