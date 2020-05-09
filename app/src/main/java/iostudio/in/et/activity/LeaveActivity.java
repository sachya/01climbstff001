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
import iostudio.in.et.adapter.LeaveRecyclerAdapter;

public class LeaveActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    RecyclerView recyclerViewLeave;
    FloatingActionButton fabAdd;
    LeaveRecyclerAdapter leaveRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        setToolbar(true, true, getString(R.string.leave_application), true);
        context = this;
        initBase();
    }

    @Override
    protected void initView() {
        recyclerViewLeave = findViewById(R.id.recyclerViewLeave);
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
        leaveRecyclerAdapter = new LeaveRecyclerAdapter(context, new ArrayList<String>());
        setRecyclerViewWithAdapter(recyclerViewLeave,
                new LinearLayoutManager(context),
                leaveRecyclerAdapter,
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
                    intent.setClass(context,CreateNewLeaveActivity.class);
                    startActivity(intent);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
