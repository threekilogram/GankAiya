package com.example.wuxio.gankexamples.beauty;

import com.example.objectbus.bus.ObjectBus;
import com.example.wuxio.gankexamples.dao.CategoryDao;
import com.example.wuxio.gankexamples.dao.CategoryDaoFactory;
import com.example.wuxio.gankexamples.model.CategoryResult;
import com.example.wuxio.gankexamples.model.CategoryResultParser;
import com.example.wuxio.gankexamples.model.ResultBeanSetIdUtil;
import com.example.wuxio.gankexamples.model.ResultsBean;
import com.example.wuxio.gankexamples.net.NetWork;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author wuxio
 */
public class CategoryBeautyRunnable implements Runnable {

    private static final String TAG = "CategoryBeautyRunnable";

    private int       count;
    private int       page;
    private ObjectBus mBus;

    public static final String RESULTS_BEANS_BUS_KEY = "CategoryBeautyRunnable_ResultsBean_List";


    public CategoryBeautyRunnable(int count, int page, ObjectBus bus) {

        this.count = count;
        this.page = page;
        this.mBus = bus;
    }


    @Override
    public void run() {

        /* 从网络读取数据,之后保存结果 */

        Call< ResponseBody > categoryData = NetWork.gankApi().getCategoryData("福利", count, page);

        try {
            Response< ResponseBody > gankResponse = categoryData.execute();
            if (gankResponse.isSuccessful()) {

                Reader charStream = gankResponse.body().charStream();

                if (charStream == null) {
                    return;
                }

                CategoryResult result = CategoryResultParser.parse(charStream);

                if (result.error) {

                    // TODO: 2018-05-05 error handle

                    return;
                } else {

                    List< ResultsBean > results = result.results;
                    ResultBeanSetIdUtil.setID(results);

                    CategoryDao categoryDao = CategoryDaoFactory.getCategoryDao();
                    categoryDao.insert(results);
                }

            } else {

                // TODO: 2018-05-01 获取数据失败 response failed

            }
        } catch (IOException e) {
            e.printStackTrace();

            // TODO: 2018-05-01 problem to connect to server
        }
    }
}