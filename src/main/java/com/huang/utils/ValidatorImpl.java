package com.huang.utils;

import com.huang.service.Model.UserModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {

    }
    private Validator validator;
    public ValidationResult validator(Object bean){
        ValidationResult validationResult=new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet=validator.validate(bean);
        if(constraintViolationSet.size()>0)
        {
            validationResult.setHasError(true);
            constraintViolationSet.forEach(constraintViolation->{
                String errorMsg=constraintViolation.getMessage();
                String propertyName=constraintViolation.getPropertyPath().toString();
                validationResult.getErrorMsgMap().put(propertyName,errorMsg);
            });
                
            }
            return validationResult;
        }

    }

