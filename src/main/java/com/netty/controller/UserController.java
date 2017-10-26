package com.netty.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.netty.entity.User;
import com.netty.service.UserService;
import com.netty.utils.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ren on 2017/10/16.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj login(User user){
        ReturnObj returnObj = new ReturnObj();
        User user1 = userService.selectUser(user);
        if(user1 != null){
            returnObj.setResult(user1.getId().toString());
            returnObj.setCode(200);
            returnObj.setMsg("登陆成功");
        }else {
            returnObj.setResult("error");
            returnObj.setCode(500);
            returnObj.setMsg("登陆失败");
        }
        return returnObj;
    }

    @RequestMapping(value = "chat/{userId}",method = RequestMethod.GET)
    public String toChat(HttpServletRequest request, @PathVariable String userId){
        User user = userService.selectUserById(Integer.parseInt(userId));
        request.setAttribute("user",user);
        return "chat";
    }

}
