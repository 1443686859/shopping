package com.huang.error;

public class BusinessException extends  Exception implements  CommonError {
    private CommonError commonError;

    public BusinessException(CommonError commonError){
        super();
        this.commonError=commonError;
    }
    public BusinessException(CommonError commonError,String msg){
        super();
        this.commonError=commonError;
        commonError.setErrorMsg(msg);
    }
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    public String getErrorMsg() {
        return this.commonError.getErrorMsg();
    }

    public CommonError setErrorMsg(String msg) {
        this.commonError.setErrorMsg(msg);
        return this;
    }
}
