package iostudio.in.et.adapter.viewholder;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import iostudio.in.et.R;


/**
 * Created by anantshah on 16/04/18.¡
 */

public class LeaveViewHolders extends BaseViewHolder {


    public Context context;
    public AppCompatTextView tv_type;
    public ConstraintLayout cl_leave;


    public LeaveViewHolders(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        tv_type=itemView.findViewById(R.id.tv_type);
        cl_leave=itemView.findViewById(R.id.cl_leave);
    }

}
