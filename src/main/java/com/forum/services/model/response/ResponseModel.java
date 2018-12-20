package com.forum.services.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel {
    private String status;
    private Object body;

    public void withStatusError(){
        this.status = "error";
    }

    public void withStatusSuccess(){
        this.status = "success";
    }
}
