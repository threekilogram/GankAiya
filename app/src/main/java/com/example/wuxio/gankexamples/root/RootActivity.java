package com.example.wuxio.gankexamples.root;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wuxio.gankexamples.app.App;
import com.example.wuxio.gankexamples.splash.SplashActivity;
import com.example.wuxio.gankexamples.utils.netstate.NetworkChangedReceiver;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        NetworkChangedReceiver.register(App.INSTANCE);
        SplashActivity.start(RootActivity.this);
    }


    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        finish();
    }


    @Override
    public void finish() {

        NetworkChangedReceiver.unRegister(App.INSTANCE);
        super.finish();
    }
}
