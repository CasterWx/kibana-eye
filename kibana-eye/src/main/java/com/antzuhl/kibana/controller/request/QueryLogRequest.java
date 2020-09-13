package com.antzuhl.kibana.controller.request;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
