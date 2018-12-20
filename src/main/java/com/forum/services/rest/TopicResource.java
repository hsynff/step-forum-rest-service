package com.forum.services.rest;

import com.forum.services.dao.TopicDaoImpl;
import com.forum.services.exceptions.TopicException;
import com.forum.services.model.Comment;
import com.forum.services.model.Topic;
import com.forum.services.model.response.ResponseModel;
import com.forum.services.service.TopicService;
import com.forum.services.service.TopicServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("topics")
public class TopicResource {

    private TopicService topicService = new TopicServiceImpl(new TopicDaoImpl());
    private static final String JSON = MediaType.APPLICATION_JSON;

    @GET
    @Produces(JSON)
    public ResponseModel getAllTopics() throws SQLException {
        List<Topic> list = topicService.getAllTopic();
        ResponseModel responseModel = new ResponseModel();
        responseModel.withStatusSuccess();
        responseModel.setBody(list);
        return responseModel;
    }

    @GET
    @Path("/{id}")
    @Produces(JSON)
    public ResponseModel getTopicById(@PathParam("id") int id) throws SQLException, TopicException {
        Topic topic = topicService.getTopicById(id);
        ResponseModel responseModel = new ResponseModel();
        responseModel.withStatusSuccess();
        responseModel.setBody(topic);
        return responseModel;
    }

    @GET
    @Path("/popular")
    @Produces(JSON)
    public ResponseModel getPopularTopics() throws SQLException {
        List<Topic> topicList = topicService.getPopularTopics();
        ResponseModel responseModel = new ResponseModel();
        responseModel.withStatusSuccess();
        responseModel.setBody(topicList);
        return responseModel;
    }

    @GET
    @Path("/similar")
    @Produces(JSON)
    public ResponseModel getSimilarTopics(@QueryParam("keywords") List<String> keywords) throws SQLException {

        List<Topic> topicList = topicService.getSimilarTopics(keywords.toArray(new String[]{}));
        ResponseModel responseModel = new ResponseModel();
        responseModel.withStatusSuccess();
        responseModel.setBody(topicList);
        return responseModel;
    }


    @GET
    @Path("/{id}/comments")
    @Produces(JSON)
    public ResponseModel getCommentsByTopicId(@PathParam("id") int id) throws SQLException, TopicException {
        //        lets check if topic exists
        topicService.getTopicById(id);
        List<Comment> commentList = topicService.getCommentsByTopicId(id);
        ResponseModel responseModel = new ResponseModel();
        responseModel.withStatusSuccess();
        responseModel.setBody(commentList);
        return responseModel;
    }



    @POST
    @Produces(JSON)
    @Consumes(JSON)
    public ResponseModel addTopic(Topic topic) throws SQLException {
        Topic newTopic = topicService.addTopic(topic);
        ResponseModel responseModel = new ResponseModel();
        responseModel.withStatusSuccess();
        responseModel.setBody(newTopic);
        return responseModel;
    }



    @PUT
    @Path("/{id}/viewCount")
    public Response updateTopicViewCount(@PathParam("id") int id) throws SQLException, TopicException {

//        lets check if topic exists
        topicService.getTopicById(id);
        topicService.updateTopicViewCount(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
