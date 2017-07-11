package org.chinasafety.cqj.anfutong.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.chinasafety.cqj.anfutong.R;
import org.chinasafety.cqj.anfutong.model.provider.GlobalDataProvider;
import org.chinasafety.cqj.anfutong.presenter.ICenterFragmentPresenter;
import org.chinasafety.cqj.anfutong.presenter.impl.CenterFragmentPresenterImpl;
import org.chinasafety.cqj.anfutong.view.activity.CompanySearchActivity;
import org.chinasafety.cqj.anfutong.view.activity.MapSelectActivity;
import org.chinasafety.cqj.anfutong.view.activity.SafeReviewActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mini on 17/5/23.
 */

public class CenterFragment extends Fragment implements ICenterFragmentPresenter.IView{

    private TextView mTvMessageUnread;
    private TextView mClassNoStudy;
    private TextView mBtnSafeCheck;
    private TextView mBtnNotification;
    private TextView mBtnSafeReview;
    private TextView mBtnWorkRecord;
    private TextView mBtnCompanySearch;
    private TextView mBtnPolicy;
    private TextView mBtnStudy;

    private ICenterFragmentPresenter mPresenter;

    public static CenterFragment newInstance() {

        Bundle args = new Bundle();

        CenterFragment fragment = new CenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page_center, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        TextView tvDate = (TextView) view.findViewById(R.id.fragment_home_page_date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日",Locale.getDefault());
        tvDate.setText(simpleDateFormat.format(new Date()));
        TextView tvCurrentUser = (TextView) view.findViewById(R.id.fragment_home_page_current_user);
        tvCurrentUser.setText(String.format(Locale.getDefault(), "%s%s", getResources().getString(R.string.home_page_current_user_prefix), GlobalDataProvider.INSTANCE.getUserInfo().getName()));
        TextView tvCurrentCompany = (TextView) view.findViewById(R.id.fragment_center_company_name);
        tvCurrentCompany.setText(GlobalDataProvider.INSTANCE.getCompanyInfo().getComFullName());
        mTvMessageUnread = (TextView) view.findViewById(R.id.home_page_message_count);
        mClassNoStudy = (TextView) view.findViewById(R.id.home_page_class_count);
        mBtnNotification = (TextView) view.findViewById(R.id.home_page_goto_notification);
        mBtnNotification.setOnClickListener(mClickListener);
        mBtnSafeCheck = (TextView) view.findViewById(R.id.home_page_goto_check);
        mBtnSafeCheck.setOnClickListener(mClickListener);
        mBtnSafeReview = (TextView) view.findViewById(R.id.home_page_goto_review);
        mBtnSafeReview.setOnClickListener(mClickListener);
        mBtnCompanySearch = (TextView) view.findViewById(R.id.home_page_goto_company_search);
        mBtnCompanySearch.setOnClickListener(mClickListener);
        mBtnWorkRecord = (TextView) view.findViewById(R.id.home_page_goto_work_record);
        mBtnWorkRecord.setOnClickListener(mClickListener);
        mBtnPolicy = (TextView) view.findViewById(R.id.home_page_goto_policy);
        mBtnPolicy.setOnClickListener(mClickListener);
        mBtnStudy = (TextView) view.findViewById(R.id.home_page_goto_class);
        mBtnStudy.setOnClickListener(mClickListener);
        mPresenter = new CenterFragmentPresenterImpl(this);
        mPresenter.getUnReadMessageCount();
        mPresenter.getUnStudyClassCount();
    }


    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == mBtnSafeCheck.getId()) {
                MapSelectActivity.start(getActivity());
            } else if (view.getId() == mBtnSafeReview.getId()) {
                SafeReviewActivity.start(getActivity());
            } else if(view.getId() == mBtnCompanySearch.getId()){
                CompanySearchActivity.start(getActivity());
            }
        }
    };

    @Override
    public void setUnReadMessageCount(String count) {
        mTvMessageUnread.setText(String.format(Locale.getDefault(),"%s%s",count,getResources().getString(R.string.home_page_message_unread_count)));
    }

    @Override
    public void setUnStydyClassCount(String classCount) {
        mClassNoStudy.setText(String.format(Locale.getDefault(),"%s%s",classCount,getResources().getString(R.string.home_page_class_un_study)));
    }

    @Override
    public void toast(@StringRes int msg) {

    }

    @Override
    public void toast(String msg) {

    }
}
