package org.chinasafety.cqj.anfutong.view.activity;

import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.model.LatLng;

import org.chinasafety.cqj.anfutong.model.CompanyDetailInfo;
import org.chinasafety.cqj.anfutong.presenter.IMapSelectPresenter;
import org.chinasafety.cqj.anfutong.presenter.impl.OneCompanyMapPresenter;

import java.util.List;

public class OneCompanyMapActivity extends MapSelectActivity {

    public static final String KEY_ORG_ID = "orgId";

    @Override
    protected IMapSelectPresenter getPresenter() {
        return new OneCompanyMapPresenter(getIntent().getStringExtra(KEY_ORG_ID),this);
    }

    public static void start(Context context,String orgId) {
        Intent starter = new Intent(context, OneCompanyMapActivity.class);
        starter.putExtra(KEY_ORG_ID,orgId);
        context.startActivity(starter);
    }

    @Override
    public void addCompanyList(List<CompanyDetailInfo> value) {
        super.addCompanyList(value);
        if(value.size()>0){
            CompanyDetailInfo companyDetailInfo = value.get(0);
            setInfoToDetail(companyDetailInfo);
            LatLng latLng = new LatLng(companyDetailInfo.getLat(),
                    companyDetailInfo.getLon());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(latLng).zoom(15.0f);
            animateMapStatus(builder.build());
        }
    }
}
