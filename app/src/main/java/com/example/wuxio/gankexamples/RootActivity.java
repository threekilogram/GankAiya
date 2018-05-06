package com.example.wuxio.gankexamples;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.wuxio.gankexamples.main.MainActivity;
import com.example.wuxio.gankexamples.utils.netState.NetworkChangedReceiver;

/**
 * 作为根activity,使用singleTask模式,用来清除任务栈
 *
 * @author wuxio
 */
public class RootActivity extends AppCompatActivity {

    private static final String TAG = "RootActivity";


    public static void start(Context context) {

        Intent starter = new Intent(context, RootActivity.class);
        context.startActivity(starter);
    }


    private RootHandler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mHandler = new RootHandler();
        postAction();
    }


    private void postAction() {

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                MainActivity.start(RootActivity.this);

                NetworkChangedReceiver.register(RootActivity.this);
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        finish();
    }


    @Override
    protected void onDestroy() {

        NetworkChangedReceiver.unRegister(this);
        super.onDestroy();
    }

    //============================ 内部类 ============================

    private static class RootHandler extends Handler {


    }

}
