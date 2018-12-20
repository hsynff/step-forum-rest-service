package com.forum.services.rest;

import com.forum.services.dao.TopicDaoImpl;
import com.forum.services.model.Comment;
import com.forum.services.model.response.ResponseModel;
import com.forum.services.service.TopicService;
import com.forum.services.service.TopicServiceImpl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("comments")
public class CommentResource {
    private TopicService topicService = new TopicServiceImpl(new TopicDaoImpl());
    private static final String JSON = MediaType.APPLICATION_JSON;


    @POST
    @Produces(JSON)
    @Consumes(JSON)
    public ResponseModel addComment(Comment comment) throws SQLException {
        Comment newComment = topicService.addComment(comment);
        ResponseModel responseModel = new ResponseModel();
        responseModel.withStatusSuccess();
        responseModel.setBody(newComment);
        return responseModel;
    }
}
