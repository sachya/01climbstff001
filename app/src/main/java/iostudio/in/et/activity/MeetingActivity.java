package iostudio.in.et.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import iostudio.in.et.R;
import iostudio.in.et.adapter.MeetingRecyclerAdapter;
import iostudio.in.et.retrofit.response.MeetingData;

public class MeetingActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    FloatingActionButton fabAdd;
    RecyclerView recyclerViewMeeting;
    private MeetingRecyclerAdapter meetingRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        setToolbar(true, true, getString(R.string.meeting), true);
        context = this;
        initBase();
    }

    @Override
    protected void initView() {

        recyclerViewMeeting = findViewById(R.id.recyclerViewMeeting);
        fabAdd = findViewById(R.id.fabAdd);
        fabAnimation();

    }
    private void fabAnimation() {

        fabAdd.hide();
        fabAdd.postDelayed(new Runnable() {
            @Override
            public void run() {
                fabAdd.show();
            }
        }, 500);
    }
    @Override
    protected void initData() {

        meetingRecyclerAdapter = new MeetingRecyclerAdapter(context, new ArrayList<MeetingData>());
        setRecyclerViewWithAdapter(recyclerViewMeeting,
                new LinearLayoutManager(context),
                meetingRecyclerAdapter,
                false);

    }

    @Override
    protected void bindEvent() {
        fabAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            Intent intent;
            switch (v.getId()) {
                case R.id.fabAdd:
                    intent = new Intent();
                    intent.setClass(context, CreateNewMeetingActivity.class);
                    context.startActivity(intent);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
