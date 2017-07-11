package org.chinasafety.cqj.anfutong.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.chinasafety.cqj.anfutong.R;
import org.chinasafety.cqj.anfutong.model.CompanyDetailInfo;
import org.chinasafety.cqj.anfutong.model.SearchCompanyInfo;
import org.chinasafety.cqj.anfutong.model.provider.ServiceCompanyProvider;
import org.chinasafety.cqj.anfutong.view.adapter.LinearLayoutManagerWrapper;
import org.chinasafety.cqj.anfutong.view.adapter.RecyclerBaseAdapter;
import org.chinasafety.cqj.anfutong.view.adapter.RecyclerItemDecoration;
import org.chinasafety.cqj.anfutong.view.adapter.viewholder.SearchCompanyDetailHolder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class MapCompanyListFragment extends Fragment {

    private final List<CompanyDetailInfo> mDataList = new ArrayList<>();

    public static MapCompanyListFragment newInstance() {

        Bundle args = new Bundle();

        MapCompanyListFragment fragment = new MapCompanyListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerBaseAdapter<CompanyDetailInfo, SearchCompanyDetailHolder> mAdapter;
    private List<CompanyDetailInfo> mDataListForSearch = new ArrayList<>();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Disposable mClickDisposable;
    private ClickCallback mItemClickAction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_company_map_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView companyListView = (RecyclerView) view.findViewById(R.id.map_company_list_view);
        companyListView.setLayoutManager(new LinearLayoutManagerWrapper(getActivity(), LinearLayoutManager.VERTICAL, false));
        companyListView.addItemDecoration(new RecyclerItemDecoration());
        mAdapter = new RecyclerBaseAdapter<>(R.layout.item_search_company_info, SearchCompanyDetailHolder.class);
        companyListView.setAdapter(mAdapter);
        initEvent();
        getData();
    }

    private void setData(CompanyDetailInfo data) {
        mAdapter.add(data);
        mDataListForSearch.add(data);
    }

    private void getData() {
        ServiceCompanyProvider.getCheckCompany()
                .flatMap(new Function<List<SearchCompanyInfo>, ObservableSource<SearchCompanyInfo>>() {
                    @Override
                    public ObservableSource<SearchCompanyInfo> apply(List<SearchCompanyInfo> searchCompanyInfos) throws Exception {
                        return Observable.fromIterable(searchCompanyInfos);
                    }
                })
                .flatMap(new Function<SearchCompanyInfo, ObservableSource<CompanyDetailInfo>>() {
                    @Override
                    public ObservableSource<CompanyDetailInfo> apply(SearchCompanyInfo searchCompanyInfo) throws Exception {
                            return ServiceCompanyProvider.getCompanyDetail(searchCompanyInfo.getCompanyId());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CompanyDetailInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(CompanyDetailInfo value) {
                        setData(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initEvent() {
        mAdapter.itemClickObserve()
                .doOnSubscribe(new Consumer<Disposable>() {

                    @Override
                    public void accept(Disposable disposable) throws Exception {

                        mClickDisposable = disposable;
                    }
                })
                .subscribe(new Consumer<CompanyDetailInfo>() {
                    @Override
                    public void accept(CompanyDetailInfo companyDetailInfo) throws Exception {
                        if (mItemClickAction != null) {
                            mItemClickAction.call(companyDetailInfo);
                        }
                    }
                });
    }

    public void search(String name) {
        if (TextUtils.isEmpty(name)) {
            mAdapter.setAll(mDataListForSearch);
            return;
        }
        mAdapter.removeAll();
        for (CompanyDetailInfo companyInfo : mDataListForSearch) {
            if (companyInfo.getName().contains(name)) {
                mAdapter.add(companyInfo);
            }
        }
    }

    public void setItemClickAction(ClickCallback itemClickAction) {
        this.mItemClickAction = itemClickAction;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
        if (mClickDisposable != null) {
            mClickDisposable.dispose();
        }
    }

    public interface ClickCallback {
        void call(CompanyDetailInfo info);
    }
}
