package iostudio.in.et.activity;

import androidx.appcompat.app.AppCompatActivity;
import iostudio.in.et.R;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateNewMeetingActivity extends BaseActivity {

    private Context context;
    MaterialButton btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_meeting);
        setToolbar(true,true,getString(R.string.add_contact_to_meeting),true);
        context = this;
        initBase();
    }

    @Override
    protected void initView() {
        btnSubmit = findViewById(R.id.btnSubmit);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindEvent() {

    }
}
