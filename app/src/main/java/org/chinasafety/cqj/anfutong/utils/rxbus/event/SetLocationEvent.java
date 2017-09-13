package org.chinasafety.cqj.anfutong.utils.rxbus.event;

import org.chinasafety.cqj.anfutong.model.SearchCompanyInfo;

/**
 * 作用：
 * Created by cqj on 2017-08-08.
 */
public class SetLocationEvent {

    private SearchCompanyInfo companyInfo;

    public SetLocationEvent(SearchCompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    public SearchCompanyInfo getCompanyInfo() {
        return companyInfo;
    }
}
