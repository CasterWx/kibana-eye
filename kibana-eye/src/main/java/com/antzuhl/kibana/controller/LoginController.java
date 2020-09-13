package com.antzuhl.kibana.controller;

import com.antzuhl.kibana.common.Constants;
import com.antzuhl.kibana.common.Response;
import com.antzuhl.kibana.controller.request.RegistryInfo;
import com.antzuhl.kibana.controller.request.User;
import com.antzuhl.kibana.controller.response.UserInfo;
import com.antzuhl.kibana.dao.SysUserRepository;
import com.antzuhl.kibana.domain.SysUser;
import com.antzuhl.kibana.utils.CookieUtil;
import com.antzuhl.kibana.utils.SecretUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author AntzUhl
 * @Date 16:25
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private SysUserRepository sysUserRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response login(HttpServletResponse response,
                          @RequestBody User user) {
        if (user == null) {
            return Response.error("参数错误");
        }
        String username = user.getUsername();
        SysUser sysUser = sysUserRepository.findSysUserByUsername(username);
        if (sysUser == null) {
            return Response.error("没有该用户");
        }
        String dbPwd = sysUser.getPassword();
        String mdPwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        log.info("LoginControllerV1_0 userId: {}, userPassword: {}, dbPwd: {}, mdPwd: {}",
                sysUser.getId(), user.getPassword(), dbPwd, mdPwd);
        if (!StringUtils.equals(mdPwd, dbPwd)) {
            return Response.error("输入的密码错误");
        }
        // SUCCESS
        String token = SecretUtil.create(sysUser.getId(), sysUser.getUsername());
        CookieUtil.set(response, Constants.AUTH_TOKEN, token, true);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return Response.success("success", map);
    }

    @GetMapping("/info")
    public Response info(@RequestParam("token") String token) {
        Long userId = SecretUtil.verify(token);
        if (userId == null) {
            return Response.error("Token错误");
        }
        Optional<SysUser> optional = sysUserRepository.findById(userId);
        if (optional.isPresent()) {
            SysUser sysUser = optional.get();
            UserInfo userInfo = new UserInfo();
            userInfo.setName(sysUser.getUsername());
            userInfo.setAvatar(sysUser.getAvatar()); //"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"
            userInfo.setIntroduction(sysUser.getNickname());
            List<String> roles = new ArrayList<>();
            roles.add("admin");
            userInfo.setRoles(roles);
            return Response.success("success", userInfo);
        }
        return Response.error("Token用户校验错误");
    }

    @PostMapping("/registry")
    public Response registry(RegistryInfo registryInfo) {
        // 校验
        SysUser checkUser = sysUserRepository.findSysUserByUsername(registryInfo.getUsername());
        if (checkUser != null) {
            return Response.error("用户名已存在");
        }
        SysUser sysUser = registryInfo.toData();
        // 密码加密
        String pwd = DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes());
        sysUser.setPassword(pwd);
        SysUser registryBody = sysUserRepository.save(sysUser);
        registryBody.setPassword(null);
        registryBody.setSalt(null);
        log.info("SysUserService registry body: {}", registryBody);
        return Response.success("success", registryBody);
    }


    @PostMapping("/logout")
    public Response logout() {
        return Response.success("success");
    }
}
