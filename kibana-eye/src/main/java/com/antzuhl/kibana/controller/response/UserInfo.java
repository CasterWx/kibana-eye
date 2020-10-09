package com.antzuhl.kibana.controller.response;

import lombok.Data;

import java.util.List;

/**
 * @author AntzUhl
 * @Date 16:38
 */
@Data
public class UserInfo {

    private String name;

    private String avatar;

    private String introduction;

    private List<String> roles;

}
