package com.antzuhl.kibana.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @author AntzUhl
 * @Date 14:57
 */
@Data
@Entity
@Table(name = "query_log")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QueryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 索引名称
    @Column(name = "name")
    private String name;

    // 应用名称
    @Column(name = "application")
    private String application;

    // 查询类型
    @Column(name = "type")
    private String type;

    // 查询状态
    @Column(name = "status")
    private Integer status;

    // 查询返回
    @Column(name = "query", columnDefinition = "text")
    private String query;

    @Column(name = "create_time")
    private Date createTime;
}
