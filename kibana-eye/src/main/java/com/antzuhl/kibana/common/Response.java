package com.antzuhl.kibana.common;

import lombok.Data;

/**
 * @author AntzUhl
 * Date 2020/8/25 15:51
 */
@Data
public class Response {

    private boolean isOk;
    private int code;
    private String message;
    private Object data;

    private Response() {}

    public static Response success() {
        Response response = new Response();
        response.setOk(true);
        response.setCode(CustomExceptionEnum.SUCCESS.getCode());
        response.setMessage("success");
        return response;
    }

    public static Response success(String message) {
        Response response = new Response();
        response.setOk(true);
        response.setCode(CustomExceptionEnum.SUCCESS.getCode());
        response.setMessage(message);
        return response;
    }

    public static Response success(String message, Object data) {
        Response response = new Response();
        response.setOk(true);
        response.setCode(CustomExceptionEnum.SUCCESS.getCode());
        response.setData(data);
        response.setMessage(message);
        return response;
    }

    public static Response error(String message) {
        Response response = new Response();
        response.setOk(false);
        response.setCode(CustomExceptionEnum.USER_INPUT_ERROR.getCode());
        response.setMessage(message);
        return response;
    }

    public static Response error(CustomException e) {
        Response response = new Response();
        response.setOk(false);
        response.setCode(e.getCode());

        if (e.getCode() == CustomExceptionEnum.USER_INPUT_ERROR.getCode()) {
            response.setMessage(e.getMessage());
        } else if (e.getCode() == CustomExceptionEnum.SYSTEM_ERROR.getCode()) {
            response.setMessage(e.getMessage() + " 系统内部异常");
        } else if (e.getCode() == CustomExceptionEnum.UNKNOWN_ERROR.getCode()) {
            response.setMessage(e.getMessage() + " 未知异常，请稍后重试");
        }
        return response;
    }
}
