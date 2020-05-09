package iostudio.in.et.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.RecyclerView;
import iostudio.in.et.R;

public class ExpensesActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    RecyclerView recyclerViewExpense;
    FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        setToolbar(true, true, getString(R.string.expenses), true);
        context = this;
        initBase();
    }

    @Override
    protected void initView() {
        recyclerViewExpense = findViewById(R.id.recyclerViewExpense);
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
                    intent.setClass(context, CreateNewExpenseActivity.class);
                    startActivity(intent);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
