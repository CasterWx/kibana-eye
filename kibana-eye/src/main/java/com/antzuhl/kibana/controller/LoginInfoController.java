package com.antzuhl.kibana.controller;

import com.antzuhl.kibana.common.Response;
import com.antzuhl.kibana.dao.LoginInfoRepository;
import com.antzuhl.kibana.domain.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author AntzUhl
 * @Date 17:02
 */
@RestController
@RequestMapping("/login")
public class LoginInfoController {

    @Autowired
    LoginInfoRepository loginInfoRepository;

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public Response all() {
//        List<LoginInfoResponse> result = new ArrayList<>();
        List<LoginInfo> infoList = loginInfoRepository.findAll();
        Collections.reverse(infoList);
        //        infoList.stream().forEach(item -> {
//            LoginInfoResponse infoResponse = new LoginInfoResponse();
//            infoResponse.setClientType(item.getClientType());
//            infoResponse.setMobile(item.getMobile());
//            infoResponse.setErrorNum(item.getErrorNum());
//            infoResponse.setLogTime(item.getLogTime());
//            result.add(infoResponse);
//        });
        return Response.success("SUCCESS", infoList);
    }
}
