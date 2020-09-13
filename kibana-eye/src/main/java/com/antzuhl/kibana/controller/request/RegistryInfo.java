package com.antzuhl.kibana.controller.request;

import com.antzuhl.kibana.domain.SysUser;
import lombok.Data;

import java.util.Calendar;

@Data
public class RegistryInfo {

    private String username;

    private String nickname;

    private String password;

    private String email;

    /** 并不是必须 */
    private String mobile;

    public SysUser toData() {
        return SysUser.builder()
                .username(username)
                .nickname(nickname)
                .avatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif")
                .password(password)
                .email(email)
                .mobile(mobile)
                .salt("")
                .createTime(Calendar.getInstance().getTime())
                .status(0)
                .build();
    }

    public boolean valid() {
        if (username == null || nickname == null || password == null || email == null) {
            return false;
        }
//        if (mobile != null && ValidatorUtil.isMobile(mobile)) {
//            return true;
//        }
        // 校验长度
        if (username.length() < 8 || password.length() < 8) {
            return false;
        }
        // TODO 校验email
        return true;
    }
}
