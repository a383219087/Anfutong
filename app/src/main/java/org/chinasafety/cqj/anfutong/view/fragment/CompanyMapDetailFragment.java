package org.chinasafety.cqj.anfutong.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.chinasafety.cqj.anfutong.R;
import org.chinasafety.cqj.anfutong.model.CompanyDetailInfo;
import org.chinasafety.cqj.anfutong.model.YhfcInfo;
import org.chinasafety.cqj.anfutong.model.provider.HiddenllnessProvider;
import org.chinasafety.cqj.anfutong.view.activity.SafeCheckActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


public class CompanyMapDetailFragment extends Fragment {

    private TextView mTvFirstYh;
    private TextView mTvSecondYh;
    private Button mToCheck;
    private Button mToNavi;
    private CompanyDetailInfo mDetailInfo;
    private TextView mTvCompanyName;
    private TextView mTvCompanyAddress;
    private TextView mTvCompanyPeople;
    private TextView mTvCompanyPhone;
    private Disposable mDisposable;
    private final PublishSubject<CompanyDetailInfo> mPublishSubject = PublishSubject.create();

    public static CompanyMapDetailFragment newInstance() {

        Bundle args = new Bundle();
        CompanyMapDetailFragment fragment = new CompanyMapDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_company_map_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    private void initView(View view) {
        mTvCompanyName = (TextView) view.findViewById(R.id.map_company_name);
        mTvCompanyAddress = (TextView) view.findViewById(R.id.map_company_address);
        mTvCompanyPeople = (TextView) view.findViewById(R.id.map_company_people);
        mTvCompanyPhone = (TextView) view.findViewById(R.id.map_company_phone);
        mTvFirstYh = (TextView) view.findViewById(R.id.map_company_yh_first);
        mTvSecondYh = (TextView) view.findViewById(R.id.map_company_yh_second);
        mToNavi = (Button) view.findViewById(R.id.map_company_to_navi);
        mToNavi.setOnClickListener(mClickListener);
        mToCheck = (Button) view.findViewById(R.id.map_company_to_check);
        mToCheck.setOnClickListener(mClickListener);
        setDetailToView();
    }

    private void setDetailToView() {
        if (mTvCompanyAddress == null || mDetailInfo == null) {
            return;
        }
        mTvCompanyAddress.setText(mDetailInfo.getAddress());
        mTvCompanyName.setText(mDetailInfo.getName());
        mTvCompanyPeople.setText(mDetailInfo.getPeople());
        mTvCompanyPhone.setText(mDetailInfo.getPhone());
        HiddenllnessProvider
                .getHiddenllnessByName(mDetailInfo.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<YhfcInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(List<YhfcInfo> value) {
                        if (value.size() == 1) {
                            String string = getFormatString(value.get(0));
                            mTvFirstYh.setText(string);
                            mTvFirstYh.setVisibility(View.VISIBLE);
                            mTvSecondYh.setVisibility(View.GONE);
                        } else if (value.size() >= 2) {
                            String string = getFormatString(value.get(0));
                            mTvFirstYh.setText(string);
                            mTvFirstYh.setVisibility(View.VISIBLE);
                            String second = getFormatString(value.get(1));
                            mTvSecondYh.setText(second);
                            mTvSecondYh.setVisibility(View.VISIBLE);
                        } else {
                            mTvFirstYh.setVisibility(View.GONE);
                            mTvSecondYh.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private String getFormatString(YhfcInfo value) {
        StringBuilder stringBuffer = new StringBuilder();
        String checkDate = value.getCheckDate();
        try {
            checkDate = checkDate.length() >= 10 ? checkDate.substring(0, 10) : "";
            stringBuffer.append(checkDate);
            stringBuffer.append("：");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String safetyTrouble = value.getSafetyTrouble();
        safetyTrouble = safetyTrouble.length() > 8 ? safetyTrouble.substring(0, 8) + "……" : safetyTrouble;
        stringBuffer.append(safetyTrouble);
        String text = "";
        if (TextUtils.isEmpty(value.getFinishDate())) {
            text = "未整改";
        } else if (TextUtils.isEmpty(value.getReviewDate())) {
            text = "已整改未复查";
        } else {
            text = "已整改已复查";
        }
        stringBuffer.append(text);
        return stringBuffer.toString();
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mDetailInfo == null) {
                return;
            }
            if (view.getId() == mToCheck.getId()) {
                SafeCheckActivity.start(getActivity(), mDetailInfo.getId(), mDetailInfo.getIdStr());
            } else if (view.getId() == mToNavi.getId()) {
                mPublishSubject.onNext(mDetailInfo);
            }
        }
    };

    public Observable<CompanyDetailInfo> getOnNaviClick() {
        return mPublishSubject;
    }

    public void setCompanyDetail(CompanyDetailInfo info) {
        mDetailInfo = info;
        setDetailToView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
