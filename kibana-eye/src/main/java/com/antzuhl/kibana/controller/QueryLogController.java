package com.antzuhl.kibana.controller;

import com.antzuhl.kibana.common.Response;
import com.antzuhl.kibana.dao.QueryLogRepository;
import com.antzuhl.kibana.domain.QueryLog;
import com.antzuhl.kibana.parser.RPCQueryParser;
import com.antzuhl.kibana.parser.SQLQueryParser;
import com.antzuhl.kibana.parser.WebappTimeParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author AntzUhl
 * @Date 15:12
 */
@RestController
@Slf4j
@RequestMapping("/querylog")
public class QueryLogController {

    @Autowired
    private QueryLogRepository queryLogRepository;

    @GetMapping(value = "/query")
    public Response query(@RequestParam("type") String type) {
        if (StringUtils.isEmpty(type)) {
            return Response.error("查询参数错误");
        }
        List<QueryLog> result = queryLogRepository.findQueryLogsByType(type);
        Map<String, Object> map = new HashMap<>();
        if (CollectionUtils.isEmpty(result)) {
            map.put("total", 0);
        } else {
            map.put("total", result.size());
            map.put("item", result);
        }
        return Response.success("success", map);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Response all() {
        List<QueryLog> logs = queryLogRepository.findAll();
        int total = logs.size();
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("item", logs);
        return Response.success("sucess", map);
    }

    @GetMapping(value = "/report")
    public Response report(@RequestParam(value = "id", required = false) Long id) {
        if (id == null) return Response.error("查询参数错误");
        Optional<QueryLog> optional = queryLogRepository.findById(id);
        if (optional.isPresent()) {
            QueryLog queryLog = optional.get();
            String resultHtml = "";
            if (StringUtils.equals(queryLog.getType(), "sql")) {
                resultHtml = SQLQueryParser.parser(queryLog.getApplication(), queryLog.getQuery());
            } else if (StringUtils.equals(queryLog.getType(), "rpc")) {
                resultHtml = RPCQueryParser.parser(queryLog.getApplication(), queryLog.getQuery());
            } else if (StringUtils.equals(queryLog.getType(), "webapp_time")) {
                resultHtml = WebappTimeParser.parser(queryLog.getApplication(), queryLog.getQuery());
            }
            return Response.success("success", resultHtml);
        } else {
            return Response.error("没有该记录");
        }
    }
}
