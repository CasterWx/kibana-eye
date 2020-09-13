package com.antzuhl.kibana.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author AntzUhl
 * Date 2020/8/25 15:45
 */
@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Response exception(CustomException e) {
        return Response.error(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response exception(Exception e) {
        return Response.error(new CustomException(CustomExceptionEnum.UNKNOWN_ERROR, e.getMessage()));
    }

}
