package org.chinasafety.cqj.anfutong.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.chinasafety.cqj.anfutong.R;
import org.chinasafety.cqj.anfutong.model.CompanyDetailInfo;
import org.chinasafety.cqj.anfutong.model.SearchCompanyInfo;
import org.chinasafety.cqj.anfutong.model.provider.ServiceCompanyProvider;
import org.chinasafety.cqj.anfutong.view.BaseActivity;
import org.chinasafety.cqj.anfutong.view.adapter.LinearLayoutManagerWrapper;
import org.chinasafety.cqj.anfutong.view.adapter.RecyclerBaseAdapter;
import org.chinasafety.cqj.anfutong.view.adapter.RecyclerItemDecoration;
import org.chinasafety.cqj.anfutong.view.adapter.viewholder.SearchCompanyHolder;
import org.chinasafety.cqj.anfutong.view.widget.sweet_dialog.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CompanySearchActivity extends BaseActivity {

    private Button mBtnSearch;
    private EditText mEdtSearch;
    private ProgressBar mProgressBar;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private RecyclerBaseAdapter<SearchCompanyInfo, SearchCompanyHolder> mAdapter;
    private List<SearchCompanyInfo> mDataListForSearch = new ArrayList<>();
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search);
        initView();
        initEvent();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEdtSearch = (EditText) findViewById(R.id.company_search_edit);
        mBtnSearch = (Button) findViewById(R.id.company_search_btn);
        mProgressBar = (ProgressBar) findViewById(R.id.company_list_progress_bar);
        RecyclerView companyListView = (RecyclerView) findViewById(R.id.search_company_result_list_view);
        companyListView.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));
        companyListView.addItemDecoration(new RecyclerItemDecoration());
        mAdapter = new RecyclerBaseAdapter<>(R.layout.item_search_company_info, SearchCompanyHolder.class);
        companyListView.setAdapter(mAdapter);
        searchCompany();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==android.R.id.home){
            finish();
        }
        return true;
    }

    private void initEvent() {
        mAdapter.itemClickObserve()
                .subscribe(new Observer<SearchCompanyInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SearchCompanyInfo value) {
                        getDetail(value.getCompanyId());
//                        OneCompanyMapActivity.start(CompanySearchActivity.this,value.getCompanyId());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mBtnSearch.setOnClickListener(mClickListener);
    }

    private void getDetail(String id) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请稍后……");
        progressDialog.show();
        ServiceCompanyProvider.getCompanyDetail(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CompanyDetailInfo>() {


                    @Override
                    public void onSubscribe(Disposable d) {

                        mDisposable = d;
                    }

                    @Override
                    public void onNext(CompanyDetailInfo value) {
                        progressDialog.dismiss();
                        if(value!=null) {
                            SafeCheckActivity.start(CompanySearchActivity.this, value.getId(), value.getIdStr());
                        }else{
                            Toast.makeText(CompanySearchActivity.this, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==mBtnSearch.getId()){
                String searchContent =mEdtSearch.getText().toString();
                if(TextUtils.isEmpty(searchContent)){
                    mAdapter.setAll(mDataListForSearch);
                    return;
                }
                mAdapter.removeAll();
                for (SearchCompanyInfo companyInfo : mDataListForSearch) {
                    if(companyInfo.getCompanyName().contains(searchContent)){
                        mAdapter.add(companyInfo);
                    }
                }
            }
        }
    };

    private void searchCompany() {
        mProgressBar.setVisibility(View.VISIBLE);
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
                        mDataListForSearch.clear();
                        mDataListForSearch.addAll(value);
                        mAdapter.setAll(value);
                        mProgressBar.setVisibility(View.GONE);
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
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CompanySearchActivity.class);
        context.startActivity(starter);
    }
}
