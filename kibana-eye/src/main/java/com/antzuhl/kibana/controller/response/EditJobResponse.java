package com.antzuhl.kibana.controller.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author AntzUhl
 * @Date 23:34
 */
@Data
public class EditJobResponse {

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
}
