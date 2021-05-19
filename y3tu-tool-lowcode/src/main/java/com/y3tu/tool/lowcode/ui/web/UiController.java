package com.y3tu.tool.lowcode.ui.web;

import com.y3tu.tool.core.exception.ServerException;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.lowcode.ui.configure.UiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * @author y3tu
 */
@RestController
@RequestMapping("y3tu-tool-lowcode/ui")
@Slf4j
public class UiController {

    @Autowired
    UiProperties uiProperties;

    @PostMapping("/login")
    public R login(@RequestBody Map params) {
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        String usernameP = uiProperties.getUsername();
        String passwordP = uiProperties.getPassword();
        if (usernameP.equals(username) && passwordP.equals(password)) {
            //登录成功！
            return R.success(UUID.randomUUID());
        }else {
            throw new ServerException("用户名或者密码错误！");
        }
    }
}
