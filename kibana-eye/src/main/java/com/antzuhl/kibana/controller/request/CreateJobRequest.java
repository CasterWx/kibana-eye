package com.antzuhl.kibana.controller.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author AntzUhl
 * @Date 19:04
 */
@Data
public class CreateJobRequest {

    private Long id;

    private String indexName;

    private String application;

    private String region; // 查询类型

    private String content;

    private String sendTo;
    private String sendCc;

    private Date executeTime;

    private String contentShort; // 描述

    private Boolean cycle;

    private List<String> notType;

    private String status;
}
