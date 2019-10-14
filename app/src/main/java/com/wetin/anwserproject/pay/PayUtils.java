package com.wetin.anwserproject.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.LogUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wetin.anwserproject.bean.WeChatPayInfo;
import com.wetin.anwserproject.utils.Config;

import java.util.Map;

/**
 * 支付宝微信支付工具类
 *  1.在application 关联withApi
 *  2.使用需要在相应的Activity实现PayResultListener
 *  3.PayListenerUtils添加监听PayResultListener
 *  4.使用PayUtils调用相应的支付
 *  5.在onDestory移除监听
 */
public class PayUtils {
    /**
     * 支付宝返回参数
     */
    final static int SDK_PAY_FLAG = 1001;

    private static PayUtils instance;
    private static IWXAPI iwxapi;
    private static Context context;

    private PayUtils(Context context) {
        this.context = context;
    }

    /**
     * 注册APP到微信平台(application 初始化)
     * @param context
     */
    public static void registerWx(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, Config.INSTANCE.getWECHATID(),true);
        iwxapi.registerApp( Config.INSTANCE.getWECHATID());
    }

    public static PayUtils getInstance(Context context) {
        if (instance == null) {
            instance = new PayUtils(context);
        }
        return instance;
    }



    /**
     * 微信支付
     * @param res
     */
    public void toWXPay(WeChatPayInfo res) {
                PayReq request = new PayReq();
                request.appId = Config.INSTANCE.getWECHATID();
                request.partnerId = "1537854211";
                request.prepayId = res.getPrepayid();
                request.packageValue ="Sign=WXPay";
                request.nonceStr = res.getNoncestr();
                request.timeStamp =res.getTimestamp();
                request.sign = res.getSign();
                iwxapi.sendReq(request);
    }


    /**
     * 支付宝
     */
    public  void toAliPay(final Activity mActivity, final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝状态
     * 9000 订单支付成功
     * 8000 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     * 4000 订单支付失败
     * 5000 重复请求
     * 6001 用户中途取消
     * 6002 网络连接出错
     * 6004 支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     * 其它   其它支付错误
     */
    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        PayListenerUtils.getInstance(context).addSuccess();
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        PayListenerUtils.getInstance(context).addCancel();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        PayListenerUtils.getInstance(context).addError();
                    }
                    break;
                }
            }
        }
    };

}
