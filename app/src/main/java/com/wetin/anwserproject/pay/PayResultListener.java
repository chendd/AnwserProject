package com.wetin.anwserproject.pay;

public interface PayResultListener {
    public void onPaySuccess();

    public void onPayError();

    public void onPayCancel();
}
