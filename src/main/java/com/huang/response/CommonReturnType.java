package com.huang.response;

/**
 * 处理接口返回的数据，使用stats返回状态，data返回数据
 */
public class CommonReturnType {
    private String status;
    private Object data;

    public static CommonReturnType create(String stats,Object data){
        CommonReturnType commonReturnType=new CommonReturnType();
        commonReturnType.setData(data);
        commonReturnType.setStats(stats);
        return commonReturnType;
    }
    public static CommonReturnType create(Object data){
        return create("success",data);
    }
    public String getStatus() {
        return status;
    }

    public void setStats(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
