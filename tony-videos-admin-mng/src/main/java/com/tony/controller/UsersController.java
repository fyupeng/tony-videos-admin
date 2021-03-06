package com.tony.controller;

import com.tony.bean.AdminUser;
import com.tony.pojo.UsersInfo;
import com.tony.service.UserService;
import com.tony.utils.IMoocJSONResult;
import com.tony.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.SkipPageException;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.UUID;

@Controller
@RequestMapping("users")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping("/showList")
    public String showList(){
        return "users/usersList";
    }

    @PostMapping("/list")
    @ResponseBody
    public PagedResult list(UsersInfo user, Integer page){
        PagedResult result = userService.queryUsers(user, page == null ? 1 : page, 10);
        return result;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("login")
    @ResponseBody
    public IMoocJSONResult userLogin(String username, String password,
                                     HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // TODO 模拟登陆
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return IMoocJSONResult.errorMap("用户名和密码不能为空");
        } else if (username.equals("lee") && password.equals("lee")) {

            String token = UUID.randomUUID().toString();
            AdminUser user = new AdminUser(username, password, token);
            request.getSession().setAttribute("sessionUser", user);
            request.getSession().setMaxInactiveInterval(60);

            return IMoocJSONResult.ok();
        }

        return IMoocJSONResult.errorMsg("登陆失败，请重试...");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("sessionUser");
        return "login";
    }
}
