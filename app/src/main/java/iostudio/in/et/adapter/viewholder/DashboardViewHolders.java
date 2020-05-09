package iostudio.in.et.adapter.viewholder;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import iostudio.in.et.R;


/**
 * Created by anantshah on 16/04/18.¡
 */

public class DashboardViewHolders extends BaseViewHolder {


    public Context context;
    public AppCompatTextView textViewTitle;
    public AppCompatTextView textViewSubTitle;
    public ConstraintLayout linear_layout_profile;


    public DashboardViewHolders(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        textViewTitle=itemView.findViewById(R.id.textViewTitle);
        textViewSubTitle=itemView.findViewById(R.id.textViewSubTitle);
        linear_layout_profile=itemView.findViewById(R.id.cl_dashboard);
    }

}
