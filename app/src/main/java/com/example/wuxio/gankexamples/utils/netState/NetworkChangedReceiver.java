package com.example.wuxio.gankexamples.utils.netState;

/**
 * @author wuxio 2018-05-05:22:34
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * @author wangyuhang@evergrande.cn
 * @date 2017/7/12 0012
 *
 * 负责监听网络状态的变化
 * Android API为M及以上时，需要动态注册监听
 */
public class NetworkChangedReceiver extends BroadcastReceiver {


    private final String TAG = "NetworkChangedReceiver";

    //============================ singleTon ============================


    private NetworkChangedReceiver() {

    }


    public static NetworkChangedReceiver getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final NetworkChangedReceiver INSTANCE = new NetworkChangedReceiver();
    }

    //============================ state ============================

    public static final int NETWORK_STATE_NON  = 0;
    public static final int NETWORK_STATE_WIFI = 1;
    public static final int NETWORK_STATE_DATA = 2;
    public static final int NETWORK_STATE_ALL  = 3;
    public static int currentState;


    public static int getNetWorkState() {

        return currentState;
    }

    //============================ register/un ============================


    public void register(Context context) {

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(this, filter);
    }


    public void unRegister(Context context) {

        context.unregisterReceiver(this);
    }

    //============================ receive ============================


    @Override
    public void onReceive(Context context, Intent intent) {

        /*网络状态发生变化*/

        testNetState(context);
    }


    public static void testNetState(Context context) {

        /*检测API是不是小于21，因为到了API 21之后getNetworkInfo(int networkType)方法被弃用*/

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            ConnectivityManager connMgr =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            /*获取ConnectivityManager对象对应的NetworkInfo对象*/

            /*获取WIFI连接的信息*/
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            /*获取移动数据连接的信息*/
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {

                /* WIFI已连接,移动数据已连接 */
                currentState = NETWORK_STATE_ALL;

            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {

                /* WIFI已连接,移动数据已断开 */
                currentState = NETWORK_STATE_WIFI;

            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {

                /* WIFI已断开,移动数据已连接 */
                currentState = NETWORK_STATE_DATA;

            } else {

                /* WIFI已断开,移动数据已断开 */
                currentState = NETWORK_STATE_NON;
            }

        } else {

            ConnectivityManager connMgr =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            /* 获取所有当前已有连接上状态的网络连接的信息 */
            assert connMgr != null;
            Network[] networks = connMgr.getAllNetworks();

            /* 用于记录最后的网络连接信息 mobile false = 1, mobile true = 2, wifi = 4 */
            int result = 0;

            for (int i = 0; i < networks.length; i++) {

                /* 获取ConnectivityManager对象对应的NetworkInfo对象 */
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);

                /* 检测到有数据连接，但是并连接状态未生效，此种状态为wifi和数据同时已连接，以wifi连接优先 */
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && !networkInfo.isConnected()) {
                    result += 1;
                }

                /* 检测到有数据连接，并连接状态已生效，此种状态为只有数据连接，wifi并未连接上 */
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected()) {
                    result += 2;
                }

                /* 检测到有wifi连接，连接状态必为true */
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    result += 4;
                }
            }

            /* 因为存在上述情况的组合情况，以组合相加的唯一值作为最终状态的判断 */
            switch (result) {
                case 0:
                    /* WIFI已断开,移动数据已断开 */
                    currentState = NETWORK_STATE_NON;
                    break;
                case 2:
                    /* WIFI已断开,移动数据已连接 */
                    currentState = NETWORK_STATE_DATA;
                    break;
                case 4:
                    /* WIFI已连接,移动数据已断开 */
                    currentState = NETWORK_STATE_WIFI;
                    break;
                case 5:
                    /* WIFI已连接,移动数据已连接 */
                    currentState = NETWORK_STATE_ALL;
                    break;
                default:
                    break;
            }
        }
    }
}
