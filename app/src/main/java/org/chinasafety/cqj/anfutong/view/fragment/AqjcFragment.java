package org.chinasafety.cqj.anfutong.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.chinasafety.cqj.anfutong.R;
import org.chinasafety.cqj.anfutong.model.AqjcCommitInfo;
import org.chinasafety.cqj.anfutong.model.CsInfo;
import org.chinasafety.cqj.anfutong.model.IChooseItem;
import org.chinasafety.cqj.anfutong.model.JcbDetailInfo;
import org.chinasafety.cqj.anfutong.model.JcbInfo;
import org.chinasafety.cqj.anfutong.model.RwInfo;
import org.chinasafety.cqj.anfutong.model.SbInfo;
import org.chinasafety.cqj.anfutong.model.SearchCompanyInfo;
import org.chinasafety.cqj.anfutong.model.provider.GlobalDataProvider;
import org.chinasafety.cqj.anfutong.presenter.IYhdjPresenter;
import org.chinasafety.cqj.anfutong.presenter.impl.YhdjPresenterCompl;
import org.chinasafety.cqj.anfutong.utils.StringUtils;
import org.chinasafety.cqj.anfutong.view.activity.AudioRecordActivity;
import org.chinasafety.cqj.anfutong.view.activity.CameraTestActivity;
import org.chinasafety.cqj.anfutong.view.activity.JcbDetailActivity;
import org.chinasafety.cqj.anfutong.view.adapter.MySpinnerAdapter;
import org.chinasafety.cqj.anfutong.view.widget.my_camera.ITakePhotoListener;
import org.chinasafety.cqj.anfutong.view.widget.my_camera.MyCamera;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/23.
 */
public class AqjcFragment extends Fragment implements IYhdjPresenter.View, View.OnClickListener, ITakePhotoListener {


    private final static int AUDIO = 25;
    private static final int QRCODE_REQUEST = 110;
    private static final String KEY_ORG_ID = "orgId";
    private static final String KEY_ORG_ID_STR = "orgIdStr";


    private ImageButton mQrCodeBtn, mCameraBtn, mCallBtn;
    private Button mCommitBtn, mAudioBtn;
    private LinearLayout mJcbLn;
    private EditText mJcbDetailEdit;
    private ImageView mJcbDetailPre, mJcbDetailNext;
    private TextView mJcbPositionTv, mJcbCountTv;
    private Spinner mJcbSp, mCsSp, mSbSp, mRwSp, mYhdjSp;
    private Spinner mSpCheckedCompany;
    private EditText mYgfyEdt, mJyzgEdt, mDateZg;
    private Button mJcbDetail;
    private TextView mPhotoCount;

    private IYhdjPresenter mPresenter;
    private File mCache;
    private Calendar mCalendar;
    private ProgressDialog mProgressDialog;
    private int mObjOrganizationID;
    private View mView;
    private MyCamera mMyCamera;
    private boolean isSetPlace;
    private int mEwmSssbId;
    private LinearLayout mLnSb;
    private EditText mEdtPlace;

    public static AqjcFragment newInstance(String orgId,String orgIdStr) {
        Bundle args = new Bundle();
        args.putString(KEY_ORG_ID, orgId);
        args.putString(KEY_ORG_ID_STR,orgIdStr);
        AqjcFragment fragment = new AqjcFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_aqjc, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComplement();
        registListener();

    }

