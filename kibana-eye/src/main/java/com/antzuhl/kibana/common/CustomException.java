package com.antzuhl.kibana.common;

/**
 * @author AntzUhl
 * Date 2020/8/25 15:49
 */
public class CustomException extends RuntimeException {

    private int code;
    private String message;

    public CustomException(CustomExceptionEnum exceptionEnum, String message) {
        this.code = exceptionEnum.getCode();
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
