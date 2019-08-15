package com.huang.service.Impl;

import com.huang.dao.UserDOMapper;
import com.huang.dao.UserPasswordDOMapper;
import com.huang.dataobject.UserDO;
import com.huang.dataobject.UserPasswordDO;
import com.huang.error.BusinessException;
import com.huang.error.EmBusinessError;
import com.huang.service.Model.UserModel;
import com.huang.service.UserService;
import com.huang.utils.ValidationResult;
import com.huang.utils.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.validation.Validator;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;
    public UserModel getUserById(Integer id) {
        UserModel userModel=new UserModel();
        UserDO userDO=userDOMapper.selectByPrimaryKey(id);
        if(userDO==null) return null;
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(userDO.getId());
        BeanUtils.copyProperties(userDO,userModel);
        if(userPasswordDO!=null)
        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        return userModel;
    }

    @Override
    public void register(UserModel userModel) throws BusinessException {
    if(userModel==null)throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//
        ValidationResult validationResult=validator.validator(userModel);
        if(validationResult.isHasError()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErroMsg());

        }
       UserDO userDO=new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        userDOMapper.insertSelective(userDO);
        UserPasswordDO userPasswordDO=new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    public UserModel validateLogin(String telephone, String password) throws BusinessException {
        UserModel userModel=new UserModel();
        UserDO userDO=userDOMapper.selectByTelephone(telephone);
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(userDO.getId());
        BeanUtils.copyProperties(userModel,userDO);
        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        if(!password.equals(userModel.getEncrptPassword()))
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        return userModel;
    }
}