    private void initComplement() {
        mCalendar = Calendar.getInstance();
        mMyCamera = (MyCamera) mView.findViewById(R.id.my_camera);
        mMyCamera.setTakePhotoListener(this);
        mQrCodeBtn = (ImageButton) mView.findViewById(R.id.ewm_btn);
        mCameraBtn = (ImageButton) mView.findViewById(R.id.xcpz_btn);
        mPhotoCount = (TextView) mView.findViewById(R.id.photo_count);
        mAudioBtn = (Button) mView.findViewById(R.id.xcly_btn);
        mCallBtn = (ImageButton) mView.findViewById(R.id.bddh_btn);
        mCommitBtn = (Button) mView.findViewById(R.id.commit_btn);
        mJcbLn = (LinearLayout) mView.findViewById(R.id.jcb_ln);
        mJcbDetailEdit = (EditText) mView.findViewById(R.id.jcb_edit);
        mJcbDetailPre = (ImageView) mView.findViewById(R.id.jcbdetail_pre);
        mJcbDetailNext = (ImageView) mView.findViewById(R.id.jcbdetail_next);
        mJcbPositionTv = (TextView) mView.findViewById(R.id.jcb_position);
        mJcbCountTv = (TextView) mView.findViewById(R.id.jcb_count);
        mJcbSp = (Spinner) mView.findViewById(R.id.jcb_spinner);
        mCsSp = (Spinner) mView.findViewById(R.id.cs_spinner);
        mSbSp = (Spinner) mView.findViewById(R.id.sb_spinner);
        mRwSp = (Spinner) mView.findViewById(R.id.rw_spinner);
        mYhdjSp = (Spinner) mView.findViewById(R.id.yhdj_spinner);
        mYgfyEdt = (EditText) mView.findViewById(R.id.ygfy_edit);
        mJyzgEdt = (EditText) mView.findViewById(R.id.zgcs_edit);
        mJcbDetail = (Button) mView.findViewById(R.id.jcb_detail_btn);
        mDateZg = (EditText) mView.findViewById(R.id.date_yhzg_edit);
        mSpCheckedCompany = (Spinner) mView.findViewById(R.id.checked_company);
        mLnSb = (LinearLayout) mView.findViewById(R.id.sb_ln);
        mEdtPlace = (EditText) mView.findViewById(R.id.cs_edit);
        mCache = new File(Environment.getExternalStorageDirectory(),
                "hwaftCache");
        if (!mCache.exists())
            mCache.mkdirs();

        mObjOrganizationID = -1;
        try {
            String orgId = getArguments().getString(KEY_ORG_ID);
            mObjOrganizationID = Integer.parseInt(orgId);
        } catch (Exception pE) {
            pE.printStackTrace();
        }
        mPresenter = new YhdjPresenterCompl(this);
        mPresenter.getCheckedCompany();
        mPresenter.orgIdToComId(getArguments().getString(KEY_ORG_ID_STR));
    }

    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;
        if (scrollDifference == 0) {
            return false;
        }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    private void registListener() {
        mJcbDetailEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View pView, MotionEvent pMotionEvent) {
                if (canVerticalScroll(mJcbDetailEdit)) {
                    pView.getParent().requestDisallowInterceptTouchEvent(true);
                    if (pMotionEvent.getAction() == MotionEvent.ACTION_UP) {
                        pView.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });
        mCameraBtn.setOnClickListener(this);
        mAudioBtn.setOnClickListener(this);
        mJcbDetailPre.setOnClickListener(this);
        mJcbDetailNext.setOnClickListener(this);
        mCallBtn.setOnClickListener(this);
        mQrCodeBtn.setOnClickListener(this);
        mJcbDetail.setOnClickListener(this);
        mCommitBtn.setOnClickListener(this);
        mDateZg.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);
        mDateZg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String monthStr;
                                if (monthOfYear < 9) {
                                    monthStr = "0" + (monthOfYear + 1);
                                } else {
                                    monthStr = (monthOfYear + 1) + "";
                                }
                                String dayStr;
                                if (dayOfMonth < 10) {
                                    dayStr = "0" + dayOfMonth;
                                } else {
                                    dayStr = dayOfMonth + "";
                                }
                                mDateZg.setText(String.format("%s-%s-%s", String.valueOf(year), monthStr, dayStr));
                            }
                        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mDateZg.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                public void onDateSet(DatePicker view,
                                                      int year, int monthOfYear,
                                                      int dayOfMonth) {
                                    String monthStr;
                                    if (monthOfYear < 9) {
                                        monthStr = "0" + (monthOfYear + 1);
                                    } else {
                                        monthStr = (monthOfYear + 1) + "";
                                    }
                                    String dayStr;
                                    if (dayOfMonth < 10) {
                                        dayStr = "0" + dayOfMonth;
                                    } else {
                                        dayStr = dayOfMonth + "";
                                    }
                                    mDateZg.setText(String.format("%s-%s-%s", String.valueOf(year), monthStr, dayStr));
                                }
                            }, mCalendar.get(Calendar.YEAR), mCalendar
                            .get(Calendar.MONTH), mCalendar
                            .get(Calendar.DAY_OF_MONTH)).show();

                }
            }
        });
        mYhdjSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                if (pI > 0) {
                    mDateZg.setText(getAfterFiveDay());
                } else {
                    mDateZg.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> pAdapterView) {

            }
        });
        mJcbSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                IChooseItem item = (IChooseItem) pAdapterView.getItemAtPosition(pI);
                if ("0".equals(item.getItemId())) {
                    mJcbLn.setVisibility(View.GONE);
                    mJyzgEdt.setText("");
                } else {
                    mJcbLn.setVisibility(View.VISIBLE);
                    mPresenter.getJcbDetail(item.getItemId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> pAdapterView) {

            }
        });
        mCsSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                IChooseItem item = (IChooseItem) pAdapterView.getItemAtPosition(pI);
                mPresenter.getSbData(item.getItemId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> pAdapterView) {

            }
        });
        mSpCheckedCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                IChooseItem item = (IChooseItem) adapterView.getItemAtPosition(i);
                mPresenter.orgIdToComId(item.getCsId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void setCheckedCompanyList(List<SearchCompanyInfo> companys) {
        MySpinnerAdapter<SearchCompanyInfo> adapter = new MySpinnerAdapter<>(companys, getActivity());
        mSpCheckedCompany.setAdapter(adapter);
        int position = adapter.selectItemById(String.valueOf(mObjOrganizationID));
        if (position != -1) {
            mSpCheckedCompany.setSelection(position);
        }
    }

    @Override
    public void getRwDataSuccess(List<RwInfo> data) {
        MySpinnerAdapter<RwInfo> adapter = new MySpinnerAdapter<>(data, getActivity());
        mRwSp.setAdapter(adapter);
    }

    @Override
    public void getCsDataSuccess(List<CsInfo> data) {
        MySpinnerAdapter<CsInfo> adapter = new MySpinnerAdapter<>(data, getActivity());
        mCsSp.setAdapter(adapter);
    }

    @Override
    public void getJcbDataSuccess(List<JcbInfo> data) {
        MySpinnerAdapter<JcbInfo> adapter = new MySpinnerAdapter<>(data, getActivity());
        mJcbSp.setAdapter(adapter);
    }

    @Override
    public void getSbDataSuccess(List<SbInfo> data) {
        MySpinnerAdapter<SbInfo> adapter = new MySpinnerAdapter<>(data, getActivity());
        mSbSp.setAdapter(adapter);
        if (isSetPlace) {
            mSbSp.setSelection(adapter.selectItemById(String.valueOf(mEwmSssbId)));
            isSetPlace = false;
        }
    }

    @Override
    public void changeJcbDetail(JcbDetailInfo info, int position, int count) {
        if (info != null) {
            mJcbDetailEdit.setText(info.getOdetail());
            mJyzgEdt.setText(info.getRAdvise());
            mJcbPositionTv.setText(String.valueOf(position));
            mJcbCountTv.setText(String.valueOf(count));
            if (TextUtils.isEmpty(info.getOblititle())) {
                mJcbDetail.setVisibility(View.GONE);
            } else {
                mJcbDetail.setVisibility(View.VISIBLE);
            }
        } else {
            mJcbDetailEdit.setText("");
            mJyzgEdt.setText("");
            mJcbPositionTv.setText("0");
            mJcbCountTv.setText("0");
        }
    }

    @Override
    public void commitStatus(boolean isSuccess) {
        toast("提交" + (isSuccess ? "成功" : "失败,请重试"));
        if (isSuccess) {
            mMyCamera.success();
            mPhotoCount.setText("0");
            mYhdjSp.setSelection(0);
            mAudioBtn.setBackgroundResource(R.drawable.yjbj_xcly_btn);
            mAudioBtn.setText("");
            mAudioBtn.setTag(null);
            mJyzgEdt.setText("");
        }
    }

    @Override
    public void toast(String toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toast(int toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void pendingDialog(int resId) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.setMessage(getString(resId));
        mProgressDialog.show();
    }

    @Override
    public void setJcbDetail(String detail) {
        Intent intent = new Intent(getActivity(), JcbDetailActivity.class);
        intent.putExtra("detail", detail);
        startActivity(intent);
    }

    @Override
    public void setJcbDetailGone() {
        mJcbDetail.setVisibility(View.GONE);
    }

    @Override
    public void cancelDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void setPlaceId(String pS) {
        MySpinnerAdapter<CsInfo> adapter = (MySpinnerAdapter<CsInfo>) mCsSp.getAdapter();
        int selection = adapter.selectItemById(pS);
        mCsSp.setSelection(selection == -1 ? 0 : selection);
        isSetPlace = true;
    }

    @Override
    public void notHaveComId() {
        mEdtPlace.setVisibility(View.VISIBLE);
        mCsSp.setVisibility(View.GONE);
        mLnSb.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View pView) {
        int id = pView.getId();
        if (id == mCameraBtn.getId()) {
            mMyCamera.takePhoto();
        } else if (id == mAudioBtn.getId()) {
            Intent intent = new Intent(getActivity(), AudioRecordActivity.class);
            String audioPath = mCache.getAbsolutePath() + File.separator
                    + DateFormat.format("yyyyMMddHHmmss", new Date())
                    + ".mp3";
            intent.putExtra("mAudioPath", audioPath);

            startActivityForResult(intent, AUDIO);
        } else if (id == mJcbDetailNext.getId()) {
            mPresenter.getNextPage();
        } else if (id == mJcbDetailPre.getId()) {
            mPresenter.getPrePage();
        } else if (id == mQrCodeBtn.getId()) {
//            mMyCamera.releaseCamera();
            Intent intent = new Intent(getActivity(), CameraTestActivity.class);
            startActivityForResult(intent, QRCODE_REQUEST);
        } else if (id == mCallBtn.getId()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            startActivity(intent);
        } else if (id == mCommitBtn.getId()) {
            commit();
        } else if (id == mJcbDetail.getId()) {
            mPresenter.getJcbDetail();
        }
    }

    private void commit() {
        int month = mCalendar.get(Calendar.MONTH) + 1;
        String monthStr;
        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = month + "";
        }
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        String dayStr;
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = day + "";
        }
        String nowDate = mCalendar.get(Calendar.YEAR) + "-"
                + monthStr + "-" + dayStr + "T00:00:00.000";
        String limit = "";
        int lEmid = 0;
        int bmId = -1;
        float dCost = 0f;
        String dScheme = "", hGrade = "";
        if (!"无隐患".equals(mYhdjSp.getSelectedItem().toString())) {
            String zgDate = mDateZg.getText().toString();
            if (!TextUtils.isEmpty(zgDate)) {
                limit = zgDate
                        + "T00:00:00.000";
            }
//            lEmid = 0;
            dScheme = mJyzgEdt.getText().toString();
            hGrade = mYhdjSp.getSelectedItem().toString();
            String zgfyStr = mYgfyEdt.getText()
                    .toString();
            if (!TextUtils.isEmpty(zgfyStr)) {
                dCost = Float.parseFloat(zgfyStr);
            }
        }
        int taskId = Integer.parseInt(((IChooseItem) mRwSp.getSelectedItem()).getItemId());
        int fliedId = Integer.parseInt(((IChooseItem) mCsSp.getSelectedItem()).getItemId());
        String sbMark = ((IChooseItem) mSbSp.getSelectedItem()).getItemName();
        AqjcCommitInfo info = new AqjcCommitInfo();
        info.setCheckDate(nowDate);
        info.setDCost(dCost);
        info.setDLimit(limit);
        info.setDScheme(dScheme);
        info.setFliedID(fliedId);
        String hDetail = "场所：" + ((IChooseItem) mCsSp.getSelectedItem()).getItemName() +
                ",设备：" + ((IChooseItem) mSbSp.getSelectedItem()).getItemName() + "。" + mJcbDetailEdit.getText().toString();
        info.setHDetail(hDetail);
        info.setHGrade(hGrade);
        info.setLEmid(lEmid);
        info.setObjOrganizationID(mObjOrganizationID);
        info.setObjPartid(bmId);
        info.setTaskName(((IChooseItem) mRwSp.getSelectedItem()).getItemName());
        info.setCsName(((IChooseItem) mCsSp.getSelectedItem()).getItemName());
        info.setTaskid(taskId);
        info.setSetStr(sbMark);
        info.setRecEmid(Integer.parseInt(GlobalDataProvider.INSTANCE.getCompanyInfo().getEmid()));
        ArrayList<String> imagePaths = mMyCamera.getImageDatas();
        StringBuilder stringBuilder = new StringBuilder();
        for (String imagePath : imagePaths) {
            stringBuilder.append(imagePath);
            stringBuilder.append(",");
        }
        String imagePath = "";
        if (stringBuilder.length() > 0) {
            imagePath = stringBuilder.substring(0, stringBuilder.length() - 1);
        }
        String audioPath = mAudioBtn.getTag() == null ? "" : "," + StringUtils.noNull(mAudioBtn.getTag());
        info.setImagePath(imagePath + audioPath);
        mPresenter.upload(info);
    }

    private String getAfterFiveDay() {
        String limit;
        long fiveTime = new Date().getTime() + 5 * 24 * 60 * 60 * 1000;
        Date five = new Date(fiveTime);
        Calendar instance = Calendar.getInstance();
        instance.setTime(five);
        int fiveMonth = instance.get(Calendar.MONTH) + 1;
        String fiveMonthStr;
        if (fiveMonth < 10) {
            fiveMonthStr = "0" + fiveMonth;
        } else {
            fiveMonthStr = fiveMonth + "";
        }
        int fiveDay = instance.get(Calendar.DAY_OF_MONTH);
        String fiveDayStr;
        if (fiveDay < 10) {
            fiveDayStr = "0" + fiveDay;
        } else {
            fiveDayStr = fiveDay + "";
        }
        limit = instance.get(Calendar.YEAR) + "-"
                + fiveMonthStr + "-" + fiveDayStr;
        return limit;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMyCamera.releaseCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMyCamera.setCameraCallback();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        mMyCamera.setCameraCallback();
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == AUDIO) // 录音
                {
                    // 显示多媒体View
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        // 检测 sdcard 是否可用
                        Toast.makeText(getActivity(), "SD卡不存在", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String strResult = data.getStringExtra("result");
                    System.out.println("audio:" + strResult);
                    if (strResult != null) {
                        mAudioBtn.setBackgroundResource(R.drawable.blue_button_background);
                        mAudioBtn.setText("重录");
                        mAudioBtn.setTag(strResult);
                    }

                } else if (requestCode == QRCODE_REQUEST) {
                    String ewmStr = data.getStringExtra("result");
                    String[] resultArr = ewmStr.split("-");
                    if (resultArr.length == 2) {
                        try {
                            String type = resultArr[0];
                            if (type.equals("aqss")) {
                                MySpinnerAdapter<SbInfo> adapter = (MySpinnerAdapter<SbInfo>) mSbSp.getAdapter();
                                adapter.resetData();
                                mSbSp.setSelection(adapter.selectItemById(resultArr[1]));
                            } else if (type.equals("chsu")) {
                                MySpinnerAdapter<CsInfo> adapter = (MySpinnerAdapter<CsInfo>) mCsSp.getAdapter();
                                int selection = adapter.selectItemById(resultArr[1]);
                                mCsSp.setSelection(selection == -1 ? 0 : selection);
                                if (selection != -1) {
                                    MySpinnerAdapter<SbInfo> sbAdapter = (MySpinnerAdapter<SbInfo>) mSbSp.getAdapter();
                                    sbAdapter.filterSb(resultArr[1]);
                                }
                            } else if (type.equals("sjdw")) {
                                try {
                                    mObjOrganizationID = Integer.parseInt(resultArr[1]);
                                } catch (NumberFormatException pE) {
                                    toast(getString(R.string.ewm_failed));
                                }
                            } else if ("sssb".equals(type)) {
                                try {
                                    mEwmSssbId = Integer.parseInt(resultArr[1]);
                                    mPresenter.getSssb(mEwmSssbId, Integer.parseInt(GlobalDataProvider.INSTANCE.getCompanyInfo().getComId()));
                                } catch (NumberFormatException pE) {
                                    toast(getString(R.string.ewm_failed));
                                }
                            } else {
                                toast(getString(R.string.ewm_failed));
                            }
                        } catch (Exception pE) {
                            pE.printStackTrace();
                            toast(getString(R.string.ewm_failed));
                        }
                    } else {
                        toast(getString(R.string.ewm_failed));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getPhotoCount(int count) {
        mPhotoCount.setText(count + "");
    }
}
