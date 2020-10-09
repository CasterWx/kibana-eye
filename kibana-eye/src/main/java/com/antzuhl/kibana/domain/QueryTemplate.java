package com.antzuhl.kibana.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author AntzUhl
 * @Date 14:51
 */
@Data
@Entity
@Table(name = "query_template")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QueryTemplate {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 索引名称
    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    // 模板内容
    @Column(name = "query", columnDefinition = "text")
    private String query;

}
