package com.antzuhl.kibana.common;

/**
 * @author AntzUhl
 * Date 2020/8/25 15:46
 */
public enum CustomExceptionEnum {
    SUCCESS(200, "成功"),
    USER_INPUT_ERROR(400, "用户输入异常"),
    SYSTEM_ERROR(500, "系统内部异常"),
    UNKNOWN_ERROR(999, "未知异常")
    ;

    int code;
    String message;
    CustomExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
