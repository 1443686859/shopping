package com.huang.controller;

import com.huang.service.Model.UserModel;
import com.huang.service.UserService;
import com.huang.view.ViewObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("user")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/get")

    @ResponseBody
    public ViewObject getUser(@RequestParam("id")Integer id){
        UserModel userModel=userService.getUserById(id);
        ViewObject viewObject= new ViewObject();
        BeanUtils.copyProperties(userModel,viewObject);
//        return userService.getUserById(id);
   return viewObject;

    }
}
