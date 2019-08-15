package com.huang.service;

import com.huang.error.BusinessException;
import com.huang.service.Model.UserModel;


public interface UserService {
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;
    UserModel validateLogin(String telephone,String password) throws BusinessException;
}
