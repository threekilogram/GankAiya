package com.example.wuxio.gankexamples.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.example.wuxio.gankexamples.App;
import com.example.wuxio.gankexamples.constant.ConstantsImageUrl;
import com.example.wuxio.gankexamples.excutors.ThreadPools;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wuxio 2018-04-15:9:52
 */
public class GlideUtils {

    /**
     * load a img with in a {@code timeOut} time,if timeout do nothing
     *
     * @param timeOut   timeout in second
     * @param imageView img to show in this view
     */
    public static void loadSplashImg(final int timeOut, final ImageView imageView) {

        int width = imageView.getMeasuredWidth();
        int height = imageView.getMeasuredHeight();

        int i = new Random().nextInt(ConstantsImageUrl.TRANSITION_URLS.length);

        final FutureTarget< Bitmap > futureTarget = Glide.with(App.INSTANCE).load(ConstantsImageUrl
                .TRANSITION_URLS[i])
                .asBitmap().into(width, height);

        /*futureTarget.get(timeOut, TimeUnit.SECONDS) 阻塞线程,后台执行 */

        ThreadPools.run(() -> {

            /* 持有软引用,当Activity销毁时不会泄漏 */

            final WeakReference< ImageView > reference = new WeakReference<>(imageView);

            try {
                final Bitmap bitmap = futureTarget.get(timeOut, TimeUnit.SECONDS);
                final ImageView view = reference.get();
                if (view != null) {
                    ThreadPools.runOnUIThread(() -> view.setImageBitmap(bitmap));
                }

            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        });
    }
}
