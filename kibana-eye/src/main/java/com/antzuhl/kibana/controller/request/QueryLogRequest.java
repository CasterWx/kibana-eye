package com.antzuhl.kibana.controller.request;

import lombok.Data;

/**
 * @author AntzUhl
 * @Date 14:04
 */
@Data
public class QueryLogRequest {
    private Integer page;
    private Integer limit;
    private String type;
    private String sort;
}
