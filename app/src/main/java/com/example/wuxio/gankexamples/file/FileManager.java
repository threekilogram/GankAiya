package com.example.wuxio.gankexamples.file;

import android.os.Environment;

import com.example.wuxio.gankexamples.App;

import java.io.File;

/**
 * @author wuxio 2018-05-05:19:13
 */
public class FileManager {

    //============================ singleTon ============================


    private FileManager() {

        initAppFile();
    }


    public static FileManager getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final FileManager INSTANCE = new FileManager();
    }

    //============================ app file init ============================

    private File appFile;


    private void initAppFile() {

        App app = App.INSTANCE;

        String storageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(storageState)) {
            appFile = app.getExternalFilesDir(null);
        } else {
            appFile = app.getFilesDir();
        }
    }


    public File getAppFile() {

        return appFile;
    }
}
