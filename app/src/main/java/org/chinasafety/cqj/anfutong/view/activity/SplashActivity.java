package org.chinasafety.cqj.anfutong.view.activity;

import android.os.Bundle;
import android.text.TextUtils;

import org.chinasafety.cqj.anfutong.R;
import org.chinasafety.cqj.anfutong.utils.AppConstant;
import org.chinasafety.cqj.anfutong.utils.SharedPreferenceUtil;
import org.chinasafety.cqj.anfutong.view.BaseActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 初始化的view
 */
public class SplashActivity extends BaseActivity {

    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Observable.timer(2, TimeUnit.SECONDS)
                .map(new Function<Long, Boolean>() {
                    @Override
                    public Boolean apply(Long aLong) throws Exception {
                        String string = SharedPreferenceUtil.getString(SplashActivity.this, AppConstant.SP_KEY_USER_INFO);
                        String company = SharedPreferenceUtil.getString(SplashActivity.this, AppConstant.SP_KEY_COMPANY_INFO);
                        return TextUtils.isEmpty(string) || TextUtils.isEmpty(company);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Boolean value) {
                        if (value) {
                            LoginActivity.start(SplashActivity.this);
                        } else {
                            HomePageActivity.start(SplashActivity.this);
                        }
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoginActivity.start(SplashActivity.this);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
