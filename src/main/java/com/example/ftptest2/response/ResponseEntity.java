package com.example.ftptest2.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseEntity {
    public Boolean success;
    public Object data;
    public Integer code;

    public static  ResponseEntity getSuccess(Object obj){
        return  new ResponseEntity().setCode(200).setSuccess(true).setData(obj);
    }
}
