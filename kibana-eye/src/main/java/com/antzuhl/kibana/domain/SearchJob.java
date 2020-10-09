package com.antzuhl.kibana.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author AntzUhl
 * @Date 10:59
 */
@Data
@Entity
@Table(name = "search_job")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearchJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 索引名称
    @Column(name = "index_name")
    private String indexName;

    // 应用名称
    @Column(name = "application")
    private String application;

    // 查询类型
    // 应用名称
    @Column(name = "type")
    private String type;

    @Column(name = "summary")
    private String summary;

    //
    @Column(name = "query", columnDefinition = "text")
    private String query;

    // 发送到
    @Column(name = "send_to")
    private String sendTo;

    // 抄送
    @Column(name = "send_cc")
    private String sendCc;

    @Column(name = "execute_time")
    private Date executeTime;

    @Column(name = "cycle")
    private Boolean cycle; // 每天循环执行

    @Column(name = "notice")
    private int notice;  // 通知类型  1: 邮件  2: 钉钉  3: 权兜耀

    // 是否禁用
    @Column(name = "deleted")
    private Integer deleted;
}
