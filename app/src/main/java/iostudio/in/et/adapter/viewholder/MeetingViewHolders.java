package iostudio.in.et.adapter.viewholder;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import iostudio.in.et.R;


/**
 * Created by anantshah on 16/04/18.¡
 */

public class MeetingViewHolders extends BaseViewHolder {


    public Context context;
    public AppCompatTextView tv_name;
    public AppCompatTextView tv_type;
    public AppCompatTextView tv_date;
    public AppCompatTextView tv_status;
    public AppCompatTextView tv_address;
    public ConstraintLayout cl_meeting;


    public MeetingViewHolders(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        tv_name=itemView.findViewById(R.id.tv_name);
        tv_type=itemView.findViewById(R.id.tv_type);
        tv_address=itemView.findViewById(R.id.tv_address);
        tv_date=itemView.findViewById(R.id.tv_date);
        tv_status=itemView.findViewById(R.id.tv_status);
        cl_meeting=itemView.findViewById(R.id.cl_meeting);
    }

}
