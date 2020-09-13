package com.antzuhl.kibana.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author AntzUhl
 * @Date 17:39
 */
@Data
@Entity
@Table(name = "index_detail")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IndexDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // 条目
    @Column(name = "total")
    private Integer total;

    // 天，只有当前天才会在定时任务中处理
    @Column(name = "day")
    private Integer day;
}
