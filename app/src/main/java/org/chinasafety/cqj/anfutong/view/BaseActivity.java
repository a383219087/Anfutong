package org.chinasafety.cqj.anfutong.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.chinasafety.cqj.anfutong.R;

/**
 * Activity基类
 * Created by cqj on 17/5/21.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }
}
