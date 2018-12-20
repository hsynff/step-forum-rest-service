package com.forum.services.rest;

import com.forum.services.constants.MessageConstants;
import com.forum.services.exceptions.CustomException;
import com.forum.services.exceptions.UserException;
import com.forum.services.model.response.ErrorWrapper;
import com.forum.services.model.response.ResponseModel;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        ErrorWrapper errorWrapper;
        if (!(e instanceof CustomException)){
            e.printStackTrace();
            errorWrapper = new ErrorWrapper(MessageConstants.ERROR_INTERNAL);
        }else{
            errorWrapper = new ErrorWrapper(e.getMessage());
        }

        ResponseModel responseModel = new ResponseModel();
        responseModel.withStatusError();
        responseModel.setBody(errorWrapper);
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(responseModel)
                .build();
    }
}
