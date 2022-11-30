package com.okestro.symphony.dashboard.cmm.websocket.ctr;

import com.okestro.symphony.dashboard.cmm.model.LoginVo;
import com.okestro.symphony.dashboard.cmm.websocket.svc.impl.WebsocketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
//@RequestMapping("/admin/")
public class TestController {

    @Autowired
    WebsocketServiceImpl websocketServiceImpl;

    @CrossOrigin("*")
//    @GetMapping("/test")
    private String retrieveUserInfo(HttpServletRequest request) {

        LoginVo loginVo = (LoginVo) request.getSession().getAttribute("loginVo");
//        websocketServiceImpl.sendMessage(loginVo.getUserId());

        return "success";

    }
}