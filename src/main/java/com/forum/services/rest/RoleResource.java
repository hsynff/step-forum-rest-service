package com.forum.services.rest;

import com.forum.services.dao.UserDaoImpl;
import com.forum.services.model.Action;
import com.forum.services.model.response.ResponseModel;
import com.forum.services.service.UserService;
import com.forum.services.service.UserServiceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("roles")
public class RoleResource {

    private UserService userService = new UserServiceImpl(new UserDaoImpl());

    private static final String JSON = MediaType.APPLICATION_JSON;

    @GET
    @Path("/{id}/actions")
    @Produces(JSON)
    public ResponseModel getActionListByRoleId(@PathParam("id")int id) throws SQLException {
        List<Action> list = userService.getActionListByRoleId(id);

        ResponseModel responseModel = new ResponseModel();
        responseModel.withStatusSuccess();
        responseModel.setBody(list);
        return responseModel;
    }

}
