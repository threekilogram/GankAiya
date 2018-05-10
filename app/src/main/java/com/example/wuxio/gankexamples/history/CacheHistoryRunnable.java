package com.example.wuxio.gankexamples.history;

import com.example.wuxio.gankexamples.model.HistoryBean;
import com.example.wuxio.gankexamples.net.NetWork;

import java.io.IOException;
import java.io.Reader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author wuxio 2018-05-10:7:45
 */
public class CacheHistoryRunnable implements Runnable {

    private static final String TAG = "CacheHistoryRunnable";

    private List< HistoryBean > mHistoryBeans;


    public CacheHistoryRunnable(List< HistoryBean > historyBeans) {

        mHistoryBeans = historyBeans;
    }


    @Override
    public void run() {

        if (mHistoryBeans == null || mHistoryBeans.size() == 0) {
            return;
        }

        GregorianCalendar calendar = new GregorianCalendar();
        Date dateDate = new Date();

        int size = mHistoryBeans.size();
        for (int i = 0; i < size; i++) {
            HistoryBean historyBean = mHistoryBeans.get(i);
            long date = historyBean.date;
            dateDate.setTime(date);
            calendar.setTime(dateDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

        }

        Call< ResponseBody > historyData = NetWork.dayApi().getHistoryData(2015, 5, 18);

        try {
            Response< ResponseBody > execute = historyData.execute();
            if (execute.isSuccessful()) {
                Reader reader = execute.body().charStream();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
