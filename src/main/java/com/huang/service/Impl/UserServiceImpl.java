package com.huang.service.Impl;

import com.huang.dao.UserDOMapper;
import com.huang.dao.UserPasswordDOMapper;
import com.huang.dataobject.UserDO;
import com.huang.dataobject.UserPasswordDO;
import com.huang.service.Model.UserModel;
import com.huang.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    public UserModel getUserById(Integer id) {
        UserModel userModel=new UserModel();
        UserDO userDO=userDOMapper.selectByPrimaryKey(id);
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(userDO.getId());
        BeanUtils.copyProperties(userDO,userModel);
        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        return userModel;
    }
}
