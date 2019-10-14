package com.wetin.anwserproject.net.help;

public class Response<T> {

    public T result;
    String resp_message;
    int resp_code;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getResp_message() {
        return resp_message;
    }

    public void setResp_message(String resp_message) {
        this.resp_message = resp_message;
    }

    public int getResp_code() {
        return resp_code;
    }

    public void setResp_code(int resp_code) {
        this.resp_code = resp_code;
    }
}