package com.forum.services.model.response;

import com.forum.services.constants.MessageConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorWrapper {
    private String message;
    private Integer code;

    public ErrorWrapper(String message){
        this.message = message;
        this.code = MessageConstants.errorCodeOf(message);
    }
}
