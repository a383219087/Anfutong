package org.chinasafety.cqj.anfutong.model.provider;

import org.chinasafety.cqj.anfutong.model.CompanyDetailInfo;
import org.chinasafety.cqj.anfutong.model.SearchCompanyInfo;
import org.chinasafety.cqj.anfutong.model.WebServiceUtil;
import org.chinasafety.cqj.anfutong.utils.StringUtils;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by mini on 17/5/25.
 */

public class ServiceCompanyProvider {

    private ServiceCompanyProvider() {
        throw new IllegalArgumentException();
    }

    public static Observable<List<SearchCompanyInfo>> getCheckCompany() {
        return Observable.create(new ObservableOnSubscribe<List<SearchCompanyInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SearchCompanyInfo>> e) throws Exception {
                String keys2[] = {"orgid", "relType", "includeself"};
                Object values2[] = {
                        GlobalDataProvider.INSTANCE.getCompanyInfo().getOrgId(),
                        "安全咨询",
                        false};
                ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                        "getRelationOrg", WebServiceUtil.HUIWEI_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                e.onNext(parse(result));
                e.onComplete();
            }
        });
    }

    public static Observable<SearchCompanyInfo> getCompanysByDistance(final double lat, final double lng){
        return Observable
                .create(new ObservableOnSubscribe<SearchCompanyInfo>() {
                    @Override
                    public void subscribe(ObservableEmitter<SearchCompanyInfo> e) throws Exception {
                        String keys2[] = {"orgid", "relType", "includeself", "lat", "lon", "dis"};
                        Object values2[] = {
                                GlobalDataProvider.INSTANCE.getCompanyInfo().getOrgId(),
                                "安全咨询",
                                false,
                                lat,//119.314974,25.53859
                                lng,
                                20*1000
                        };
                        ArrayList<HashMap<String, Object>> result = null;
                        try {
                            result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                                    "getRelationOrgFromDistance",new String[]{"relOrgName","relOrgID","orgidstr"}, WebServiceUtil.HUIWEI_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                        } catch (InterruptedIOException e1) {
                            e.onError(e1);
                        }
                        if(result!=null){
                            for (HashMap<String, Object> map : result) {
                                SearchCompanyInfo info =new SearchCompanyInfo();
                                info.setCompanyId(StringUtils.noNull(map.get("relOrgID")));
                                info.setCompanyName(StringUtils.noNull(map.get("relOrgName")));
                                info.setOrgIdStr(StringUtils.noNull(map.get("orgidstr")));
                                e.onNext(info);
                            }
                        }
                        e.onComplete();
                    }
                });
    }

    private static List<SearchCompanyInfo> parse(ArrayList<HashMap<String, Object>> result) {
        List<SearchCompanyInfo> companyInfoList = new ArrayList<>();
        for (HashMap<String, Object> map : result) {
            SearchCompanyInfo info = new SearchCompanyInfo();
            info.setCompanyId(StringUtils.noNull(map.get("relOrgID")));
            info.setCompanyName(StringUtils.noNull(map.get("relOrgName")));
            info.setOrgIdStr(StringUtils.noNull(map.get("orgidstr")));
            companyInfoList.add(info);
        }
        return companyInfoList;
    }

    public static Observable<CompanyDetailInfo> getCompanyDetail(final String orgId) {
        return Observable.create(new ObservableOnSubscribe<CompanyDetailInfo>() {
            @Override
            public void subscribe(ObservableEmitter<CompanyDetailInfo> e) throws Exception {
                String keys2[] = {"orgid", "comid"};
                Object values2[] = {
                        orgId,
                        GlobalDataProvider.INSTANCE.getCompanyInfo().getComId()};
                ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                        "getOrgDetail", WebServiceUtil.HUIWEI_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                if (result.size() > 0) {
                    CompanyDetailInfo detailInfo = CompanyDetailInfo.fromMap(result.get(0));
                    detailInfo.setId(orgId);
                    e.onNext(detailInfo);
                }else{
                    e.onNext(null);
                }
                e.onComplete();
            }
        });
    }
}
