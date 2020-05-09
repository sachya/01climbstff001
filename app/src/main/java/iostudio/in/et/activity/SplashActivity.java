package iostudio.in.et.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import iostudio.in.et.R;
import iostudio.in.et.pref.IOPref;
import iostudio.in.et.utility.Utility;

public class SplashActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;

        final String userID = Utility.getUserID(context);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (IOPref.getInstance().getString(SplashActivity.this, IOPref.PreferenceKey.PIN, "").length() > 0) {
                    startActivity(new Intent(context, DashboardActivity.class));
                    startActivity(new Intent(context, EnterPinActivity.class));
                    finish();
                } else {
                    if (!TextUtils.isEmpty(userID)) {
                        startActivity(new Intent(context, SetPinActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(context, PhoneNumberActivity.class));
                        finish();
                    }
                }

            }
        }, 1500);

    }
}
