package com.forum.services.rest;

import com.forum.services.dao.TopicDaoImpl;
import com.forum.services.dao.UserDaoImpl;
import com.forum.services.exceptions.UserException;
import com.forum.services.model.Topic;
import com.forum.services.model.User;
import com.forum.services.model.response.ResponseModel;
import com.forum.services.service.TopicService;
import com.forum.services.service.TopicServiceImpl;
import com.forum.services.service.UserService;
import com.forum.services.service.UserServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("users")
public class UserResource {

    private TopicService topicService = new TopicServiceImpl(new TopicDaoImpl());
    private UserService userService = new UserServiceImpl(new UserDaoImpl());

    private static final String JSON = MediaType.APPLICATION_JSON;

    @GET
    @Path("/{id}/topics")
    @Produces(JSON)
    public ResponseModel getTopicsByUserId(@PathParam("id") int id) throws SQLException {
        List<Topic> topicList = topicService.getTopicByUserId(id);

        ResponseModel responseModel = new ResponseModel();
        responseModel.withStatusSuccess();
        responseModel.setBody(topicList);
        return responseModel;
    }

    @GET
    @Path("/email/{email}")
    @Produces(JSON)
    public ResponseModel getUserByEmail(@PathParam("email") String email) throws SQLException, UserException {
        User user = userService.getUserByEmail(email);

        ResponseModel responseModel = new ResponseModel();
        responseModel.withStatusSuccess();
        responseModel.setBody(user);

        return responseModel;
    }

    @POST
    @Produces(JSON)
    @Consumes(JSON)
    public ResponseModel addUser(User user) throws SQLException, UserException {
        User newUser = userService.addUser(user);

        ResponseModel responseModel = new ResponseModel();
        responseModel.withStatusSuccess();
        responseModel.setBody(newUser);

        return responseModel;
    }







}
