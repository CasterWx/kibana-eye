package com.antzuhl.kibana.domain;

import lombok.*;

import javax.persistence.*;

/**
 * @author AntzUhl
 * Blog    http://antzuhl.cn
 * Github  https://github.com/CasterWx
 * Date 2020/9/8 16:10
 */
@Data
@Entity
@Table(name = "login_info")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo{
    /**
     "log_time" : "2020-09-06 08:53:06.170",
     "content" : "appId[172] mobile[xxx] usage[5] user_mobile_verify_error_three",
        appId: 端类型
        手机号
        失败次数
     "koo_env" : "prod",
     "ip" : "172.18xxx"
     * */
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    @Id
    @Column(name = "trace_id")
    private Long traceId;

    /**
     * Log time.
     */
    @Column(name = "log_time")
    private String logTime;

    /**
     * Log content.
     */
    @Column(name = "mobile")
    private String mobile;


    /**
     * 客户端类型
     */
    @Column(name = "client_type")
    private String clientType;

    /**
     * 失败次数
     */
    @Column(name = "error_num")
    private Integer errorNum;


    /**
     * env
     */
    @Column(name = "env")
    private String env;

    /**
     * env
     */
    @Column(name = "ip")
    private String ip;

}
