package org.chinasafety.cqj.anfutong.presenter;


import org.chinasafety.cqj.anfutong.model.AqjcCommitInfo;
import org.chinasafety.cqj.anfutong.model.CsInfo;
import org.chinasafety.cqj.anfutong.model.JcbDetailInfo;
import org.chinasafety.cqj.anfutong.model.JcbInfo;
import org.chinasafety.cqj.anfutong.model.RwInfo;
import org.chinasafety.cqj.anfutong.model.SbInfo;
import org.chinasafety.cqj.anfutong.model.SearchCompanyInfo;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/3/8.
 */
public interface IYhdjPresenter {

    void getCheckedCompany();

    void orgIdToComId(String orgId);

    void getRwData(String comId);

    void getCsData(String comId);

    void getJcbData(String comId);

    void getSbData(String CsId);

    void getJcbDetail(String jcbId);

    void getPrePage();

    void getNextPage();

    void getJcbDetail();

    boolean isCommitting();

    void upload(AqjcCommitInfo info);

    void cancel();

    void getSssb(int pSbid, int comid);

    interface View{
        void setCheckedCompanyList(List<SearchCompanyInfo> companys);
        void getRwDataSuccess(List<RwInfo> data);
        void getCsDataSuccess(List<CsInfo> data);
        void getJcbDataSuccess(List<JcbInfo> data);
        void getSbDataSuccess(List<SbInfo> data);
        void changeJcbDetail(JcbDetailInfo info, int postion, int count);
        void commitStatus(boolean isSuccess);
        void toast(String toast);
        void toast(int toast);
        void pendingDialog(int resId);
        void setJcbDetail(String detail);
        void setJcbDetailGone();
        void cancelDialog();

        void setPlaceId(String pS);

        void notHaveComId();
    }
}
