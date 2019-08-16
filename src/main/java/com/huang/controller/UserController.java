package com.huang.controller;

import com.alibaba.druid.util.StringUtils;
import com.huang.error.BusinessException;
import com.huang.error.EmBusinessError;
import com.huang.response.CommonReturnType;
import com.huang.service.Model.UserModel;
import com.huang.service.UserService;
import com.huang.view.ViewObject;
import com.sun.xml.internal.xsom.impl.Ref;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
public class UserController extends BaseController {

    private String EncodeByMd5(String password) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = password.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/getotp", method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam("telephone") String telephone) {
        Random random = new Random();
        int randomInt = random.nextInt(9999);
        randomInt += 10000;
        String optCode = String.valueOf(randomInt);
        httpServletRequest.getSession().setAttribute("optCode", optCode);
        httpServletRequest.getSession().setAttribute("phone",telephone);
        System.out.println(telephone + " " + optCode);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam("id") Integer id) throws BusinessException {
        UserModel userModel = userService.getUserById(id);
        if (userModel == null) {
//            userModel.setEncrptPassword("888888");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIT);
        }
        ViewObject viewObject = new ViewObject();
        BeanUtils.copyProperties(userModel, viewObject);
//        return userService.getUserById(id);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password
    ) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和对应的otpCode相符
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        //使用阿里巴巴的等值判别，好处是已经做了非空判断
        if (!StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合要求！");
        }
        //若手机号与验证码相对应,则开始注册，需要一个service去执行注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        //转换gender的字符串类型
        userModel.setGender(String.valueOf(gender.intValue()));
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        //对密码做加密处理

        userModel.setEncrptPassword(this.EncodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);

    }


    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public CommonReturnType login(@RequestParam("telephone") String telephone, @RequestParam("password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        {

            if (StringUtils.isEmpty(telephone) || StringUtils.isEmpty(password))
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
            UserModel userModel = userService.validateLogin(telephone, this.EncodeByMd5(password));
            this.httpServletRequest.getSession().setAttribute("loginedUser", userModel);
            this.httpServletRequest.getSession().setAttribute("loginStatus", true);
            return CommonReturnType.create(null);
        }


    }
}
