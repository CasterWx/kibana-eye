package com.antzuhl.kibana.controller;

import com.antzuhl.kibana.common.Response;
import com.antzuhl.kibana.dao.IndexDetailRepository;
import com.antzuhl.kibana.domain.IndexDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author AntzUhl
 * @Date 18:12
 */
@RestController
@RequestMapping("/index")
public class IndexDetailController {

    @Autowired
    IndexDetailRepository indexDetailRepository;

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public Response all() {
        return Response.success("success", indexDetailRepository.findAll());
    }


    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Response put(@RequestParam(name = "indexName") String indexName,
                        @RequestParam(name = "day") Integer day) {
        if (indexName == null || day == null) {
            return Response.error("参数错误");
        }
        IndexDetail res =  indexDetailRepository.findIndexDetailByName(indexName);
        if (res != null) {
            return Response.error("重复的提交");
        }
        IndexDetail indexDetail = new IndexDetail();
        indexDetail.setTotal(0);
        indexDetail.setDay(day);
        indexDetail.setName(indexName);

        indexDetailRepository.save(indexDetail);
        return Response.success("SUCCESS", indexDetail);
    }
}
