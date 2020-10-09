package com.antzuhl.kibana.controller;

import com.antzuhl.kibana.common.Response;
import com.antzuhl.kibana.dao.QueryTemplateRepository;
import com.antzuhl.kibana.domain.QueryTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模板
 * @author AntzUhl
 * @Date 14:53
 */
@RestController
@RequestMapping("/template")
public class TemplateController {


    @Autowired
    private QueryTemplateRepository queryTemplateRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response all() {
        List<QueryTemplate> queryTemplates = queryTemplateRepository.findAll();
        return Response.success("success", queryTemplates);
    }

    @RequestMapping(value = "/type", method = RequestMethod.GET)
    public Response get(@RequestParam(name = "type") String type) {
        if (type == null) {
            return Response.error("参数错误");
        }
        QueryTemplate queryTemplate = queryTemplateRepository.findQueryTemplateByType(type);
        if (queryTemplate == null) {
            return Response.error("没有这个类型");
        }
        return Response.success("success", queryTemplate);
    }

    /** 添加新的template */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Response upload(@RequestParam("name") String name,
                           @RequestParam("type") String type,
                           @RequestParam("query") String query) {
        if (name == null || type == null || query == null) {
            return Response.success("参数错误");
        }
        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setName(name);
        queryTemplate.setType(type);
        queryTemplate.setQuery(query);
        QueryTemplate save = queryTemplateRepository.save(queryTemplate);
        if (save == null) {
            return Response.error("添加失败");
        }
        return Response.success("success");
    }

}
