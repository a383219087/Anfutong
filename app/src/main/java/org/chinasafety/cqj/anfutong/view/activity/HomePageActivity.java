package org.chinasafety.cqj.anfutong.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.chinasafety.cqj.anfutong.R;
import org.chinasafety.cqj.anfutong.model.CompanyInfo;
import org.chinasafety.cqj.anfutong.model.UserInfo;
import org.chinasafety.cqj.anfutong.model.provider.GlobalDataProvider;
import org.chinasafety.cqj.anfutong.utils.AppConstant;
import org.chinasafety.cqj.anfutong.utils.PermisionUtils;
import org.chinasafety.cqj.anfutong.utils.SharedPreferenceUtil;
import org.chinasafety.cqj.anfutong.view.BaseActivity;
import org.chinasafety.cqj.anfutong.view.fragment.CenterFragment;
import org.chinasafety.cqj.anfutong.view.fragment.LeftFragment;
import org.chinasafety.cqj.anfutong.view.fragment.RightFragment;
import org.chinasafety.cqj.anfutong.view.widget.sweet_dialog.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 * Created by cqj on 17/5/21.
 */

public class HomePageActivity extends BaseActivity {


    private RadioGroup mButtonGroup;
    private final List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        PermisionUtils.verifyStoragePermissions(this);
        PermisionUtils.verifyCameraPermissions(this);
        if (initView()) {
            initEvent();
        }
    }

    private boolean initView() {
        if (GlobalDataProvider.INSTANCE.getUserInfo() == null) {
            String userInfoString = SharedPreferenceUtil.getString(this, AppConstant.SP_KEY_USER_INFO);
            String companyInfoString = SharedPreferenceUtil.getString(this, AppConstant.SP_KEY_COMPANY_INFO);
            try {
                UserInfo userInfo = UserInfo.fromString(userInfoString);
                GlobalDataProvider.INSTANCE.setUserInfo(userInfo);
                CompanyInfo info = CompanyInfo.fromString(companyInfoString);
                GlobalDataProvider.INSTANCE.setCompanyInfo(info);
            } catch (Exception e) {
                Toast.makeText(this, "取出保存的数据失败，请重新登录！", Toast.LENGTH_SHORT).show();
                LoginActivity.start(this);
                finish();
                return false;
            }
        }
        mButtonGroup = (RadioGroup) findViewById(R.id.home_page_button_group);
        CenterFragment centerFragment = CenterFragment.newInstance();
        LeftFragment leftFragment = LeftFragment.newInstance();
        RightFragment rightFragment = RightFragment.newInstance();
        mFragmentList.add(leftFragment);
        mFragmentList.add(centerFragment);
        mFragmentList.add(rightFragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .add(R.id.home_page_fragment_container, leftFragment)
                .add(R.id.home_page_fragment_container, centerFragment)
                .add(R.id.home_page_fragment_container, rightFragment)
                .show(centerFragment).hide(leftFragment).hide(rightFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commitAllowingStateLoss();
        return true;
    }

    private void initEvent() {
        mButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.home_page_bottom_left_button:
                        switchFragment(0);
                        break;
                    case R.id.home_page_bottom_middle_button:
                        switchFragment(1);
                        break;
                    case R.id.home_page_bottom_right_button:
                        switchFragment(2);
                        break;
                    default:
                        switchFragment(1);
                        break;
                }
            }
        });
    }

    private void switchFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        for (int i = 0; i < mFragmentList.size(); i++) {
            if (position == i) {
                transaction.show(mFragmentList.get(position));
            } else {
                transaction.hide(mFragmentList.get(i));
            }
        }
        transaction.commit();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, HomePageActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this,
                SweetAlertDialog.WARNING_TYPE)
                .setTitleText(
                        getResources().getString(R.string.dialog_default_title))
                .setContentText("你确定退出吗？").setCancelText("点错了")
                .setConfirmText("是的！").showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
                finish();
            }
        }).show();
    }
}
