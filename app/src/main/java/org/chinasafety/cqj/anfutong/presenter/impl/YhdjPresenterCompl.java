package org.chinasafety.cqj.anfutong.presenter.impl;

import android.text.TextUtils;

import org.chinasafety.cqj.anfutong.R;
import org.chinasafety.cqj.anfutong.model.AqjcCommitInfo;
import org.chinasafety.cqj.anfutong.model.CsInfo;
import org.chinasafety.cqj.anfutong.model.JcbDetailInfo;
import org.chinasafety.cqj.anfutong.model.JcbInfo;
import org.chinasafety.cqj.anfutong.model.RwInfo;
import org.chinasafety.cqj.anfutong.model.SbInfo;
import org.chinasafety.cqj.anfutong.model.SearchCompanyInfo;
import org.chinasafety.cqj.anfutong.model.WebServiceUtil;
import org.chinasafety.cqj.anfutong.model.provider.GlobalDataProvider;
import org.chinasafety.cqj.anfutong.model.provider.ServiceCompanyProvider;
import org.chinasafety.cqj.anfutong.presenter.IYhdjPresenter;
import org.chinasafety.cqj.anfutong.utils.StringUtils;
import org.chinasafety.cqj.anfutong.utils.UploadDataHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class YhdjPresenterCompl implements IYhdjPresenter {

    private IYhdjPresenter.View mView;
    private List<JcbDetailInfo> mJcbDetailList;
    private int mJcbDetailPage;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private String mComId;

    public YhdjPresenterCompl(IYhdjPresenter.View pView) {
        this.mView = pView;
        mComId = GlobalDataProvider.INSTANCE.getCompanyInfo().getComId();
    }

    @Override
    public void getCheckedCompany() {
        ServiceCompanyProvider.getCheckCompany()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<SearchCompanyInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<SearchCompanyInfo> value) {
                        mView.setCheckedCompanyList(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void orgIdToComId(final String orgId) {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        //OrgidstrtoUserCompanyID
                        String keys2[] = {"orgIDstr"};
                        Object values2[] = {orgId};
                        try {
                            ArrayList<HashMap<String, Object>> data = WebServiceUtil.getWebServiceMsg(keys2, values2,
                                    "OrgidstrtoUserCompanyID", WebServiceUtil.HUIWEI_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                            if (data.size() > 0) {
                                emitter.onNext(StringUtils.noNull(data.get(0).get("onceValue")));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            emitter.onError(e);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(String comId) {
                        if (comId == null || "0".equals(comId)) {
                            mView.notHaveComId();
                        } else {
                            getCsData(comId);
                        }
                        getRwData(comId);
                        getJcbData(comId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.notHaveComId();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getRwData(final String comId) {
        Observable.create(new ObservableOnSubscribe<List<RwInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<RwInfo>> emitter) throws Exception {
                ArrayList<HashMap<String, Object>> result = new ArrayList<>();
                HashMap<String, Object> map = new HashMap<>();
                map.put("TaskID", "0");
                map.put("TaskTitle", "默认安全检查任务");
                result.add(map);
                String keys2[] = {"ComID", "nStart", "eStart"};
                Object values2[] = {
                        mComId,
                        UploadDataHelper.getBeforeOneYearDate(),
                        UploadDataHelper.getNowDate()};
                try {
                    ArrayList<HashMap<String, Object>> data = WebServiceUtil.getWebServiceMsg(keys2, values2,
                            "getSafetyCheckTaskListFromCom", new String[]{
                                    "TaskTitle", "TaskID"},
                            WebServiceUtil.SAFE_URL);
                    result.addAll(data);
                    emitter.onNext(parseRwInfo(result));
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<RwInfo>>() {

                    @Override
                    public void onError(Throwable e) {
                        mView.toast("任务信息获取失败，请重试");
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<RwInfo> hashMaps) {
                        mView.getRwDataSuccess(hashMaps);
                    }
                });
    }

    @Override
    public void getCsData(final String comId) {
        Observable.create(new ObservableOnSubscribe<List<CsInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CsInfo>> emitter) throws Exception {
                ArrayList<HashMap<String, Object>> result = new ArrayList<>();
                HashMap<String, Object> map = new HashMap<>();
                map.put("mplid", "0");
                map.put("mplname", "无");
                result.add(map);
                String keys2[] = {"comid", "keepEmid"};
                Object values2[] = {comId, 0};
                try {
                    ArrayList<HashMap<String, Object>> data = WebServiceUtil.getWebServiceMsg(keys2, values2,
                            "getAllPlace", new String[]{"mplid",
                                    "mplname"}, WebServiceUtil.HUIWEI_URL,WebServiceUtil.HUIWEI_NAMESPACE);
                    result.addAll(data);
                    emitter.onNext(parseCsInfo(result));
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CsInfo>>() {

                    @Override
                    public void onError(Throwable e) {
                        mView.toast("场所信息获取失败，请重试");
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<CsInfo> pCsInfos) {
                        mView.getCsDataSuccess(pCsInfos);
                    }
                });
    }

    @Override
    public void getJcbData(final String comId) {
        Observable.create(new ObservableOnSubscribe<List<JcbInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<JcbInfo>> emitter) throws Exception {
                ArrayList<HashMap<String, Object>> result;
                try {
                    String keys2[] = {"SCheckID", "CDocID", "InduID", "FliedID",
                            "ProName", "Proclid", "Comid"};
                    Object values2[] = {0, 0, 0, 0, "", 0, mComId};
                    result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                            "getSafetyCheckList", new String[]{"oblid", "oblititle"},
                            WebServiceUtil.HUIWEI_SAFE_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("oblid", "0");
                    map.put("oblititle", "无检查表");
                    if (result == null) {
                        result = new ArrayList<>();
                        result.add(map);
                    } else {
                        result.add(0, map);
                    }
                    emitter.onNext(parseJcbInfo(result));
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<JcbInfo>>() {

                    @Override
                    public void onError(Throwable e) {
                        mView.toast("检查表获取失败，请重试");
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<JcbInfo> pCsInfos) {
                        mView.getJcbDataSuccess(pCsInfos);
                    }
                });
    }

    @Override
    public void getSbData(final String csId) {
        Observable.create(new ObservableOnSubscribe<List<SbInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SbInfo>> emitter) throws Exception {
                ArrayList<HashMap<String, Object>> result;
                String keys2[] = {"cpid", "proclaid", "placeid", "kemid", "comid"};
                Object values2[] = {0, 0, Integer.parseInt(csId), 0, 0};
                try {
                    result = new ArrayList<>();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("cpid", "0");
                    map.put("cproname", "无设备");
                    result.add(map);
                    ArrayList<HashMap<String, Object>> data = WebServiceUtil.getWebServiceMsg(keys2, values2,
                            "getAllEquipment", new String[]{"cpid",
                                    "cproname", "num"}, WebServiceUtil.HUIWEI_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                    result.addAll(data);
                    emitter.onNext(parseSbInfo(result, csId));
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SbInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<SbInfo> value) {
                        mView.getSbDataSuccess(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.getMessage();
                        mView.toast("设备信息获取失败，请重试");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getJcbDetail(final String jcbId) {
        Observable.create(new ObservableOnSubscribe<List<JcbDetailInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<JcbDetailInfo>> emitter) throws Exception {
                ArrayList<HashMap<String, Object>> result;
                try {
                    String keys2[] = {"SCheckID", "CDocID", "InduID", "FliedID",
                            "ProName"};
                    Object values2[] = {0, jcbId, 0, 0, ""};
                    result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                            "getSafetyCheckList", new String[]{"oblititle",
                                    "odetail", "sCheckListtype", "induname",
                                    "inseName", "malName", "rAdvise"},
                            WebServiceUtil.SAFE_URL);
                    if (result == null || result.isEmpty()) {
                        result = new ArrayList<>();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("oblititle", "无检查表项");
                        map.put("odetail", "无检查表项");
                        result.add(map);
                    }
                    emitter.onNext(parseJcbDetailInfo(result, jcbId));
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<JcbDetailInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<JcbDetailInfo> value) {
                        mJcbDetailList = value;
                        if (value.isEmpty()) {
                            mView.changeJcbDetail(null, 0, value.size());
                        } else {
                            mView.changeJcbDetail(value.get(0), 1, value.size());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.toast("检查项获取失败，请重试");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getPrePage() {
        if (mJcbDetailList == null || mJcbDetailList.isEmpty()) {
            return;
        }
        if (mJcbDetailPage == 0) {
            mView.toast("没有上一条了");
            return;
        }
        mJcbDetailPage--;
        mView.changeJcbDetail(mJcbDetailList.get(mJcbDetailPage), mJcbDetailPage + 1, mJcbDetailList.size());
    }

    @Override
    public void getNextPage() {
        if (mJcbDetailList == null || mJcbDetailList.isEmpty()) {
            return;
        }
        if (mJcbDetailPage == mJcbDetailList.size() - 1) {
            mView.toast("没有下一条了");
            return;
        }
        mJcbDetailPage++;
        mView.changeJcbDetail(mJcbDetailList.get(mJcbDetailPage), mJcbDetailPage + 1, mJcbDetailList.size());
    }

    @Override
    public void getJcbDetail() {
        if (mJcbDetailList == null || mJcbDetailList.isEmpty()) {
            mView.setJcbDetailGone();
            return;
        }
        if (TextUtils.isEmpty(mJcbDetailList.get(mJcbDetailPage).getOblititle())) {
            mView.setJcbDetailGone();
        }
        String sb = "标题：" +
                mJcbDetailList.get(mJcbDetailPage).getOblititle() +
                "\n" +
                "细则：" +
                mJcbDetailList.get(mJcbDetailPage).getOdetail() +
                "\n" +
                "检查表项针对的人的行为、设备、环境的因素：" +
                mJcbDetailList.get(mJcbDetailPage).getSCheckListtype() +
                "\n" +
                "行业类别：" +
                mJcbDetailList.get(mJcbDetailPage).getInduname() +
                "\n" +
                "伤害类别：" +
                mJcbDetailList.get(mJcbDetailPage).getInseName() +
                "\n" +
                "事故类别：" +
                mJcbDetailList.get(mJcbDetailPage).getMalName() +
                "\n" +
                "整改建议：" +
                mJcbDetailList.get(mJcbDetailPage).getRAdvise() +
                "\n";
        mView.setJcbDetail(sb);
    }

    @Override
    public boolean isCommitting() {
        return !mCompositeDisposable.isDisposed();
    }

    @Override
    public void upload(final AqjcCommitInfo info) {
        mView.pendingDialog(R.string.yhdj_commit_hint);
        // 进行Base64编码
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                try {
                    ArrayList<HashMap<String, Object>> data = UploadDataHelper.uploadAqjc(info);
                    if (data == null || data.isEmpty()) {
                        e.onNext("");
                    } else {
                        e.onNext(StringUtils.noNull(data.get(0).get("retstr")));
                    }
                } catch (Exception pE) {
                    pE.printStackTrace();
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(String value) {
                        mView.cancelDialog();
                        mView.commitStatus(TextUtils.isEmpty(value));
                        if (!TextUtils.isEmpty(value)) {
                            mView.toast(value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.cancelDialog();
                        mView.commitStatus(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void cancel() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void getSssb(final int pSbid, final int comid) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                try {
                    String keys2[] = {"cpid", "proclaid", "placeid", "kemid",
                            "comid"};
                    Object values2[] = {pSbid, 0, 0, 0, comid};
                    ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                            "getAllEquipment", new String[]{"mroomid"},
                            WebServiceUtil.HUIWEI_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                    if (result.size() > 0) {
                        String placeId = String.valueOf(result.get(0).get("mroomid"));
                        e.onNext(placeId);
                    } else {
                        e.onError(new Throwable());
                    }
                } catch (Exception pE) {
                    pE.printStackTrace();
                }
                e.onComplete();

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(String value) {
                        mView.setPlaceId(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.cancelDialog();
                        mView.toast(R.string.ewm_failed);
                    }

                    @Override
                    public void onComplete() {
                        mView.cancelDialog();
                    }
                });
    }

    private List<RwInfo> parseRwInfo(List<HashMap<String, Object>> result) {
        List<RwInfo> rwInfoList = new ArrayList<>();
        for (HashMap<String, Object> map : result) {
            RwInfo rwInfo = new RwInfo(StringUtils.noNull(map.get("TaskID")), StringUtils.noNull(map.get("TaskTitle")));
            rwInfoList.add(rwInfo);
        }
        return rwInfoList;
    }

    private List<CsInfo> parseCsInfo(List<HashMap<String, Object>> result) {
        List<CsInfo> rwInfoList = new ArrayList<>();
        for (HashMap<String, Object> map : result) {
            CsInfo rwInfo = new CsInfo(StringUtils.noNull(map.get("mplid")), StringUtils.noNull(map.get("mplname")));
            rwInfoList.add(rwInfo);
        }
        return rwInfoList;
    }

    private List<SbInfo> parseSbInfo(List<HashMap<String, Object>> result, String csId) {
        List<SbInfo> rwInfoList = new ArrayList<>();
        for (HashMap<String, Object> map : result) {
            String detail = "0".equals(StringUtils.noNull(map.get("cpid"))) ? "" : "(" + StringUtils.noNull(map.get("num")) + ")";
            SbInfo rwInfo = new SbInfo(StringUtils.noNull(map.get("cpid")),
                    String.format("%s%s", StringUtils.noNull(map.get("cproname")), detail), csId);
            rwInfoList.add(rwInfo);
        }
        return rwInfoList;
    }

    private List<JcbInfo> parseJcbInfo(List<HashMap<String, Object>> result) {
        List<JcbInfo> rwInfoList = new ArrayList<>();
        for (HashMap<String, Object> map : result) {
            JcbInfo rwInfo = new JcbInfo(StringUtils.noNull(map.get("oblid")), StringUtils.noNull(map.get("oblititle")));
            if(!rwInfoList.contains(rwInfo)) {
                rwInfoList.add(rwInfo);
            }
        }
        return rwInfoList;
    }

    private List<JcbDetailInfo> parseJcbDetailInfo(List<HashMap<String, Object>> result, String jcbId) {
        List<JcbDetailInfo> rwInfoList = new ArrayList<>();
        for (HashMap<String, Object> map : result) {
            JcbDetailInfo rwInfo = new JcbDetailInfo();
            rwInfo.setJcb_id(jcbId);
            rwInfo.setOblititle(StringUtils.noNull(map.get("oblititle")));
            rwInfo.setOdetail(StringUtils.noNull(map.get("odetail")));
            rwInfo.setSCheckListtype(StringUtils.noNull(map.get("sCheckListtype")));
            rwInfo.setInduname(StringUtils.noNull(map.get("induname")));
            rwInfo.setInseName(StringUtils.noNull(map.get("inseName")));
            rwInfo.setMalName(StringUtils.noNull(map.get("malName")));
            rwInfo.setRAdvise(StringUtils.noNull(map.get("rAdvise")));
            rwInfo.setIs_choose(false);
            rwInfoList.add(rwInfo);
        }
        return rwInfoList;
    }
}
