package iostudio.in.et.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import iostudio.in.et.R;

public class ReportActivity extends BaseActivity {
Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        context  = this;
        setToolbar(true,true,getString(R.string.report),true);
        initBase();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindEvent() {

    }
}
