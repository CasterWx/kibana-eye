package com.antzuhl.kibana.controller.response;

import lombok.Data;

/**
 * @author AntzUhl
 * @Date 17:02
 */
@Data
public class LoginInfoResponse {

    /**
     * Log time.
     */
    private String logTime;

    /**
     * Log content.
     */
    private String mobile;


    /**
     * 客户端类型
     */
    private String clientType;

    /**
     * 失败次数
     */
    private Integer errorNum;


    /**
     * env
     */
    private String env;

    /**
     * env
     */
    private String ip;


}
