package com.huang.controller;

import com.huang.error.BusinessException;
import com.huang.error.EmBusinessError;
import com.huang.response.CommonReturnType;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

public class BaseController {
    //定义Exceptionhandle解决未被controller层吸收的exception,及抛出的错误
    @ExceptionHandler(Exception.class)
    //即便controller抛出异常，我们捕获它然后正常返回200的值
    @ResponseStatus(HttpStatus.OK)
    //接收HttpServletRequest和异常Exception,只能返回一个页面路径，若想返回对象，则需要加上下面的注解
    @ResponseBody
    public Object handleException(HttpServletRequest request, Exception ex){

        Map<String, Object> responseData = new HashMap<>();
        //如果ex返回的是BusinessException，则把Exception强转为BusinessException
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;

            //使用Map存放errCode和errMsg
            responseData.put("errCode", businessException.getErrorCode());
            responseData.put("errMsg", businessException.getErrorMsg());
        }else {
            //如果ex返回的是不BusinessException,则直接输出未知错误，我们需要去枚举里面再定义一个未知错误然后返回
            //使用Map存放errCode和errMsg
            responseData.put("errCode", EmBusinessError.UNKNOW_ERROR.getErrorCode());
            responseData.put("errMsg",EmBusinessError.UNKNOW_ERROR.getErrorMsg());
        }
        //使用构造函数返回data和status
        return CommonReturnType.create("fail",responseData);

    }

}
