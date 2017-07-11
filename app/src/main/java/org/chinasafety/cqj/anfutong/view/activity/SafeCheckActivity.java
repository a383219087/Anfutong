package org.chinasafety.cqj.anfutong.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.chinasafety.cqj.anfutong.R;
import org.chinasafety.cqj.anfutong.view.BaseActivity;
import org.chinasafety.cqj.anfutong.view.fragment.AqjcFragment;

public class SafeCheckActivity extends BaseActivity {

    private static final String KEY_ORG_ID = "orgId";
    private static final String KEY_OGR_ID_STR = "ogrIdStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String orgId = getIntent().getStringExtra(KEY_ORG_ID);
        String orgIdStr = getIntent().getStringExtra(KEY_OGR_ID_STR);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_safe_check_container, AqjcFragment.newInstance(orgId,orgIdStr))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public static void start(Context context,String orgId,String orgIdStr) {
        Intent starter = new Intent(context, SafeCheckActivity.class);
        starter.putExtra(KEY_ORG_ID,orgId);
        starter.putExtra(KEY_OGR_ID_STR,orgIdStr);
        context.startActivity(starter);
    }
}
