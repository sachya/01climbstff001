package iostudio.in.et.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import iostudio.in.et.R;

import android.content.Context;
import android.os.Bundle;

public class TourActivity extends BaseActivity {

    private Context context;
RecyclerView recyclerViewTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        setToolbar(true,true,getString(R.string.tour_details),true);
        context = this;
        initBase();

    }

    @Override
    protected void initView() {
        recyclerViewTour = findViewById(R.id.recyclerViewTour);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindEvent() {

    }
}
