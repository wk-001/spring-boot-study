package com.wk.util;

import java.util.List;

//封装layui.table需要的数据格式
public class DataGrid {

    private int code;

    private String msg;

    private int count;

    private List<?> data;

    public static final int SUCCESS = 0;
    public static final int FAILED = 1;
    public static final String NO_MSG = "";

    public static DataGrid successWithData(List<?> data){
        return new DataGrid(SUCCESS,NO_MSG,data.size(),data);
    }

    public static DataGrid failed(String massage){
        return new DataGrid(FAILED,massage,0,null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public DataGrid() {
    }

    public DataGrid(int code, String msg, int count, List<?> data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }
}
