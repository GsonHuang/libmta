package com.libmta;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Gson
 * @date 2021/8/4 17:12
 */
public class MtaBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MTAManager.getInstance().enterPage(getClass().getSimpleName());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MTAManager.getInstance().leavepPage(getClass().getSimpleName());
    }
}
