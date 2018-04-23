package com.example.wuxio.gankexamples;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wuxio.gankexamples.utils.SystemUI;

/**
 * @author wuxio
 */
public class MainActivity extends AppCompatActivity {

    private boolean setFlag;


    public static void start(Context context) {

        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SystemUI.immersiveSticky(this);
    }
}