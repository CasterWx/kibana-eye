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
