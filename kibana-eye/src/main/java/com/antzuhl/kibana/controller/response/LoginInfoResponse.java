package com.antzuhl.kibana.controller.response;

import lombok.Data;

/**
 * @author AntzUhl
 * @Date 17:02
 */
@Data
public class LoginInfoResponse {
    /**
     "log_time" : "2020-09-06 08:53:06.170",
     "content" : "appId[172] mobile[18241417678] usage[5] user_mobile_verify_error_three",
     appId: 端类型
     手机号
     失败次数
     "koo_env" : "prod",
     "ip" : "172.18.196.3"
     * */

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
