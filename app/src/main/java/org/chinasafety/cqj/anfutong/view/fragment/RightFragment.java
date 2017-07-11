package org.chinasafety.cqj.anfutong.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.chinasafety.cqj.anfutong.R;
import org.chinasafety.cqj.anfutong.model.provider.GlobalDataProvider;
import org.chinasafety.cqj.anfutong.utils.AppConstant;
import org.chinasafety.cqj.anfutong.utils.SharedPreferenceUtil;
import org.chinasafety.cqj.anfutong.view.activity.LoginActivity;
import org.chinasafety.cqj.anfutong.view.widget.sweet_dialog.SweetAlertDialog;

/**
 * Created by mini on 17/5/23.
 */

public class RightFragment extends Fragment {

    private Button mLogout;

    public static RightFragment newInstance() {
        
        Bundle args = new Bundle();
        
        RightFragment fragment = new RightFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page_right,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLogout = (Button) view.findViewById(R.id.home_page_right_logout);
        mLogout.setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == mLogout.getId()){
                new SweetAlertDialog(getActivity(),
                        SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(
                                getResources().getString(R.string.dialog_default_title))
                        .setContentText("你确定退出登录吗？").setCancelText("点错了")
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
                        logout();
                    }
                }).show();

            }
        }
    };

    private void logout() {
        GlobalDataProvider.INSTANCE.setUserInfo(null);
        GlobalDataProvider.INSTANCE.setCompanyInfo(null);
        SharedPreferenceUtil.savePreferences(getActivity(), AppConstant.SP_KEY_USER_INFO,"");
        SharedPreferenceUtil.savePreferences(getActivity(), AppConstant.SP_KEY_COMPANY_INFO,"");
        LoginActivity.start(getActivity());
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
