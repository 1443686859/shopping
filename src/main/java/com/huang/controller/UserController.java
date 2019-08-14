package com.huang.controller;

import com.huang.error.BusinessException;
import com.huang.error.EmBusinessError;
import com.huang.response.CommonReturnType;
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
public class UserController extends BaseController{
    @Autowired
    private UserService userService;
    @RequestMapping("/get")

    @ResponseBody
    public CommonReturnType getUser(@RequestParam("id")Integer id)throws  BusinessException{
        UserModel userModel=userService.getUserById(id);
        if(userModel==null){
//            userModel.setEncrptPassword("888888");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIT);
        }
        ViewObject viewObject= new ViewObject();
        BeanUtils.copyProperties(userModel,viewObject);
//        return userService.getUserById(id);
   return CommonReturnType.create(viewObject);

    }
}
