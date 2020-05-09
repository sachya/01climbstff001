package iostudio.in.et.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import iostudio.in.et.R;
import iostudio.in.et.utility.Constant;

public class TermsNCondActivity extends BaseActivity {
    private WebView webView;
    private String url = "https://climbstaff.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_ncond);
        initBase();
    }

    @Override
    protected void initView() {
        webView = findViewById(R.id.wv_terms);

    }

    @Override
    protected void initData() {
        boolean doLoadTerms = getIntent().getBooleanExtra(Constant.INTENT.DO_LOAD_TERMS, false);
        String suffix = doLoadTerms ? "terms" : "privacy";
        url = url + suffix;
        webView.loadUrl(url);
    }

    @Override
    protected void bindEvent() {

    }
}
