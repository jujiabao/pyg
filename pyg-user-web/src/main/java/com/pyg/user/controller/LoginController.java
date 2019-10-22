package com.pyg.user.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title LoginController
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-22 22:49
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/name")
    public Map showName(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = new HashMap();
        map.put("loginName", name);
        return map;
    }
}
