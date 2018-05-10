package com.example.wuxio.gankexamples.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

/**
 * @author wuxio 2018-05-10:8:12
 */
@Entity
@Uid(1752219575056127637L)
public class DayContentBean {

    @Id(assignable = true)
    public long date;

    private int contentAllType;


    public int getContentAllType() {

        return contentAllType;
    }


    /**
     * {@link com.example.wuxio.gankexamples.constant.CategoryConstant#REST_VIDEO}
     */
    public void setHasRestVideo() {

        contentAllType |= 0b1;
    }


    public boolean hasRestVideo() {

        return (contentAllType & 0b1) == 1;
    }


    /**
     * {@link com.example.wuxio.gankexamples.constant.CategoryConstant#EXTRA_RESOURCES}
     */
    public void setHasExtraResources() {

        contentAllType |= 0b10;
    }


    public boolean hasExtraResources() {

        return (contentAllType >> 1 & 0b1) == 1;
    }


    /**
     * {@link com.example.wuxio.gankexamples.constant.CategoryConstant#FRONT}
     */
    public void setHasFront() {

        contentAllType |= 0b100;
    }


    public boolean hasFront() {

        return (contentAllType >> 2 & 0b1) == 1;
    }


    /**
     * {@link com.example.wuxio.gankexamples.constant.CategoryConstant#Android}
     */
    public void setHasAndroid() {

        contentAllType |= 0b1000;
    }


    public boolean hasAndroid() {

        return (contentAllType >> 3 & 0b1) == 1;
    }


    /**
     * {@link com.example.wuxio.gankexamples.constant.CategoryConstant#RECOMMEND}
     */
    public void setHasRecommend() {

        contentAllType |= 0b10000;
    }


    public boolean hasRecommend() {

        return (contentAllType >> 4 & 0b1) == 1;
    }


    /**
     * {@link com.example.wuxio.gankexamples.constant.CategoryConstant#App}
     */
    public void setHasApp() {

        contentAllType |= 0b100000;
    }


    public boolean hasApp() {

        return (contentAllType >> 5 & 0b1) == 1;
    }


    /**
     * {@link com.example.wuxio.gankexamples.constant.CategoryConstant#iOS}
     */
    public void setHasIos() {

        contentAllType |= 0b1000000;
    }


    public boolean hasIos() {

        return (contentAllType >> 6 & 0b1) == 1;
    }


    /**
     * {@link com.example.wuxio.gankexamples.constant.CategoryConstant#BEAUTY}
     */
    public void setHasBeauty() {

        contentAllType |= 0b10000000;
    }


    public boolean hasBeauty() {

        return (contentAllType >> 7 & 0b1) == 1;
    }
}
