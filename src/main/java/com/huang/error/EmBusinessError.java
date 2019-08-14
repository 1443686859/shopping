package com.huang.error;

public enum EmBusinessError implements  CommonError  {
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    USER_NOT_EXIT(20001,"用户不存在"),
    UNKNOW_ERROR(10002,"未知错误");
    ;
    private int errorCode;
    private String errMsg;
    private EmBusinessError(int errorCode,String  errMsg){
        this.errMsg=errMsg;
        this.errorCode=errorCode;
    }
    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errMsg;
    }

    public CommonError setErrorMsg(String msg) {
        this.errMsg=errMsg;
        return this;
    }
}
