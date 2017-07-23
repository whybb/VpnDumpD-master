package com.vpndumpdemo.view;

import android.widget.BaseAdapter;


/**
 */
public interface IGraspBagView {

    //为null时表示抓包失败
    void onGraspFininished(String savePath);

    //联网应用的初始化
    void onLoadApp(BaseAdapter baseAdapter);

    //操作类型和错误信息
    void onOptionFailed(int typeId, String msg);

    //抓包数据的不断更新
    void onDataShowRefresh();
}
